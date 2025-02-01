package org.alter.plugins.content.objects.crates

import org.alter.api.*
import org.alter.api.cfg.*
import org.alter.api.dsl.*
import org.alter.api.ext.*
import org.alter.game.*
import org.alter.game.model.*
import org.alter.game.model.attr.*
import org.alter.game.model.container.*
import org.alter.game.model.container.key.*
import org.alter.game.model.entity.*
import org.alter.game.model.item.*
import org.alter.game.model.queue.*
import org.alter.game.model.shop.*
import org.alter.game.model.timer.*
import org.alter.game.plugin.*

class SearchCratesPlugin(
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {
        
    init {
        val CRATES =
            setOf(
                "object.crate_354",
                "object.crate_355",
                "object.crate_356",
                "object.crate_357",
                "object.crate_358",
                "object.crate_366",
                "object.crate_1990",
                "object.crate_1999",
                "object.crate_2064",
            )

        CRATES.forEach { crate ->
            onObjOption(obj = crate, option = "search") {
                player.message("You search the crate but find nothing.")
            }
        }
    }
}
