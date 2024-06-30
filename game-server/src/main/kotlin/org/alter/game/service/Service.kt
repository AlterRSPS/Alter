package org.alter.game.service

import gg.rsmod.util.ServerProperties
import org.alter.game.model.World

/**
 * Any service that should be initialized when our server is starting up.
 *
 * @author Tom <rspsmods@gmail.com>
 */
interface Service {
    /**
     * Called when the server is starting up.
     */
    fun init(
        server: org.alter.game.Server,
        world: World,
        serviceProperties: ServerProperties,
    ) {
        // Default implementation (no-op)
    }

    /**
     * Called after the server has finished started up.
     */
    fun postLoad(
        server: org.alter.game.Server,
        world: World,
    ) {
        // Default implementation (no-op)
    }

    /**
     * Called after the server sets its bootstrap's children and parameters.
     */
    fun bindNet(
        server: org.alter.game.Server,
        world: World,
    ) {
        // Default implementation (no-op)
    }

    /**
     * Called when the server is shutting off.
     */
    fun terminate(
        server: org.alter.game.Server,
        world: World,
    ) {
        // Default implementation (no-op)
    }
}
