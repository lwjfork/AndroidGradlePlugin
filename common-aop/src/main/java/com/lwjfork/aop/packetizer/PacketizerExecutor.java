package com.lwjfork.aop.packetizer;

import com.lwjfork.aop.packetizer.model.PacketDirModel;
import com.lwjfork.aop.packetizer.model.PacketJarModel;
import com.lwjfork.aop.utils.FileUtil;
import com.lwjfork.aop.utils.TaskUtil;

import java.io.File;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.concurrent.*;

/**
 * Created by lwj on 2020/8/11.
 * lwjfork@gmail.com
 * 最终的来源为三类
 * 1. jar  包，直接复制到目的路径即可
 * 2. jar  解压的路径，直接将解压的目录压缩为 jar 包，并复制到目的路径即可
 * 3. dir  直接复制到指定目的路径即可
 */
public class PacketizerExecutor {

    ArrayList<PacketDirModel> dirModels = new ArrayList<>();

    ArrayList<PacketJarModel> jarModels = new ArrayList<>();
    boolean deleteUnzipJar = true;

    public PacketizerExecutor(boolean deleteUnzipJar) {
        this.deleteUnzipJar = deleteUnzipJar;
    }

    public void addDirectory(String sourcePath, String destPath, String ignorePattern, boolean needCopy, boolean deleteSrc) {
        PacketDirModel packetDirModel = new PacketDirModel();
        packetDirModel.sourceDirPath = sourcePath;
        packetDirModel.destDirPath = destPath;
        packetDirModel.ignorePattern = ignorePattern;
        packetDirModel.needCopy = needCopy;
        packetDirModel.deleteSrc = deleteSrc;
        this.dirModels.add(packetDirModel);
    }

    public void addDirectory(String sourcePath, String destPath, boolean needCopy, boolean deleteSrc) {
        this.addDirectory(sourcePath, destPath, "", needCopy, deleteSrc);
    }


    public void addDirectory(String sourcePath, String destPath, boolean needCopy) {
        this.addDirectory(sourcePath, destPath, "", needCopy, true);
    }

    public void addDirectory(String sourcePath, String destPath, String ignorePattern) {
        this.addDirectory(sourcePath, destPath, ignorePattern, true, true);
    }

    public void addDirectory(String sourcePath, String destPath) {
        this.addDirectory(sourcePath, destPath, "");
    }


    public void addJar(String sourcePath, String unzipPath, String destPath, String ignorePattern, boolean needCopy, boolean deleteSrc) {
        PacketJarModel jarModel = new PacketJarModel();
        jarModel.sourceJarPath = sourcePath;
        jarModel.unzipDirPath = unzipPath;
        jarModel.destJarPath = destPath;
        jarModel.ignorePattern = ignorePattern;
        jarModel.needCopy = needCopy;
        jarModel.deleteSrc = deleteSrc;
        this.jarModels.add(jarModel);
    }

    public void addJar(String sourcePath, String unzipPath, String destPath, boolean needCopy) {
        this.addJar(sourcePath, unzipPath, destPath, "", needCopy, true);
    }

    public void addJar(String sourcePath, String unzipPath, String destPath, String ignorePattern) {
        this.addJar(sourcePath, unzipPath, destPath, true);
    }

    public void addJar(String sourcePath, String unzipPath, String destPath) {
        this.addJar(sourcePath, unzipPath, destPath, "");
    }

    public void packet() {
        try {
            // 将解压的jar包压缩
            zip();
            // 复制jar 和 dir
            copy();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void copy() throws InterruptedException, ExecutionException {
        ExecutorService exec = Executors.newCachedThreadPool();
        CompletionService<Void> execService = new ExecutorCompletionService<Void>(exec);
        int count = 0;
        for (PacketJarModel jarModel : jarModels) {
            if (jarModel.needCopy) {
                execService.submit(new Callable<Void>() {
                    @Override
                    public Void call() throws Exception {
                        FileUtil.copyZipFile(jarModel.unzipDirPath + ".jar", jarModel.destJarPath, "", StandardCopyOption.REPLACE_EXISTING);
                        if (jarModel.deleteSrc) {
                            FileUtil.deleteIfExists(jarModel.unzipDirPath + ".jar");
                        }
                        return null;
                    }
                });
                count++;
            }
        }
        for (PacketDirModel dirModel : dirModels) {
            if (dirModel.needCopy) {
                execService.submit(new Callable<Void>() {
                    @Override
                    public Void call() throws Exception {
                        FileUtil.copyDirectory(dirModel.sourceDirPath, dirModel.destDirPath, dirModel.ignorePattern, StandardCopyOption.REPLACE_EXISTING);
                        return null;
                    }
                });
                count++;
            }
        }

        for (int i = 0; i < count; i++) {
            execService.take().get();
        }
        exec.shutdown();
    }

    private void zip() throws InterruptedException, ExecutionException {
        ExecutorService exec = Executors.newCachedThreadPool();
        CompletionService<Void> execService = new ExecutorCompletionService<Void>(exec);
        int count = 0;
        for (PacketJarModel jarModel : jarModels) {
            if (!jarModel.needCopy) {
                continue;
            }
            execService.submit(TaskUtil.getTask(() -> {
                try {

                    File dir = new File(jarModel.unzipDirPath);
                    if (dir.exists() && dir.isDirectory() && dir.listFiles() != null && dir.listFiles().length > 0) {
                        FileUtil.zip(jarModel.unzipDirPath, jarModel.unzipDirPath + ".jar", jarModel.ignorePattern, deleteUnzipJar);
                    } else if (dir.exists() && dir.isDirectory() && dir.listFiles() != null && dir.listFiles().length == 0) {
                        // 空文件，直接忽略掉，直接从原来的复制过去吧
                        FileUtil.copyFile(jarModel.sourceJarPath, jarModel.unzipDirPath + ".jar");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }));
            count++;
        }
        for (int i = 0; i < count; i++) {
            execService.take().get();
        }
        exec.shutdown();
    }
}
