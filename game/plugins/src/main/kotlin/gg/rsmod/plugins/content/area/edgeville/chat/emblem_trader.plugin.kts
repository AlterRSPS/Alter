package gg.rsmod.plugins.content.area.edgeville.chat
/**
 * @TODO fix the chat for limiting targets & add coffer
 * Emblem trader "real" npc has an id of 308, this npc then changes appearance
 * based on its varbit state for each player.
 */

spawn_npc(npc = Npcs.EMBLEM_TRADER, x = 3096, z = 3504, walkRadius = 2)

val HIDE_STREAK_VARBIT = 1621
val LIMIT_TARGETS_VARBIT = 6503
val SKULL_SHORT_DURATION = 500
val SKULL_LONG_DURATION = 2000

    on_npc_option(npc = Npcs.EMBLEM_TRADER, option = "Talk-to") {
        player.queue { chat() }
    }

    on_npc_option(npc = Npcs.EMBLEM_TRADER, option = "Rewards") { open_shop(player) }

    on_npc_option(npc = Npcs.EMBLEM_TRADER, option = "Coffer") {
        player.queue {
            coffer_option()
        }
    }

    on_npc_option(npc = Npcs.EMBLEM_TRADER, option = "skull") {
        player.queue {
            if (player.hasSkullIcon(SkullIcon.NONE)) {
                give_pk_skull()
            } else {
                extend_pk_skull()
            }
        }
    }
fun open_shop(p: Player) {
    p.openShop("Emblem Trader")
}
suspend fun QueueTask.chat() {
    chatNpc("Don't suppose you've come across any strange...<br>emblems or artefacts along your journey? Ancient<br>artifacts?", animation = 588)
    chatNpc("Nothing to report now.", animation = 589)
    chatNpc("If you find any, please bring them to me. I am happy<br>to offer rewards for such items. My master is pleased<br>by your interest, so I'll mark that task off on your<br>diary.", animation = 588)
    options()
}

suspend fun QueueTask.options() {
    when (options("Let's trade for rewards.", "Can I have a PK skull, please?", "Let's talk about Targets.", "I'll leave you alone.")) {
        1 -> {
            chatPlayer("Let's trade for rewards.", animation = 554)
            open_shop(player)
        }
        2 -> pk_skull()
        3 -> limit_targets()
        4 -> leave_alone()
    }
}

suspend fun QueueTask.pk_skull() {
    val player = player
    if (player.hasSkullIcon(SkullIcon.NONE)) {
        chatPlayer("Can I have a PK skull, please?", animation = 554)
        give_pk_skull()
    } else {
        chatPlayer("Can you make my PK skull last longer?", animation = 554)
        extend_pk_skull()
    }
}

suspend fun QueueTask.give_pk_skull() {
    if (options("Give me a PK skull.", "Cancel.", title = "A PK skull means you drop ALL your items on death.") == 1) {
        player.skull(SkullIcon.WHITE, durationCycles = SKULL_SHORT_DURATION)
        itemMessageBox("You are now skulled.", item = Items.SKULL, amountOrZoom = 400)
    }
}

suspend fun QueueTask.extend_pk_skull() {
    if (options("Yes", "No", title = "Extend your PK skull duration?") == 1) {
        player.skull(SkullIcon.WHITE, durationCycles = SKULL_LONG_DURATION)
        itemMessageBox("Your PK skull will now last for the full 20 minutes.", item = Items.SKULL)
    }
}

suspend fun QueueTask.limit_targets() {
    val player = player
    if (player.getVarbit(LIMIT_TARGETS_VARBIT) == 0) {
        chatPlayer("Let's talk about Targets.", animation = 554)
        when (options("Can I access my Target Coffer?", "I'd like to limit my potential Targets.", "I'll leave you alone")) {
            1 -> {
                chatPlayer("Can I access my Target Coffer?")
                coffer_option()
            }
            2 -> {
                chatPlayer("I'd like to limit my potential Targets.")
                chatNpc("Very well, you will no longer be assigned targets deep<br><br>in the wilderness.", animation = 568)
                coffer_umlimit()
            }
            3 -> leave_alone()
        }
        } else {
        chatPlayer("I don't want to limit my potential targets.", animation = 554)
        chatNpc("Very well, you may now be assigned targets deep in<br>the wilderness.", animation = 568)
    }
}

suspend fun QueueTask.coffer_umlimit() {
    when (options("Can I access my Target Coffer?", "I don't want to limit my potential Targets.", "I'll leave you alone")) {
        1 -> coffer_option()
        2 -> {
            chatPlayer("I don't want to limit my potential Targets.")
            chatNpc("Very well, you may now be assigned Targets deep in<br><br>the wilderness.")
            access_coffer()
        }
        3 -> leave_alone()
    }
}

suspend fun QueueTask.access_coffer(){
    when (options("Can I access my Target Coffer?", "I'd like to limit my potential Targets.", "I'll leave you alone")) {
        1 -> coffer_option()
        2 -> limit_targets()
        3 -> leave_alone()
    }
}

suspend fun QueueTask.coffer_option() {
    chatPlayer("Can I access my Target Coffer?")
    when (options("Ask about the coffer", "Deposit", "Other options")) {
        1 -> {
            chatNpc("Anyone wishing to participate in the Targeting system<br>must take a little risk, if they wish to be considered a<br>worthy opponent.")
            itemMessageBox("They may put money in their Target Coffer here, by<br>talking to me. They must store at least <col=7f0000>20,000 coins</col> to<br>play.", item = 1004)
            chatNpc(" If another person kills you in the Wilderness on a<br>Target world, <col=7f0000>20,000 coins</col> will be taken from your<br>Coffer, and given to your killer.")
            chatNpc("Any surplus money in your Coffer will remain safe,<br>here, with me. Only <col=7f0000>20,000 coins</col> will be taken from it<br>as you die.")
            coffer_option()
        }
        2 -> {
            chatNpc("You must be on a Target world to deposit coins into<br>the Target Coffer.")
            coffer_option()
        }
        3 -> access_coffer()
    }
}

suspend fun QueueTask.leave_alone() {
    chatPlayer("I'll leave you alone.", animation = 567)
}