package com.lwjfork.aop.packetizer.model;

/**
 * Created by lwj on 2020/8/11.
 * lwjfork@gmail.com
 */
public class PacketJarModel {

    //  jar包源路径
  public   String sourceJarPath;
    // jar 的解压路径
    public  String unzipDirPath;
    // 目的路径
    public  String destJarPath;
    public  String ignorePattern;
    // 是否需要 copy，默认需要复制
    public boolean needCopy = true;
    // 复制的时候，是否要删除源文件
    public  boolean deleteSrc = true;
}
