package org.alter.plugins.content.objects.gates

import org.alter.api.cfg.Sound
import org.alter.api.ext.getInteractingGameObj
import org.alter.api.ext.playSound
import org.alter.api.ext.player
import org.alter.game.Server
import org.alter.game.model.World
import org.alter.game.model.attr.AttributeKey
import org.alter.game.model.entity.DynamicObject
import org.alter.game.model.entity.GameObject
import org.alter.game.model.entity.Player
import org.alter.game.plugin.KotlinPlugin
import org.alter.game.plugin.PluginRepository
import org.alter.plugins.content.objects.door.DoorStickState

class GatePlugin(
    r: PluginRepository, world: World,
    server: Server) : KotlinPlugin(r, world, server) {

    val STICK_STATE = AttributeKey<DoorStickState>()

    init {
        loadService(GateService())

        onWorldInit {
            world.getService(GateService::class.java)?.let { service ->
                service.gates.forEach { gate ->
                    onObjOption(obj = gate.closed.hinge, option = "open", lineOfSightDistance = 1) {
                        openGate(player, player.getInteractingGameObj(), gate)
                    }

                    onObjOption(obj = gate.closed.extension, option = "open", lineOfSightDistance = 1) {
                        openGate(player, player.getInteractingGameObj(), gate)
                    }

                    onObjOption(obj = gate.opened.hinge, option = "close", lineOfSightDistance = 1) {
                        closeGate(player, player.getInteractingGameObj(), gate)
                    }

                    onObjOption(obj = gate.opened.extension, option = "close", lineOfSightDistance = 1) {
                        closeGate(player, player.getInteractingGameObj(), gate)
                    }
                }
            }
        }

    }

    fun copyStickVars(from: GameObject, to: GameObject) {
        if (from.attr.has(STICK_STATE)) {
            to.attr[STICK_STATE] = from.attr[STICK_STATE]!!
        }
    }

    fun openGate(p: Player, obj: GameObject, gates: GateSet) {
        val oldRot = obj.rot

        val hinge = obj.id == gates.closed.hinge || obj.id == gates.opened.hinge
        val extension = obj.id == gates.closed.extension || obj.id == gates.opened.extension
        val otherGateId = if (hinge) gates.closed.extension else gates.closed.hinge

        val otherGate = getNeighbourGate(world, obj, otherGateId) ?: return
        val hingeObj = if (hinge) obj else otherGate
        val extensionObj = if (extension) obj else otherGate

        val newHinge: DynamicObject
        val newExtension: DynamicObject
        when (oldRot) {
            0 -> {
                newHinge = DynamicObject(
                    id = gates.opened.hinge,
                    type = obj.type,
                    rot = 3,
                    tile = hingeObj.tile.transform(-1, 0)
                )
                newExtension = DynamicObject(
                    id = gates.opened.extension,
                    type = obj.type,
                    rot = 3,
                    tile = hingeObj.tile.transform(-2, 0)
                )
            }

            1 -> {
                newHinge = DynamicObject(
                    id = gates.opened.hinge,
                    type = obj.type,
                    rot = 0,
                    tile = hingeObj.tile.transform(0, 1)
                )
                newExtension = DynamicObject(
                    id = gates.opened.extension,
                    type = obj.type,
                    rot = 0,
                    tile = hingeObj.tile.transform(0, 2)
                )
            }

            2 -> {
                newHinge = DynamicObject(
                    id = gates.opened.hinge,
                    type = obj.type,
                    rot = 1,
                    tile = hingeObj.tile.transform(1, 0)
                )
                newExtension = DynamicObject(
                    id = gates.opened.extension,
                    type = obj.type,
                    rot = 1,
                    tile = hingeObj.tile.transform(2, 0)
                )
            }

            3 -> {
                newHinge = DynamicObject(
                    id = gates.opened.hinge,
                    type = obj.type,
                    rot = 2,
                    tile = hingeObj.tile.transform(0, -1)
                )
                newExtension = DynamicObject(
                    id = gates.opened.extension,
                    type = obj.type,
                    rot = 2,
                    tile = hingeObj.tile.transform(0, -2)
                )
            }

            else -> throw IllegalStateException("Invalid object rotation: $obj")
        }

        world.remove(obj)
        world.remove(otherGate)
        world.spawn(newHinge)
        world.spawn(newExtension)

        copyStickVars(hingeObj, newHinge)
        copyStickVars(extensionObj, newExtension)

        p.playSound(Sound.OPEN_DOOR_SFX)
    }

    fun closeGate(p: Player, obj: GameObject, gates: GateSet) {
        val oldRot = obj.rot

        val hinge = obj.id == gates.closed.hinge || obj.id == gates.opened.hinge
        val extension = obj.id == gates.closed.extension || obj.id == gates.opened.extension
        val otherGateId = if (hinge) gates.opened.extension else gates.opened.hinge

        val otherGate = getNeighbourGate(world, obj, otherGateId) ?: return
        val hingeObj = if (hinge) obj else otherGate
        val extensionObj = if (extension) obj else otherGate

        val newHinge: DynamicObject
        val newExtension: DynamicObject
        when (oldRot) {
            0 -> {
                newHinge = DynamicObject(
                    id = gates.closed.hinge,
                    type = obj.type,
                    rot = 1,
                    tile = hingeObj.tile.transform(0, -1)
                )
                newExtension = DynamicObject(
                    id = gates.closed.extension,
                    type = obj.type,
                    rot = 1,
                    tile = hingeObj.tile.transform(1, -1)
                )
            }

            1 -> {
                newHinge = DynamicObject(
                    id = gates.closed.hinge,
                    type = obj.type,
                    rot = 2,
                    tile = hingeObj.tile.transform(-1, 0)
                )
                newExtension = DynamicObject(
                    id = gates.closed.extension,
                    type = obj.type,
                    rot = 2,
                    tile = hingeObj.tile.transform(-1, -1)
                )
            }

            2 -> {
                newHinge = DynamicObject(
                    id = gates.closed.hinge,
                    type = obj.type,
                    rot = 3,
                    tile = hingeObj.tile.transform(0, 1)
                )
                newExtension = DynamicObject(
                    id = gates.closed.extension,
                    type = obj.type,
                    rot = 3,
                    tile = hingeObj.tile.transform(-1, 1)
                )
            }

            3 -> {
                newHinge = DynamicObject(
                    id = gates.closed.hinge,
                    type = obj.type,
                    rot = 0,
                    tile = hingeObj.tile.transform(1, 0)
                )
                newExtension = DynamicObject(
                    id = gates.closed.extension,
                    type = obj.type,
                    rot = 0,
                    tile = hingeObj.tile.transform(1, 1)
                )
            }

            else -> throw IllegalStateException("Invalid object rotation: $obj")
        }

        world.remove(obj)
        world.remove(otherGate)
        world.spawn(newHinge)
        world.spawn(newExtension)

        copyStickVars(hingeObj, newHinge)
        copyStickVars(extensionObj, newExtension)

        p.playSound(Sound.CLOSE_DOOR_SFX)
    }

    fun getNeighbourGate(world: World, obj: GameObject, otherGate: Int): GameObject? {
        val tile = obj.tile

        for (x in -1..1) {
            for (z in -1..1) {
                if (x == 0 && z == 0) {
                    continue
                }
                val transform = tile.transform(x, z)
                val tileObj = world.getObject(transform, type = obj.type)
                if (tileObj?.id == otherGate) {
                    return tileObj
                }
            }
        }
        return null
    }
}

