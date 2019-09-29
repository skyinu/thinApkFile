package com.skyinu.thinapkfile

import com.android.build.gradle.tasks.PackageApplication
import org.apache.commons.io.FileUtils
import org.gradle.api.Project
import org.gradle.api.file.FileCollection
import java.io.File
import java.util.regex.Pattern

object ThinkApkFileTask {
    private const val THIN_FILE_PREFIX = "thin"
    private val DEFAULT_KEEP_LIST = listOf(
        ".*publicsuffixes.gz$"
    )

    fun thinJavaResources(packageApkTask: PackageApplication, project: Project) {
        thinJavaResources(packageApkTask.javaResourceFiles, project)
    }

    private fun thinJavaResources(
        javaResourceFiles: FileCollection, project: Project
    ) {
        val javaResFileList = javaResourceFiles.files
        val pluginExtension = project.extensions.findByType(ThinApkFileExtension::class.java)
        val printLog = pluginExtension?.printDetail ?: false
        val keepRules = buildKeepPattern(pluginExtension?.keepRules)
        val logFile = lazy {
            val file = File(project.buildDir, "${THIN_FILE_PREFIX}_log.txt")
            if (!file.exists()) {
                file.createNewFile()
            }
            return@lazy file
        }
        for (fileItem in javaResFileList) {
            println("remove file ---> " + fileItem.absolutePath)
            val targetFile = File(fileItem.parent, "${THIN_FILE_PREFIX}_${fileItem.name}")
            FileUtils.deleteQuietly(targetFile)
            targetFile.createNewFile()
            ZipUtils.copyFileWithKeepRules(fileItem, targetFile, keepRules, printLog, logFile)
            FileUtils.deleteQuietly(fileItem)
            targetFile.renameTo(fileItem)
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