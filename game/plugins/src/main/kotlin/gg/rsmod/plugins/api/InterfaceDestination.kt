package gg.rsmod.plugins.api

import gg.rsmod.game.model.interf.DisplayMode





enum class InterfaceDestination(val interfaceId: Int, val fixedChildId: Int, val resizeChildId: Int, val resizeListChildId: Int,
                                val fullscreenChildId: Int = -1, val clickThrough: Boolean = true) {

    CHAT_BOX(interfaceId = 162, fixedChildId = 10, resizeChildId = 94, resizeListChildId = 91, fullscreenChildId = 1),
    XP_COUNTER(interfaceId = 122, fixedChildId = 21, resizeChildId = 4, resizeListChildId = 11, fullscreenChildId = 11),
    ATTACK(interfaceId = 593, fixedChildId = 79, resizeChildId = 35, resizeListChildId = 72, fullscreenChildId = 15),
    SKILLS(interfaceId = 320, fixedChildId = 80, resizeChildId = 36, resizeListChildId = 73, fullscreenChildId = 16),
    QUEST_ROOT(interfaceId = 629, fixedChildId = 81, resizeChildId = 37, resizeListChildId = 74, fullscreenChildId = 17),
    INVENTORY(interfaceId = 149, fixedChildId = 82, resizeChildId = 38, resizeListChildId = 75, fullscreenChildId = 18),
    EQUIPMENT(interfaceId = 387, fixedChildId = 83, resizeChildId = 39, resizeListChildId = 76, fullscreenChildId = 19),
    PRAYER(interfaceId = 541, fixedChildId = 84, resizeChildId = 40, resizeListChildId = 77, fullscreenChildId = 20),
    MAGIC(interfaceId = 218, fixedChildId = 85, resizeChildId = 41, resizeListChildId = 78, fullscreenChildId = 21),
    CLAN_CHAT(interfaceId = 707, fixedChildId = 86, resizeChildId = 42, resizeListChildId = 79, fullscreenChildId = 22),
    ACCOUNT_MANAGEMENT(interfaceId = 109, fixedChildId = 87, resizeChildId = 43, resizeListChildId = 80, fullscreenChildId = 23),
    SOCIAL(interfaceId = 429, fixedChildId = 88, resizeChildId = 44, resizeListChildId = 81, fullscreenChildId = 24),
    LOG_OUT(interfaceId = 182, fixedChildId = 89, resizeChildId = 45, resizeListChildId = 82, fullscreenChildId = 25),
    SETTINGS(interfaceId = 116, fixedChildId = 90, resizeChildId = 46, resizeListChildId = 83, fullscreenChildId = 26),
    EMOTES(interfaceId = 216, fixedChildId = 91, resizeChildId = 47, resizeListChildId = 84, fullscreenChildId = 27),
    MUSIC(interfaceId = 239, fixedChildId = 92, resizeChildId = 48, resizeListChildId = 85, fullscreenChildId = 28),
    PRIVATE_CHAT(interfaceId = 163, fixedChildId = 35, resizeChildId = 16, resizeListChildId = 88, fullscreenChildId = 30),
    MINI_MAP(interfaceId = 160, fixedChildId = 24, resizeChildId = 36, resizeListChildId = 90, fullscreenChildId = 31),
    MAIN_SCREEN(interfaceId = -1, fixedChildId = 9, resizeChildId = 20, resizeListChildId = 16, fullscreenChildId = 13, clickThrough = false),
    TAB_AREA(interfaceId = -1, fixedChildId = 16, resizeChildId = 72, resizeListChildId = 70, clickThrough = false),
    WALKABLE(interfaceId = -1, fixedChildId = 9, resizeChildId = 3, resizeListChildId = 3),
    WORLD_MAP(interfaceId = -1, fixedChildId = 9, resizeChildId = 17, resizeListChildId = 15, fullscreenChildId = 36),
    WORLD_MAP_FULL(interfaceId = -1, fixedChildId = 27, resizeChildId = 21, resizeListChildId = 37, fullscreenChildId = 27, clickThrough = false), // @TODO
    OVERLAY(interfaceId = -1, fixedChildId = 9, resizeChildId = 92, resizeListChildId = 68, fullscreenChildId = 29);

    /**
     * gg.rsmod.game.message.impl.RebuildNormalMessage
    gg.rsmod.game.message.impl.UpdateZonePartialEnclosedMessage
     */
    fun isSwitchable(): Boolean = when (this) {
        CHAT_BOX, MAIN_SCREEN, WALKABLE, TAB_AREA,
        ATTACK, SKILLS, QUEST_ROOT, INVENTORY, EQUIPMENT,
        PRAYER, MAGIC, CLAN_CHAT, ACCOUNT_MANAGEMENT,
        SOCIAL, LOG_OUT, SETTINGS, EMOTES, MUSIC, OVERLAY,
        PRIVATE_CHAT, MINI_MAP, XP_COUNTER, WORLD_MAP -> true
        else -> false
    }

    companion object {
        val values = enumValues<InterfaceDestination>()

        fun getModals() = values.filter { pane -> pane.interfaceId != -1 }
    }
}

fun getDisplayComponentId(displayMode: DisplayMode) =
    when (displayMode) {
        DisplayMode.FIXED -> 165
        DisplayMode.RESIZABLE_NORMAL -> 161
        DisplayMode.RESIZABLE_LIST -> 164
        DisplayMode.FULLSCREEN -> 165
        else -> throw RuntimeException("Unhandled display mode.")
    }

fun getChildId(pane: InterfaceDestination, displayMode: DisplayMode): Int = when (displayMode) {
    DisplayMode.FIXED -> pane.fixedChildId
    DisplayMode.RESIZABLE_NORMAL -> pane.resizeChildId
    DisplayMode.RESIZABLE_LIST -> pane.resizeListChildId
    DisplayMode.FULLSCREEN -> pane.fullscreenChildId
    else -> throw RuntimeException("Unhandled display mode.")
}