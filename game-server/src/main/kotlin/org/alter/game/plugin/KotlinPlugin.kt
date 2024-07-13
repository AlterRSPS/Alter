package org.alter.game.plugin

import dev.openrune.cache.CacheManager.getItem
import dev.openrune.cache.CacheManager.getNpc
import dev.openrune.cache.CacheManager.getObject
import org.alter.game.Server
import org.alter.game.event.Event
import org.alter.game.fs.ObjectExamineHolder
import org.alter.game.model.*
import org.alter.game.model.combat.NpcCombatDef
import org.alter.game.model.container.key.ContainerKey
import org.alter.game.model.entity.*
import org.alter.game.model.shop.PurchasePolicy
import org.alter.game.model.shop.Shop
import org.alter.game.model.shop.ShopCurrency
import org.alter.game.model.shop.StockType
import org.alter.game.model.timer.TimerKey
import org.alter.game.service.Service
import kotlin.script.experimental.annotations.KotlinScript

/**
 * Represents a KotlinScript plugin.
 *
 * @author Tom <rspsmods@gmail.com>
 */
@KotlinScript(
    displayName = "Kotlin Plugin",
    fileExtension = "plugin.kts",
    compilationConfiguration = KotlinPluginConfiguration::class,
)
abstract class KotlinPlugin(private val r: PluginRepository, val world: World, val server: Server) {
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
    fun load_service(service: Service) {
        r.services.add(service)
    }

    /**
     * Set the [org.alter.game.model.region.ChunkCoords] with [chunk] as its
     * [org.alter.game.model.region.ChunkCoords.hashCode], as a multi-combat area.
     */
    fun set_multi_combat_chunk(chunk: Int) {
        r.multiCombatChunks.add(chunk)
    }

    /**
     * Set the 8x8 [org.alter.game.model.region.ChunkCoords]s that belong to [region]
     * as multi-combat areas.
     */
    fun set_multi_combat_region(region: Int) {
        r.multiCombatRegions.add(region)
    }

    /**
     * Set the [NpcCombatDef] for npcs with [Npc.id] of [npc].
     */
    fun set_combat_def(
        npc: Int,
        def: NpcCombatDef,
    ) {
        check(!r.npcCombatDefs.containsKey(npc)) { "Npc combat definition has been previously set: $npc" }
        r.npcCombatDefs[npc] = def
    }

    /**
     * Set the [NpcCombatDef] for npcs with [Npc.id] of [npc] and [others].
     */
    fun set_combat_def(
        npc: Int,
        vararg others: Int,
        def: NpcCombatDef,
    ) {
        set_combat_def(npc, def)
        others.forEach { other -> set_combat_def(other, def) }
    }

    /**
     * Create a [Shop] in our world.
     */
    fun create_shop(
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
    fun register_container_key(key: ContainerKey) {
        r.containerKeys.add(key)
    }

    /**
     * Spawn an [Npc] on the given coordinates.
     */
    fun spawn_npc(
        npc: Int,
        x: Int,
        z: Int,
        height: Int = 0,
        walkRadius: Int = 0,
        direction: Direction = Direction.SOUTH,
    ) {
        val n = Npc(npc, Tile(x, z, height), world)
        n.respawns = true
        n.walkRadius = walkRadius
        n.lastFacingDirection = direction
        r.npcSpawns.add(n)
    }

    /**
     * Spawn an [Npc] on the given [tile].
     */
    fun spawn_npc(
        npc: Int,
        tile: Tile,
        walkRadius: Int = 0,
        direction: Direction = Direction.SOUTH,
    ) {
        val n = Npc(npc, tile, world)
        n.respawns = true
        n.walkRadius = walkRadius
        n.lastFacingDirection = direction
        r.npcSpawns.add(n)
    }

    /**
     * Spawn a [DynamicObject] on the given coordinates.
     */
    fun spawn_obj(
        obj: Int,
        x: Int,
        z: Int,
        height: Int = 0,
        type: Int = 10,
        rot: Int = 0,
    ) {
        val o = DynamicObject(obj, type, rot, Tile(x, z, height))
        r.objSpawns.add(o)
    }

    /**
     * Spawn a [GroundItem] on the given coordinates.
     */
    fun spawn_item(
        item: Int,
        amount: Int,
        x: Int,
        z: Int,
        height: Int = 0,
        respawnCycles: Int = GroundItem.DEFAULT_RESPAWN_CYCLES,
    ) {
        val ground = GroundItem(item, amount, Tile(x, z, height))
        ground.respawnCycles = respawnCycles
        r.itemSpawns.add(ground)
    }

    /**
     * Spawn a [GroundItem] on the given coordinates.
     */
    fun spawn_item(
        item: Int,
        amount: Int,
        tile: Tile,
        respawnCycles: Int = GroundItem.DEFAULT_RESPAWN_CYCLES,
    ) {
        val ground = GroundItem(item, amount, Tile(tile))
        ground.respawnCycles = respawnCycles
        r.itemSpawns.add(ground)
    }

    /**
     * Spawn a [GroundItem] on the given coordinates for player
     */
    fun spawn_item(
        item: Int,
        amount: Int,
        tile: Tile,
        owner: Player,
    ) {
        val ground = GroundItem(item, amount, Tile(tile))
        ground.ownerUID = owner.uid
        r.itemSpawns.add(ground)
    }

    /**
     * Invoke [logic] when the [option] option is clicked on an inventory
     * [org.alter.game.model.item.Item].
     *
     * This method should be used over the option-int variant whenever possible.
     */
    fun on_item_option(
        item: Int,
        option: String,
        logic: (Plugin).() -> Unit,
    ) {
        val opt = option.lowercase()
        val def = getItem(item)
        val option = def.interfaceOptions.indexOfFirst { it?.lowercase() == opt }
        check(option != -1) {
            "Option \"$option\" not found for item $item [options=${def.interfaceOptions.filterNotNull().filter { it.isNotBlank() }}]"
        }
        r.bindItem(item, option + 1, logic)
    }

    /**
     * Invoke [logic] when the [option] option is clicked on an equipment
     * [org.alter.game.model.item.Item].
     */
    fun on_equipment_option(
        item: Int,
        option: String,
        logic: (Plugin).() -> Unit,
    ) {
        val opt = option.lowercase()
        val def = ObjectExamineHolder.EQUIPMENT_MENU.get(item)
        val slot = def.equipmentMenu.indexOfFirst { it?.lowercase() == opt }

        check(slot != -1) {
            "Option \"$option\" not found for item equipment $item [options=${def.equipmentMenu.filterNotNull().filter { it.isNotBlank() }}]"
        }

        r.bindEquipmentOption(item, slot + 1, logic)
    }

    /**
     * Invoke [logic] when the [option] option is clicked on a
     * [org.alter.game.model.entity.GameObject].
     *
     * This method should be used over the option-int variant whenever possible.
     */
    fun on_obj_option(
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
        ) { "Option \"$option\" not found for object $obj [options=${def.actions.filterNotNull().filter { it.isNotBlank() }}]" }

        r.bindObject(obj, slot + 1, lineOfSightDistance, logic)
    }

    fun itemHasGroundOption(
        item: Int,
        option: String,
    ): Boolean {
        val slot =
            getItem(item).interfaceOptions.indexOfFirst {
                it?.lowercase() == option.lowercase()
            }
        return slot != -1
    }

    fun itemHasInventoryOption(
        item: Int,
        option: String,
    ): Boolean {
        val slot =
            getItem(item).interfaceOptions.indexOfFirst {
                it?.lowercase() == option.lowercase()
            }
        return slot != -1
    }

    fun objHasOption(
        obj: Int,
        option: String,
    ): Boolean {
        val slot =
            getObject(obj).actions.indexOfFirst {
                it?.lowercase() == option.lowercase()
            }
        return slot != -1
    }

    /**
     * Checks if a [NPC] has [option]
     */
    fun npcHasOption(
        npc: Int,
        option: String,
    ): Boolean { // TODO ADVO what is this for?
        val slot =
            getNpc(npc).actions.indexOfFirst {
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
    fun on_npc_option(
        npc: Int,
        option: String,
        lineOfSightDistance: Int = -1,
        logic: (Plugin).() -> Unit,
    ) {
        val opt = option.lowercase()
        val def = getNpc(npc)
        val slot = def.actions.indexOfFirst { it?.lowercase() == opt }

        check(
            slot != -1,
        ) { "Option \"$option\" not found for npc $npc [options=${def.actions.filterNotNull().filter { it.isNotBlank() }}]" }

        r.bindNpc(npc, slot + 1, lineOfSightDistance, logic)
    }

    /**
     * Invoke [logic] when [option] option is clicked on a [GroundItem].
     *
     * This method should be used over the option-int variant whenever possible.
     */
    fun on_ground_item_option(
        item: Int,
        option: String,
        logic: (Plugin).() -> Unit,
    ) {
        val opt = option.lowercase()
        val def = getItem(item)
        val slot = def.options.indexOfFirst { it?.lowercase() == opt }

        check(slot != -1) {
            "Option \"$option\" not found for ground item $item [options=${def.options.filterNotNull().filter { it.isNotBlank() }}]"
        }

        r.bindGroundItem(item, slot + 1, logic)
    }

    /**
     * Invoke [logic] when an [item] is used on a [org.alter.game.model.entity.GameObject]
     *
     * @param obj the game object id
     * @param item the item id
     */
    fun on_item_on_obj(
        obj: Int,
        item: Int,
        lineOfSightDistance: Int = -1,
        logic: (Plugin).() -> Unit,
    ) {
        r.bindItemOnObject(obj, item, lineOfSightDistance, logic)
    }

    /**
     * Invoke [plugin] when [item1] is used on [item2] or vise-versa.
     */
    fun on_item_on_item(
        item1: Int,
        item2: Int,
        plugin: Plugin.() -> Unit,
    ) = r.bindItemOnItem(item1, item2, plugin)

    /**
     * Invoke [plugin] when [item] in inventory is used on [groundItem] on ground.
     */
    fun on_item_on_ground_item(
        item: Int,
        groundItem: Int,
        plugin: Plugin.() -> Unit,
    ) = r.bindItemOnGroundItem(item, groundItem, plugin)

    /**
     * Set the logic to execute when [org.alter.game.message.impl.WindowStatusMessage]
     * is handled.
     */
    fun set_window_status_logic(logic: (Plugin).() -> Unit) = r.bindWindowStatus(logic)

    /**
     * Set the logic to execute when [org.alter.game.message.impl.CloseModalMessage]
     * is handled.
     */
    fun set_modal_close_logic(logic: (Plugin).() -> Unit) = r.bindModalClose(logic)

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
    fun set_menu_open_check(logic: Plugin.() -> Boolean) = r.setMenuOpenedCheck(logic)

    /**
     * Set the logic to execute by default when [org.alter.game.model.entity.Pawn.attack]
     * is handled.
     */
    fun set_combat_logic(logic: (Plugin).() -> Unit) = r.bindCombat(logic)

    /**
     * Set the logic to execute when a player levels a skill.
     */
    fun set_level_up_logic(logic: (Plugin).() -> Unit) = r.bindSkillLevelUp(logic)

    /**
     * Invoke [logic] when [World.postLoad] is handled.
     */
    fun on_world_init(logic: (Plugin).() -> Unit) = r.bindWorldInit(logic)

    /**
     * Invoke [logic] when an [Event] is triggered.
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : Event> on_event(
        event: Class<out T>,
        logic: Plugin.(T) -> Unit,
    ) = r.bindEvent(event, logic as Plugin.(Event) -> Unit)

    /**
     * Invoke [logic] on player log in.
     */
    fun on_login(logic: (Plugin).() -> Unit) = r.bindLogin(logic)

    /**
     * Invoke [logic] on player log out.
     */
    fun on_logout(logic: (Plugin).() -> Unit) = r.bindLogout(logic)

    /**
     * Invoked when an item is swapped on the same component.
     */
    fun on_component_item_swap(
        interfaceId: Int,
        component: Int,
        plugin: Plugin.() -> Unit,
    ) = r.bindComponentItemSwap(interfaceId, component, plugin)

    /**
     * Invoked when an item is swapped between two components.
     */
    fun on_component_to_component_item_swap(
        srcInterfaceId: Int,
        srcComponent: Int,
        dstInterfaceId: Int,
        dstComponent: Int,
        plugin: Plugin.() -> Unit,
    ) = r.bindComponentToComponentItemSwap(srcInterfaceId, srcComponent, dstInterfaceId, dstComponent, plugin)

    /**
     * Invokes when a player interaction option is executed
     */
    fun on_player_option(
        option: String,
        plugin: Plugin.() -> Unit,
    ) = r.bindPlayerOption(option, plugin)

    /**
     * Invoked when a player hits 0 hp and is starting their death task.
     */
    fun on_player_pre_death(plugin: Plugin.() -> Unit) = r.bindPlayerPreDeath(plugin)

    /**
     * Invoked when a player is sent back to their respawn location on
     * death.
     */
    fun on_player_death(plugin: Plugin.() -> Unit) = r.bindPlayerDeath(plugin)

    /**
     * Invoked when npc with [Npc.id] of [npc] invokes their death task.
     */
    fun on_npc_pre_death(
        npc: Int,
        plugin: Plugin.() -> Unit,
    ) = r.bindNpcPreDeath(npc, plugin)

    /**
     * Invoked when npc with [Npc.id] of [npc] finishes their death task and
     * is de-registered from the world.
     */
    fun on_npc_death(
        npc: Int,
        plugin: Plugin.() -> Unit,
    ) = r.bindNpcDeath(npc, plugin)

    /**
     * Completely overrides the npc death mechanic.
     */
    fun full_npc_death(
        npc: Int,
        plugin: Plugin.() -> Unit,
    ) = r.bindNpcFullDeath(npc, plugin)

    /**
     * Set the combat logic for [npc] and [others], which will override the [set_combat_logic]
     * logic.
     */
    fun on_npc_combat(
        npc: Int,
        vararg others: Int,
        logic: (Plugin).() -> Unit,
    ) {
        r.bindNpcCombat(npc, logic)
        others.forEach { other -> r.bindNpcCombat(other, logic) }
    }

    /**
     * Invoke [logic] when [org.alter.game.message.impl.OpNpcTMessage] is handled.
     */
    fun on_spell_on_npc(
        parent: Int,
        child: Int,
        logic: (Plugin).() -> Unit,
    ) = r.bindSpellOnNpc(parent, child, logic)

    /**
     * Invoke [logic] when [org.alter.game.message.impl.OpNpcTMessage] is handled.
     */
    fun on_spell_on_player(
        parent: Int,
        child: Int,
        logic: (Plugin).() -> Unit,
    ) = r.bindSpellOnPlayer(parent, child, logic)

    /**
     * Invoke [logic] when [org.alter.game.message.impl.IfOpenSubMessage] is handled.
     */
    fun on_interface_open(
        interfaceId: Int,
        logic: (Plugin).() -> Unit,
    ) = r.bindInterfaceOpen(interfaceId, logic)

    /**
     * Invoke [logic] when [org.alter.game.model.interf.InterfaceSet.closeByHash]
     * is handled.
     */
    fun on_interface_close(
        interfaceId: Int,
        logic: (Plugin).() -> Unit,
    ) = r.bindInterfaceClose(interfaceId, logic)

    /**
     * Invoke [logic] when [org.alter.game.message.impl.IfButtonMessage] is handled.
     */
    fun on_button(
        interfaceId: Int,
        component: Int,
        logic: (Plugin).() -> Unit,
    ) = r.bindButton(interfaceId, component, logic)

    fun on_button(
        interfaceId: Int,
        vararg components: Int,
        logic: (Plugin).() -> Unit,
    ) {
        components.forEach {
            on_button(interfaceId, it, logic)
        }
    }

    /**
     * Invoke [logic] when [key] reaches a time value of 0.
     */
    fun on_timer(
        key: TimerKey,
        logic: (Plugin).() -> Unit,
    ) = r.bindTimer(key, logic)

    /**
     * Invoke [logic] when any npc is spawned into the game with [World.spawn].
     */
    fun on_global_npc_spawn(logic: (Plugin).() -> Unit) = r.bindGlobalNpcSpawn(logic)

    /**
     * Invoke [logic] when a ground item is picked up by a [org.alter.game.model.entity.Player].
     */
    fun on_global_item_pickup(logic: Plugin.() -> Unit) = r.bindGlobalGroundItemPickUp(logic)

    /**
     * Invoke [logic] when an npc with [Npc.id] matching [npc] is spawned into
     * the game with [World.spawn].
     */
    fun on_npc_spawn(
        npc: Int,
        logic: (Plugin).() -> Unit,
    ) = r.bindNpcSpawn(npc, logic)

    /**
     * Invoke [logic] when [org.alter.game.message.impl.ClientCheatMessage] is handled.
     */
    fun on_command(
        command: String,
        powerRequired: String? = null,
        description: String? = null,
        logic: (Plugin).() -> Unit,
    ) = r.bindCommand(command, powerRequired, description, logic)

    /**
     * Invoke [logic] when an item is equipped onto equipment slot [equipSlot].
     */
    fun on_equip_to_slot(
        equipSlot: Int,
        logic: (Plugin).() -> Unit,
    ) = r.bindEquipSlot(equipSlot, logic)

    /**
     * Invoke [logic] when an item is un-equipped from equipment slot [equipSlot].
     */
    fun on_unequip_from_slot(
        equipSlot: Int,
        logic: (Plugin).() -> Unit,
    ) = r.bindUnequipSlot(equipSlot, logic)

    /**
     * Return true if [item] can be equipped, false if it can't.
     */
    fun can_equip_item(
        item: Int,
        logic: (Plugin).() -> Boolean,
    ) = r.bindEquipItemRequirement(item, logic)

    /**
     * Invoke [logic] when [item] is equipped.
     */
    fun on_item_equip(
        item: Int,
        logic: (Plugin).() -> Unit,
    ) = r.bindEquipItem(item, logic)

    /**
     * Invoke [logic] when attacking with that [item].
     * @TODO
     * Add check if dealhit was done. -> If u have {BERSERKER_RING} and weap without overrides it wont execute or just split weapons and items
     */
    fun set_item_combat_logic(
        item: Int,
        logic: (Plugin).() -> Unit,
    ) {
        r.setItemCombatLogic(item, logic)
    }

    /**
     * Invoke [logic] when [item] is removed from equipment.
     */
    fun on_item_unequip(
        item: Int,
        logic: (Plugin).() -> Unit,
    ) = r.bindUnequipItem(item, logic)

    /**
     * Invoke [logic] when a player enters a region (8x8 Chunks).
     */
    fun on_enter_region(
        regionId: Int,
        logic: (Plugin).() -> Unit,
    ) = r.bindRegionEnter(regionId, logic)

    /**
     * Invoke [logic] when a player exits a region (8x8 Chunks).
     */
    fun on_exit_region(
        regionId: Int,
        logic: (Plugin).() -> Unit,
    ) = r.bindRegionExit(regionId, logic)

    /**
     * Invoke [logic] when a player enters a chunk (8x8 Tiles).
     */
    fun on_enter_chunk(
        chunkHash: Int,
        logic: (Plugin).() -> Unit,
    ) = r.bindChunkEnter(chunkHash, logic)

    /**
     * Invoke [logic] when a player exits a chunk (8x8 Tiles).
     */
    fun on_exit_chunk(
        chunkHash: Int,
        logic: (Plugin).() -> Unit,
    ) = r.bindChunkExit(chunkHash, logic)

    /**
     * Invoke [logic] when the the option in index [option] is clicked on an inventory item.
     *
     * String option method should be used over this method whenever possible.
     */
    fun on_item_option(
        item: Int,
        option: Int,
        logic: (Plugin).() -> Unit,
    ) = r.bindItem(item, option, logic)

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
    fun on_obj_option(
        obj: Int,
        option: Int,
        lineOfSightDistance: Int = -1,
        logic: (Plugin).() -> Unit,
    ) = r.bindObject(obj, option, lineOfSightDistance, logic)

    /**
     * Invoke [logic] when the the option in index [option] is clicked on an [Npc].
     *
     * String option method should be used over this method whenever possible.
     */
    fun on_npc_option(
        npc: Int,
        option: Int,
        lineOfSightDistance: Int = -1,
        logic: (Plugin).() -> Unit,
    ) = r.bindNpc(npc, option, lineOfSightDistance, logic)

    /**
     * Invoke [logic] when the the option in index [option] is clicked on a [GroundItem].
     * String option method should be used over this method whenever possible.
     */
    fun on_ground_item_option(
        item: Int,
        option: Int,
        logic: (Plugin).() -> Unit,
    ) = r.bindGroundItem(item, option, logic)

    /**
     * Set the condition of whether [item] can be picked up as a ground item.
     * @return false if the item can not be picked up.
     */
    fun setGroundItemCondition(
        item: Int,
        plugin: Plugin.() -> Boolean,
    ) = r.setGroundItemPickupCondition(item, plugin)

    /**
     * Invoke [plugin] when a spell is used on an item.
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
        item: Int,
        plugin: (Plugin).() -> Boolean,
    ) = r.bindCanItemDrop(item, plugin)

    /**
     * Invoke [plugin] when [item] is used on [npc].
     */
    fun onItemOnNpc(
        item: Int,
        npc: Int,
        plugin: Plugin.() -> Unit,
    ) = r.bindItemOnNpc(npc = npc, item = item, plugin = plugin)

    fun onAnimation(
        animid: Int,
        plugin: Plugin.() -> Unit,
    ) = r.bindOnAnimation(animid, plugin)

    fun getNpcCombatDef(npc: Int): NpcCombatDef? {
        return world.plugins.npcCombatDefs.getOrDefault(npc, null)
    }

    fun getNpcFromTile(tile: Tile): Npc? {
        val chunk = world.chunks.get(tile)
        return chunk?.getEntities<Npc>(tile, EntityType.NPC)?.firstOrNull()
    }

    /**
     * Returns how many times @param from to @param range contains.
     * ex: @param value = 5
     * ex: @param range = 0..90
     * @return returns 18
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

    fun getPluginRepository(): PluginRepository {
        return r
    }

    fun obj_has_option(
        obj: Int,
        option: String,
    ): Boolean {
        val objDefs = getObject(obj)
        return objDefs.actions.contains(option)
    }

    fun npc_has_option(
        npc: Int,
        option: String,
    ): Boolean {
        val npcDefs = getNpc(npc)
        return npcDefs.actions.contains(option)
    }

    fun on_item_on_any_npc(Item: Int) {
        /** Run this block -> if Npc does not have specific handling for it refering to ['on_item_on_npc']
         **/
    }

    fun on_item_on_any_obj(Item: Int) {
        /**
         * Run this block -> if Object does not have specific handling for it refering to ['on_item_on_obj']
         */
    }

}
