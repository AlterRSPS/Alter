package org.alter.game.sync.task

import net.rsprot.crypto.xtea.XteaKey
import net.rsprot.protocol.game.outgoing.info.util.BuildArea
import net.rsprot.protocol.game.outgoing.map.RebuildNormal
import net.rsprot.protocol.game.outgoing.map.RebuildRegion
import org.alter.game.model.Coordinate
import org.alter.game.model.Tile
import org.alter.game.model.entity.Player
import org.alter.game.model.instance.InstancedChunkSet
import org.alter.game.model.region.Chunk
import org.alter.game.sync.SynchronizationTask

/**
 * @author Tom <rspsmods@gmail.com>
 */
object PlayerPreSynchronizationTask : SynchronizationTask<Player> {

    override fun run(pawn: Player) {
        pawn.handleFutureRoute()
        pawn.movementQueue.cycle()

        val last = pawn.lastKnownRegionBase
        val current = pawn.tile

        if (last == null || shouldRebuildRegion(last, current)) {
            val regionX = ((current.x shr 3) - (Chunk.MAX_VIEWPORT shr 4)) shl 3
            val regionZ = ((current.z shr 3) - (Chunk.MAX_VIEWPORT shr 4)) shl 3
            // @TODO UpdateZoneFullFollowsMessage
            pawn.lastKnownRegionBase = Coordinate(regionX, regionZ, current.height)

            val xteaService = pawn.world.xteaKeyService!!
            val instance = pawn.world.instanceAllocator.getMap(current)
            val rebuildMessage = when {
                instance != null -> {
                    RebuildRegion(current.x shr 3, current.z shr 3, true, object  : RebuildRegion.RebuildRegionZoneProvider {
                        override fun provide(zoneX: Int, zoneZ: Int, level: Int): RebuildRegion.RebuildRegionZone? {
                            val coord = InstancedChunkSet.getCoordinates(zoneX, zoneZ, level)
                            val chunk = instance.chunks.values[coord]?: return null
                            return RebuildRegion.RebuildRegionZone(
                                chunk.zoneX,
                                chunk.zoneZ,
                                chunk.height,
                                chunk.rot,
                                XteaKey.ZERO
                            )
                        }
                    })
                }
                else -> RebuildNormal(current.x shr 3, current.z shr 3, xteaService)
            }
            val buildArea = BuildArea((current.x ushr 3) - 6, (current.z ushr 3) - 6)
            pawn.playerInfo.updateBuildArea(buildArea)
            pawn.npcInfo.updateBuildArea(buildArea)
            pawn.write(rebuildMessage)
        }
    }

    private fun shouldRebuildRegion(old: Coordinate, new: Tile): Boolean {
        val dx = new.x - old.x
        val dz = new.z - old.z

        return dx <= Player.NORMAL_VIEW_DISTANCE || dx >= Chunk.MAX_VIEWPORT - Player.NORMAL_VIEW_DISTANCE - 1
                || dz <= Player.NORMAL_VIEW_DISTANCE || dz >= Chunk.MAX_VIEWPORT - Player.NORMAL_VIEW_DISTANCE - 1
    }
}