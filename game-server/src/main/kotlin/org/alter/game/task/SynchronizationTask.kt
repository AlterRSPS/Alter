package org.alter.game.task

import net.rsprot.crypto.xtea.XteaKey
import net.rsprot.protocol.game.outgoing.info.npcinfo.SetNpcUpdateOrigin
import net.rsprot.protocol.game.outgoing.info.util.BuildArea
import net.rsprot.protocol.game.outgoing.map.RebuildNormal
import net.rsprot.protocol.game.outgoing.map.RebuildRegion
import net.rsprot.protocol.game.outgoing.map.util.RebuildRegionZone
import net.rsprot.protocol.game.outgoing.worldentity.SetActiveWorldV2
import org.alter.game.model.Coordinate
import org.alter.game.model.Tile
import org.alter.game.model.World
import org.alter.game.model.entity.Npc
import org.alter.game.model.entity.Player
import org.alter.game.model.instance.InstancedChunkSet
import org.alter.game.model.region.Chunk
import org.alter.game.service.GameService

/**
 * A [GameTask] that is responsible for sending [org.alter.game.model.entity.Pawn]
 * data to [org.alter.game.model.entity.Pawn]s.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class SequentialSynchronizationTask : GameTask {
    override fun execute(
        world: World,
        service: GameService,
    ) {
        val worldPlayers = world.players
        val worldNpcs = world.npcs

        worldPlayers.forEach(Player::playerCoordCycleTask)

        world.network.worldEntityInfoProtocol.update()

        // First off, write the world entity info to the client - it must
        // be aware of the updates before receiving the rebuild world entity packets
        world.players.forEach {
            if (it.entityType.isHumanControlled && it.initiated) {
                it.write(it.worldEntityInfo.toPacket()) // try-catch it, this _can_ throw exceptions during .toPacket()
            }
        }

        world.players.forEach {
            if (it.entityType.isHumanControlled && it.initiated) {
                // If the player is not on a dynamic world entity, we can set the
                // origin point as the local player coordinate
                val (x, z, level) = it.tile
                it.npcInfo.updateCoord(-1, level, x, z)
                it.playerInfo.updateRenderCoord(-1, level, x, z)
            }
        }
        world.network.playerInfoProtocol.update()
        world.network.npcInfoProtocol.update()

        world.players.forEach {
            /**
             * Non-human [org.alter.game.model.entity.Player]s do not need this
             * to send any synchronization data to their game-client as they do
             * not have one.
             */
            if (it.entityType.isHumanControlled && it.initiated) {
                it.write(SetActiveWorldV2(SetActiveWorldV2.RootWorldType(it.tile.height)))
                it.write(it.playerInfo.toPacket()) // try-catch it, this _can_ throw exceptions during .toPacket()
                it.write(
                    SetNpcUpdateOrigin(
                        it.tile.x - (it.buildArea.zoneX shl 3),
                        it.tile.z - (it.buildArea.zoneZ shl 3),
                    ),
                )
                it.write(it.npcInfo.toPacket(-1))
            }
        }

        for (n in worldNpcs.entries) {
            n?.npcPostSynchronizationTask()
        }
        worldPlayers.forEach(Player::postCycle)
    }
}

fun Player.playerPreSynchronizationTask() {
    val pawn = this
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
        val rebuildMessage =
            when {
                instance != null -> {
                    RebuildRegion(
                        current.x shr 3,
                        current.z shr 3,
                        true,
                        object : RebuildRegion.RebuildRegionZoneProvider {
                            override fun provide(
                                zoneX: Int,
                                zoneZ: Int,
                                level: Int,
                            ): RebuildRegionZone? {
                                val coord = InstancedChunkSet.getCoordinates(zoneX, zoneZ, level)
                                val chunk = instance.chunks.values[coord] ?: return null
                                return RebuildRegionZone(
                                    chunk.zoneX,
                                    chunk.zoneZ,
                                    chunk.height,
                                    chunk.rot,
                                    XteaKey.ZERO,
                                )
                            }
                        },
                    )
                }
                else -> RebuildNormal(current.x shr 3, current.z shr 3, -1, xteaService)
            }
        pawn.buildArea =
            BuildArea((current.x ushr 3) - 6, (current.z ushr 3) - 6).apply {
                pawn.playerInfo.updateBuildArea(-1, this)
                pawn.npcInfo.updateBuildArea(-1, this)
                pawn.worldEntityInfo.updateBuildArea(this)
            }
        pawn.write(rebuildMessage)
    }
}

private fun shouldRebuildRegion(
    old: Coordinate,
    new: Tile,
): Boolean {
    val dx = new.x - old.x
    val dz = new.z - old.z

    return dx <= Player.NORMAL_VIEW_DISTANCE || dx >= Chunk.MAX_VIEWPORT - Player.NORMAL_VIEW_DISTANCE - 1 ||
        dz <= Player.NORMAL_VIEW_DISTANCE || dz >= Chunk.MAX_VIEWPORT - Player.NORMAL_VIEW_DISTANCE - 1
}

fun Npc.npcPreSynchronizationTask() {
    val pawn = this
    pawn.movementQueue.cycle()
}

fun Npc.npcPostSynchronizationTask() {
    val pawn = this
    val oldTile = pawn.lastTile
    val moved = oldTile == null || !oldTile.sameAs(pawn.tile)

    if (moved) {
        pawn.lastTile = pawn.tile
    }
    pawn.moved = false
    pawn.steps = null
}

/**
 * Updates the coords for all players within the rsprot library. This is run after processing to properly account for
 * displacement effects [dspear, etc]
 */
fun Player.playerCoordCycleTask() {
    this.playerInfo.updateCoord(this.tile.height, this.tile.x, this.tile.z)
    this.npcInfo.updateCoord(-1, this.tile.height, this.tile.x, this.tile.z)
    this.worldEntityInfo.updateCoord(-1, this.tile.height, this.tile.x, this.tile.z)
}
