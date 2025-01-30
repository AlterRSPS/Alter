package org.alter.game.model.collision

object GameObjectShape {
    const val WALL_STRAIGHT = 0
    const val WALL_DIAGONALCORNER = 1
    const val WALL_L = 2
    const val WALL_SQUARECORNER = 3
    const val WALLDECOR_STRAIGHT_NOOFFSET = 4
    const val WALLDECOR_STRAIGHT_OFFSET = 5
    const val WALLDECOR_DIAGONAL_OFFSET = 6
    const val WALLDECOR_DIAGONAL_NOOFFSET = 7
    const val WALLDECOR_DIAGONAL_BOTH = 8
    const val WALL_DIAGONAL = 9
    const val CENTREPIECE_STRAIGHT = 10
    const val CENTREPIECE_DIAGONAL = 11
    const val ROOF_STRAIGHT = 12
    const val ROOF_DIAGONAL_WITH_ROOFEDGE = 13
    const val ROOF_DIAGONAL = 14
    const val ROOF_L_CONCAVE = 15
    const val ROOF_L_CONVEX = 16
    const val ROOF_FLAT = 17
    const val ROOFEDGE_STRAIGHT = 18
    const val ROOFEDGE_DIAGONALCORNER = 19
    const val ROOFEDGE_L = 20
    const val ROOFEDGE_SQUARECORNER = 21
    const val GROUNDDECOR = 22
    const val WALL_L_ALT = 23
    const val WALLDECOR_DIAGONAL_BOTH_ALT = 24

    val WALL_SHAPES =
        intArrayOf(
            WALL_STRAIGHT,
            WALL_DIAGONALCORNER,
            WALL_L,
            WALL_SQUARECORNER,
            WALL_L_ALT,
        )

    val WALL_DECOR_SHAPES =
        intArrayOf(
            WALLDECOR_STRAIGHT_NOOFFSET,
            WALLDECOR_STRAIGHT_OFFSET,
            WALLDECOR_DIAGONAL_OFFSET,
            WALLDECOR_DIAGONAL_NOOFFSET,
            WALLDECOR_DIAGONAL_BOTH,
            WALLDECOR_DIAGONAL_BOTH_ALT,
        )

    val NORMAL_SHAPES =
        intArrayOf(
            WALL_DIAGONAL,
            ROOF_STRAIGHT,
            ROOF_DIAGONAL_WITH_ROOFEDGE,
            ROOF_DIAGONAL,
            ROOF_L_CONCAVE,
            ROOF_L_CONVEX,
            ROOF_FLAT,
            ROOFEDGE_STRAIGHT,
            ROOFEDGE_DIAGONALCORNER,
            ROOFEDGE_L,
            ROOFEDGE_SQUARECORNER,
            CENTREPIECE_STRAIGHT,
            CENTREPIECE_DIAGONAL,
        )

    val GROUND_DECOR_SHAPES =
        intArrayOf(
            GROUNDDECOR,
        )
}
