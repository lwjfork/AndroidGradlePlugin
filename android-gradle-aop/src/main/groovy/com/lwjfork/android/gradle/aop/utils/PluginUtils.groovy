package com.lwjfork.android.gradle.aop.utils

import com.google.gson.GsonBuilder

class PluginUtils {

    /**
     * 判断是否是 class 文件
     * @param path
     * @return
     */
    static boolean isClassFile(String path) {
        return path.endsWith(".class")
    }

    /**
     * 通过相对路径获取类名
     * @param path
     * @return
     */
    static String getClassNameByPath(String path) {
        return path.replace('\\', '.').replace('/', '.').replace('.class', '')
    }



    static def printJsonFile(Object object, String path, String fileName) {
        String json = new GsonBuilder().enableComplexMapKeySerialization().setPrettyPrinting().create().toJson(object)
        printJsonStr2File(json, path, fileName)
    }

    static def printJsonStr2File(String jsonStr, String path, String fileName) {
        File file = new File(path)
        // 删除不是目录
        if (file.exists() && !file.isDirectory()) {
            file.delete()
            file.mkdirs()
        } else {
            file.mkdirs()
        }
        File jsonFile = new File(path, fileName + ".json")
        if (!jsonFile.exists()) {
            jsonFile.createNewFile()
        }
        jsonFile.write(jsonStr)
    }

    static def readJsonFile(String path, String fileName) {
        File file = new File(path)
        // 删除不是目录
        if (file.exists() && !file.isDirectory()) {
            file.delete()
            file.mkdirs()
        } else {
            file.mkdirs()
        }
        File jsonFile = new File(path, fileName + ".json")
        if (!jsonFile.exists()) {
            return null
        } else {
            return jsonFile.text
        }
    }

    static def isEmptyStr(String str) {
        return str == null || str.length() == 0
    }
}
