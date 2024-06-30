package org.alter.plugins.service.restapi.controllers

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import org.alter.game.model.World
import org.alter.game.model.item.Item
import spark.Request
import spark.Response

class CommandsController(req: Request, resp: Response, auth: Boolean) : Controller(req, resp, auth) {
    val req = req
    val auth = auth

    override fun init(world: World): JsonObject {
        if (!super.authState && auth) {
            val arr = JsonArray()
            val obj = JsonObject()
            obj.addProperty("error", "Auth code not supplied or invalid.")
            arr.add(obj)
            return obj
        }

        val itemId = req.params("id").toInt()
        val amount = req.params("amount").toInt()
        val player = req.params("player")

        world.getPlayerForName(player)?.inventory?.add(Item(itemId, amount))

        return JsonObject()
    }
}
