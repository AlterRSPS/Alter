package org.alter.plugins.service.restapi.controllers

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import io.github.oshai.kotlinlogging.KotlinLogging
import org.alter.api.Skills
import org.alter.game.model.World
import spark.Request
import spark.Response

class PlayerController(req: Request, resp: Response, auth: Boolean) : Controller(req, resp, auth) {
    val req = req
    private val logger = KotlinLogging.logger {}

    override fun init(world: World): JsonObject {
        val obj = JsonObject()
        val user = JsonArray()

        logger.info { "Username: ${req.params("name")}"  }

        logger.info { "params: ${req.params()}"  }

        val username = req.params("name")

        world.players.forEach { player ->

            if (player.username == username) {
                val pObj = JsonObject()

                // Main Player Object

                pObj.addProperty("username", player.username)
                pObj.addProperty("privilege", player.privilege.id)
                pObj.addProperty("gameMode", player.gameMode)
                pObj.addProperty("combatLvl", player.combatLevel)
                pObj.addProperty("isOnline", player.isOnline)
                pObj.addProperty("xpRate", player.xpRate)
                pObj.addProperty("UID", player.uid.toString())

                val skillArr = JsonArray()

                for (i in 0..22) {
                    val skill = JsonObject()
                    skill.addProperty("name", Skills.getSkillName(world, player.getSkills().get(i).id))
                    skill.addProperty("currentLevel", player.getSkills().get(i).currentLevel)
                    skillArr.add(skill)
                }

                pObj.add("skills", skillArr)

                if (player.isOnline) {
                    val pos = JsonObject()

                    // Player Position Object

                    pos.addProperty("regionId", player.tile.regionId)
                    pos.addProperty("xPos", player.tile.x)
                    pos.addProperty("yPos", player.tile.height)
                    pos.addProperty("zPos", player.tile.z)

                    val posArr = JsonArray()
                    posArr.add(pos)

                    pObj.add("position", posArr)
                }

                user.add(pObj)
            }
        }

        obj.add("player", user)

        return obj
    }
}
