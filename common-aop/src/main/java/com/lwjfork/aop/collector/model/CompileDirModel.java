package com.lwjfork.aop.collector.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 参与编译的单独的 dir 目录 文件信息
 * 包含了 dir 目录 包中的文件信息
 */
public class CompileDirModel implements Serializable {
    // 目录 路径
    private String sourcePath;

    private ArrayList<CompileSingleFileModel> childFiles = new ArrayList<>();

    private boolean needScan = true;

    public CompileDirModel(String sourcePath, boolean needScan) {
        this.sourcePath = sourcePath;
        this.needScan = needScan;
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
}
