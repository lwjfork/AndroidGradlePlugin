package com.lwjfork.aop.utils.visitor;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.regex.Pattern;

/**
 * Created by lwj on 2020/8/11.
 * lwjfork@gmail.com
 */
public class UnzipDirectoryVisitor extends SimpleFileVisitor<Path> {

    Path sourcePath;
    Path destDirectory;
    String ignorePathPattern;
    boolean isDelete;


    /**
     * @param sourcePath        源路径
     * @param destDirectory     目的路径
     * @param ignorePathPattern 忽略文件的正则
     */
    public UnzipDirectoryVisitor(Path sourcePath, Path destDirectory, String ignorePathPattern,boolean isDelete) {
        this.sourcePath = sourcePath;
        this.destDirectory = destDirectory;
        this.ignorePathPattern = ignorePathPattern;
        this.isDelete = isDelete;
    }

    public UnzipDirectoryVisitor(Path sourcePath, Path destDirectory,boolean isDelete) {
        this(sourcePath, destDirectory, "",isDelete);
    }

    @SuppressWarnings("unused")
    public UnzipDirectoryVisitor(Path sourcePath, Path destDirectory) {
        this(sourcePath, destDirectory, false);
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        String realtivePathStr = dir.toString().substring(1);
        if (realtivePathStr.length() == 0) {
            return FileVisitResult.CONTINUE;
        }
        String destPathStr = destDirectory.toString() + File.separator + realtivePathStr;
        if (!Pattern.matches(ignorePathPattern, realtivePathStr)) {
            Files.createDirectories(Paths.get(destPathStr));
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        String realtivePathStr = file.toString().substring(1);
        String destPathStr = destDirectory.toString() + File.separator + realtivePathStr;
        if (!Pattern.matches(ignorePathPattern, realtivePathStr)) {
            Path dest = Paths.get(destPathStr);
            Files.deleteIfExists(dest);
            Files.copy(file, dest);
        }
        return super.visitFile(file, attrs);
    }

}