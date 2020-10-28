package com.lwjfork.gradle.utils


import org.gradle.api.Project

/**
 * Created by lwj on 2020-01-03.
 *  lwjfork@gmail.com
 */
class Utils {

    static def printLine() {
        printLine("")
    }


    static def printLine(def msg) {
        25.times {
            print('-')
        }

        print(msg)

        25.times {
            print('-')
        }
        println()
    }

    static def isEmpty(String str) {
        if (!str) {
            return true
        }
        if (str.length() == 0) {
            return true
        }
        return false
    }




}
