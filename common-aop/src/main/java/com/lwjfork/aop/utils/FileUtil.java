package com.lwjfork.aop.utils;


import com.lwjfork.aop.collector.model.CompileSingleFileModel;
import com.lwjfork.aop.utils.visitor.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by lwj on 2020/8/11.
 * lwjfork@gmail.com
 */
public class FileUtil {

    /**
     * 复制文件
     * @param sourcePath 源文件路径
     * @param destPath   目的路径
     * @param options    复制选项
     * @throws IOException 异常
     */
    public static void copyFile(String sourcePath, String destPath, CopyOption... options) throws IOException {
        Files.copy(Paths.get(sourcePath), Paths.get(destPath), options);
    }


    /**
     * 移动文件
     * @param sourcePath 源文件路径
     * @param destPath   目的路径
     * @param options    复制选项
     * @throws IOException 异常
     */
    public static void moveFile(String sourcePath, String destPath, CopyOption... options) throws IOException {
        Files.move(Paths.get(sourcePath), Paths.get(destPath), options);
    }

    /**
     * 复制zip文件
     * @param sourcePath 源文件路径
     * @param destPath   目的路径
     * @param ignorePattern   忽略压缩包中的某些文件
     * @param options    复制选项
     * @throws IOException 异常
     */
    @SuppressWarnings("unused")
    public static void copyZipFile(String sourcePath, String destPath, String ignorePattern, CopyOption... options) throws IOException {
        copyFile(sourcePath,destPath,options);
        if(ignorePattern != null && ignorePattern.length() > 0){
            deleteFileFromZip(destPath,ignorePattern);
        }
    }
    /**
     * 移动zip文件
     * @param sourcePath 源文件路径
     * @param destPath   目的路径
     * @param ignorePattern   忽略压缩包中的某些文件
     * @param options    复制选项
     * @throws IOException 异常
     */
    @SuppressWarnings("unused")
    public static void moveZipFile(String sourcePath, String destPath, String ignorePattern, CopyOption... options) throws IOException {
        moveFile(sourcePath,destPath,options);
        if(ignorePattern != null && ignorePattern.length() > 0){
            deleteFileFromZip(destPath,ignorePattern);
        }
    }


    /**
     * 删除压缩包中的部分内容
     * @param zipPath   压缩包文件路径
     * @param deletePattern  删除文件的正则
     * @throws IOException  异常
     */
    public static void deleteFileFromZip(String zipPath, String deletePattern) throws IOException {
        Path dest = Paths.get(zipPath);
        FileSystem fs = FileSystems.newFileSystem(dest, null);
        Files.walkFileTree(fs.getPath(fs.getSeparator()), new DeleteFileFromZipVisitor(deletePattern));
        fs.close();
    }


    /**
     * 复制目录
     *
     * @param sourceDirectory 源路径
     * @param destDirectory   目的路径
     * @param options         复制的选项
     * @throws IOException 异常IO
     */
    @SuppressWarnings("unused")
    public static void copyDirectory(String sourceDirectory, String destDirectory, CopyOption... options) throws IOException {
        copyDirectory(sourceDirectory, destDirectory, "", options);
    }

    /**
     * 复制目录
     *
     * @param sourceDirectory   源路径
     * @param destDirectory     目的路径
     * @param ignorePathPattern 忽略文件路径的正则
     * @param options           复制的选项
     * @throws IOException 异常IO
     */
    public static void copyDirectory(String sourceDirectory, String destDirectory, String ignorePathPattern, CopyOption... options) throws IOException {
        Path source = Paths.get(sourceDirectory);
        Path target = Paths.get(destDirectory);
        copyOrMoveDirectory(source, target, false, ignorePathPattern, options);
    }


    /**
     * 移动目录
     *
     * @param sourceDirectory 源路径
     * @param destDirectory   目的路径
     * @param options         移动的选项
     * @throws IOException 异常IO
     */
    @SuppressWarnings("unused")
    public static void moveDirectory(String sourceDirectory, String destDirectory, CopyOption... options) throws IOException {
        moveDirectory(sourceDirectory, destDirectory, "", options);
    }

    /**
     * 移动目录
     *
     * @param sourceDirectory   源路径
     * @param destDirectory     目的路径
     * @param ignorePathPattern 忽略文件的路径正则
     * @param options           移动的选项
     * @throws IOException 异常IO
     */
    public static void moveDirectory(String sourceDirectory, String destDirectory, String ignorePathPattern, CopyOption... options) throws IOException {
        Path source = Paths.get(sourceDirectory);
        Path target = Paths.get(destDirectory);
        copyOrMoveDirectory(source, target, true, ignorePathPattern, options);
    }


    /**
     * 移动目录
     *
     * @param source            源路径
     * @param target            目的路径
     * @param ignorePathPattern 忽略文件的正则
     * @param options           移动的选项
     * @throws IOException 异常IO
     */
    private static void copyOrMoveDirectory(Path source, Path target, boolean move, String ignorePathPattern, CopyOption... options) throws IOException {
        Path dest = target.resolve(source.getFileName());
        // 如果相同则返回
        if (Files.exists(dest) && Files.isSameFile(source, dest)) {

            return;
        }
        boolean clear = true;
        for (CopyOption option : options)
            if (StandardCopyOption.REPLACE_EXISTING == option) {
                clear = false;
                break;
            }
        // 如果指定了REPLACE_EXISTING选项则不清除目标文件夹
        if (clear) {
            deleteIfExists(dest);
        }
        Files.walkFileTree(source, new CopyDirectoryVisitor(source, target, move, ignorePathPattern, options));
    }

    /**
     * 强制删除文件/文件夹(含不为空的文件夹)
     *
     * @param path 文件/文件夹目录
     * @throws IOException 异常 IO
     */
    public static void deleteIfExists(Path path) throws IOException {
        if(Files.exists(path)){
            if(path.toFile().isDirectory()){
                Files.walkFileTree(path, new DeleteVisitor());
            }else {
                Files.deleteIfExists(path);
            }
        }
    }

    /**
     * 强制删除文件/文件夹(含不为空的文件夹)
     *
     * @param path 文件/文件夹目录
     * @throws IOException 异常IO
     */
    public static void deleteIfExists(String path) throws IOException {
        deleteIfExists(Paths.get(path));
    }


    /**
     * 解压压缩文件到指定目录
     * @param sourcePath 源文件-压缩包
     * @param destDirectory 目的目录路径
     * @param isDelete 是否要删除源文件
     * @throws IOException 异常信息
     */
    public static void unzip(String sourcePath, final String destDirectory, boolean isDelete) throws IOException {
        unzip(sourcePath, destDirectory, "", isDelete);
    }

    /**
     * 解压压缩文件到指定目录
     * @param sourcePath 源文件-压缩包
     * @param destDirectory 目的目录路径
     * @param ignorePathPattern 忽略文件正则
     * @param isDelete 是否要删除源文件
     * @throws IOException 异常信息
     */
    public static void unzip(String sourcePath, final String destDirectory, String ignorePathPattern, boolean isDelete) throws IOException {
        FileSystem fs = FileSystems.newFileSystem(Paths.get(sourcePath), null);
        Path source = Paths.get(sourcePath);
        Path dest = Paths.get(destDirectory);
        FileUtil.deleteIfExists(dest);
        Files.createDirectories(dest);
        Files.walkFileTree(fs.getPath(fs.getSeparator()), new UnzipDirectoryVisitor(source, dest, ignorePathPattern, isDelete));
        fs.close();
        if (isDelete) {
            deleteIfExists(sourcePath);
        }
    }

    /**
     * @param sourceDirectory 源目录
     * @param destZipPath     压缩包路径
     * @param isDelete        是否删除源文件
     * @throws IOException 异常
     */
    public static void zip(String sourceDirectory, final String destZipPath, boolean isDelete) throws IOException {
        zip(sourceDirectory, destZipPath, "", isDelete);
    }

    /**
     * @param sourceDirectory   源目录
     * @param destZipPath       压缩包路径
     * @param ignorePathPattern 忽略文件路径的正则
     * @param isDelete          是否删除源文件
     * @throws IOException 异常
     */
    public static void zip(String sourceDirectory, final String destZipPath, String ignorePathPattern, boolean isDelete) throws IOException {
        File sourceFile = new File(sourceDirectory);
        if (!sourceFile.exists()) {
            return;
        }
        ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(new File(destZipPath)));
        zip(sourceDirectory, sourceFile, ignorePathPattern, zipOutputStream);
        zipOutputStream.flush();
        zipOutputStream.close();
        if (isDelete) {
            deleteIfExists(sourceDirectory);
        }
    }

    /**
     * 压缩目录
     *
     * @param sourceDirectory 源目录路径
     * @param file  需要压缩进压缩包的文件或目录
     * @param ignorePathPattern 忽略文件路径正则
     * @param zipOutputStream  zipOutputStream
     * @throws IOException 异常信息
     */
    private static void zip(String sourceDirectory, File file, String ignorePathPattern, ZipOutputStream zipOutputStream) throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        if (file.isFile()) {
            String absolutePath = file.getAbsolutePath();
            String realtivePath = absolutePath.substring(sourceDirectory.length() + 1);
            if (!Pattern.matches(ignorePathPattern, realtivePath)) {
                zipOutputStream.putNextEntry(new ZipEntry(realtivePath));
                FileChannel channel = new FileInputStream(file).getChannel();
                while (true) {
                    byteBuffer.clear();
                    int read = channel.read(byteBuffer);
                    if (read == -1) break;
                    zipOutputStream.write(byteBuffer.array());
                }
                channel.close();
                zipOutputStream.closeEntry();
            }
        } else {
            File[] files = file.listFiles();
            if (files == null || files.length == 0) {
                // 空文件，直接忽略掉吧
//                String absolutePath = file.getAbsolutePath();
//                String realtivePath = absolutePath.substring(sourceDirectory.length() + 1);
//                if (!Pattern.matches(ignorePathPattern, realtivePath)) {
//                    zipOutputStream.putNextEntry(new ZipEntry(realtivePath + File.separator));
//                    zipOutputStream.closeEntry();
//                }

            } else {
                for (File file2 : files) {
                    zip(sourceDirectory, file2, ignorePathPattern, zipOutputStream);
                }
            }
        }
    }


    /**
     * 扫描目录或者压缩包中的所有文件（深层遍历）
     *
     * @param sourcePath    源目录
     * @param ignorePattern 忽略文件正则
     * @param isDirectory   是否是目录 true 是，false表示为压缩包
     * @return 文件相对路径集合
     * @throws IOException 异常信息
     */
    public static ArrayList<CompileSingleFileModel> collectFiles(String sourcePath, String ignorePattern, boolean isDirectory) throws IOException {
        ArrayList<CompileSingleFileModel> result = new ArrayList<>();
        Path source = Paths.get(sourcePath);
        if (isDirectory) {
            Files.walkFileTree(source, new CollectFileVisitor(result, ignorePattern, source.toString().length() + 1));
        } else {
            FileSystem fs = FileSystems.newFileSystem(source, null);
            Files.walkFileTree(fs.getPath(fs.getSeparator()), new CollectFileVisitor(result, ignorePattern, 1));
        }
        return result;
    }

    /**
     * 扫描目录或者压缩包中的所有文件（深层遍历）
     *
     * @param sourcePath 源目录
     * @return 文件相对路径集合
     * @throws IOException 异常信息
     */
    public static ArrayList<CompileSingleFileModel> collectFiles(String sourcePath) throws IOException {
        return collectFiles(sourcePath, "", true);
    }

    /**
     * 扫描目录或者压缩包中的所有文件（深层遍历）
     *
     * @param sourcePath  源目录
     * @param isDirectory 是否是目录 true 是，false表示为压缩包
     * @return 文件相对路径集合
     * @throws IOException 异常信息
     */
    @SuppressWarnings("unused")
    public static ArrayList<CompileSingleFileModel> collectFiles(String sourcePath, boolean isDirectory) throws IOException {
        return collectFiles(sourcePath, "", isDirectory);
    }

}
