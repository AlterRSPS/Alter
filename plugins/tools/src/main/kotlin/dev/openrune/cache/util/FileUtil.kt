package dev.openrune.cache.util

import java.io.File

object FileUtil {

    lateinit var cacheLocation : File

    fun getTemp() : File {
        val file = File(cacheLocation,"/temp/")
        if(!file.exists()) file.mkdirs()
        return file
    }

    fun getTempDir(dir : String) : File {
        val file = File(cacheLocation,"/temp/${dir}/")
        if(!file.exists()) file.mkdirs()
        return file
    }

}