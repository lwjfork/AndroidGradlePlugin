package com.lwjfork.aop.utils.visitor;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.regex.Pattern;

/**
 * Created by lwj on 2020/8/11.
 * lwjfork@gmail.com
 * 复制目录
 */
public class CopyDirectoryVisitor extends SimpleFileVisitor<Path> {

    Path source;
    Path dest;
    Path target;
    boolean move ;
    String ignorePathPattern;
    CopyOption[] options;


    /**
     * @param source  源路径
     * @param target  目的路径
     * @param ignorePathPattern   忽略文件的正则
     * @param move    false表示复制，true表示移动，默认是复制
     * @param options 移动时的选项参数
     */
    public CopyDirectoryVisitor(Path source, Path target, boolean move,String ignorePathPattern, CopyOption... options) {
        this.source = source;
        this.target = target;
        this.dest = target.resolve(source.getFileName());
        this.move = move;
        this.ignorePathPattern = ignorePathPattern;
        this.options = options;
    }

    @SuppressWarnings("unused")
    public CopyDirectoryVisitor(Path source, Path target, CopyOption... options) {
        this(source, target, false,"", options);
    }


    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        // 在目标文件夹中创建dir对应的子文件夹
        if (dir.compareTo(source) == 0) {
            if(!Files.exists(target)){
                Files.createDirectories(target);
            }
            return FileVisitResult.CONTINUE;
        }
        Path realtivePath = dir.subpath(source.getNameCount(), dir.getNameCount());
        Path subDir = dest.resolveSibling(realtivePath);
        if(!Pattern.matches(ignorePathPattern,realtivePath.toString())){
            Files.createDirectories(subDir);
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        Path realtivePath = file.subpath(source.getNameCount(), file.getNameCount());
        if(!Pattern.matches(ignorePathPattern,realtivePath.toString())){
            if (move) {
                Files.move(file, dest.resolveSibling(realtivePath), this.options);
            } else {
                Files.copy(file, dest.resolveSibling(realtivePath), this.options);
            }
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        // 移动操作时删除源文件夹
        if (move) {
            Files.delete(dir);
        }
        return super.postVisitDirectory(dir, exc);
    }
}