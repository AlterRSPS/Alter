package org.alter.game.model.move

enum class MovementType(val value: Int) {
    STATIONARY(-1),
    CRAWL(0),
    WALK(1),
    RUN(2)
}