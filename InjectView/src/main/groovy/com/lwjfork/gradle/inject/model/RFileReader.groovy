package com.lwjfork.gradle.inject.model;

/**
 * Created by lwj on 2019-09-02.
 * lwjfork@gmail.com
 * R 读取类
 */
class RFileReader {
    GenerateRFile generatorRFile

    RFileReader(GenerateRFile generatorRFile) {
        this.generatorRFile = generatorRFile
    }

    def readSymbolOutputFile(def textSymbolOutputFile) {
        textSymbolOutputFile.readLines().each {
            processFileLine(it)
        }
    }


    def processFileLine(String lineText) {
        def array = lineText.split(" ")
        if (array.size() < 4 || (array[0] != "int")) {
            return
        }
        def typeName = array[1]
        def idName = array[2]
        def idValue = array[3]
        generatorRFile.addItem(typeName, idName, idValue)
    }

}
