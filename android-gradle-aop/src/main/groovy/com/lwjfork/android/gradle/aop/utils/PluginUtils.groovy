package com.lwjfork.android.gradle.aop.utils

import com.google.gson.GsonBuilder

class PluginUtils {

    /**
     * 判断是否是 class 文件
     * @param path
     * @return
     */
    static boolean isClassFile(String path) {
        return path.endsWith(".class") && !path.endsWith("module-info.class")
    }

    /**
     * 根据绝对路径，同时读取 magic 值 判断是否是字节码文件
     * @param absolutePath
     * @return
     */
    static boolean isClassFileByAbsolutePath(String absolutePath) {
        if(!isClassFile(absolutePath)){
            return false
        }
        File file = new File(absolutePath)
        FileInputStream fi = new FileInputStream(file)
        byte[] magicBytes = new byte[4]
        int num = 0;
        if(fi.read(magicBytes)>0){
            for (int i= 0; i < magicBytes.length; i++) {
                num <<= 8
                num |= (magicBytes[i] & 0xff)
            }
        }
        fi.close()
        return  "cafebabe".equals(Integer.toHexString(num))
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
