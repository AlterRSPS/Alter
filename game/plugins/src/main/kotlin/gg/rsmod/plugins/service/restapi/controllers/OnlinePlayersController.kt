package gg.rsmod.plugins.service.restapi.controllers

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import gg.rsmod.game.model.World
import spark.Request
import spark.Response

class OnlinePlayersController(req: Request, resp: Response, auth: Boolean) : Controller(req, resp, auth) {

    override fun init(world: World): JsonObject {

        val obj = JsonObject()
        val players = JsonArray()

        obj.addProperty("count", world.players.count())

        world.players.forEach { player ->
            val pObj = JsonObject()

            // Main Player Object

            pObj.addProperty("username", player.username)
            pObj.addProperty("privilege", player.privilege.id)
            pObj.addProperty("gameMode", player.gameMode)
            pObj.addProperty("combatLvl", player.combatLevel)

            val pos = JsonObject()

            // Player Position Object

            pos.addProperty("regionId", player.tile.regionId)
            pos.addProperty("xPos", player.tile.x)
            pos.addProperty("yPos", player.tile.height)
            pos.addProperty("zPos", player.tile.z)

            val posArr = JsonArray()
            posArr.add(pos)

            pObj.add("position", posArr)
            players.add(pObj)
        }

        obj.add("players", players)

        return obj
    }
}