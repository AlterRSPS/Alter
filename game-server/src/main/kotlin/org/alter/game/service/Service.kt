package org.alter.game.service

import org.alter.game.Server
import org.alter.game.model.World
import gg.rsmod.util.ServerProperties

/**
 * Any service that should be initialized when our server is starting up.
 *
 * @author Tom <rspsmods@gmail.com>
 */
interface Service {

    /**
     * Called when the server is starting up.
     */
    fun init(server: org.alter.game.Server, world: World, serviceProperties: ServerProperties)

    /**
     * Called after the server has finished started up.
     */
    fun postLoad(server: org.alter.game.Server, world: World)

    /**
     * Called after the server sets its bootstrap's children and parameters.
     */
    fun bindNet(server: org.alter.game.Server, world: World)

    /**
     * Called when the server is shutting off.
     */
    fun terminate(server: org.alter.game.Server, world: World)
}