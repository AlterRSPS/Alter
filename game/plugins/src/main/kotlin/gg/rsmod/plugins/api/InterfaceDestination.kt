package gg.rsmod.plugins.api

import gg.rsmod.game.model.interf.DisplayMode

enum class InterfaceDestination(val interfaceId: Int, val fixedChildId: Int, val resizeChildId: Int, val resizeListChildId: Int,
                                val fullscreenChildId: Int = -1, val clickThrough: Boolean = true) {

    CHAT_BOX(interfaceId = 162, fixedChildId = 32, resizeChildId = 37, resizeListChildId = 39, fullscreenChildId = 1),

    XP_COUNTER(interfaceId = 122, fixedChildId = 21, resizeChildId = 11, resizeListChildId = 11, fullscreenChildId = 11),

    ATTACK(interfaceId = 593, fixedChildId = 75, resizeChildId = 78, resizeListChildId = 77, fullscreenChildId = 15),

    SKILLS(interfaceId = 320, fixedChildId = 76, resizeChildId = 79, resizeListChildId = 78, fullscreenChildId = 16),

    QUEST_ROOT(interfaceId = 629, fixedChildId = 77, resizeChildId = 80, resizeListChildId = 79, fullscreenChildId = 17),

    INVENTORY(interfaceId = 149, fixedChildId = 78, resizeChildId = 81, resizeListChildId = 80, fullscreenChildId = 18),

    EQUIPMENT(interfaceId = 387, fixedChildId = 79, resizeChildId = 82, resizeListChildId = 81, fullscreenChildId = 19),

    PRAYER(interfaceId = 541, fixedChildId = 80, resizeChildId = 83, resizeListChildId = 82, fullscreenChildId = 20),

    MAGIC(interfaceId = 218, fixedChildId = 81, resizeChildId = 84, resizeListChildId = 83, fullscreenChildId = 21),

    CLAN_CHAT(interfaceId = 707, fixedChildId = 82, resizeChildId = 85, resizeListChildId = 84, fullscreenChildId = 22),

    ACCOUNT_MANAGEMENT(interfaceId = 109, fixedChildId = 83, resizeChildId = 86, resizeListChildId = 85, fullscreenChildId = 23),

    SOCIAL(interfaceId = 429, fixedChildId = 84, resizeChildId = 87, resizeListChildId = 86, fullscreenChildId = 24),

    LOG_OUT(interfaceId = 182, fixedChildId = 85, resizeChildId = 88, resizeListChildId = 87, fullscreenChildId = 25),

    SETTINGS(interfaceId = 116, fixedChildId = 86, resizeChildId = 89, resizeListChildId = 88, fullscreenChildId = 26),

    EMOTES(interfaceId = 216, fixedChildId = 87, resizeChildId = 90, resizeListChildId = 89, fullscreenChildId = 27),

    MUSIC(interfaceId = 239, fixedChildId = 88, resizeChildId = 91, resizeListChildId = 90, fullscreenChildId = 28),

    PRIVATE_CHAT(interfaceId = 163, fixedChildId = 23, resizeChildId = 16, resizeListChildId = 16, fullscreenChildId = 30),

    MINI_MAP(interfaceId = 160, fixedChildId = 11, resizeChildId = 36, resizeListChildId = 36, fullscreenChildId = 31),

    MAIN_SCREEN(interfaceId = -1, fixedChildId = 28, resizeChildId = 20, resizeListChildId = 20, fullscreenChildId = 13,
            clickThrough = false),

    TAB_AREA(interfaceId = -1, fixedChildId = 73, resizeChildId = 76, resizeListChildId = 75,
            clickThrough = false),

    WALKABLE(interfaceId = -1, fixedChildId = 14, resizeChildId = 3, resizeListChildId = 3),

    WORLD_MAP(interfaceId = -1, fixedChildId = 29, resizeChildId = 21, resizeListChildId = 21, fullscreenChildId = 36),

    WORLD_MAP_FULL(interfaceId = -1, fixedChildId = 27, resizeChildId = 27, resizeListChildId = 27, fullscreenChildId = 27, clickThrough = false),
    // interface 90??
    OVERLAY(interfaceId = -1, fixedChildId = 16, resizeChildId = 5, resizeListChildId = 5, fullscreenChildId = 29)
    ;

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
    DisplayMode.FIXED -> 548
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