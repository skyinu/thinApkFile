package com.skyinu.thinapkfile

import com.android.build.gradle.tasks.PackageApplication
import java.io.File
import java.util.zip.ZipFile

object ThinkApkFileTask {
    fun thinApkFile(packageApkTask: PackageApplication) {
        val apkFiles = packageApkTask.apkNames
        val apkOutputDirectory = packageApkTask.outputDirectory
        for (apkName in apkFiles) {
            var apkFile = ZipFile(File(apkOutputDirectory, apkName))
        }
    }
}