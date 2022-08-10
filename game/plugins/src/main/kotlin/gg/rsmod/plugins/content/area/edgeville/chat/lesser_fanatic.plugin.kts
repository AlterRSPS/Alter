package gg.rsmod.plugins.content.area.edgeville.chat

spawn_npc(npc = Npcs.LESSER_FANATIC, x = 3120, z = 3517, walkRadius = 3)
spawn_npc(npc = Npcs.LUNA, x = 3120, z = 3518, walkRadius = 3)

on_npc_option(npc = Npcs.LESSER_FANATIC, option = "talk-to") {
    player.queue { chat() }
}

suspend fun QueueTask.chat() {
    chatNpc("Hello!", animation = 567)
    options()
}

suspend fun QueueTask.options() {
    when (options("Who are you?", "I have a question about my Achievement Diary", "Bye!")) {
        1 -> who_are_you()
        2 -> question()
        3 -> bye()
    }
}

suspend fun QueueTask.who_are_you() {
    chatPlayer("Who are you?", animation = 554)
    chatNpc("I'm Jeffery, the taskmaster for the Wilderness<br>Achievement Diary.", animation = 589)
    what_is_achievement_diary()
}

suspend fun QueueTask.question() {
    chatPlayer("I have a question about my Achievement diary.", animation = 554)
    when (options("What is the Achievement Diary?", "What are the rewards?", "How do I claim the rewards?", "Bye!")) {
        1 -> what_is_achievement_diary()
        2 -> what_are_rewards()
        3 -> claim_rewards()
        4 -> bye()
    }
}

suspend fun QueueTask.what_is_achievement_diary() {
    chatPlayer("What is the Achievement Diary?", animation = 554)
    chatNpc("It's a diary that helps you keep track of particular<br>achievements. In the Wilderness it can help you<br>discover some quite useful things. Eventually, with<br>enough exploration, the inhabitants will reward you.", animation = 591)
    chatNpc("You can see the list of tasks on the side-panel.", animation = 588)
    options()
}

suspend fun QueueTask.what_are_rewards() {
    chatNpc("Well, there are four different Wilderness Swords, which<br>match up with the four levels of difficulty. Each has the<br>same rewards as the previous level and some additional<br>benefits too... which tier of rewards would you like to", animation = 570)
    chatNpc("know more about?", animation = 567)
    when (options("Easy Rewards.", "Medium Rewards.", "Hard Rewards.", "Elite Rewards.")) {
        1 -> easy_rewards()
        2 -> medium_rewards()
        3 -> hard_rewards()
        4 -> elite_rewards()
    }
}

suspend fun QueueTask.easy_rewards() {
    chatPlayer("Tell me more about the Easy rewards please!", animation = 588)
    chatNpc("If you complete all of the easy tasks in the Wilderness,<br>you can speak to Lundail at the Mage Arena every day<br>to receive 10 free runes and you can teleport to either<br>Edgeville or Ardougne from the Wilderness lever.", animation = 570)
    chatPlayer("Thanks!", animation = 567)
}

suspend fun QueueTask.medium_rewards() {
    chatPlayer("Tell me more about the Medium rewards please!", animation = 588)
    chatNpc("In addition to the easy rewards, Mandrith will give you<br>a 20% discount off entry to the Resource Area, you'll<br>cut Ents faster, you can access a shortcut in the deep<br>Wilderness dungeon. You can also own 4 ecumenical", animation = 570)
    chatNpc("keys at once and receive 20 runes a day from Lundail.", animation = 567)
    chatPlayer("Thanks!", animation = 567)
}

suspend fun QueueTask.hard_rewards() {
    chatPlayer("Tell me more about the Hard rewards please!", animation = 588)
    chatNpc("In addition to the easy and medium benefits, the sword<br>can teleport you to the Fountain of Rune once per<br>day, you'll get 50% more lava shards when grinding<br>scales and even cheaper access to the Resource Area.", animation = 570)
    chatNpc("You can own 5 ecumenical keys, access a shortcut to<br>Lava Dragon Isle and Lundail will give you 30 free<br>runes each day. You can also control which Obelisk you<br>want to travel to.", animation = 570)
    chatPlayer("Thanks!", animation = 567)
}

suspend fun QueueTask.elite_rewards() {
    chatPlayer("Tell me more about the Elite rewards please!", animation = 588)
    chatNpc("In addition to the previous tiers of rewards, access to<br>the Resource Area is free and the sword will now give<br>you unlimited teleports to the Fountain of Rune. All<br>dragon bone drops in the Wilderness become noted and", animation = 570)
    chatNpc("Lundail will give you 50 free runes per day and the<br>rate at which you catch Dark Crabs is increased.", animation = 568)
    chatPlayer("Thanks!", animation = 567)
}

suspend fun QueueTask.claim_rewards() {
    chatPlayer("How do I claim the rewards?", animation = 554)
    chatNpc("Just complete the tasks in the Wilderness so they're<br>ticked off, then come and speak to me for your<br>rewards.", animation = 590)
    options()
}

suspend fun QueueTask.bye() {
    chatPlayer("Bye!", animation = 567)
    chatNpc("See you later.", animation = 567)
}
