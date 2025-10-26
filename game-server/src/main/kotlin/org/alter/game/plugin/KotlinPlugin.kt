package org.alter.game.plugin

import dev.openrune.cache.CacheManager.getItem
import dev.openrune.cache.CacheManager.getNpc
import dev.openrune.cache.CacheManager.getObject
import org.alter.rscm.RSCM.getRSCM
import org.alter.game.Server
import org.alter.game.event.Event
import org.alter.game.fs.ObjectExamineHolder
import org.alter.game.model.Direction
import org.alter.game.model.EntityType
import org.alter.game.model.Tile
import org.alter.game.model.World
import org.alter.game.model.combat.NpcCombatDef
import org.alter.game.model.container.key.ContainerKey
import org.alter.game.model.entity.DynamicObject
import org.alter.game.model.entity.GroundItem
import org.alter.game.model.entity.Npc
import org.alter.game.model.entity.Player
import org.alter.game.model.shop.PurchasePolicy
import org.alter.game.model.shop.Shop
import org.alter.game.model.shop.ShopCurrency
import org.alter.game.model.shop.StockType
import org.alter.game.model.timer.TimerKey
import org.alter.game.service.Service

/**
 * Represents a KotlinScript plugin.
 *
 * @author Tom <rspsmods@gmail.com>
 */
@Suppress("UNUSED", "Underscore", "ktlint:standard:function-naming")
abstract class KotlinPlugin(
    private val r: PluginRepository,
    val world: World,
    val server: Server,
) {
    /**
     * A map of properties that will be copied from the [PluginMetadata] and
     * exposed to the plugin.
     */
    private lateinit var properties: MutableMap<String, Any>

    /**
     * Get property associated with [key] casted as [T].
     */
    @Suppress("UNCHECKED_CAST")
    fun <T> getProperty(key: String): T? = properties[key] as? T?

    /**
     * Load [service] on plugin start-up.
     */
    fun loadService(service: Service) {
        r.services.add(service)
    }

    /**
     * Set the [org.alter.game.model.region.ChunkCoords] with [chunk] as its
     * [org.alter.game.model.region.ChunkCoords.hashCode], as a multi-combat area.
     */
    fun setMultiCombatChunk(chunk: Int) {
        r.multiCombatChunks.add(chunk)
    }

    /**
     * Set the 8x8 [org.alter.game.model.region.ChunkCoords]s that belong to [region]
     * as multi-combat areas.
     */
    fun setMultiCombatRegion(region: Int) {
        r.multiCombatRegions.add(region)
    }

    /**
     * Set the [NpcCombatDef] for npcs with [Npc.id] of [npc].
     */
    fun setCombatDef(
        npc: String,
        def: NpcCombatDef,
    ) {
        var npc = getRSCM(npc)
        check(!r.npcCombatDefs.containsKey(npc)) { "Npc combat definition has been previously set: $npc" }
        r.npcCombatDefs[npc] = def
    }

    /**
     * Set the [NpcCombatDef] for npcs with [Npc.id] of [npc] and [others].
     */
    fun setCombatDef(
        npc: String,
        vararg others: String,
        def: NpcCombatDef,
    ) {
        setCombatDef(npc, def)
        others.forEach { other -> setCombatDef(other, def) }
    }

    /**
     * Create a [Shop] in our world.
     */
    fun createShop(
        name: String,
        currency: ShopCurrency,
        stockType: StockType = StockType.NORMAL,
        stockSize: Int = Shop.DEFAULT_STOCK_SIZE,
        purchasePolicy: PurchasePolicy = PurchasePolicy.BUY_TRADEABLES,
        init: Shop.() -> Unit,
    ) {
        val shop = Shop(name, stockType, purchasePolicy, currency, arrayOfNulls(stockSize))
        r.shops[name] = shop
        init(shop)
    }

    /**
     * Create a [ContainerKey] to register to the [World] for serialization
     * later on.
     */
    fun registerContainerKey(key: ContainerKey) {
        r.containerKeys.add(key)
    }

    /**
     * Spawn an [Npc] on the given coordinates.
     */
    fun spawnNpc(
        npc: String,
        x: Int,
        z: Int,
        height: Int = 0,
        walkRadius: Int = 0,
        direction: Direction = Direction.SOUTH,
        active: Boolean = true,
    ) = spawnNpc(npc, Tile(x, z, height), walkRadius, direction, active)


    /**
     * Spawn an [Npc] on the given [tile].
     */
    fun spawnNpc(
        npc: String,
        tile: Tile,
        walkRadius: Int = 0,
        direction: Direction = Direction.SOUTH,
        active: Boolean = true,
    ) {
        val n = Npc(getRSCM(npc), tile, world)
        n.respawns = true
        n.walkRadius = walkRadius
        n.lastFacingDirection = direction
        n.setActive(active)
        r.npcSpawns.add(n)
    }

    fun spawnObj(obj: String, tile: Tile, type: Int = 10, rot: Int = 0) =
        spawnObj(obj, tile.x, tile.z, tile.height, type, rot)

    /**
     * Spawn a [DynamicObject] on the given [tile], using a [Direction] to determine rotation.
     */
    fun spawnObject(
        obj: String,
        tile: Tile,
        type: Int = 10,
        direction: Direction = Direction.SOUTH,
    ) =
        spawnObject(obj, tile.x, tile.z, tile.height, type, direction)

    /**
     * Spawn a [DynamicObject] on the given coordinates.
     */
    fun spawnObj(
        obj: String,
        x: Int,
        z: Int,
        height: Int = 0,
        type: Int = 10,
        rot: Int = 0,
    ) {
        val o = DynamicObject(getRSCM(obj), type, rot, Tile(x, z, height))
        r.objSpawns.add(o)
    }

    /**
     * Spawn a [DynamicObject] on the given coordinates using [direction] for rotation.
     */
    fun spawnObject(
        obj: String,
        x: Int,
        z: Int,
        height: Int = 0,
        type: Int = 10,
        direction: Direction = Direction.SOUTH,
    ) {
        val rotation = direction.toObjectRotation()
        spawnObj(obj, x, z, height, type, rotation)
    }

    private fun Direction.toObjectRotation(): Int =
        when (this) {
            Direction.WEST -> 0
            Direction.NORTH -> 1
            Direction.EAST -> 2
            Direction.SOUTH -> 3
            else -> throw IllegalArgumentException("Objects can only face cardinal directions. [direction=$this]")
        }

    /**
     * Spawn a [GroundItem] on the given coordinates.
     */
    fun spawnItem(
        item: String,
        amount: Int,
        x: Int,
        z: Int,
        height: Int = 0,
        respawnCycles: Int = GroundItem.DEFAULT_RESPAWN_CYCLES,
    ) {
        val ground = GroundItem(getRSCM(item), amount, Tile(x, z, height))
        ground.respawnCycles = respawnCycles
        r.itemSpawns.add(ground)
    }

    /**
     * Spawn a [GroundItem] on the given coordinates.
     */
    fun spawnItem(
        item: String,
        amount: Int,
        tile: Tile,
        respawnCycles: Int = GroundItem.DEFAULT_RESPAWN_CYCLES,
    ) {
        val ground = GroundItem(getRSCM(item), amount, tile)
        ground.respawnCycles = respawnCycles
        r.itemSpawns.add(ground)
    }

    /**
     * Spawn a [GroundItem] on the given coordinates for player
     */
    fun spawnItem(
        item: String,
        amount: Int,
        tile: Tile,
        owner: Player,
    ) {
        val ground = GroundItem(getRSCM(item), amount, tile)
        ground.ownerUID = owner.uid
        r.itemSpawns.add(ground)
    }

    /**
     * Invoke [logic] when the [option] option is clicked on an inventory
     * [org.alter.game.model.item.Item].
     *
     * This method should be used over the option-int variant whenever possible.
     */
    fun onItemOption(
        item: String,
        option: String,
        logic: (Plugin).() -> Unit,
    ) {
        val opt = option.lowercase()
        val def = getItem(getRSCM(item))
        val option = def.interfaceOptions.indexOfFirst { it?.lowercase() == opt }
        check(option != -1) {
            "Option \"$option\" not found for item $item [options=${
                def.interfaceOptions.filterNotNull().filter { it.isNotBlank() }
            }]"
        }
        r.bindItem(def.id, option + 1, logic)
    }

    /**
     * Invoke [logic] when the [option] option is clicked on an equipment
     * [org.alter.game.model.item.Item].
     */
    fun onEquipmentOption(
        item: String,
        option: String,
        logic: (Plugin).() -> Unit,
    ) {
        val opt = option.lowercase()
        val rItem = getRSCM(item)
        val def = ObjectExamineHolder.EQUIPMENT_MENU.get(rItem)
        val slot = def.equipmentMenu.indexOfFirst { it?.lowercase() == opt }

        check(slot != -1) {
            "Option \"$option\" not found for item equipment $item [options=${
                def.equipmentMenu.filterNotNull().filter {
                    it.isNotBlank()
                }
            }]"
        }

        r.bindEquipmentOption(rItem, slot + 1, logic)
    }


    /**
     * Invoke [logic] when the [option] option is clicked on a
     * [org.alter.game.model.entity.GameObject].
     *
     * This method should be used over the option-int variant whenever possible.
     */
    fun onObjOption(
        obj: String,
        option: String,
        lineOfSightDistance: Int = -1,
        logic: (Plugin).() -> Unit
    ) {
        onObjOption(getRSCM(obj), option, lineOfSightDistance, logic)
    }

    fun onObjOption(
        obj: Int,
        option: String,
        lineOfSightDistance: Int = -1,
        logic: (Plugin).() -> Unit,
    ) {
        val opt = option.lowercase()
        val def = getObject(obj)
        val slot = def.actions.indexOfFirst { it?.lowercase() == opt }

        check(
            slot != -1,
        ) {
            "Option \"$option\" not found for object $obj [options=${
                def.actions.filterNotNull().filter { it.isNotBlank() }
            }]"
        }

        r.bindObject(obj, slot + 1, lineOfSightDistance, logic)
    }


    fun itemHasGroundOption(
        item: String,
        option: String,
    ): Boolean {
        val slot =
            getItem(getRSCM(item)).interfaceOptions.indexOfFirst {
                it?.lowercase() == option.lowercase()
            }
        return slot != -1
    }

    fun itemHasInventoryOption(
        item: String,
        option: String,
    ): Boolean {
        val slot =
            getItem(getRSCM(item)).interfaceOptions.indexOfFirst {
                it?.lowercase() == option.lowercase()
            }
        return slot != -1
    }

    fun objHasOption(
        obj: String,
        option: String,
    ): Boolean {
        val slot =
            getObject(getRSCM(obj)).actions.indexOfFirst {
                it?.lowercase() == option.lowercase()
            }
        return slot != -1
    }

    /**
     * Checks if a [Npc] has [option]
     */
    fun npcHasOption(
        npc: String,
        option: String,
    ): Boolean { // TODO ADVO what is this for?
        val slot =
            getNpc(getRSCM(npc)).actions.indexOfFirst {
                it?.lowercase() == option.lowercase()
            }
        return slot != -1
    }

    /**
     * Invoke [logic] when the [option] option is clicked on an [Npc].
     *
     * This method should be used over the option-int variant whenever possible.
     *
     * @param lineOfSightDistance
     * If the npc is behind an object such as a prison cell or bank booth, this
     * distance should be set. If the npc can be reached normally, you shouldn't
     * specify this value.
     */
    fun onNpcOption(
        npc: String,
        option: String,
        lineOfSightDistance: Int = -1,
        logic: (Plugin).() -> Unit,
    ) {
        val opt = option.lowercase()
        val rNpc = getRSCM(npc)
        val def = getNpc(rNpc)
        val slot = def.actions.indexOfFirst { it?.lowercase() == opt }

        check(
            slot != -1,
        ) {
            "Option \"$option\" not found for npc $npc [options=${
                def.actions.filterNotNull().filter { it.isNotBlank() }
            }]"
        }

        r.bindNpc(rNpc, slot + 1, lineOfSightDistance, logic)
    }

    /**
     * Invoke [logic] when [option] option is clicked on a [GroundItem].
     *
     * This method should be used over the option-int variant whenever possible.
     */
    fun onGroundItemOption(
        item: String,
        option: String,
        logic: (Plugin).() -> Unit,
    ) {
        val rItem = getRSCM(item)
        val opt = option.lowercase()
        val def = getItem(rItem)
        val slot = def.options.indexOfFirst { it?.lowercase() == opt }

        check(slot != -1) {
            "Option \"$option\" not found for ground item $item [options=${
                def.options.filterNotNull().filter { it.isNotBlank() }
            }]"
        }

        r.bindGroundItem(rItem, slot + 1, logic)
    }

    /**
     * Invoke [logic] when an [item] is used on a [org.alter.game.model.entity.GameObject]
     *
     * @param obj the game object id
     * @param item the item id
     */
    fun onItemOnObj(
        obj: String,
        item: String,
        lineOfSightDistance: Int = -1,
        logic: (Plugin).() -> Unit,
    ) {
        r.bindItemOnObject(getRSCM(obj), getRSCM(item), lineOfSightDistance, logic)
    }

    /**
     * Invoke [plugin] when [item1] is used on [item2] or vise-versa.
     */
    fun onItemOnItem(
        item1: String,
        item2: String,
        plugin: Plugin.() -> Unit,
    ) = r.bindItemOnItem(getRSCM(item1), getRSCM(item2), plugin)

    /**
     * Invoke [plugin] when [item] in inventory is used on [groundItem] on ground.
     */
    fun onItemOnGroundItem(
        item: String,
        groundItem: String,
        plugin: Plugin.() -> Unit,
    ) = r.bindItemOnGroundItem(getRSCM(item), getRSCM(groundItem), plugin)

    /**
     * Set the logic to execute when Message Class -> WindowStateMessage
     * is handled.
     * @TODO
     */
    fun setWindowStatusLogic(logic: (Plugin).() -> Unit) = r.bindWindowStatus(logic)

    /**
     * Set the logic to execute when [net.rsprot.protocol.game.incoming.misc.user.CloseModal]
     * is handled.
     */
    fun setModalCloseLogic(logic: (Plugin).() -> Unit) = r.bindModalClose(logic)

    /**
     * Set the logic to check if a player has a menu opened and any [org.alter.game.model.queue.QueueTask]
     * with a [org.alter.game.model.queue.TaskPriority.STANDARD] priority should wait before executing.
     *
     * @see PluginRepository.isMenuOpenedPlugin
     *
     * @return
     * True if the player has a menu opened and any standard task should wait
     * before executing.
     */
    fun setMenuOpenCheck(logic: Plugin.() -> Boolean) = r.setMenuOpenedCheck(logic)

    /**
     * Set the logic to execute by default when [org.alter.game.model.entity.Pawn.attack]
     * is handled.
     */
    fun setCombatLogic(logic: (Plugin).() -> Unit) = r.bindCombat(logic)

    /**
     * Set the logic to execute when a player levels a skill.
     */
    fun setLevelUpLogic(logic: (Plugin).() -> Unit) = r.bindSkillLevelUp(logic)

    /**
     * Invoke [logic] when [World.postLoad] is handled.
     */
    fun onWorldInit(logic: (Plugin).() -> Unit) = r.bindWorldInit(logic)

    /**
     * Invoke [logic] when an [Event] is triggered.
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : Event> onEvent(
        event: Class<out T>,
        logic: Plugin.(T) -> Unit,
    ) = r.bindEvent(event, logic as Plugin.(Event) -> Unit)

    /**
     * Invoke [logic] on player log in.
     */
    fun onLogin(logic: (Plugin).() -> Unit) = r.bindLogin(logic)

    /**
     * Invoke [logic] on player log out.
     */
    fun onLogout(logic: (Plugin).() -> Unit) = r.bindLogout(logic)

    /**
     * Invoked when an item is swapped on the same component.
     * @TODO Check
     */
    fun onComponentItemSwap(
        interfaceId: Int,
        component: Int,
        plugin: Plugin.() -> Unit,
    ) = r.bindComponentItemSwap(interfaceId, component, plugin)

    /**
     * Invoked when an item is swapped between two components.
     * @TODO Check
     */
    fun onComponentToComponentItemSwap(
        srcInterfaceId: Int,
        srcComponent: Int,
        dstInterfaceId: Int,
        dstComponent: Int,
        plugin: Plugin.() -> Unit,
    ) = r.bindComponentToComponentItemSwap(srcInterfaceId, srcComponent, dstInterfaceId, dstComponent, plugin)

    /**
     * Invokes when a player interaction option is executed
     */
    fun onPlayerOption(
        option: String,
        plugin: Plugin.() -> Unit,
    ) = r.bindPlayerOption(option, plugin)

    /**
     * Invoked when a player hits 0 hp and is starting their death task.
     */
    fun onPlayerPreDeath(plugin: Plugin.() -> Unit) = r.bindPlayerPreDeath(plugin)

    /**
     * Invoked when a player is sent back to their respawn location on
     * death.
     */
    fun onPlayerDeath(plugin: Plugin.() -> Unit) = r.bindPlayerDeath(plugin)

    /**
     * Invoked when npc with [Npc.id] of [npc] invokes their death task.
     */
    fun onNpcPreDeath(
        npc: Int,
        plugin: Plugin.() -> Unit,
    ) = r.bindNpcPreDeath(npc, plugin)

    /**
     * Invoked when npc with [Npc.id] of [npc] finishes their death task and
     * is de-registered from the world.
     */
    fun onNpcDeath(
        npc: String,
        plugin: Plugin.() -> Unit,
    ) = r.bindNpcDeath(getRSCM(npc), plugin)

    /**
     * Invoked when any npc finishes their death task and is de-registered
     * from the world.
     */
    fun onAnyNpcDeath(plugin: Plugin.() -> Unit) = r.bindAnyNpcDeath(plugin)

    /**
     * Completely overrides the npc death mechanic.
     */
    fun fullNpcDeath(
        npc: String,
        plugin: Plugin.() -> Unit,
    ) = r.bindNpcFullDeath(getRSCM(npc), plugin)

    /**
     * Set the combat logic for [npc] and [others], which will override the [setCombatLogic]
     * logic.
     */
    fun onNpcCombat(
        npc: String,
        vararg others: String,
        logic: (Plugin).() -> Unit,
    ) {
        r.bindNpcCombat(getRSCM(npc), logic)
        others.forEach { other -> r.bindNpcCombat(getRSCM(other), logic) }
    }

    /**
     * Invoke [logic] when [net.rsprot.protocol.game.incoming.npcs.OpNpcT] is handled.
     * @TODO CHECK
     */
    fun onSpellOnNpc(
        parent: Int,
        child: Int,
        logic: (Plugin).() -> Unit,
    ) = r.bindSpellOnNpc(parent, child, logic)

    /**
     * Invoke [logic] when [net.rsprot.protocol.game.incoming.npcs.OpNpcT] is handled.
     * @TODO CHECK
     */
    fun onSpellOnPlayer(
        parent: Int,
        child: Int,
        logic: (Plugin).() -> Unit,
    ) = r.bindSpellOnPlayer(parent, child, logic)

    /**
     * Invoke [logic] when [net.rsprot.protocol.game.outgoing.interfaces.IfOpenSub] is handled.
     */
    fun onInterfaceOpen(
        interfaceId: Int,
        logic: (Plugin).() -> Unit,
    ) = r.bindInterfaceOpen(interfaceId, logic)

    /**
     * Invoke [logic] when [org.alter.game.model.interf.InterfaceSet.closeByHash]
     * is handled.
     */
    fun onInterfaceClose(
        interfaceId: Int,
        logic: (Plugin).() -> Unit,
    ) = r.bindInterfaceClose(interfaceId, logic)

    /**
     * Invoke [logic] when [net.rsprot.protocol.game.incoming.buttons.If1Button] is handled.
     */
    fun onButton(
        interfaceId: Int,
        component: Int,
        logic: (Plugin).() -> Unit,
    ) = r.bindButton(interfaceId, component, logic)

    fun onButton(
        interfaceId: Int,
        vararg components: Int,
        logic: (Plugin).() -> Unit,
    ) {
        components.forEach {
            onButton(interfaceId, it, logic)
        }
    }

    /**
     * Invoke [logic] when [key] reaches a time value of 0.
     */
    fun onTimer(
        key: TimerKey,
        logic: (Plugin).() -> Unit,
    ) = r.bindTimer(key, logic)

    /**
     * Invoke [logic] when any npc is spawned into the game with [World.spawn].
     */
    fun onGlobalNpcSpawn(logic: (Plugin).() -> Unit) = r.bindGlobalNpcSpawn(logic)

    /**
     * Invoke [logic] when a ground item is picked up by a [org.alter.game.model.entity.Player].
     */
    fun onGlobalItemPickup(logic: Plugin.() -> Unit) = r.bindGlobalGroundItemPickUp(logic)

    /**
     * Invoke [logic] when an npc with [Npc.id] matching [npc] is spawned into
     * the game with [World.spawn].
     */
    fun onNpcSpawn(
        npc: String,
        logic: (Plugin).() -> Unit,
    ) = r.bindNpcSpawn(getRSCM(npc), logic)

    /**
     * Invoke [logic] when [net.rsprot.protocol.game.incoming.misc.user.ClientCheat] is handled.
     */
    fun onCommand(
        command: String,
        powerRequired: String? = null,
        description: String? = null,
        logic: (Plugin).() -> Unit,
    ) = r.bindCommand(command, powerRequired, description, logic)

    /**
     * Invoke [logic] when an item is equipped onto equipment slot [equipSlot].
     */
    fun onEquipToSlot(
        equipSlot: Int,
        logic: (Plugin).() -> Unit,
    ) = r.bindEquipSlot(equipSlot, logic)

    /**
     * Invoke [logic] when an item is un-equipped from equipment slot [equipSlot].
     */
    fun onUnequipFromSlot(
        equipSlot: Int,
        logic: (Plugin).() -> Unit,
    ) = r.bindUnequipSlot(equipSlot, logic)

    /**
     * Return true if [item] can be equipped, false if it can't.
     */
    fun canEquipItem(
        item: String,
        logic: (Plugin).() -> Boolean,
    ) = r.bindEquipItemRequirement(getRSCM(item), logic)

    /**
     * Invoke [logic] when [item] is equipped.
     */
    fun onItemEquip(
        item: String,
        logic: (Plugin).() -> Unit,
    ) = r.bindEquipItem(getRSCM(item), logic)

    /**
     * Invoke [logic] when attacking with that [item].
     * @TODO
     * Add check if dealhit was done. -> If u have {BERSERKER_RING} and weap without overrides it wont execute or just split weapons and items
     */
    fun setItemCombatLogic(
        item: String,
        logic: (Plugin).() -> Unit,
    ) {
        r.setItemCombatLogic(getRSCM(item), logic)
    }

    /**
     * Invoke [logic] when [item] is removed from equipment.
     */
    fun onItemUnequip(
        item: String,
        logic: (Plugin).() -> Unit,
    ) = r.bindUnequipItem(getRSCM(item), logic)

    /**
     * Invoke [logic] when a player enters a region (8x8 Chunks).
     */
    fun onEnterRegion(
        regionId: Int,
        logic: (Plugin).() -> Unit,
    ) = r.bindRegionEnter(regionId, logic)

    /**
     * Invoke [logic] when a player exits a region (8x8 Chunks).
     */
    fun onExitRegion(
        regionId: Int,
        logic: (Plugin).() -> Unit,
    ) = r.bindRegionExit(regionId, logic)

    /**
     * Invoke [logic] when a player enters a chunk (8x8 Tiles).
     */
    fun onEnterChunk(
        chunkHash: Int,
        logic: (Plugin).() -> Unit,
    ) = r.bindChunkEnter(chunkHash, logic)

    /**
     * Invoke [logic] when a player exits a chunk (8x8 Tiles).
     */
    fun onExitChunk(
        chunkHash: Int,
        logic: (Plugin).() -> Unit,
    ) = r.bindChunkExit(chunkHash, logic)

    /**
     * Invoke [logic] when the the option in index [option] is clicked on an inventory item.
     *
     * String option method should be used over this method whenever possible.
     */
    fun onItemOption(
        item: String,
        option: Int,
        logic: (Plugin).() -> Unit,
    ) = r.bindItem(getRSCM(item), option, logic)

    /**
     * Invoke [logic] when the the option in index [option] is clicked on a
     * [org.alter.game.model.entity.GameObject].
     *
     * String option method should be used over this method whenever possible.
     *
     * @param lineOfSightDistance
     * If the npc is behind an object such as a prison cell or bank booth, this
     * distance should be set. If the npc can be reached normally, you shouldn't
     * specify this value.
     */
    fun onObjOption(
        obj: String,
        option: Int,
        lineOfSightDistance: Int = -1,
        logic: (Plugin).() -> Unit,
    ) = r.bindObject(getRSCM(obj), option, lineOfSightDistance, logic)

    /**
     * Invoke [logic] when the the option in index [option] is clicked on an [Npc].
     *
     * String option method should be used over this method whenever possible.
     */
    fun onNpcOption(
        npc: String,
        option: Int,
        lineOfSightDistance: Int = -1,
        logic: (Plugin).() -> Unit,
    ) = r.bindNpc(getRSCM(npc), option, lineOfSightDistance, logic)

    /**
     * Invoke [logic] when the the option in index [option] is clicked on a [GroundItem].
     * String option method should be used over this method whenever possible.
     */
    fun onGroundItemOption(
        item: String,
        option: Int,
        logic: (Plugin).() -> Unit,
    ) = r.bindGroundItem(getRSCM(item), option, logic)

    /**
     * Set the condition of whether [item] can be picked up as a ground item.
     * @return false if the item can not be picked up.
     */
    fun setGroundItemCondition(
        item: String,
        plugin: Plugin.() -> Boolean,
    ) = r.setGroundItemPickupCondition(getRSCM(item), plugin)

    /**
     * Invoke [plugin] when a spell is used on an item.
     * @TODO
     */
    fun onSpellOnItem(
        fromInterface: Int,
        fromComponent: Int,
        toInterface: Int,
        toComponent: Int,
        plugin: Plugin.() -> Unit,
    ) = r.bindSpellOnItem((fromInterface shl 16) or fromComponent, (toInterface shl 16) or toComponent, plugin)

    /**
     * Returns true if the item can be dropped on the floor via the 'drop' menu
     * option - return false otherwise.
     */
    fun canDropItem(
        item: String,
        plugin: (Plugin).() -> Boolean,
    ) = r.bindCanItemDrop(getRSCM(item), plugin)

    /**
     * Invoke [plugin] when [item] is used on [npc].
     */
    fun onItemOnNpc(
        item: String,
        npc: String,
        plugin: Plugin.() -> Unit,
    ) = r.bindItemOnNpc(npc = getRSCM(npc), item = getRSCM(item), plugin = plugin)

    fun onAnimation(
        animid: Int,
        plugin: Plugin.() -> Unit,
    ) = r.bindOnAnimation(animid, plugin)

    fun getNpcCombatDef(npc: String): NpcCombatDef? = world.plugins.npcCombatDefs.getOrDefault(getRSCM(npc), null)

    fun getNpcFromTile(tile: Tile): Npc? {
        val chunk = world.chunks.get(tile)
        return chunk?.getEntities<Npc>(tile, EntityType.NPC)?.firstOrNull()
    }

    /**
     * Returns how many times @param from to @param range contains.
     * ex: @param value = 5
     * ex: @param range = 0..90
     * @return returns 18
     *
     * @TODO Should be mvoed out
     */
    fun every(
        value: Int,
        range: Int,
        from: Int = 0,
    ): Int {
        var times = 0
        for (index in from until range) {
            if (index % value == 0) {
                times++
            }
        }
        return times
    }

    fun objHasOption1(
        obj: String,
        option: String,
    ): Boolean {
        val objDefs = getObject(getRSCM(obj))
        return objDefs.actions.contains(option)
    }

    fun npcHasOption1(
        npc: String,
        option: String,
    ): Boolean {
        val npcDefs = getNpc(getRSCM(npc))
        return npcDefs.actions.contains(option)
    }

//    fun on_item_on_any_npc(Item: Int) {
//        /** Run this block -> if Npc does not have specific handling for it refering to ['on_item_on_npc']
//         **/
//    }
//
//    fun on_item_on_any_obj(Item: Int) {
//        /**
//         * Run this block -> if Object does not have specific handling for it refering to ['on_item_on_obj']
//         */
//    }
}
