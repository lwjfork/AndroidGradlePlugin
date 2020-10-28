package com.lwjfork.gradle.transform


import com.android.build.gradle.LibraryExtension
import org.gradle.api.Project

/**
 * Created by lwj on 2020-01-04.
 *  lwjfork@gmail.com
 */
abstract class BaseLibraryTransform extends BaseTransform {


    LibraryExtension libraryExtension

    BaseLibraryTransform(Project project, LibraryExtension libraryExtension) {
        super(project)
        this.libraryExtension = libraryExtension
    }
}
