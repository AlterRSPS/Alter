package gg.rsmod.plugins.content.area.lumbridge.chat

spawn_npc(Npcs.PRAYER_TUTOR, x = 3242, z = 3214, walkRadius = 3)

on_npc_option(Npcs.PRAYER_TUTOR, option = "talk-to") {
    player.queue { dialog() }
}

suspend fun QueueTask.dialog() {
    chatPlayer("Good day, sister.")
    chatNpc("Greetings, ${player.username}. Can I help you with anything,<br><br> today?")
    when (options("How can I train my prayer?", "What is prayer useful for?", "No, thank you.")) {
        1 -> how_to_train()
        2 -> prayer_useful()
        3 -> no_thanks()

    }
}

suspend fun QueueTask.no_thanks(){
    chatPlayer("No, thank you.", animation = 588)
    chatNpc("Very well. Saradomin be with you!")
}

suspend fun QueueTask.how_to_train(){
    chatPlayer("How can I train my prayer?")
    chatNpc("The most common way to train prayer is by either<br>burying bones, or offering them to the gods at some<br>kind of an altar.")
    chatNpc("Lots of adventurers build such altars in their own<br>homes, or there are a few frequent places of warship<br>around the world.")
    chatNpc("Different kinds of bones will help you train faster.<br>Generally speaking, the bigger they are and the more<br>frightening a creature they come from, the better they are for it.")
    chatNpc("Is there anything else you would like to know?")
    when(options("What is prayer useful for?", "No, thank you.")) {
        1 -> prayer_useful()
        2 -> no_thanks()
    }
}

suspend fun QueueTask.prayer_useful(){
    chatPlayer("What is prayer useful for?")
    chatNpc("The gods look kindly upon their devout followers. There<br>are all kinds of denefits they may provide, if you pray<br>for them!")
    chatPlayer("Really? What kind of benefits?")
    chatNpc("They could help you in combat, help your wonds to<br>heal more quickly, protect your belongings... There's a<br>lot they can do for you!")
    player.focusTab(GameframeTab.PRAYER)
    chatNpc("You can find out more by looking in your prayer book.")
    chatPlayer("Wow! That sounds great.")
    chatNpc("You need to be careful that your prayers don't run<br>out, though. You can get prayer potions to help you<br>recharge, or you can pray at an altar whenever one's<br>nearby.")
    chatNpc("is there anything else you would like to know?")
    when(options("How can I train my prayer?", "No, thank you.")) {
        1 -> how_to_train()
        2 -> no_thanks()
    }
}
