package com.lwjfork.gradle.inject.model

import com.squareup.javapoet.FieldSpec
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.TypeSpec

import javax.lang.model.element.Modifier

/**
 * Created by lwj on 2019-09-02.
 * lwjfork@gmail.com
 * 生成 R 文件
 */
class GenerateRFile {

    def packageName
    def className
    def outPutDir

    HashMap<String, ArrayList<ItemModel>> items = new HashMap<>()

    static String[] SupportTypes = ["anim", "array", "attr", "bool", "color", "dimen",
                                    "drawable", "id", "integer", "layout", "menu", "plurals", "string", "style", "styleable"]


    def addItem(def typeName, def idName, def idValue) {
        if (!SupportTypes.contains(typeName)) {
            return
        }
        ItemModel itemModel = new ItemModel()
        itemModel.idName = idName
        itemModel.idValue = idValue
        ArrayList<ItemModel> itemModes = items.get(typeName)
        if (itemModes == null) {
            itemModes = new ArrayList<>()
        }
        itemModes.add(itemModel)
        items.put(typeName, itemModes)
    }


    def generateR2File() {
        TypeSpec.Builder classSpecBuilder = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
        HashMap<String, TypeSpec.Builder> innerSpecBuilder = new HashMap<>()
        SupportTypes.each {
            TypeSpec.Builder innerClassSpecBuilder = TypeSpec.classBuilder(it)
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC)
            innerSpecBuilder.put(it, innerClassSpecBuilder)
        }
        items.eachWithIndex { Map.Entry<String, ArrayList<ItemModel>> entry, int i ->
            String type = entry.key
            if (SupportTypes.contains(type)) {
                if (entry.value != null && entry.value.size() > 0) {
                    TypeSpec.Builder innerClassSpecBuilder = innerSpecBuilder.get(type)
                    if (innerClassSpecBuilder == null) {
                        innerClassSpecBuilder = TypeSpec.classBuilder(it)
                                .addModifiers(Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC)
                        innerSpecBuilder.put(type, innerClassSpecBuilder)
                    }
                    entry.value.each {
                        String idName = it.idName
                        String idValue = it.idValue
                        innerClassSpecBuilder.addField(FieldSpec.builder(int.class, idName)
                                .addModifiers(Modifier.FINAL, Modifier.STATIC, Modifier.PUBLIC)
                                .initializer(idValue).build())
                    }
                    classSpecBuilder.addType(innerClassSpecBuilder.build())
                }
            }
        }
        JavaFile.builder(packageName, classSpecBuilder.build()).build().writeTo(outPutDir)
    }
}
