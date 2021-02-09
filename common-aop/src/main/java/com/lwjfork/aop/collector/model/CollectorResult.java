package com.lwjfork.aop.collector.model;

import java.io.Serializable;
import java.util.ArrayList;

public class CollectorResult implements Serializable {

    // 单独的文件
    private ArrayList<CompileSingleFileModel> singleFiles = new ArrayList<>();
    // 目录
    private ArrayList<CompileDirModel> dirs = new ArrayList<>();
    // jar 包 需要解压的 jar 包
    private ArrayList<CompileJarModel> jars = new ArrayList<>();
    // jar 包 不需要解压的jar包
    private ArrayList<String> originJarPaths = new ArrayList<>();

    public void addSingleFile(CompileSingleFileModel singleFileModel){
        this.singleFiles.add(singleFileModel);
    }
    public void addDirModel(CompileDirModel dirModel){
        this.dirs.add(dirModel);
    }
    public void addJarModel(CompileJarModel jarModel){
        this.jars.add(jarModel);
    }
    public void addOriginJarPath(String path){this.originJarPaths.add(path);}

    public ArrayList<CompileSingleFileModel> getSingleFiles() {
        return singleFiles;
    }

    public void setSingleFiles(ArrayList<CompileSingleFileModel> singleFiles) {
        this.singleFiles = singleFiles;
    }

    public ArrayList<CompileDirModel> getDirs() {
        return dirs;
    }

    public void setDirs(ArrayList<CompileDirModel> dirs) {
        this.dirs = dirs;
    }

    public ArrayList<CompileJarModel> getJars() {
        return jars;
    }

    public void setJars(ArrayList<CompileJarModel> jars) {
        this.jars = jars;
    }

    public ArrayList<String> getOriginJarPaths() {
        return originJarPaths;
    }

    public void setOriginJarPaths(ArrayList<String> originJarPaths) {
        this.originJarPaths = originJarPaths;
    }
}
