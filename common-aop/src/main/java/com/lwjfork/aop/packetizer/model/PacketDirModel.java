package com.lwjfork.aop.packetizer.model;

/**
 * Created by lwj on 2020/8/11.
 * lwjfork@gmail.com
 */
public class PacketDirModel {

    // 源路径
   public String sourceDirPath;
    // 目的路径
    public String destDirPath;
    // 是否需要 copy，默认需要复制
    public  boolean needCopy = true;
    // 复制的时候，是否要删除源文件
    public  boolean deleteSrc = true;
    public String ignorePattern;

}
