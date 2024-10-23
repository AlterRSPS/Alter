package org.alter.game.model

data class AnimationSet(
    val readyAnim: Int,
    val turnAnim: Int,
    val walkAnim: Int,
    val walkAnimBack: Int,
    val walkAnimLeft: Int,
    val walkAnimRight: Int,
    val runAnim: Int
)
