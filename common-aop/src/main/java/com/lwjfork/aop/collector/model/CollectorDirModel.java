package com.lwjfork.aop.collector.model;

import java.io.Serializable;

/**
 * Created by lwj on 2020/8/11.
 * lwjfork@gmail.com
 */
public class CollectorDirModel implements Serializable {

    // 源路径
    private String sourceDirPath;
    // 是否需要 copy，默认需要复制
    private boolean needScan = true;
    private String ignorePattern;

    public CollectorDirModel(String sourceDirPath, boolean needScan, String ignorePattern) {
        this.sourceDirPath = sourceDirPath;
        this.needScan = needScan;
        this.ignorePattern = ignorePattern;
    }

    public CollectorDirModel(String sourceDirPath, boolean needScan) {
        this.sourceDirPath = sourceDirPath;
        this.needScan = needScan;
    }

    public CollectorDirModel(String sourceDirPath) {
       this(sourceDirPath,true,"");
    }

    public String getSourceDirPath() {
        return sourceDirPath;
    }

    public void setSourceDirPath(String sourceDirPath) {
        this.sourceDirPath = sourceDirPath;
    }

    public boolean isNeedScan() {
        return needScan;
    }

    public void setNeedScan(boolean needScan) {
        this.needScan = needScan;
    }

    public String getIgnorePattern() {
        return ignorePattern;
    }

    public void setIgnorePattern(String ignorePattern) {
        this.ignorePattern = ignorePattern;
    }
}
