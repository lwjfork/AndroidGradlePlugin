package com.lwjfork.aop.collector.model;

import java.io.Serializable;

/**
 * Created by lwj on 2020/8/11.
 * lwjfork@gmail.com
 */
public class CollectorJarModel implements Serializable {

    //  jar包源路径
    private String sourceJarPath;
    // jar 的解压路径
    private String unzipDirPath;
    private String ignorePattern;
    // 是否需要 copy，默认需要扫描
    private boolean needScan = true;


    public CollectorJarModel(String sourceJarPath, String unzipDirPath, String ignorePattern, boolean needScan) {
        this.sourceJarPath = sourceJarPath;
        this.unzipDirPath = unzipDirPath;
        this.ignorePattern = ignorePattern;
        this.needScan = needScan;
    }

    public CollectorJarModel(String sourceJarPath, String unzipDirPath, String ignorePattern) {
        this.sourceJarPath = sourceJarPath;
        this.unzipDirPath = unzipDirPath;
        this.ignorePattern = ignorePattern;
    }

    public CollectorJarModel(String sourceJarPath, String unzipDirPath) {
       this(sourceJarPath,unzipDirPath,"");
    }

    public String getSourceJarPath() {
        return sourceJarPath;
    }

    public void setSourceJarPath(String sourceJarPath) {
        this.sourceJarPath = sourceJarPath;
    }

    public String getUnzipDirPath() {
        return unzipDirPath;
    }

    public void setUnzipDirPath(String unzipDirPath) {
        this.unzipDirPath = unzipDirPath;
    }

    public String getIgnorePattern() {
        return ignorePattern;
    }

    public void setIgnorePattern(String ignorePattern) {
        this.ignorePattern = ignorePattern;
    }

    public boolean isNeedScan() {
        return needScan;
    }

    public void setNeedScan(boolean needScan) {
        this.needScan = needScan;
    }
}
