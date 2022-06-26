package gg.rsmod.plugins.service.restapi

import gg.rsmod.game.Server
import gg.rsmod.game.model.World
import gg.rsmod.game.service.Service
import gg.rsmod.plugins.service.restapi.routes.CorsRoute
import gg.rsmod.plugins.service.restapi.routes.RestApiRoutes
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