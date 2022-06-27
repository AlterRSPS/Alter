package gg.rsmod.plugins.service.restapi.routes

import com.google.gson.Gson
import gg.rsmod.game.model.World
import gg.rsmod.plugins.service.restapi.controllers.CommandsController
import gg.rsmod.plugins.service.restapi.controllers.OnlinePlayersController
import gg.rsmod.plugins.service.restapi.controllers.PlayerController
import spark.Spark.*

class RestApiRoutes {
    fun init(world: World, auth: Boolean) {

        get("/players") {
                req, res -> Gson().toJson(OnlinePlayersController(req, res, false).init(world))
        }

        get("/player/:name") {
                req, res -> Gson().toJson(PlayerController(req, res, false).init(world))
        }
    }
}