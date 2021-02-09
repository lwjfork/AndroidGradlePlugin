package com.lwjfork.aop.collector;

import com.lwjfork.aop.collector.model.*;
import com.lwjfork.aop.utils.FileUtil;
import com.lwjfork.aop.utils.TaskUtil;

import java.util.ArrayList;
import java.util.concurrent.*;

/**
 * Created by lwj on 2020/8/7.
 * lwjfork@gmail.com
 * 类收集器
 */
public class ClassCollectorExecutor {

    // 需要收集的 目录
    ArrayList<CollectorDirModel> dirModels = new ArrayList<>();
    // 需要收集的 jar包
    ArrayList<CollectorJarModel> jarModels = new ArrayList<>();
    // 需要收集的单独文件
    ArrayList<CollectorSingleFileModel> singleFileModels = new ArrayList<>();

    // jar 包 不需要解压的jar包
    private ArrayList<String> originJarPaths = new ArrayList<>();

    public ClassCollectorExecutor() {
    }

    /**
     * 添加收集的单独文件
     * @param sourcePath  文件路径
     */
    public void addSingleFile(String sourcePath, boolean needScan){
        singleFileModels.add(new CollectorSingleFileModel(sourcePath,needScan));
    }
    /**
     * 添加收集的单独文件
     * @param sourcePath  文件路径
     */
    public void addSingleFile(String sourcePath){
        this.addSingleFile(sourcePath,true);
    }

    /**
     * 需要收集的 目录
     * @param sourcePath 目录源路径
     * @param ignorePattern 忽略文件的正则
     * @param needScan 是否需要遍历收集
     */
    public void addDirectory(String sourcePath, String ignorePattern, boolean needScan) {
        this.dirModels.add(new CollectorDirModel(sourcePath,needScan,ignorePattern));
    }

    /**
     * 收集的 jar 包，此jar包不需要解压的
     * @param path
     */
    public void addOriginJarPath(String path){this.originJarPaths.add(path);}

    /**
     * 需要收集的 jar
     * @param sourcePath 源路径
     * @param unzipPath 解压路径
     * @param ignorePattern 忽略文件的正则
     * @param needScan 是否需要遍历收集
     */
    public void addJar(String sourcePath, String unzipPath, String ignorePattern, boolean needScan) {
        this.jarModels.add(new CollectorJarModel(sourcePath,unzipPath,ignorePattern,needScan));
    }

    public void addJar(String sourcePath, String unzipPath,  boolean needScan) {
        this.addJar(sourcePath, unzipPath, "", needScan);
    }

    public void addJar(String sourcePath, String unzipPath) {
        this.addJar(sourcePath, unzipPath, true);
    }

    public void addJar(String sourcePath, String unzipPath, String ignorePattern) {
        this.addJar(sourcePath, unzipPath, ignorePattern, true);
    }

    public void addDirectory(String sourcePath) {
        this.addDirectory(sourcePath,  true);
    }
    public void addDirectory(String sourcePath,String ignorePattern) {
        this.addDirectory(sourcePath, ignorePattern,  true);
    }
    public void addDirectory(String sourcePath,  boolean needCopy) {
        this.addDirectory(sourcePath, "", needCopy);
    }


    public CollectorResult collect()throws  InterruptedException, ExecutionException{
        unzip();
        return scanFile();
    }

    // 将 jar 解压
    private void unzip()throws  InterruptedException, ExecutionException{
        ExecutorService exec = Executors.newCachedThreadPool();
        CompletionService<Void> execService = new ExecutorCompletionService<Void>(exec);
        int count = 0;
        for (CollectorJarModel jarModel : jarModels) {
            execService.submit(() -> {
                FileUtil.unzip(jarModel.getSourceJarPath(), jarModel.getUnzipDirPath() ,jarModel.getIgnorePattern(), false);
                return null;
            });
            count++;
        }
        for (int i = 0; i < count; i++) {
            execService.take().get();
        }
    }

    /**
     * 扫描所有的 jar 和 class 目录
     *
     * 返回文件集合
     *    key 为 文件目录的源路径
     *    value 为 文件目录下包含所有 class 文件
     *
     * @return 返回所有的文件集合
     */
    private CollectorResult scanFile()throws  InterruptedException, ExecutionException{
        ExecutorService exec = Executors.newCachedThreadPool();
        CompletionService<Void> execService = new ExecutorCompletionService<Void>(exec);
        CollectorResult collectorResult = new CollectorResult();
        // 收集单文件 jar 包，此jar 不需要解压
        // 收集单文件
        for (CollectorSingleFileModel singleFileModel : singleFileModels) {
            collectorResult.addSingleFile(new CompileSingleFileModel(singleFileModel.getSourcePath(),singleFileModel.isNeedScan()));
        }
        int count = 0;
        // 收集 jar 包文件
        for (CollectorJarModel jarModel : jarModels) {
            execService.submit(TaskUtil.getTask(() -> {
                try {
                    ArrayList<CompileSingleFileModel> containFiles =    FileUtil.collectFiles(jarModel.getUnzipDirPath() , jarModel.getIgnorePattern(),true);
                    CompileJarModel compileJarModel = new CompileJarModel(jarModel.getSourceJarPath(),jarModel.getUnzipDirPath(),jarModel.isNeedScan());
                    containFiles.forEach(compileJarModel::addChildFile);
                    collectorResult.addJarModel(compileJarModel);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }));
            count++;
        }
        // 收集 Dir 目录文件
        for (CollectorDirModel dirModel : dirModels) {
            execService.submit(TaskUtil.getTask(() -> {
                try {
                    ArrayList<CompileSingleFileModel> containFiles = FileUtil.collectFiles(dirModel.getSourceDirPath(),dirModel.getIgnorePattern(),true);
                    CompileDirModel compileDirModel = new CompileDirModel(dirModel.getSourceDirPath(),dirModel.isNeedScan());
                    containFiles.forEach(compileDirModel::addChildFile);
                    collectorResult.addDirModel(compileDirModel);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }));
            count++;
        }

        for (int i = 0; i < count; i++) {
            execService.take().get();
        }
        exec.shutdown();
        return collectorResult;
    }

}
