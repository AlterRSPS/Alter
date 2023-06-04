package org.alter.plugins.service.restapi

import org.alter.game.Server
import org.alter.game.model.World
import org.alter.game.service.Service
import org.alter.plugins.service.restapi.routes.CorsRoute
import org.alter.plugins.service.restapi.routes.RestApiRoutes
import gg.rsmod.util.ServerProperties
import spark.Spark.stop

class RestApiService : Service {
    override fun init(server: Server, world: World, serviceProperties: ServerProperties) {
        CorsRoute(serviceProperties.getOrDefault("origin", "*"), serviceProperties.getOrDefault("methods", "GET, POST"), serviceProperties.getOrDefault("headers", "X-PINGOTHER, Content-Type"))
        RestApiRoutes().init(world, serviceProperties.getOrDefault("auth", false))
    }

    override fun postLoad(server: Server, world: World) {}

    override fun bindNet(server: Server, world: World) {}

    override fun terminate(server: Server, world: World) {
        stop()
    }

}