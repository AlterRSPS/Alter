package org.alter.game.type.bas

data class BasType(
    val name: String,
    val walkAnim: Int,
    val runAnim: Int,
    val readyAnim: Int,
    val turnAnim: Int,
    val walkAnimBack: Int,
    val walkAnimLeft: Int,
    val walkAnimRight: Int,
    val accurateAnim: Int,
    val accurateSound: Int,
    val aggressiveAnim: Int,
    val aggressiveSound: Int,
    val controlledAnim: Int,
    val controlledSound: Int,
    val defensiveAnim: Int,
    val defensiveSound: Int,
    val blockAnim: Int,
)