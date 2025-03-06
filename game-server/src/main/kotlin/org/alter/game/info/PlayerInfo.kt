//package org.alter.game.info
//
//import dev.openrune.cache.CacheManager
//import org.alter.game.model.ForcedMovement
//import org.alter.game.model.Tile
//import org.alter.game.model.appearance.Gender
//import org.alter.game.model.entity.Player
//import org.alter.game.model.move.MovementType
//
//class PlayerInfo(var player: Player) {
//    /**
//     * @TODO Rewip
//     */
//
//    public val info = player.playerInfo.avatar.extendedInfo
//    val DEFAULT_ANIM_SET = AnimationSet(readyAnim = 808, turnAnim = 823, walkAnim = 819, walkAnimBack = 820, walkAnimLeft = 821, walkAnimRight = 822, runAnim = 824)
//    var animSequance = DEFAULT_ANIM_SET
//
//    private fun setIdentKit(slot: IdentKitSlot, value: Int) {
//        info.setIdentKit(slot.index, value)
//    }
//
//
//
//
//    fun syncAnimationSet() {
//        info.setBaseAnimationSet(
//            readyAnim = animSequance.readyAnim,
//            turnAnim = animSequance.turnAnim,
//            walkAnim = animSequance.walkAnim,
//            walkAnimBack = animSequance.walkAnimBack,
//            walkAnimLeft = animSequance.walkAnimLeft,
//            walkAnimRight = animSequance.walkAnimRight,
//            runAnim = animSequance.runAnim,
//        )
//    }
//
//
//
//
//
//    fun tinting(hue: Int = 0, saturation: Int = 0, luminance: Int = 0, opacity: Int = 0, delay: Int = 0, duration: Int = 0) {
//        info.setTinting(
//            startTime = delay,
//            endTime = duration,
//            hue = hue,
//            saturation = saturation,
//            lightness = luminance,
//            weight = opacity,
//        )
//    }
//
//    fun setSay(message:String) {
//        info.setSay(message)
//    }
//
//    fun setFaceCoord(
//        face: Tile,
//        width: Int = 1,
//        length: Int = 1,
//    ) {
//        val srcX = player.tile.x * 64
//        val srcZ = player.tile.z * 64
//        val dstX = face.x * 64
//        val dstZ = face.z * 64
//        var degreesX = (srcX - dstX).toDouble()
//        var degreesZ = (srcZ - dstZ).toDouble()
//        degreesX += (Math.floor(width / 2.0)) * 32
//        degreesZ += (Math.floor(length / 2.0)) * 32
//        info.setFaceAngle((Math.atan2(degreesX, degreesZ) * 325.949).toInt() and 0x7ff)
//    }
//
//    fun facePawn(index: Int) {
//        info.setFacePathingEntity(index)
//    }
//
//    fun graphic(id: Int, height: Int, delay: Int) {
//        info.setSpotAnim(0, id, delay, height)
//    }
//
//    fun forceMove(movement: ForcedMovement) {
//        info.setExactMove(
//            deltaX1 = movement.diffX1,
//            deltaZ1 = movement.diffZ1,
//            delay1 = movement.clientDuration1,
//            deltaX2 = movement.diffX2,
//            deltaZ2 = movement.diffZ2,
//            delay2 = movement.clientDuration2,
//            angle = movement.directionAngle,
//        )
//    }
//
//    fun setMoveSpeed(movementType: MovementType) {
//        info.setMoveSpeed(movementType.value)
//    }
//}