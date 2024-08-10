package dev.openrune.cache.util

import com.google.gson.Gson
import io.github.oshai.kotlinlogging.KotlinLogging
import java.io.File

val logger = KotlinLogging.logger {}

data class Xtea(
    val archive : Int,
    val name_hash : Long,
    val name : String,
    val mapsquare: Int,
    var key: IntArray
)

object XteaLoader {

    public val xteas: MutableMap<Int, Xtea> = mutableMapOf()
    private val xteasList: MutableMap<Int, IntArray> = mutableMapOf()

    fun load(xteaLocation: File = File(FileUtil.getTemp(), "xteas.json")) {
        val data: Array<Xtea> = Gson().fromJson(xteaLocation.readText(), Array<Xtea>::class.java)
        data.forEach {
            xteas[it.mapsquare] = it
            xteasList[it.mapsquare] = it.key
        }
        logger.info { "Keys Loaded: ${xteasList.size}" }
    }

    fun getKeys(region: Int): IntArray? = xteasList[region]
}