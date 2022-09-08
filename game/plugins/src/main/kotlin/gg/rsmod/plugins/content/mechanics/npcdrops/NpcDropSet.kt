package gg.rsmod.plugins.content.mechanics.npcdrops

import gg.rsmod.plugins.content.mechanics.gates.Gate

data class NpcDropSet(val id: Int, val name: String, val members: Boolean, val quantity: String, val noted : Boolean, val rarity : Float, val rolls : Int)