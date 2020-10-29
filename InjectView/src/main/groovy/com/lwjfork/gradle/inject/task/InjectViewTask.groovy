package com.lwjfork.gradle.inject.task

import com.lwjfork.gradle.inject.model.GenerateRFile
import com.lwjfork.gradle.inject.model.RFileReader
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

/**
 * Created by lwj on 2020-01-07.
 *  lwjfork@gmail.com
 */
class InjectViewTask extends DefaultTask {


    def outPutDir
    def rFile
    def packageName
    def className

    @TaskAction
    def generatorR2File() {
        GenerateRFile generateRFile = new GenerateRFile()
        generateRFile.outPutDir = outPutDir
        generateRFile.packageName = packageName
        generateRFile.className = className

        RFileReader rFileReader = new RFileReader(generateRFile)
        rFileReader.readSymbolOutputFile(rFile)
        generateRFile.generateR2File()
    }

}
