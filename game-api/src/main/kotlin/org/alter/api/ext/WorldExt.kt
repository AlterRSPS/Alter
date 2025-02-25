package org.alter.api.ext

import org.alter.game.model.World
import org.alter.game.model.collision.WALL_DIAGONAL
import org.alter.game.model.entity.DynamicObject
import org.alter.game.model.entity.GameObject
import org.alter.rscm.RSCM.getRSCM
import kotlin.math.abs

fun World.openDoor(
    obj: GameObject,
    opened: Int = obj.id + 1,
    invertRot: Boolean = false,
    invertTransform: Boolean = false,
): GameObject {
    val oldRot = obj.rot
    val newRot = abs((oldRot + (if (invertRot) -1 else 1)) and 0x3)
    val diagonal = obj.type == WALL_DIAGONAL

    val newTile =
        when (oldRot) {
            0 -> if (diagonal) obj.tile.transform(0, 1) else obj.tile.transform(if (invertTransform) 1 else -1, 0)
            1 -> if (diagonal) obj.tile.transform(1, 0) else obj.tile.transform(0, if (invertTransform) -1 else 1)
            2 -> if (diagonal) obj.tile.transform(0, -1) else obj.tile.transform(if (invertTransform) -1 else 1, 0)
            3 -> if (diagonal) obj.tile.transform(-1, 0) else obj.tile.transform(0, if (invertTransform) 1 else -1)
            else -> throw IllegalStateException("Invalid door rotation: [currentRot=$oldRot, replaceRot=$newRot]")
        }

    val newDoor = DynamicObject(id = opened, type = obj.type, rot = newRot, tile = newTile)
    remove(obj)
    spawn(newDoor)
    return newDoor
}

fun World.openDoor(
    obj: GameObject,
    opened: String,
    invertRot: Boolean = false,
    invertTransform: Boolean = false,
): GameObject = this.openDoor(obj, getRSCM(opened), invertRot, invertTransform)


fun World.closeDoor(
    obj: GameObject,
    closed: Int = obj.id - 1,
    invertRot: Boolean = false,
    invertTransform: Boolean = false,
): GameObject {
    val oldRot = obj.rot
    val newRot = abs((oldRot + (if (invertRot) 1 else -1)) and 0x3)
    val diagonal = obj.type == WALL_DIAGONAL

    val newTile =
        when (oldRot) {
            0 -> if (diagonal) obj.tile.transform(1, 0) else obj.tile.transform(0, if (invertTransform) -1 else 1)
            1 -> if (diagonal) obj.tile.transform(0, -1) else obj.tile.transform(if (invertTransform) -1 else 1, 0)
            2 -> if (diagonal) obj.tile.transform(-1, 0) else obj.tile.transform(0, if (invertTransform) 1 else -1)
            3 -> if (diagonal) obj.tile.transform(0, 1) else obj.tile.transform(if (invertTransform) 1 else -1, 0)
            else -> throw IllegalStateException("Invalid door rotation: [currentRot=$oldRot, replaceRot=$newRot]")
        }

    val newDoor = DynamicObject(id = closed, type = obj.type, rot = newRot, tile = newTile)
    remove(obj)
    spawn(newDoor)
    return newDoor
}

fun World.closeDoor(
    obj: GameObject,
    closed: String,
    invertRot: Boolean = false,
    invertTransform: Boolean = false,
): GameObject = this.closeDoor(obj, getRSCM(closed), invertRot, invertTransform)

//
// fun World.getSettings() : Settings {
//    return this.settings as Settings
// }
