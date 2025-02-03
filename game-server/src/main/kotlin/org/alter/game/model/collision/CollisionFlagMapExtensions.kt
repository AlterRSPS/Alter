package org.alter.game.model.collision

import dev.openrune.cache.filestore.definition.data.ObjectType
import org.alter.game.model.Tile
import org.alter.game.model.entity.GameObject
import org.rsmod.routefinder.collision.CollisionFlagMap
import org.rsmod.routefinder.flag.CollisionFlag
import org.rsmod.routefinder.loc.LocAngleConstants
import org.rsmod.routefinder.loc.LocShapeConstants

/**
 * All the below code is from: https://github.com/rsmod/rsmod/
 *
 * org.rsmod.game.map.collision.CollisionFlagMapExtensions.kt
 *
 * Commit hash: 4e118a6a7aa1f82a46fa824f9a6a4f7453a8530f
 *
 * Summary of naming differences [Rsmod2->Rsmod1]
 * Coordgrid->Tile
 * Coordgrid#level->Tile#height
 * Coordgrid#translate->Tile#transform
 *
 * LocInfo->GameObject
 * LocInfo#coords->GameObject#tile
 * LocInfo#shapeId->GameObject#type
 * LocInfo#angleId->GameObject#rot
 *
 * UnpackedLocType->ObjectType
 * UnpackedLocType#width->ObjectType#sizeX
 * UnpackedLocType#length->ObjectType#sizeY
 * UnpackedLocType#blockWalk->ObjectType#solid
 * UnpackedLocType#blockRange->ObjectType#impenetrable
 * UnpackedLocType#breakRouteFinding->ObjectType#obstructive
 */
private typealias LocShapes = LocShapeConstants

public operator fun CollisionFlagMap.get(coords: Tile): Int =
    get(coords.x, coords.z, coords.height)

public operator fun CollisionFlagMap.set(coords: Tile, mask: Int): Unit =
    set(coords.x, coords.z, coords.height, mask)

public fun CollisionFlagMap.add(coords: Tile, mask: Int): Unit =
    add(coords.x, coords.z, coords.height, mask)

public fun CollisionFlagMap.remove(coords: Tile, mask: Int): Unit =
    remove(coords.x, coords.z, coords.height, mask)

public fun CollisionFlagMap.addLoc(loc: GameObject, type: ObjectType): Unit =
    toggleLoc(loc, type, add = true)

public fun CollisionFlagMap.removeLoc(loc: GameObject, type: ObjectType): Unit =
    toggleLoc(loc, type, add = false)

public fun CollisionFlagMap.toggleLoc(loc: GameObject, type: ObjectType, add: Boolean) {
    toggleLoc(
        coords = loc.tile,
        width = type.sizeX,
        length = type.sizeY,
        shape = loc.type,
        angle = loc.rot,
        blockWalk = type.solid,
        blockRange = type.impenetrable,
        breakRouteFinding = type.obstructive,
        add = add,
    )
}

public fun CollisionFlagMap.toggleLoc(
    coords: Tile,
    width: Int,
    length: Int,
    shape: Int,
    angle: Int,
    blockWalk: Int,
    blockRange: Boolean,
    breakRouteFinding: Boolean,
    add: Boolean,
) {
    var rotatedWidth = width
    var rotatedLength = length
    if (angle == LocAngleConstants.NORTH || angle == LocAngleConstants.SOUTH) {
        rotatedWidth = length
        rotatedLength = width
    }
    if (shape == LocShapes.GROUND_DECOR) {
        if (blockWalk == 1) {
            toggleGroundDecor(coords, add)
        }
    } else if (shape != LocShapes.CENTREPIECE_STRAIGHT && shape != LocShapes.CENTREPIECE_DIAGONAL) {
        val primary = shape >= LocShapes.ROOF_STRAIGHT || shape == LocShapes.WALL_DIAGONAL
        val wall =
            shape == LocShapes.WALL_STRAIGHT ||
                    shape == LocShapes.WALL_DIAGONAL_CORNER ||
                    shape == LocShapes.WALL_L ||
                    shape == LocShapes.WALL_SQUARE_CORNER
        if (primary && blockWalk != 0) {
            toggleGround(coords, rotatedWidth, rotatedLength, breakRouteFinding, blockRange, add)
        } else if (wall && blockWalk != 0) {
            toggleWall(coords, angle, shape, blockRange, add)
        }
    } else {
        if (blockWalk != 0) {
            toggleGround(coords, rotatedWidth, rotatedLength, breakRouteFinding, blockRange, add)
        }
    }
}

private fun CollisionFlagMap.toggleGround(
    coords: Tile,
    width: Int,
    length: Int,
    blockRoute: Boolean,
    blockRange: Boolean,
    add: Boolean,
) {
    for (x in 0 until width) {
        for (z in 0 until length) {
            val translate = coords.transform(x, z)
            toggle(translate, CollisionFlag.LOC, add)
            if (blockRange) {
                toggle(translate, CollisionFlag.LOC_PROJ_BLOCKER, add)
            }
            if (blockRoute) {
                toggle(translate, CollisionFlag.LOC_ROUTE_BLOCKER, add)
            }
        }
    }
}

@Suppress("CascadeIf")
private fun CollisionFlagMap.toggleWall(
    coords: Tile,
    angle: Int,
    shape: Int,
    blockRange: Boolean,
    add: Boolean,
) {
    if (shape == LocShapes.WALL_STRAIGHT) {
        when (angle) {
            0 -> {
                toggle(coords, CollisionFlag.WALL_WEST, add)
                toggle(coords.transform(-1, 0), CollisionFlag.WALL_EAST, add)
                if (blockRange) {
                    toggle(coords, CollisionFlag.WALL_WEST_PROJ_BLOCKER, add)
                    toggle(coords.transform(-1, 0), CollisionFlag.WALL_EAST_PROJ_BLOCKER, add)
                }
            }
            1 -> {
                toggle(coords, CollisionFlag.WALL_NORTH, add)
                toggle(coords.transform(0, 1), CollisionFlag.WALL_SOUTH, add)
                if (blockRange) {
                    toggle(coords, CollisionFlag.WALL_NORTH_PROJ_BLOCKER, add)
                    toggle(coords.transform(0, 1), CollisionFlag.WALL_SOUTH_PROJ_BLOCKER, add)
                }
            }
            2 -> {
                toggle(coords, CollisionFlag.WALL_EAST, add)
                toggle(coords.transform(1, 0), CollisionFlag.WALL_WEST, add)
                if (blockRange) {
                    toggle(coords, CollisionFlag.WALL_EAST_PROJ_BLOCKER, add)
                    toggle(coords.transform(1, 0), CollisionFlag.WALL_WEST_PROJ_BLOCKER, add)
                }
            }
            3 -> {
                toggle(coords, CollisionFlag.WALL_SOUTH, add)
                toggle(coords.transform(0, -1), CollisionFlag.WALL_NORTH, add)
                if (blockRange) {
                    toggle(coords, CollisionFlag.WALL_SOUTH_PROJ_BLOCKER, add)
                    toggle(coords.transform(0, -1), CollisionFlag.WALL_NORTH_PROJ_BLOCKER, add)
                }
            }
        }
    } else if (shape == LocShapes.WALL_DIAGONAL_CORNER || shape == LocShapes.WALL_SQUARE_CORNER) {
        when (angle) {
            0 -> {
                toggle(coords, CollisionFlag.WALL_NORTH_WEST, add)
                toggle(coords.transform(-1, 1), CollisionFlag.WALL_SOUTH_EAST, add)
                if (blockRange) {
                    toggle(coords, CollisionFlag.WALL_NORTH_WEST_PROJ_BLOCKER, add)
                    toggle(coords.transform(-1, 1), CollisionFlag.WALL_SOUTH_EAST_PROJ_BLOCKER, add)
                }
            }
            1 -> {
                toggle(coords, CollisionFlag.WALL_NORTH_EAST, add)
                toggle(coords.transform(1, 1), CollisionFlag.WALL_SOUTH_WEST, add)
                if (blockRange) {
                    toggle(coords, CollisionFlag.WALL_NORTH_EAST_PROJ_BLOCKER, add)
                    toggle(coords.transform(1, 1), CollisionFlag.WALL_SOUTH_WEST_PROJ_BLOCKER, add)
                }
            }
            2 -> {
                toggle(coords, CollisionFlag.WALL_SOUTH_EAST, add)
                toggle(coords.transform(1, -1), CollisionFlag.WALL_NORTH_WEST, add)
                if (blockRange) {
                    toggle(coords, CollisionFlag.WALL_SOUTH_EAST_PROJ_BLOCKER, add)
                    toggle(coords.transform(1, -1), CollisionFlag.WALL_NORTH_WEST_PROJ_BLOCKER, add)
                }
            }
            3 -> {
                toggle(coords, CollisionFlag.WALL_SOUTH_WEST, add)
                toggle(coords.transform(-1, -1), CollisionFlag.WALL_NORTH_EAST, add)
                if (blockRange) {
                    toggle(coords, CollisionFlag.WALL_SOUTH_WEST_PROJ_BLOCKER, add)
                    toggle(
                        coords.transform(-1, -1),
                        CollisionFlag.WALL_NORTH_EAST_PROJ_BLOCKER,
                        add,
                    )
                }
            }
        }
    } else if (shape == LocShapes.WALL_L) {
        when (angle) {
            0 -> {
                toggle(coords, CollisionFlag.WALL_WEST or CollisionFlag.WALL_NORTH, add)
                toggle(coords.transform(-1, 0), CollisionFlag.WALL_EAST, add)
                toggle(coords.transform(0, 1), CollisionFlag.WALL_SOUTH, add)
                if (blockRange) {
                    val flag =
                        CollisionFlag.WALL_WEST_PROJ_BLOCKER or
                                CollisionFlag.WALL_NORTH_PROJ_BLOCKER
                    toggle(coords, flag, add)
                    toggle(coords.transform(-1, 0), CollisionFlag.WALL_EAST_PROJ_BLOCKER, add)
                    toggle(coords.transform(0, 1), CollisionFlag.WALL_SOUTH_PROJ_BLOCKER, add)
                }
            }
            1 -> {
                toggle(coords, CollisionFlag.WALL_NORTH or CollisionFlag.WALL_EAST, add)
                toggle(coords.transform(0, 1), CollisionFlag.WALL_SOUTH, add)
                toggle(coords.transform(1, 0), CollisionFlag.WALL_WEST, add)
                if (blockRange) {
                    val flag =
                        CollisionFlag.WALL_NORTH_PROJ_BLOCKER or
                                CollisionFlag.WALL_EAST_PROJ_BLOCKER
                    toggle(coords, flag, add)
                    toggle(coords.transform(0, 1), CollisionFlag.WALL_SOUTH_PROJ_BLOCKER, add)
                    toggle(coords.transform(1, 0), CollisionFlag.WALL_WEST_PROJ_BLOCKER, add)
                }
            }
            2 -> {
                toggle(coords, CollisionFlag.WALL_EAST or CollisionFlag.WALL_SOUTH, add)
                toggle(coords.transform(1, 0), CollisionFlag.WALL_WEST, add)
                toggle(coords.transform(0, -1), CollisionFlag.WALL_NORTH, add)
                if (blockRange) {
                    val flag =
                        CollisionFlag.WALL_EAST_PROJ_BLOCKER or
                                CollisionFlag.WALL_SOUTH_PROJ_BLOCKER
                    toggle(coords, flag, add)
                    toggle(coords.transform(1, 0), CollisionFlag.WALL_WEST_PROJ_BLOCKER, add)
                    toggle(coords.transform(0, -1), CollisionFlag.WALL_NORTH_PROJ_BLOCKER, add)
                }
            }
            3 -> {
                toggle(coords, CollisionFlag.WALL_SOUTH or CollisionFlag.WALL_WEST, add)
                toggle(coords.transform(0, -1), CollisionFlag.WALL_NORTH, add)
                toggle(coords.transform(-1, 0), CollisionFlag.WALL_EAST, add)
                if (blockRange) {
                    val flag =
                        CollisionFlag.WALL_SOUTH_PROJ_BLOCKER or
                                CollisionFlag.WALL_WEST_PROJ_BLOCKER
                    toggle(coords, flag, add)
                    toggle(coords.transform(0, -1), CollisionFlag.WALL_NORTH_PROJ_BLOCKER, add)
                    toggle(coords.transform(-1, 0), CollisionFlag.WALL_EAST_PROJ_BLOCKER, add)
                }
            }
        }
    }
}

private fun CollisionFlagMap.toggleGroundDecor(coords: Tile, add: Boolean) {
    toggle(coords, CollisionFlag.GROUND_DECOR, add)
}

private fun CollisionFlagMap.toggle(coords: Tile, mask: Int, add: Boolean) {
    if (add) {
        add(coords.x, coords.z, coords.height, mask)
    } else {
        remove(coords.x, coords.z, coords.height, mask)
    }
}