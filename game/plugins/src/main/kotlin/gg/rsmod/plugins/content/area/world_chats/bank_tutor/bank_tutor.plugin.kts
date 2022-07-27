package gg.rsmod.plugins.content.area.world_chats.bank_tutor

import gg.rsmod.plugins.content.inter.bank.openBank

spawn_npc(Npcs.BANKER_TUTOR, x = 3208, z = 3222, height = 2, direction = Direction.SOUTH)

arrayOf(Npcs.BANKER_TUTOR).forEach { bankerTutor ->
    on_npc_option(npc = bankerTutor, option = "talk-to", lineOfSightDistance = 2) {
        player.queue { dialogTutor() }
    }
    on_npc_option(npc = bankerTutor, option = "bank", lineOfSightDistance = 2) {
        player.openBank()
    }
    on_npc_option(npc = bankerTutor, option = "collect", lineOfSightDistance = 2) {
        open_collect(player)
    }
}

suspend fun QueueTask.dialogTutor() {
    chatNpc("Good day, how may I help you?")
    when (options("How do I use the bank?", "I'd like to access my bank account, please.", "I'd like to check my PIN settings.", "I'd like to collect items.", "I'd like to buy more bank slots.", "What is this place?")) {
        1 -> how_do_i_use()
        2 -> player.openBank()
        3 -> open_pin(player)
        4 -> open_collect(player)
        5 -> more_bank_slots()
    }
}

suspend fun QueueTask.how_do_i_use() {
    when (options("Using the bank itself.", "Using Bank deposit boxes.", "What's this PIN thing that people keep talking about?", "Can you show me the bank tutorial please?", "Goodbye.")) {
        1 -> {
            chatPlayer("Using the bank itself. I'm not sure how....?")
            chatNpc("To open your bank you can speak to any banker, as<br>well as use a bank booth or bank chest. If you have a<br>PIN setup you will be asked to enter the PIN before<br>you are given access to you bank.", animation = 568)
            when (options("What's a bank PIN?", "Continue.")) {
                1 -> {
                    chatPlayer("What's a bank PIN?")
                    chatNpc("The PIN - Personal Identification Number - can be<br>set on your bank account to protect your items in case<br>someone finds out your account password. It consists<br>of four numbers that you remember, tell no one.")
                    chatNpc("So if someone did manage to get your password they<br><br>couldn't steal your items if they were in the bank.")
                    chatPlayer("Ok, so after I am in the bank, how do i use it?")
                    bank_usuage()
                }
                2 -> bank_usuage()
            }
        }
        2 -> {
            chatPlayer("Using Bank deposit boxes.... what are they?")
            chatNpc("They look like grey pillars, there's one just over there,<br>near the desk. You can usually find a Bank deposit box<br>next to a bank.")
            chatNpc("Bank deposit boxes save so much time as you do not<br>have to enter in your bank PIN. If you're simply<br>wanting to deposit a single item, 'Use' it on the deposit box.")
            chatNpc("Otherwise, simply click once on the box and it will give<br>you a choice of what to deposit in an interface very<br>similar to the bank itself. Very quick for when you're<br>simply fishing or mining etc.")
            how_do_i_use()
        }
        3 -> {
            chatPlayer("What's this PIN thing that people keep talking about?")
            chatNpc("The PIN - Personal Identification Number - can be<br>set on your bank account to protect your items in case<br>someone finds out your account password. It consists<br>of four numbers that you remember, tell no one.")
            chatNpc("So if someone did manage to get your password they<br><br>couldn't steal your items if they were in the bank.")
            bank_pin()
        }
        4 -> {
            // TODO: 7/26/2022 add in bank tut
            chatNpc("maybe later!")
            how_do_i_use()
        }
        5 -> chatPlayer("Goodbye.")
    }
}

suspend fun QueueTask.bank_usuage(){
    chatNpc("To withdraw one item, left-click on it once. To withdraw<br>many, right-click on the item and select from the menu.<br>The same can be done for depositing items.")
    chatNpc("While you are in your bank you can click and drag<br>items to move them around the bank. There are two<br>modes for moving items, Swap or Insert.")
    chatNpc("If you are using swap, the two items will switch place.<br>If use are using insert, the item you are moving will<br>be placed either in front or behind the item you<br>targeted with the item you are moving.")
    chatNpc("You may withdraw 'notes' or 'certificates'. This will only<br>work for items which are tradable and so not stack. To<br>withdraw an item as note, you need to select the 'note' withdraw as button.")
    doubleItemMessageBox("A noted item looks like a piece of paper with the image<br>of the actual item on top of it", item1 = 316, item2 = 315)
    chatNpc("You can use bank notes on any banker to un-note the<br>item. Alternatively, you can deposit the items into the<br>bank. Then withdraw them as an item instead of a note.")
    how_do_i_use()
}

suspend fun QueueTask.bank_pin() {
    when (options("How do I set my PIN?", "How do I remove my PIN?", "What happens if I forget my PIN?", "I know about the PIN, tell me about the bank.", "Goodbye")){
        1 -> {
            chatPlayer("How do I set my PIN?")
            chatNpc("It seems you already have a bank pin set up, but I'll<br><br>remind you anyway.")
            chatNpc("You can set your PIN by talking to any banker, they<br>will allow you to access your bank pin setting. Here<br>you can choose to set your pin and recovery delay.")
            chatNpc("Remember not to set it to anything personal such as<br>your real life bank PIN or birthday. The recovery<br>delay is to protect your banked items from account<br>thieves.")
            chatNpc("If someone stole your account and asked to have the<br>PIN deleted, they would have to wait a few days before<br>accessing your bank account to steal your items. This<br>will give you time to recover your account.")
            chatNpc("There will also be a delay in actually setting the PIN<br>to be used, this is so that if your account is stolen and<br>a PIN set, you can cancel it before it comes into use!")
            bank_pin()
        }
        2 -> {
            chatPlayer("How do I remove my PIN?")
            chatNpc("Talking to any banker will enable you to access your<br>PIN settings. There you can cancel or change your<br>PIN, but you will need to wait for your recovery<br>delay to expire to be able to access your bank.")
            chatNpc("This can be set in the settings page and will protect<br><br>your items should your account be stolen.")
            bank_pin()
        }
        3 -> {
            chatPlayer("What happens if I forget my PIN?")
            chatNpc("If you find yourself faces with the PIN keypad and<br>you don't know the PIN, just look on the right-hand<br>side for a button marked 'I don't know it'. Click this<br>button.")
            chatNpc("Your PIN will be deleted (after a delay of a few days)<br>and you'll be able to use your bank as before. You<br>may still use the bank deposit box without your PIN.")
            bank_pin()
        }
        4 -> how_do_i_use()
        5 -> chatPlayer("Goodbye.")
    }
}

// TODO: 7/26/2022 add in more slots interface
suspend fun QueueTask.more_bank_slots() {
    chatPlayer("I'd like to buy more bank slots.")
    chatNpc("I can sell you up to 360 additional bank slots in sets of<br>40. How many are you interested in buying?", animation = 568)
    chatPlayer("Actually, I've changed my mind.", animation = 554)
}

fun open_collect(p: Player) {
    p.setInterfaceUnderlay(color = -1, transparency = -1)
    p.openInterface(interfaceId = 402, dest = InterfaceDestination.MAIN_SCREEN)
}

fun open_pin(p: Player) {
    p.setInterfaceUnderlay(color = -1, transparency = -1)
    p.openInterface(interfaceId = 14, dest = InterfaceDestination.MAIN_SCREEN)
}