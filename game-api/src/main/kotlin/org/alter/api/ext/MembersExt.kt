package org.alter.api.ext

import org.alter.game.model.attr.MEMBERS_EXPIRES_ATTR
import org.alter.game.model.entity.Player
import org.alter.game.model.timer.TimeConstants.DAY
import kotlin.math.roundToInt

fun Player.membersDaysLeft(): Int {
    val now = System.currentTimeMillis()
    val expires = attr.getOrDefault(MEMBERS_EXPIRES_ATTR, System.currentTimeMillis().toString()).toLong()
    return ((expires - now) / DAY.toDouble()).roundToInt()
}

fun Player.hasMembers(): Boolean = membersDaysLeft() > 0
