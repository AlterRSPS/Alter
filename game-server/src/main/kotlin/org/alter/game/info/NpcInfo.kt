package org.alter.game.info

import org.alter.game.model.entity.Npc

/**
 * @TODO Basically make all properties all within this block <-- Updated and etc and then send out everything out on next tick,
 */
class NpcInfo(var npc: Npc) {
    var avatar = npc.avatar
    private val info = avatar.extendedInfo

    fun walk(x: Int, y: Int) {
        avatar.walk(x, y)
    }

    fun setSpotAnim(
        slot: Int,
        id: Int,
        height: Int,
        delay: Int,
    ) {
        info.setSpotAnim(slot, id, delay, height)
    }

    fun setInaccessible(state: Boolean) {
        avatar.setInaccessible(state)
    }

    fun addHeadBar(
        sourceIndex: Int,
        selfType: Int,
        otherType: Int = selfType,
        startFill: Int,
        endFill: Int = startFill,
        startTime: Int = 0,
        endTime: Int = 0,
    ) {
        info.addHeadBar(
            sourceIndex,
            selfType,
            otherType,
            startFill,
            endFill,
            startTime,
            endTime
        )
    }

    fun addHitMark(
        sourceIndex: Int,
        selfType: Int,
        otherType: Int = selfType,
        value: Int,
        delay: Int = 0,
    ) {
        info.addHitMark(
            sourceIndex,
            selfType,
            otherType,
            value,
            delay
        )
    }

    fun setSequence(
        id: Int,
        delay: Int
    ) {
        info.setSequence(id, delay)
    }

    fun setTinting(
        startTime: Int,
        endTime: Int,
        hue: Int,
        saturation: Int,
        lightness: Int,
        weight: Int,
    ) {
        info.setTinting(
            startTime,
            endTime,
            hue,
            saturation,
            lightness,
            weight
        )
    }

    fun setCombatLevelChange(level: Int) {
        info.setCombatLevelChange(level)
    }

    fun setTempName(name: String) {
        info.setNameChange(name)
    }

    fun setSay(say: String) {
        info.setSay(say)
    }

    fun setFaceCoord(
        x: Int,
        z: Int,
        instant: Boolean = false,
    ) {
        info.setFaceCoord(x, z, instant)
    }

    fun setFacePathingEntity(index: Int) {
        info.setFacePathingEntity(index)
    }

    fun teleport(
        level: Int,
        x: Int,
        z: Int,
        jump: Boolean,
    ) {
        avatar.teleport(
            level = level,
            x = x,
            z = z,
            jump = jump
        )
    }

    fun setAllOpsInvisible() {
        info.setAllOpsInvisible()
    }
    fun setAllOpsVisible() {
        info.setAllOpsVisible()
    }
}