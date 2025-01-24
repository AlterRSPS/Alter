package org.alter.plugins.service.restapi.routes

import com.google.gson.Gson
import org.alter.game.model.World
import org.alter.plugins.service.restapi.controllers.OnlinePlayersController
import org.alter.plugins.service.restapi.controllers.PlayerController
import spark.Spark.*

/**
 * @TODO Http-api
 */
class RestApiRoutes {
    fun init(
        world: World,
        auth: Boolean,
    ) {
        get("/players") {
                req, res ->
            Gson().toJson(OnlinePlayersController(req, res, false).init(world))
        }

        get("/player/:name") {
                req, res ->
            Gson().toJson(PlayerController(req, res, false).init(world))
        }
        get("/jav_config.ws") { req, res ->
            val filePath = "../jav_config.ws"
            res.type("application/octet-stream")
            res.header("Content-Disposition", "attachment; filename=jav_config.ws")
            try {
                val file = java.nio.file.Paths.get(filePath)
                val fileContent = java.nio.file.Files.readAllBytes(file)
                res.raw().outputStream.write(fileContent)
                res.raw().outputStream.flush()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            null
        }






        get("/world_list.ws") { req, res ->
            val filePath = "../world_list.ws"  // Replace with the actual path to your file
            // Set response headers to indicate a file download
            res.type("application/octet-stream")  // You can change this to the correct MIME type if known
            res.header("Content-Disposition", "attachment; filename=world_list.ws")
            // Read the file and return its contents as the response
            try {
                val file = java.nio.file.Paths.get(filePath)
                println(file.toAbsolutePath().toString())
                val fileContent = java.nio.file.Files.readAllBytes(file)
                res.raw().outputStream.write(fileContent)
                res.raw().outputStream.flush()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            // Return null as the response is handled directly by writing to the output stream
            null
        }
    }
}
