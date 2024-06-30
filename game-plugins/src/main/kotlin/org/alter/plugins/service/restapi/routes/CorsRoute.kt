package org.alter.plugins.service.restapi.routes

import spark.Spark.before
import spark.Spark.options

class CorsRoute(origin: String, methods: String, headers: String) {
    init {
        options("/*") { request, response ->

            val accessControlRequestHeaders = request.headers("Access-Control-Request-Headers")
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders)
            }

            val accessControlRequestMethod = request.headers("Access-Control-Request-Method")
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod)
            }

            "OK"
        }

        before { _, response ->
            response.header("Access-Control-Allow-Origin", origin)
            response.header("Access-Control-Request-Method", methods)
            response.header("Access-Control-Allow-Headers", headers)
            response.type("application/json")
        }
    }
}
