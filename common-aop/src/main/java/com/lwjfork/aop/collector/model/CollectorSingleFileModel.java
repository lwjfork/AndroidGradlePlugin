package com.lwjfork.aop.collector.model;

import java.io.Serializable;

/**
 * Created by lwj on 2020/8/11.
 * lwjfork@gmail.com
 */
public class CollectorSingleFileModel implements Serializable {

    // 源路径
    private String sourcePath;
    private String destPath;
    private boolean needScan = true;

    public CollectorSingleFileModel(String sourcePath,String destPath, boolean needScan) {
        this.sourcePath = sourcePath;
        this.needScan = needScan;
        this.destPath = destPath;
    }

    public CollectorSingleFileModel(String sourcePath) {
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
}
