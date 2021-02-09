package com.lwjfork.gradle.utils

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
