package com.lwjfork.aop.collector.visitor;


import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Created by lwj on 2020/8/7.
 * lwjfork@gmail.com
 */
public abstract class ClassFileVisitor extends SimpleFileVisitor<Path> {
    protected abstract void collectClass(Path file);

    protected abstract void collectOtherFile(Path file);

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        String path = file.toString();
        if (path.endsWith(".class")) {
            collectClass(file);
        }else {
            collectOtherFile(file);
        }
        return super.visitFile(file, attrs);
    }
}
