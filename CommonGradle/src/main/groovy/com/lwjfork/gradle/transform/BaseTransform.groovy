package com.lwjfork.gradle.transform

import com.android.build.api.transform.*
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils
import org.gradle.api.Project

/**
 * Created by lwj on 2020-01-04.
 *  lwjfork@gmail.com
 */
abstract class BaseTransform extends Transform {

    Project project

    BaseTransform(Project project) {
        this.project = project
    }


    @Override
    final void transform(Context context, Collection<TransformInput> inputs, Collection<TransformInput> referencedInputs, TransformOutputProvider outputProvider, boolean isIncremental) throws IOException, TransformException, InterruptedException {
        def clearCache = !isIncremental
        // 非增量更新删除上次构建信息
        if (clearCache) {
            outputProvider.deleteAll()
        }
        delegateTransform(context, inputs, referencedInputs, outputProvider, isIncremental)
    }


    abstract void delegateTransform(Context context,
                                    Collection<TransformInput> inputs,
                                    Collection<TransformInput> referencedInputs,
                                    TransformOutputProvider outputProvider,
                                    boolean isIncremental)
            throws IOException, TransformException, InterruptedException


    /**
     * 拷贝 DirectoryInput
     */
    final def copyDir(TransformOutputProvider output, DirectoryInput input) {
        File dest = output.getContentLocation(input.name, input.contentTypes, input.scopes, Format.DIRECTORY)
        FileUtils.copyDirectory(input.file, dest)
    }

    /**
     * 拷贝 Jar
     */
    final def copyJar(TransformOutputProvider output, JarInput input) {
        File jar = input.file
        String destName = input.name
        def hexName = DigestUtils.md5Hex(jar.absolutePath)
        if (destName.endsWith('.jar')) {
            destName = destName.substring(0, destName.length() - 4)
        }
        File dest = output.getContentLocation(destName + '_' + hexName, input.contentTypes, input.scopes, Format.JAR)
        FileUtils.copyFile(jar, dest)
    }


}
