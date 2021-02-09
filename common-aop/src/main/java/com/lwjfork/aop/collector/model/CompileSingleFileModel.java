package com.lwjfork.aop.collector.model;

import java.io.Serializable;

/**
 * 参与编译的单独的  文件信息
 */
public class CompileSingleFileModel implements Serializable {
    // 文件路径
    private String sourcePath;

    private boolean needScan = true;


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
}
