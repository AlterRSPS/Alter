package org.alter.plugins.content.mechanics.multi

val MULTIWAY_VARBIT = 4605

onWorldInit {
    world.getMultiCombatRegions().forEach { region ->
        onEnterRegion(region) {
            player.setVarbit(MULTIWAY_VARBIT, 1)
        }

        onExitRegion(region) {
            player.setVarbit(MULTIWAY_VARBIT, 0)
        }
    }

    world.getMultiCombatChunks().forEach { chunk ->
        onEnterChunk(chunk) {
            player.setVarbit(MULTIWAY_VARBIT, 1)
        }

        onExitChunk(chunk) {
            player.setVarbit(MULTIWAY_VARBIT, 0)
        }
    }
}
