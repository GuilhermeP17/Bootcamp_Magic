package com.bootcamp.bootcampmagic.utils

object FileUtils {

    fun readResourceFile(fileName: String): String {
        val fileInputStream = javaClass.classLoader?.getResourceAsStream(fileName)
        return fileInputStream?.bufferedReader()?.readText() ?: ""
    }

}