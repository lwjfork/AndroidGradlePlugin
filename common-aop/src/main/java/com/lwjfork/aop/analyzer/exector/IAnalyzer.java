package com.lwjfork.aop.analyzer.exector;

import com.lwjfork.aop.collector.model.CompileDirModel;

public interface IAnalyzer<T> {

    void before(T res);
    void analyze(T res);
    void after(T res);
}
