package gg.rsmod.plugins.content.area.lumbridge.chat

/**
 * Thanks to Tomm for the movement code
 * Thanks to Desetude for gate open and close code
 */

spawn_npc(Npcs.BORDER_GUARD, x = 3267, z = 3226, direction = Direction.WEST)
spawn_npc(Npcs.BORDER_GUARD, x = 3267, z = 3229, direction = Direction.WEST)
spawn_npc(Npcs.BORDER_GUARD_4288, x = 3268, z = 3226, direction = Direction.EAST)
spawn_npc(Npcs.BORDER_GUARD_4288, x = 3268, z = 3229, direction = Direction.EAST)


on_npc_option(Npcs.BORDER_GUARD, option = "talk-to") {
        player.queue { dialog() }
    }

on_npc_option(Npcs.BORDER_GUARD_4288, option = "talk-to") {
    player.queue { dialog2() }
}

suspend fun QueueTask.dialog() {

    chatPlayer("Can I come through this gate?", animation = 588)
    chatNpc("You must pay a toll of 10 gold coins to pass.", animation = 590)
    when (options("No thank you, I'll walk around.", "Who does my money go to?", "Yes, ok.")) {
        1 -> {
            chatPlayer("No thank you, I'll walk around.", animation = 554)
            chatNpc("Ok suit yourself.", animation = 588)
        }
        2 -> {
            chatPlayer("Who does my money go to?", animation = 554)
            chatNpc("The money goes to the city of Al-Kharid.", animation = 590)
        }
        3 -> {
            chatPlayer("Yes, ok.", animation = 554)

            val inventory = player.inventory
            if (inventory.getItemCount(Items.COINS_995) >= 10) {
                
                player.lock()
                player.moveTo(3267, 3227, 0)
                world.queue {
                    wait(2)
                    world.openDoor(world.getObject(Tile(3268, 3228), 0)!!, 1574)
                    world.openDoor(world.getObject(Tile(3268, 3227), 0)!!, 1573, invertRot = true)
                    wait(3)
                    world.closeDoor(world.getObject(Tile(3267, 3227), 0)!!, Objs.GATE_44052, invertTransform = true, invertRot = true)
                    world.closeDoor(world.getObject(Tile(3267, 3228), 0)!!, Objs.GATE_44053)
                }
                player.inventory.remove(Items.COINS_995, 10)
                wait(2)
                player.moveTo(3268, 3227, 0)
                wait(2)
                player.unlock()
            } else
                chatPlayer("Oh dear, I don't actually seem to have enough money.", animation = 554)
        }
    }
}

suspend fun QueueTask.dialog2() {

    chatPlayer("Can I come through this gate?", animation = 588)
    chatNpc("You must pay a toll of 10 gold coins to pass.", animation = 590)
    when (options("No thank you, I'll walk around.", "Who does my money go to?", "Yes, ok.")) {
        1 -> {
            chatPlayer("No thank you, I'll walk around.", animation = 554)
            chatNpc("Ok suit yourself.", animation = 588)
        }
        2 -> {
            chatPlayer("Who does my money go to?", animation = 554)
            chatNpc("The money goes to the city of Al-Kharid.", animation = 590)
        }
        3 -> {
            chatPlayer("Yes, ok.", animation = 554)

            val inventory = player.inventory
            if (inventory.getItemCount(Items.COINS_995) >= 10) {
                player.lock()
                player.moveTo(3268, 3227, 0)
                world.queue {
                    wait(2)
                    world.openDoor(world.getObject(Tile(3268, 3228), 0)!!, 1574)
                    world.openDoor(world.getObject(Tile(3268, 3227), 0)!!, 1573, invertRot = true)
                    wait(3)
                    world.closeDoor(world.getObject(Tile(3267, 3227), 0)!!, Objs.GATE_44052, invertTransform = true, invertRot = true)
                    world.closeDoor(world.getObject(Tile(3267, 3228), 0)!!, Objs.GATE_44053)
                }
                player.inventory.remove(Items.COINS_995, 10)
                wait(2)
                player.moveTo(3267, 3227, 0)
                wait(2)
                player.unlock()
            } else
                chatPlayer("Oh dear, I don't actually seem to have enough money.", animation = 554)
        }
    }
}