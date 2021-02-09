package com.lwjfork.aop.utils.visitor;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.regex.Pattern;

/**
 * Created by lwj on 2020/8/14.
 * lwjfork@gmail.com
 */
public class DeleteFileFromZipVisitor extends SimpleFileVisitor<Path> {

    String ignorePathPattern;

    public DeleteFileFromZipVisitor(String ignorePathPattern) {
        this.ignorePathPattern = ignorePathPattern;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        if (Pattern.matches(ignorePathPattern, dir.toString().substring(1))) {
                Files.delete(dir);
        }
        return super.postVisitDirectory(dir, exc);
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        if(Pattern.matches(ignorePathPattern,file.toString().substring(1))){
            Files.delete(file);
        }
        return super.visitFile(file, attrs);
    }
}
