package com.lwjfork.aop.analyzer;

import com.lwjfork.aop.analyzer.exector.IDirectoryAnalyzer;
import com.lwjfork.aop.analyzer.exector.IJarAnalyzer;
import com.lwjfork.aop.analyzer.exector.IOriginJarAnalyzer;
import com.lwjfork.aop.analyzer.exector.ISingleFileAnalyzer;
import com.lwjfork.aop.collector.model.CollectorResult;

import java.util.ArrayList;

/**
 * 通过收集的文件信息，并对 class 文件进行分析，过滤出需要修改的类/方法/变量
 * Created by lwj on 2020/8/20.
 * lwjfork@gmail.com
 */
public class AnalyzerExecutor {
    /**
     * 收集的文件信息
     */
    private CollectorResult result;
    public AnalyzerExecutor(CollectorResult result) {
        this.result = result;
    }

    /**
     * 目录遍历分析器
     */
    private ArrayList<IDirectoryAnalyzer> directoryAnalyzers = new ArrayList<>();
    /**
     * jar 包遍历分析器(解压过得)
     */
    private ArrayList<IJarAnalyzer> jarAnalyzers = new ArrayList<>();
    /**
     * Jar 分析器（不需要解压）
     */
    private ArrayList<IOriginJarAnalyzer> originJarAnalyzers = new ArrayList<>();
    /**
     * 单文件分析器
     */
    private ArrayList<ISingleFileAnalyzer> singleFileAnalyzers = new ArrayList<>();
    public void analyze(){
     if(result == null){
         return;
     }
     beforeAnalyze();
     realAnalyze();
     afterAnalyze();
    }

    public void beforeAnalyze(){
        result.getSingleFiles().forEach(item->{
            singleFileAnalyzers.forEach(Analyzer->{
                Analyzer.before(item);
            });
        });
        result.getDirs().forEach(item->{
            directoryAnalyzers.forEach(Analyzer->{
                Analyzer.before(item);
            });
        });
        result.getJars().forEach(item->{
            jarAnalyzers.forEach(Analyzer->{
                Analyzer.before(item);
            });
        });
        result.getOriginJarPaths().forEach(item->{
            originJarAnalyzers.forEach(Analyzer->{
                Analyzer.before(item);
            });
        });
    }

    public void realAnalyze(){
        result.getSingleFiles().forEach(item->{
            singleFileAnalyzers.forEach(Analyzer->{
                Analyzer.analyze(item);
            });
        });
        result.getDirs().forEach(item->{
            directoryAnalyzers.forEach(Analyzer->{
                Analyzer.analyze(item);
            });
        });
        result.getJars().forEach(item->{
            jarAnalyzers.forEach(Analyzer->{
                Analyzer.analyze(item);
            });
        });
        result.getOriginJarPaths().forEach(item->{
            originJarAnalyzers.forEach(Analyzer->{
                Analyzer.analyze(item);
            });
        });
    }

    public void afterAnalyze(){
        result.getSingleFiles().forEach(item->{
            singleFileAnalyzers.forEach(Analyzer->{
                Analyzer.after(item);
            });
        });
        result.getDirs().forEach(item->{
            directoryAnalyzers.forEach(Analyzer->{
                Analyzer.after(item);
            });
        });
        result.getJars().forEach(item->{
            jarAnalyzers.forEach(Analyzer->{
                Analyzer.after(item);
            });
        });
        result.getOriginJarPaths().forEach(item->{
            originJarAnalyzers.forEach(Analyzer->{
                Analyzer.after(item);
            });
        });
    }

    public void addSingleFileAnalyzer(ISingleFileAnalyzer singleFileAnalyzer){
        this.singleFileAnalyzers.add(singleFileAnalyzer);
    }
    public void addOriginJarAnalyzer(IOriginJarAnalyzer originJarAnalyzer){
        this.originJarAnalyzers.add(originJarAnalyzer);
    }
    public void addDirectoryAnalyzer(IDirectoryAnalyzer directoryAnalyzer){
        this.directoryAnalyzers.add(directoryAnalyzer);
    }
    public void addJarAnalyzer(IJarAnalyzer jarAnalyzer){
        this.jarAnalyzers.add(jarAnalyzer);
    }
}
