package gg.rsmod.plugins.content.mechanics.npcdrops

import gg.rsmod.plugins.content.mechanics.doors.DoorStickState
import it.unimi.dsi.fastutil.objects.ObjectArrayList

load_metadata {
    propertyFileName = "npcdrops"

    author = "Me"
    name = "Npc Drops"
    description = "Drops monster loot"

    properties()
}

load_service(NpcDropsService())

on_world_init {
    world.getService(NpcDropsService::class.java)?.let { service ->
        service.drops.forEach { drop ->
            on_npc_death(npc = drop.key) {
                drop_loot(npc, drop.value)
            }
        }
    }
}

fun drop_loot(npc: Npc, dropSets: ObjectArrayList<NpcDropSet>) {
    dropSets.forEach { dropset ->
        val spawnerTest = world.random.nextFloat()
        if(spawnerTest > dropset.rarity) {
            return@forEach
        }
        val splitQuantity = dropset.quantity.split('-')
        val splitSize = splitQuantity.count()
        if(splitSize == 1) {
            world.spawn(GroundItem(dropset.id, dropset.quantity.toInt(), npc.tile))
            return@forEach
        }
        if(splitSize == 2) {
            val minQuantity = splitQuantity[0].toInt()
            val maxQuantity = splitQuantity[1].toInt()
            val quantity = world.random.nextInt(maxQuantity+minQuantity) - minQuantity
            if(quantity <= 0) {
                return@forEach
            }
            world.spawn(GroundItem(dropset.id, quantity, npc.tile))
            return@forEach
        }
    }
}

