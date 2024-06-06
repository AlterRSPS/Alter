package org.alter.plugins.service.restapi.controllers

import com.google.gson.JsonObject
import org.alter.game.model.World
import org.alter.plugins.service.restapi.auth.Auth
import spark.Request
import spark.Response

abstract class Controller(req: Request, resp: Response, auth: Boolean) {
    protected var authState = true

    init {
        if (auth) {
            val xAuth = req.headers("X-AUTH") ?: ""

            if (xAuth.isEmpty()) {
                // X-AUTH was not present in request headers
                authState = false
            }

            if (!Auth.auth(xAuth)) {
                // X-AUTH was not valid or has expired
                authState = false
            }

            Auth.remove(xAuth)
        }
    }

    abstract fun init(world: World): JsonObject

    fun deploy(): String {
        return Auth.build()
    }
}
