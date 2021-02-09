package com.lwjfork.aop.collector.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 参与编译的单独的 jar 文件信息
 * 包含了jar包中的文件信息
 */
public class CompileJarModel implements Serializable {
    // jar 包路径
    private String sourcePath;
    // 解压后的路径
    private String unzipDirPath;
    // 压缩后的路径
    private String destPath;
    // jar 包下的路径
    private ArrayList<CompileSingleFileModel> childFiles = new ArrayList<>();

    private boolean needScan = true;

    public CompileJarModel(String sourcePath,String unzipDirPath,String destPath, boolean needScan) {
        this.sourcePath = sourcePath;
        this.needScan = needScan;
        this.unzipDirPath = unzipDirPath;
        this.destPath = destPath;
    }

    public String getDestPath() {
        return destPath;
    }

    public void setDestPath(String destPath) {
        this.destPath = destPath;
    }

    public String getSourcePath() {
        return sourcePath;
    }

    public void setSourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
    }

    public ArrayList<CompileSingleFileModel> getChildFiles() {
        return childFiles;
    }

    public void setChildFiles(ArrayList<CompileSingleFileModel> childFiles) {
        this.childFiles = childFiles;
    }

    public void addChildFile(CompileSingleFileModel childFile) {
        childFile.setNeedScan(this.needScan && childFile.isNeedScan());
        this.childFiles.add(childFile);
    }

    public boolean isNeedScan() {
        return needScan;
    }

    public void setNeedScan(boolean needScan) {
        this.needScan = needScan;
    }

    public String getUnzipDirPath() {
        return unzipDirPath;
    }

    public void setUnzipDirPath(String unzipDirPath) {
        this.unzipDirPath = unzipDirPath;
    }
}
