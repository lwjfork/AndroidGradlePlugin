package com.lwjfork.aop.utils.visitor;


import com.lwjfork.aop.collector.model.CompileSingleFileModel;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Created by lwj on 2020/8/13.
 * lwjfork@gmail.com
 * 收集目录下的所有文件
 */
public class CollectFileVisitor extends SimpleFileVisitor<Path> {

    ArrayList<CompileSingleFileModel> files ;
   int subLength ;
   String ignorePattern;

    public CollectFileVisitor(ArrayList<CompileSingleFileModel> files, String ignorePattern, int subLength) {
        this.files = files;
        this.ignorePattern = ignorePattern;
        this.subLength = subLength;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        String path = file.toString().substring(subLength);
        if(!Pattern.matches(ignorePattern,path)){
            files.add(new CompileSingleFileModel(path));
        }else {
            files.add(new CompileSingleFileModel(path,false));
        }
        return super.visitFile(file,attrs);
    }
}
