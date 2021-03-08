package com.lwjfork.android.gradle.aop.analyzer.jar


import com.lwjfork.aop.analyzer.exector.IJarAnalyzer
import com.lwjfork.aop.collector.model.CompileDirModel
import com.lwjfork.aop.collector.model.CompileJarModel
import javassist.ClassPool

abstract class SimpleJarAnalyzer implements IJarAnalyzer{
  ClassPool classPool

    ClassPool getClassPool() {
        return classPool
    }

    void setClassPool(ClassPool classPool) {
        this.classPool = classPool
    }


    @Override
    void before(CompileJarModel res) {

    }

    @Override
    void analyze(CompileJarModel res) {

    }

    @Override
    void after(CompileJarModel res) {

    }
}
