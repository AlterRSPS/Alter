package org.alter.plugins.content.interfaces.gameframe.tabs.worn_equipment.kod

import org.alter.api.*
import org.alter.api.EquipmentType.Companion.EQUIPMENT_INTERFACE_ID
import org.alter.api.cfg.*
import org.alter.api.dsl.*
import org.alter.api.ext.*
import org.alter.game.*
import org.alter.game.model.*
import org.alter.game.model.attr.*
import org.alter.game.model.container.*
import org.alter.game.model.container.key.*
import org.alter.game.model.entity.*
import org.alter.game.model.item.*
import org.alter.game.model.queue.*
import org.alter.game.model.shop.*
import org.alter.game.model.timer.*
import org.alter.game.plugin.*
import org.alter.plugins.content.interfaces.kod.KeptOnDeath
import org.alter.plugins.content.interfaces.kod.KeptOnDeath.KOD_COMPONENT_ID
import org.alter.plugins.content.interfaces.kod.KeptOnDeath.KOD_INTERFACE_ID

class KeptOnDeathPlugin(
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {

    init {
        onButton(interfaceId = EQUIPMENT_INTERFACE_ID, component = KOD_COMPONENT_ID) {
            if (!player.lock.canInterfaceInteract()) {
                return@onButton
            }
            KeptOnDeath.open(player, world)
        }

        onInterfaceClose(interfaceId = KOD_INTERFACE_ID) {
            /**
             * Have to resend inventory when this interface is closed as it sent a 'fake'
             * inventory container to the tab area, which can mess up other tab area
             * interfaces such as equipment stats.
             */
            player.inventory.dirty = true
        }
    }
}
