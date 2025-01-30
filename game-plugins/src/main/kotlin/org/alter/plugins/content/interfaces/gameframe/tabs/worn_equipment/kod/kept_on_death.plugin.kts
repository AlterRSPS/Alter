package org.alter.plugins.content.interfaces.kod

import org.alter.api.EquipmentType.Companion.EQUIPMENT_INTERFACE_ID
import org.alter.plugins.content.interfaces.kod.KeptOnDeath.KOD_COMPONENT_ID
import org.alter.plugins.content.interfaces.kod.KeptOnDeath.KOD_INTERFACE_ID

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
