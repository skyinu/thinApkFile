package com.skyinu.thinapkfile

import com.android.build.gradle.tasks.PackageApplication
import org.gradle.api.file.FileCollection
import org.gradle.api.internal.file.collections.DefaultConfigurableFileCollection
import java.io.File
import java.util.regex.Pattern

object ThinkApkFileTask {
    private const val THIN_FILE_PREFIX = "thin"
    private val DEFAULT_KEEP_LIST = listOf(
        "AndroidManifest.xml",
        "resources.arsc",
        "^classes\\S*.dex",
        "^res${File.separator}\\S+",
        "^res/\\S+"
    )

    fun thinJavaResources(packageApkTask: PackageApplication, extension: ThinApkFileExtension?) {
        thinJavaResources(packageApkTask.javaResourceFiles)
    }

    /**
     * simply delete all java resource here
     */
    private fun thinJavaResources(
        javaResourceFiles: FileCollection
    ) {
        when (javaResourceFiles) {
            is DefaultConfigurableFileCollection -> {
                val javaResFileList = javaResourceFiles.from
                javaResFileList.clear()
            }
            else -> {
                throw RuntimeException("class type is not compatible, current class type is ${javaResourceFiles::class.java}")
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