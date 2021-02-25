package com.lwjfork.gradle.adapter.manifest

abstract class BaseReadManifest {

    def readMultipleManifest(File file) {
        if (file == null || !file.exists()) return
        if (file.isDirectory()) {
            file.listFiles().each {
                readMultipleManifest(it)
            }
        } else if (file.name.equalsIgnoreCase("AndroidManifest.xml")) {
            readManifest(file)
        }
    }

    abstract readManifest(File file)


}
