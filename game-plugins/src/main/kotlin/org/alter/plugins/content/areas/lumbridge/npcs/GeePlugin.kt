package org.alter.plugins.content.areas.lumbridge.npcs

import org.alter.api.ext.chatNpc
import org.alter.api.ext.chatPlayer
import org.alter.api.ext.options
import org.alter.api.ext.player
import org.alter.game.Server
import org.alter.game.model.Direction
import org.alter.game.model.World
import org.alter.game.model.entity.Player
import org.alter.game.model.queue.QueueTask
import org.alter.game.plugin.KotlinPlugin
import org.alter.game.plugin.PluginRepository

class GeePlugin(
    r: PluginRepository, world: World, server: Server
) : KotlinPlugin(r, world, server) {

    init {
        spawnNpc("npc.gee", x = 3223, z = 3229, walkRadius = 19, direction = Direction.SOUTH)
        spawnNpc("npc.gee", 3243, 3265, walkRadius = 22)

        onNpcOption("npc.gee", option = "talk-to") {
            player.queue { dialog(player) }
        }
    }

    suspend fun QueueTask.dialog(player: Player) {
        chatNpc(player, "Hello there, can I help you?", animation = 591)
        when (world.random(3)) {
            0 -> conversationFlow0(player)
            1 -> conversationFlow1(player)
            2 -> conversationFlow2(player)
            3 -> conversationFlow3(player)
        }
    }

    private suspend fun QueueTask.conversationFlow0(player: Player) {
        when (options(
            player,
            "Where am I?",
            "How are you today?",
            "Are there any quests I can do here?",
            "Your shoe lace is untied."
        )) {
            1 -> {
                chatPlayer(player, "Where am I?")
                chatNpc(player, "This is the town of Lumbridge my friend.")
                when (options(
                    player,
                    "How are you today?",
                    "Do you know of any quests I can do?",
                    "Your shoe lace is untied."
                )) {
                    1 -> howAreYou(player)
                    2 -> anyQuests(player)
                    3 -> shoelace(player)
                }
            }
            2 -> howAreYou(player)
            3 -> anyQuests(player)
            4 -> shoelace(player)
        }
    }

    private suspend fun QueueTask.conversationFlow1(player: Player) {
        when (options(player, "What's up?", "Are there any quests I can do here?", "Can I buy your stick?")) {
            1 -> {
                chatPlayer(player, "What's up?")
                chatNpc(player, "I assume the sky is up..")
                chatPlayer(player, "You assume?")
                chatNpc(player, "Yeah, unfortunately I don't seem to be able to look up.")
            }
            2 -> anyQuests(player)
            3 -> stick(player)
        }
    }

    private suspend fun QueueTask.conversationFlow2(player: Player) {
        when (options(
            player,
            "Do you have anything of value which I can have?",
            "Are there any quests I can do here?",
            "Can I buy your stick?"
        )) {
            1 -> {
                chatPlayer(player, "Do you have anything of value which I can have?")
                chatNpc(player, "Are you asking for free stuff?")
                chatPlayer(player, "Well... er... yes.")
                chatNpc(
                    player,
                    "No I do not have anything I can give you. If I did<br>have anything of value I wouldn't want to give it away."
                )
            }
            2 -> anyQuests(player)
            3 -> stick(player)
        }
    }

    private suspend fun QueueTask.conversationFlow3(player: Player) {
        when (options(
            player,
            "Where am I?",
            "How are you today?",
            "Are there any quests I can do here?",
            "Where can I get a haircut like yours?"
        )) {
            1 -> {
                chatPlayer(player, "Where am I?")
                chatNpc(player, "This is the town of Lumbridge my friend.")
                when (options(
                    player,
                    "How are you today?",
                    "Do you know of any quests I can do?",
                    "Your shoe lace is untied."
                )) {
                    1 -> howAreYou(player)
                    2 -> anyQuests(player)
                    3 -> shoelace(player)
                }
            }
            2 -> howAreYou(player)
            3 -> anyQuests(player)
            4 -> {
                chatPlayer(player, "Where can I get a haircut like yours?")
                chatNpc(player, "Yes, it does look like you need a hairdresser.")
                chatPlayer(player, "Oh thanks!")
                chatNpc(
                    player, "No problem. The hairdresser in Falador will probably be<br>able to sort you out."
                )
                chatNpc(player, "The Lumbridge general store sells useful maps if you<br>don't know the way.")
            }
        }
    }

    private suspend fun QueueTask.howAreYou(player: Player) {
        chatPlayer(player, "How are you today?")
        chatNpc(player, "Aye, not too bad thank you. Lovely weather in Gielinor<br>this fine day.")
        chatPlayer(player, "Weather?")
        chatNpc(player, "Yes weather, you know.")
        chatNpc(
            player,
            "The state or condition of the atmosphere at a time and<br>place, with respect to variables such as temperature,<br>moisture, wind velocity, and barometric pressure."
        )
        chatPlayer(player, "...")
        chatNpc(player, "Not just a pretty face eh? Ha ha ha.")
    }

    private suspend fun QueueTask.anyQuests(player: Player) {
        chatPlayer(player, "Do you know of any quests I can do?")
        chatNpc(player, "What kind of quest are you looking for?")
        when (options(
            player,
            "I fancy a bit of a fight, anything dangerous?",
            "Something easy please, I'm new here.",
            "I'm a thinker rather than fighter, anything skill oriented?",
            "I want to do all kinds of things, do you know of anything like that?",
            "Maybe another time."
        )) {
            1 -> questFightDialog(player)
            2 -> questEasyDialog(player)
            3 -> questSkillDialog(player)
            4 -> questAllKindsDialog(player)
            5 -> chatPlayer(player, "Maybe another time.")
        }
    }

    private suspend fun QueueTask.questFightDialog(player: Player) {
        chatPlayer(player, "I fancy a bit of a fight, anything dangerous?")
        chatNpc(player, "Hmm.. dangerous you say? What sort of creatures are<br>you looking to fight?")
        when (options(
            player,
            "Big scary demons!",
            "Vampyres!",
            "Small.. something small would be good.",
            "Maybe another time."
        )) {
            1 -> {
                chatPlayer(player, "Big scary demons!")
                chatNpc(player, "You are a brave soul indeed.")
                chatNpc(
                    player,
                    "Now that you mention it, I heard a rumour about a<br>gypsy in Varrock who is rambling about some kind of<br>greater evil.. sounds demon-like if you ask me."
                )
                chatNpc(player, "Perhaps you could check it out if you are as brave as<br>you say?")
                chatPlayer(player, "Thanks for the tip, perhaps I will.")
            }
            2 -> {
                chatPlayer(player, "Vampyres!")
                chatNpc(
                    player,
                    "Ha ha. I personally don't believe in such things.<br>However, there is a man in Draynor Village who has<br>been scaring the village folk with stories of vampyres."
                )
                chatNpc(
                    player,
                    "He's named Morgan and can be found in one of the<br>village houses. Perhaps you could see what the matter<br>is?"
                )
                chatPlayer(player, "Thanks for the tip.")
            }
            3 -> {
                chatPlayer(player, "Small.. something small would be good.")
                chatNpc(player, "Small? Small isn't really that dangerous though is it?")
                chatPlayer(
                    player,
                    "Yes it can be! There could be anything from an evil<br>chicken to a poisonous spider. They attack in numbers<br>you know!"
                )
                chatNpc(
                    player,
                    "Yes ok, point taken. Speaking of small monsters, I hear<br>old Wizard Mizgog in the wizards' tower has just had<br>all his beads taken by a gang of mischievous imps."
                )
                chatNpc(player, "Sounds like it could be a quest for you?")
                chatPlayer(player, "Thanks for your help.")
            }
            4 -> chatPlayer(player, "Maybe another time.")
        }
    }

    private suspend fun QueueTask.questEasyDialog(player: Player) {
        chatPlayer(player, "Something easy please, I'm new here.")
        chatNpc(player, "I can tell you about plenty of small easy tasks.")
        chatNpc(
            player,
            "The Lumbridge cook has been having problems, the<br>Duke is confused over some strange rocks and on top<br>of all that, poor lad Romeo in Varrock has girlfriend<br>problems."
        )
        when (options(
            player,
            "The Lumbridge cook.",
            "The Duke's strange stones.",
            "Romeo and his girlfriend.",
            "Maybe another time.",
            title = "Tell me about.."
        )) {
            1 -> {
                chatPlayer(player, "Tell me about the Lumbridge cook.")
                chatNpc(
                    player,
                    "It's funny really, the cook would forget his head if it<br>wasn't screwed on. This time he forgot to get<br>ingredients for the Duke's birthday cake."
                )
                chatNpc(
                    player,
                    "Perhaps you could help him? You will probably find him<br>in the Lumbridge Castle kitchen."
                )
                chatPlayer(player, "Thank you. I shall go speak with him.")
            }
            2 -> {
                chatPlayer(player, "Tell me about the Duke's strange stones.")
                chatNpc(
                    player,
                    "Well the Duke of Lumbridge has found a strange stone<br>that no one seems to understand. Perhaps you could<br>help him? You can probably find him upstairs in<br>Lumbridge Castle."
                )
                chatPlayer(player, "Sounds mysterious. I may just do that. Thanks.")
            }
            3 -> {
                chatPlayer(player, "Tell me about Romeo and his girlfriend please.")
                chatNpc(
                    player,
                    "Romeo in Varrock needs help with finding his beloved<br>Juliet, you may be able to help him out."
                )
                chatNpc(
                    player,
                    "Unless of course you manage to find Juliet first in<br>which case she has probably lost Romeo."
                )
                chatPlayer(player, "Right, ok. Romeo is in Varrock?")
                chatNpc(player, "Yes you can't miss him, he's wandering aimlessly in the<br>square.")
            }
            4 -> chatPlayer(player, "Maybe another time.")
        }
    }

    private suspend fun QueueTask.questSkillDialog(player: Player) {
        chatPlayer(player, "I'm a thinker rather than fighter, anything skill<br>orientated?")
        chatNpc(
            player,
            "Skills play a big part when you want to progress in<br>knowledge throughout Gielinor. I know of a few skill-<br>related quests that can get you started."
        )
        chatNpc(
            player,
            "You may be able to help out Fred the farmer who is in<br>need of someones crafting expertise."
        )
        chatNpc(player, "Or, there's always Doric the dwarf who needs an<br>errand running for him?")
        when (options(player, "Fred the farmer.", "Doric the dwarf.", "Maybe another time.", title = "Tell me about..")) {
            1 -> {
                chatPlayer(player, "Tell me about Fred the farmer please.")
                chatNpc(
                    player,
                    "You can find Fred next to the field of sheep in<br>Lumbridge. Perhaps you should go and speak with him."
                )
                chatPlayer(player, "Thanks, maybe I will.")
            }
            2 -> {
                chatPlayer(player, "Tell me about Doric the dwarf.")
                chatNpc(
                    player,
                    "Doric the dwarf is located north of Falador. He might<br>be able to help you with smithing. You should speak to<br>him. He may let you use his anvils."
                )
                chatPlayer(player, "Thanks for the tip.")
            }
            3 -> chatPlayer(player, "Maybe another time.")
        }
    }

    private suspend fun QueueTask.questAllKindsDialog(player: Player) {
        chatPlayer(player, "I want to do all kinds of things, do you know of<br>anything like that?")
        chatNpc(player, "Of course I do. Gielinor is a huge place you know, now<br>let me think...")
        chatNpc(
            player,
            "Hetty the witch in Rimmington might be able to offer<br>help in the ways of magical abilities.."
        )
        chatNpc(
            player,
            "Also, pirates are currently docked in Port Sarim,<br>Where pirates are, treasure is never far away..."
        )
        chatNpc(player, "Or you could go help out Ernest who got lost in<br>Draynor Manor, spooky place that.")
        when (options(player,
            "Hetty the Witch.",
            "Pirate's treasure.",
            "Ernest and Draynor Manor.",
            "Maybe another time.",
            title = "Tell me about.."
        )) {
            1 -> {
                chatPlayer(player, "Tell me about Hetty the witch.")
                chatNpc(
                    player,
                    "Hetty the witch can be found in Rimmington, south of<br>Falador. She's currently working on some new potions.<br>Perhaps you could give her a hand? She might be able<br>to offer help with your magical abilities."
                )
                chatPlayer(player, "Ok thanks, let's hope she doesn't turn me into a potato<br>or something..")
            }
            2 -> {
                chatPlayer(player, "Tell me about Pirate's Treasure.")
                chatNpc(
                    player,
                    "RedBeard Frank in Port Sarim's bar, the Rusty<br>Anchor might be able to tell you about the rumored<br>treasure that is buried somewhere in Gielinor."
                )
                chatPlayer(player, "Sounds adventurous, I may have to check that out.<br>Thank you.")
            }
            3 -> {
                chatPlayer(player, "Tell me about Ernest please.")
                chatNpc(
                    player,
                    "The best place to start would be at the gate to<br>Draynor Manor. There you will find Veronica who will<br>be able to tell you more."
                )
                chatNpc(player, "I suggest you tread carefully in that place; it's haunted.")
                chatPlayer(player, "Sounds like fun. I've never been to a Haunted Manor<br>before.")
            }
            4 -> chatPlayer(player, "Maybe another time.")
        }
    }

    private suspend fun QueueTask.stick(player: Player) {
        chatPlayer(player, "Can I buy your stick?")
        chatNpc(player, "It's not a stick! I'll have you know it's a very powerful<br>staff!")
        chatPlayer(player, "Really? Show me what it can do!")
        chatNpc(player, "Um..It's a bit low on power at the moment..")
        chatPlayer(player, "It's a stick isn't it?")
        chatNpc(
            player,
            "...Ok, it's a stick.. But only while I save up for a staff.<br>Zaff in Varrock square sells them in his shop."
        )
        chatPlayer(player, "Well good luck with that.")
    }

    private suspend fun QueueTask.shoelace(player: Player) {
        chatPlayer(player, "Your shoe lace is untied.")
        chatNpc(player, "No it's not!")
        chatPlayer(player, "No you're right. I have nothing to back that up.")
        chatNpc(player, "Fool! Leave me alone!")
    }
}
