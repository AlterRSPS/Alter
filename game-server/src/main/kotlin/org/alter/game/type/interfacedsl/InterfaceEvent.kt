package org.alter.game.type.interfacedsl

data class InterfaceEvent(
    val startIndex: Int,
    val endIndex: Int,
    val events: List<InterfaceFlag>
)