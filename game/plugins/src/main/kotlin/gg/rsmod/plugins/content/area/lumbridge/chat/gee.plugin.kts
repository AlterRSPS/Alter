package gg.rsmod.plugins.content.area.lumbridge.chat

spawn_npc(Npcs.GEE, x = 3223, z = 3229, walkRadius = 19, direction = Direction.SOUTH)
spawn_npc(Npcs.GEE, 3243, 3265, walkRadius = 22)

on_npc_option(Npcs.GEE, option = "talk-to") {
    player.queue { dialog() }
}

suspend fun QueueTask.dialog() {
    chatNpc("Hello there, can I help you?", animation = 591)
    when (world.random(3)) {
        0 -> {
            when (options("Where am I?", "How are you today?", "Are there any quests I can do here?", "Your shoe lace is untied.")) {
                1 -> {
                    chatPlayer("Where am I?")
                    chatNpc("This is the town of Lumbridge my friend.")
                    when (options("How are you today?", "Do you know of any quests I can do?", "Your shoe lace is untied.")) {
                        1 -> howareyou()
                        2 -> anyquests()
                        3 -> shoelace()
                    }
                }
                2 -> howareyou()
                3 -> anyquests()
                4 -> shoelace()
            }
        }
        1 -> {
            when (options("What's up?", "Are there any quests I can do here?", "Can I buy your stick?")) {
                1 -> {
                    chatPlayer("What's up?")
                    chatNpc("I assume the sky is up..")
                    chatPlayer("You assume?")
                    chatNpc("Yeah, unfortunately I don't seem to be able to look up.")
                }
                2 -> anyquests()
                3 -> stick()
            }
        }
        2 -> {
            when (options("Do you have anything of value which I can have?", "Are there any quests I can do here?", "Can I buy your stick?")) {
                1 -> {
                    chatPlayer("Do you have anything of value which I can have?")
                    chatNpc("Are you asking for free stuff?")
                    chatPlayer("Well... er... yes.")
                    chatNpc("No I do not have anything I can give you. If I did<br>have anything of value I wouldn't want to give it away.")
                }
                2 -> anyquests()
                3 -> stick()
            }
        }
        3 -> {
            when (options("Where am I?", "How are you today?", "Are there any quests I can do here?", "Where can I get a haircut like yours?")) {
                1 -> {
                    chatPlayer("Where am I?")
                    chatNpc("This is the town of Lumbridge my friend.")
                    when (options("How are you today?", "Do you know of any quests I can do?", "Your shoe lace is untied.")) {
                        1 -> howareyou()
                        2 -> anyquests()
                        3 -> shoelace()
                    }
                }
                2 -> howareyou()
                3 -> anyquests()
                4 -> {
                    chatPlayer("Where can I get a haircut like yours?")
                    chatNpc("Yes, it does look like you need a hairdresser.")
                    chatPlayer("Oh thanks!")
                    chatNpc("No problem. The hairdresser in Falador will probably be<br>able to sort you out.")
                    chatNpc("The Lumbridge general store sells useful maps if you<br>don't know the way.")
                }
            }
        }
    }
}

suspend fun QueueTask.howareyou() {
    chatPlayer("How are you today?")
    chatNpc("Aye, not too bad thank you. Lovely weather in Gielinor<br>this fine day.")
    chatPlayer("Weather?")
    chatNpc("Yes weather, you know.")
    chatNpc("The state or condition of the atmosphere at a time and<br>place, with respect to variables such as temperature,<br>moisture, wind velocity, and barometric pressure.")
    chatPlayer("...")
    chatNpc("Not just a pretty face eh? Ha ha ha.")
}

suspend fun QueueTask.anyquests() {
    chatPlayer("Do you know of any quests I can do?")
    chatNpc("What kind of quest are you looking for?")
    when (options("I fancy a bit of a fight, anything dangerous?", "Something easy please, I'm new here.", "I'm a thinker rather than fighter, anything skill oriented?", "I want to do all kinds of things, do you know of anything like that?", "Maybe another time.")) {
        1 -> {
            chatPlayer("I fancy a bit of a fight, anything dangerous?")
            chatNpc("Hmm.. dangerous you say? What sort of creatures are<br>you looking to fight?")
            when (options("Big scary demons!", "Vampyres!", "Small.. something small would be good.", "Maybe another time.")) {
                1 -> {
                    chatPlayer("Big scary demons!")
                    chatNpc("You are a brave soul indeed.")
                    chatNpc("Now that you mention it, I heard a rumour about a<br>gypsy in Varrock who is rambling about some kind of<br>greater evil.. sounds demon-like if you ask me.")
                    chatNpc("Perhaps you could check it out if you are as brave as<br>you say?")
                    chatPlayer("Thanks for the tip, perhaps I will.")
                }
                2 -> {
                    chatPlayer("Vampyres!")
                    chatNpc("Ha ha. I personally don't believe in such things.<br>However, there is a man in Draynor Village who has<br>been scaring the village folk with stories of vampyres.")
                    chatNpc("He's named Morgan and can be found in one of the<br>village houses. Perhaps you could see what the matter<br>is?")
                    chatPlayer("Thanks for the tip.")
                }
                3 -> {
                    chatPlayer("Small.. something small would be good.")
                    chatNpc("Small? Small isn't really that dangerous though is it?")
                    chatPlayer("Yes it can be! There could be anything from an evil<br>chicken to a poisonous spider. They attack in numbers<br>you know!")
                    chatNpc("Yes ok, point taken. Speaking of small monsters, I hear<br>old Wizard Mizgog in the wizards' tower has just had<br>all his beads taken by a gang of mischievous imps.")
                    chatNpc("Sounds like it could be a quest for you?")
                    chatPlayer("Thanks for your help.")
                }
                4 -> chatPlayer("Maybe another time.")
            }
        }
        2 -> {
            chatPlayer("Something easy please, I'm new here.")
            chatNpc("I can tell you about plenty of small easy tasks.")
            chatNpc("The Lumbridge cook has been having problems, the<br>Duke is confused over some strange rocks and on top<br>of all that, poor lad Romeo in Varrock has girlfriend<br>problems.")
            when (options("The Lumbridge cook.", "The Duke's strange stones.", "Romeo and his girlfriend.", "Maybe another time.", title = "Tell me about..")) {
                1 -> {
                    chatPlayer("Tell me about the Lumbridge cook.")
                    chatNpc("It's funny really, the cook would forget his head if it<br>wasn't screwed on. This time he forgot to get<br>ingredients for the Duke's birthday cake.")
                    chatNpc("Perhaps you could help him? You will probably find him<br>in the Lumbridge Castle kitchen.")
                    chatPlayer("Thank you. I shall go speak with him.")
                }
                2 -> {
                    chatPlayer("Tell me about the Duke's strange stones.")
                    chatNpc("Well the Duke of Lumbridge has found a strange stone<br>that no one seems to understand. Perhaps you could<br>help him? You can probably find him upstairs in<br>Lumbridge Castle.")
                    chatPlayer("Sounds mysterious. I may just do that. Thanks.")
                }
                3 -> {
                    chatPlayer("Tell me about Romeo and his girlfriend please.")
                    chatNpc("Romeo in Varrock needs help with finding his beloved<br>Juliet, you may be able to help him out.")
                    chatNpc("Unless of course you manage to find Juliet first in<br>which case she has probably lost Romeo.")
                    chatPlayer("Right, ok. Romeo is in Varrock?")
                    chatNpc("Yes you can't miss him, he's wandering aimlessly in the<br>square.")
                }
                4 -> chatPlayer("Maybe another time.")
            }
        }
        3 -> {
            chatPlayer("I'm a thinker rather than fighter, anything skill<br>orientated?")
            chatNpc("Skills play a big part when you want to progress in<br>knowledge throughout Gielinor. I know of a few skill-<br>related quests that can get you started.")
            chatNpc("You may be able to help out Fred the farmer who is in<br>need of someones crafting expertise.")
            chatNpc("Or, there's always Doric the dwarf who needs an<br>errand running for him?")
            when (options("Fred the farmer.", "Doric the dwarf.", "Maybe another time.", title = "Tell me about..")) {
                1 -> {
                    chatPlayer("Tell me about Fred the farmer please.")
                    chatNpc("You can find Fred next to the field of sheep in<br>Lumbridge. Perhaps you should go and speak with him.")
                    chatPlayer("Thanks, maybe I will.")
                }
                2 -> {
                    chatPlayer("Tell me about Doric the dwarf.")
                    chatNpc("Doric the dwarf is located north of Falador. He might<br>be able to help you with smithing. You should speak to<br>him. He may let you use his anvils.")
                    chatPlayer("Thanks for the tip.")
                }
                3 -> chatPlayer("Maybe another time.")
            }


        }
        4 -> {
            chatPlayer("I want to do all kinds of things, do you know of<br>anything like that?")
            chatNpc("Of course I do. Gielinor is a huge place you know, now<br>let me think...")
            chatNpc("Hetty the witch in Rimmington might be able to offer<br>help in the ways of magical abilities..")
            chatNpc("Also, pirates are currently docked in Port Sarim,<br>Where pirates are, treasure is never far away...")
            chatNpc("Or you could go help out Ernest who got lost in<br>Draynor Manor, spooky place that.")
            when (options("Hetty the Witch.", "Pirate's treasure.", "Ernest and Draynor Manor.", "Maybe another time.", title = "Tell me about..")) {
                1 -> {
                    chatPlayer("Tell me about Hetty the witch.")
                    chatNpc("Hetty the witch can be found in Rimmington, south of<br>Falador. She's currently working on some new potions.<br>Perhaps you could give her a hand? She might be able<br>to offer help with your magical abilities.")
                    chatPlayer("Ok thanks, let's hope she doesn't turn me into a potato<br>or something..")
                }
                2 -> {
                    chatPlayer("Tell me about Pirate's Treasure.")
                    chatNpc("RedBeard Frank in Port Sarim's bar, the Rusty<br>Anchor might be able to tell you about the rumored<br>treasure that is buried somewhere in Gielinor.")
                    chatPlayer("Sounds adventurous, I may have to check that out.<br>Thank you.")
                }
                3 -> {
                    chatPlayer("Tell me about Ernest please.")
                    chatNpc("The best place to start would be at the gate to<br>Draynor Manor. There you will find Veronica who will<br>be able to tell you more.")
                    chatNpc("I suggest you tread carefully in that place; it's haunted.")
                    chatPlayer("Sounds like fun. I've never been to a Haunted Manor<br>before.")
                }
                4 -> chatPlayer("Maybe another time.")
            }
        }
        5 -> chatPlayer("Maybe another time.")
    }
}

suspend fun QueueTask.stick() {
    chatPlayer("Can I buy your stick?")
    chatNpc("It's not a stick! I'll have you know it's a very powerful<br>staff!")
    chatPlayer("Really? Show me what it can do!")
    chatNpc("Um..It's a bit low on power at the moment..")
    chatPlayer("It's a stick isn't it?")
    chatNpc("...Ok it's a stick.. But only while I save up for a staff.<br>Zaff in Varrock square sells them in his shop.")
    chatPlayer("Well good luck with that.")
}

suspend fun QueueTask.shoelace() {
    chatPlayer("Your shoe lace is untied.")
    chatNpc("No it's not!")
    chatPlayer("No you're right. I have nothing to back that up.")
    chatNpc("Fool! Leave me alone!")
}