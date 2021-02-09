package com.lwjfork.aop.analyzer.exector;


public interface IAnalyzer<T> {

    void before(T res);
    void analyze(T res);
    void after(T res);
}
