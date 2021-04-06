package com.lwjfork.android.gradle.aop.analyzer.common

import com.lwjfork.android.gradle.aop.utils.PluginUtils

abstract class ICommonAnalyzer {

    boolean isClassFile(String dir, String subPath) {
        String path = null
        if (!dir.endsWith(File.separator) && !subPath.startsWith(File.separator)) {
            path = dir + File.separator + subPath
        } else if (dir.endsWith(File.separator) && subPath.startsWith(File.separator)) {
            path = dir + subPath.substring(1)
        } else {
            path = dir + subPath
        }
        if (path != null && path.length() > 0) {
            return PluginUtils.isClassFileByAbsolutePath(path)
        }
        return  false
    }

    /**
     * 判断是否是 class 文件
     * @param path
     * @return
     */
     boolean isClassFile(String path) {
        return path.endsWith(".class") && !path.endsWith("module-info.class")
    }

}
