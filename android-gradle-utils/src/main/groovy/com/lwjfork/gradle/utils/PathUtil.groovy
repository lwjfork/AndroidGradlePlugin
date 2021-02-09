package com.lwjfork.gradle.utils

import com.android.build.gradle.internal.pipeline.IntermediateFolderUtils
import org.apache.commons.codec.digest.DigestUtils
import org.gradle.api.Project

/**
 * Created by lwj on 2020/8/19.
 *  lwjfork@gmail.com
 */
class PathUtil {
    static def getBuildDir(Project project) {
        project.buildDir.absolutePath
    }
    static def getGeneratedDir(Project project) {
        getBuildDir(project) + File.separator + 'generated'
    }

    static def getOutPutDir(Project project) {
        getBuildDir(project) + File.separator + "outputs"
    }
    static def getIntermediatesDir(Project project) {
        IntermediateFolderUtils
        return project.buildDir.absolutePath + File.separator +
                'intermediates' + File.separator
    }
    static def getBuildApkOutDir(Project project) {
        return project.buildDir.absolutePath + File.separator +
                'outputs' + File.separator + "apk"
    }
    /**
     * 通过输入的 JarInput 生成输出 jar 包的名称，防止出现名称冲突
     * @param sourceJarFile
     * @return
     */
    static def getJarInputDestFileName(File sourceJarFile) {
        String destName = sourceJarFile.name
        def hexName = DigestUtils.md5Hex(sourceJarFile.absolutePath)
        if (destName.endsWith('.jar')) {
            destName = destName.substring(0, destName.length() - 4)
        }
        "${destName}_${hexName}"
    }
}
