package org.alter.game.model.collision

import dev.openrune.cache.CacheManager.getObject
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import it.unimi.dsi.fastutil.objects.ObjectList
import org.alter.game.fs.DefinitionSet
import org.alter.game.model.Direction
import org.alter.game.model.Tile
import org.alter.game.model.entity.GameObject
import org.rsmod.routefinder.loc.LocShapeConstants

class CollisionUpdate private constructor(val type: Type, val flags: Object2ObjectOpenHashMap<Tile, ObjectList<DirectionFlag>>) {
    enum class Type {
        ADD,
        REMOVE,
    }

    class Builder {
        private val flags = Object2ObjectOpenHashMap<Tile, ObjectList<DirectionFlag>>()

        private var type: Type? = null

        fun build(): CollisionUpdate {
            check(type != null) { "Type has not been set." }
            return CollisionUpdate(type!!, flags)
        }

        fun setType(type: Type) {
            check(this.type == null) { "Type has already been set." }
            this.type = type
        }

        fun putTile(
            tile: Tile,
            impenetrable: Boolean,
            vararg directions: Direction,
        ) {
            check(directions.isNotEmpty()) { "Directions must not be empty." }
            val flags = flags[tile] ?: ObjectArrayList<DirectionFlag>()
            directions.forEach { dir -> flags.add(DirectionFlag(dir, impenetrable)) }
            this.flags[tile] = flags
        }

        private fun putWall(
            tile: Tile,
            impenetrable: Boolean,
            orientation: Direction,
        ) {
            putTile(tile, impenetrable, orientation)
            putTile(tile.step(orientation), impenetrable, orientation.getOpposite())
        }

        private fun putLargeCornerWall(
            tile: Tile,
            impenetrable: Boolean,
            orientation: Direction,
        ) {
            val directions = orientation.getDiagonalComponents()
            putTile(tile, impenetrable, *directions)

            directions.forEach { dir ->
                putTile(tile.step(dir), impenetrable, dir.getOpposite())
            }
        }

        fun putObject(
            definitions: DefinitionSet,
            obj: GameObject,
        ) {
            val def = getObject(obj.id)
            val type = obj.type
            val tile = obj.tile

            if (!unwalkable(def, type)) {
                return
            }

            val x = tile.x
            val y = tile.z
            val height = tile.height
            var width = def.sizeX
            var length = def.sizeY
            val impenetrable = def.impenetrable
            val orientation = obj.rot

            if (orientation == 1 || orientation == 3) {
                width = def.sizeY
                length = def.sizeX
            }

            if (type == LocShapeConstants.GROUND_DECOR) {
                if (def.interactive == 1 && def.solid != 1) {
                    putTile(Tile(x, y, height), impenetrable, *Direction.NESW)
                }
            } else if (type >= LocShapeConstants.WALL_DIAGONAL && type < LocShapeConstants.GROUND_DECOR) {
                for (dx in 0 until width) {
                    for (dz in 0 until length) {
                        putTile(Tile(x + dx, y + dz, height), impenetrable, *Direction.NESW)
                    }
                }
            } else if (type == LocShapeConstants.WALL_STRAIGHT) {
                putWall(tile, impenetrable, Direction.WNES[orientation])
            } else if (type == LocShapeConstants.WALL_DIAGONAL_CORNER || type == LocShapeConstants.WALL_SQUARE_CORNER) {
                putWall(tile, impenetrable, Direction.WNES_DIAGONAL[orientation])
            } else if (type == LocShapeConstants.WALL_L) {
                putLargeCornerWall(tile, impenetrable, Direction.WNES_DIAGONAL[orientation])
            }
        }

        private fun unwalkable(
            def: dev.openrune.cache.filestore.definition.data.ObjectType,
            type: Int,
        ): Boolean {
            val isSolidFloorDecoration = type == LocShapeConstants.GROUND_DECOR && def.interactive == 1
            val isRoof = type > LocShapeConstants.CENTREPIECE_DIAGONAL && type < LocShapeConstants.GROUND_DECOR && def.solid != 1
            val isWall = (type >= LocShapeConstants.WALL_STRAIGHT && type <= LocShapeConstants.WALL_SQUARE_CORNER || type == LocShapeConstants.WALL_DIAGONAL) && def.solid != 1
            val isSolidInteractable = (type == LocShapeConstants.CENTREPIECE_DIAGONAL || type == LocShapeConstants.CENTREPIECE_STRAIGHT) && def.solid != 1

            return isWall || isRoof || isSolidInteractable || isSolidFloorDecoration
        }
    }
}
