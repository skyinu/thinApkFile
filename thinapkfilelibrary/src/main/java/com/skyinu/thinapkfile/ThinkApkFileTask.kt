package com.skyinu.thinapkfile

import com.android.build.gradle.tasks.PackageApplication
import org.gradle.api.Project
import org.gradle.api.file.FileCollection
import org.gradle.api.internal.file.collections.DefaultConfigurableFileCollection
import java.io.File
import java.io.FileInputStream
import java.util.regex.Pattern
import java.util.zip.ZipInputStream

object ThinkApkFileTask {
    private const val THIN_FILE_PREFIX = "thin"
    private const val JAR_SUFFIX = ".jar"
    private val DEFAULT_KEEP_LIST = listOf(
        "AndroidManifest.xml",
        "resources.arsc",
        "^classes\\S*.dex",
        "^res${File.separator}\\S+",
        "^res/\\S+"
    )

    fun thinJavaResources(packageApkTask: PackageApplication, project: Project) {
        thinJavaResources(packageApkTask.javaResourceFiles, project)
    }

    /**
     * simply delete all java resource here
     */
    private fun thinJavaResources(
        javaResourceFiles: FileCollection, project: Project
    ) {
        when (javaResourceFiles) {
            is DefaultConfigurableFileCollection -> {
                val javaResFileList = javaResourceFiles.from
                printLog(javaResourceFiles.files, project)
                javaResFileList.clear()
            }
            else -> {
                throw RuntimeException("class type is not compatible, current class type is ${javaResourceFiles::class.java}")
            }
        }
    }

    private fun printLog(javaResFileList: Set<File>, project: Project) {
        if (javaResFileList.isEmpty()) {
            println("there is no java resource")
            return
        }
        val pluginExtension = project.extensions.findByType(ThinApkFileExtension::class.java)
        val logFile = lazy {
            val file = File(project.buildDir, "${THIN_FILE_PREFIX}_log.txt")
            if (!file.exists()) {
                file.createNewFile()
            }
            return@lazy file
        }
        for (fileItem in javaResFileList) {
            println("remove file ---> " + fileItem.absolutePath)
            if (pluginExtension?.printDetail != true) {
                continue
            }
            if (fileItem.name.endsWith(JAR_SUFFIX)) {
                logFile.value.writeText("File  ${fileItem.absolutePath}----->\n")
                val originFileIns = ZipInputStream(FileInputStream(fileItem))
                originFileIns.use { ins ->
                    var entry = ins.nextEntry
                    while (entry != null) {
                        logFile.value.appendText("--- remove item -> ${entry.name}\n")
                        entry = originFileIns.nextEntry
                    }
                }
            }
        }
    }

    private fun buildKeepPattern(keepList: Array<String>?): List<Pattern> {
        val keepRules = mutableListOf<String>()
        keepRules.addAll(DEFAULT_KEEP_LIST)
        keepList?.let {
            keepRules.addAll(it)
        }
        val keepPattern = mutableListOf<Pattern>()
        for (rule in keepRules) {
            keepPattern.add(Pattern.compile(rule))
        }
        return keepPattern
    }
}