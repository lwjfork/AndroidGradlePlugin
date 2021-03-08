package com.lwjfork.android.gradle.aop.analyzer.dir

import com.lwjfork.aop.analyzer.exector.IDirectoryAnalyzer
import com.lwjfork.aop.collector.model.CompileDirModel
import javassist.ClassPool

abstract class SimpleDirectoryAnalyzer implements IDirectoryAnalyzer{
  ClassPool classPool

    ClassPool getClassPool() {
        return classPool
    }

    void setClassPool(ClassPool classPool) {
        this.classPool = classPool
    }

    @Override
    void before(CompileDirModel res) {

    }

    @Override
    void analyze(CompileDirModel res) {

    }

    @Override
    void after(CompileDirModel res) {

    }
}
