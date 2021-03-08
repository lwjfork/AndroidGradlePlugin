package com.lwjfork.android.gradle.aop.analyzer

import com.lwjfork.aop.analyzer.exector.IAnalyzer
import javassist.ClassPool

abstract class SimpleAopAnalyzer implements IAnalyzer<Void> {

    ClassPool classPool

    final   ClassPool getClassPool() {
        return classPool
    }

    final  void setClassPool(ClassPool classPool) {
        this.classPool = classPool
    }

    @Override
    void before(Void res) {

    }

    @Override
    void analyze(Void res) {

    }

    @Override
    void after(Void res) {

    }
}
