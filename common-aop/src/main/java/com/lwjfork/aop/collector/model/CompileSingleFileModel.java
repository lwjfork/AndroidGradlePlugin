package com.lwjfork.aop.collector.model;

import java.io.Serializable;

/**
 * 参与编译的单独的  文件信息
 */
public class CompileSingleFileModel implements Serializable {
    // 文件路径
    private String sourcePath;
    private String destPath;

    private boolean needScan = true;


    public CompileSingleFileModel(String sourcePath,String destPath, boolean needScan) {
        this.sourcePath = sourcePath;
        this.needScan = needScan;
        this.destPath = destPath;
    }

    public CompileSingleFileModel(String sourcePath, boolean needScan) {
        this.sourcePath = sourcePath;
        this.needScan = needScan;
    }

    public CompileSingleFileModel(String sourcePath) {
        this.sourcePath = sourcePath;
    }

    public String getSourcePath() {
        return sourcePath;
    }

    public void setSourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
    }

    public boolean isNeedScan() {
        return needScan;
    }

    public void setNeedScan(boolean needScan) {
        this.needScan = needScan;
    }

    public String getDestPath() {
        return destPath;
    }

    public void setDestPath(String destPath) {
        this.destPath = destPath;
    }

    @Override
    public String toString() {
        return "CompileSingleFileModel{" +
                "sourcePath='" + sourcePath + '\'' +
                ", destPath='" + destPath + '\'' +
                ", needScan=" + needScan +
                '}';
    }
}
