package com.skyinu.thinapkfile

import com.android.build.gradle.AppPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import com.android.build.gradle.AppExtension
import com.android.build.gradle.api.BaseVariant
import com.android.build.gradle.tasks.PackageApplication
import org.gradle.api.DomainObjectSet

class ThinApkFilePlugin : Plugin<Project> {
    companion object {
        const val PACKAGE_APK_TASK_PREFIX = "package"
        const val THIN_APK_EXTENSION = "thinApk"
    }

    override fun apply(project: Project) {
        project.extensions.create(THIN_APK_EXTENSION, ThinApkFileExtension::class.java)
        project.afterEvaluate {
            project.plugins.all {
                when (it) {
                    is AppPlugin -> {
                        project.extensions.findByType(AppExtension::class.java)?.run {
                            configThinkApkFileTask(project, applicationVariants)
                        }
                    }
                }
            }
        }
    }

    private fun configThinkApkFileTask(project: Project, variants: DomainObjectSet<out BaseVariant>) {
        val thinApkExtension = project.extensions.findByType(ThinApkFileExtension::class.java)
        if (thinApkExtension == null) {
            println("retrieve thinkApk extension failed")
        }
        if (thinApkExtension?.thinSwitch == false) {
            println("thinkApkFile plugin disabled")
            return
        }
        variants.all { variant ->
            val packageTask = project.tasks.findByName("$PACKAGE_APK_TASK_PREFIX${variant.name.capitalize()}")
            packageTask?.doFirst {
                ThinkApkFileTask.thinJavaResources(it as PackageApplication, project)
            }
            if (packageTask == null) {
                println("can't find package task")
            }
        }
    }
}