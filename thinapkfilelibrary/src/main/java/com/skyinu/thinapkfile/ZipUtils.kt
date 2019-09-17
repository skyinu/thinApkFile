package com.skyinu.thinapkfile

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.regex.Pattern
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

object ZipUtils {
    fun copyFileWithKeepRules(originFile: File, targetFile: File, keepRules: List<Pattern>) {
        val buffer = ByteArray(8096)
        val originFileIns = ZipInputStream(FileInputStream(originFile))
        val targetFileOutS = ZipOutputStream(FileOutputStream(targetFile))
        try {
            var entry = originFileIns.nextEntry
            while (entry != null) {
                val entryName = entry.name
                if (shouldKeep(keepRules, entryName)) {
                    targetFileOutS.putNextEntry(ZipEntry(entryName))
                    var len = originFileIns.read(buffer)
                    while (len > 0) {
                        targetFileOutS.write(buffer, 0, len)
                        len = originFileIns.read(buffer)
                    }
                } else {
                    println("remove item -> $entryName")
                }
                entry = originFileIns.nextEntry
            }
        } finally {
            originFileIns.close()
            targetFileOutS.close()
        }
    }

    private fun shouldKeep(keepRules: List<Pattern>, entryName: String): Boolean {
        for (rule in keepRules) {
            if (rule.matcher(entryName).matches()) {
                return true
            }
        }
        return false
    }
}