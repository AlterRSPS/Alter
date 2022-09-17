package gg.rsmod.plugins.content.music

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import gg.rsmod.game.Server
import gg.rsmod.game.model.World
import gg.rsmod.game.service.Service
import gg.rsmod.util.ServerProperties
import mu.KLogging
import java.io.File
import java.io.FileNotFoundException

class RegionMusicService : Service {
    var listiner = hashMapOf<Int, IntArray>()
    //
    override fun init(server: Server, world: World, serviceProperties: ServerProperties) {
        val filez = File("./data/cfg/music/music_by_region.yaml")
        if (!filez.exists()) {
            throw FileNotFoundException("Path does not exist $filez.")
        }
        var mapper = ObjectMapper(YAMLFactory())
        filez.bufferedReader().use {

        }
        val newFile = File("./data/cfg/music/music_new.yaml")
        if(!newFile.exists()) { newFile.createNewFile() }

        val newMapper = ObjectMapper(YAMLFactory())
        newMapper.writeValue(newFile, listiner)
    }

    override fun postLoad(server: Server, world: World) {
    }
    override fun bindNet(server: Server, world: World) {
    }
    override fun terminate(server: Server, world: World) {
    }
    companion object : KLogging()
}
data class NeededType (
    val regionID: Int = -1,
    val musicID: Int = -1
)
