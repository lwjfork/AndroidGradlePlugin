package com.lwjfork.gradle.utils

import java.util.zip.ZipFile

/**
 * Created by lwj on 2020-01-06.
 *  lwjfork@gmail.com
 */
class ZipUtil {

    /**
     * 压缩 dirPath 到 zipFilePath
     */
    static def zipDir(String dirPath, String zipFilePath) {
        File dir = new File(dirPath)
        if (dir.exists()) {
            new AntBuilder().zip(destfile: zipFilePath, basedir: dirPath)
        } else {
            println ">>> Zip file is empty! Ignore"
        }
    }

    /**
     * 解压 zipFilePath 到 目录 dirPath
     */
    static boolean unzip(String zipFilePath, String dirPath) {
        // 若这个Zip包是空内容的（如引入了Bugly就会出现），则直接忽略
        if (isZipEmpty(zipFilePath)) {
            println ">>> Zip file is empty! Ignore"
            return false
        }
        new AntBuilder().unzip(src: zipFilePath, dest: dirPath, overwrite: 'true')
        return true
    }

    static boolean isZipEmpty(String zipFilePath) {
        ZipFile z
        try {
            z = new ZipFile(zipFilePath)
            return z.size() == 0
        } finally {
            if (z != null) {
                z.close()
            }
        }
    }


    /**
     * 将解压的 class 文件重新打包，然后删除 解压的class 文件
     * @param srcJarPath jar 的原路径
     * @param map jar 的原路径-- jar包解压后的路径
     * @return
     */
    static def reZip(HashSet<String> srcJarPath, HashMap<String, String> map) {
        srcJarPath.each { String it ->
            File jar = new File(it)
            String JarAfterZip = map.get(jar.getParent() + File.separatorChar + jar.getName())
            String dirAfterUnzip = JarAfterZip.replace('.jar', '')
            zipDir(dirAfterUnzip, JarAfterZip)
            FileUtils.deleteDirectory(new File(dirAfterUnzip))
        }
    }

}
