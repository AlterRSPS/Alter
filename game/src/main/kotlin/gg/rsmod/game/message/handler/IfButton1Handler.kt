package gg.rsmod.game.message.handler

import gg.rsmod.game.action.EquipAction
import gg.rsmod.game.message.MessageHandler
import gg.rsmod.game.message.impl.IfButtonMessage
import gg.rsmod.game.model.ExamineEntityType
import gg.rsmod.game.model.World
import gg.rsmod.game.model.attr.*
import gg.rsmod.game.model.entity.Client
import gg.rsmod.game.service.game.ItemMetadataService
import java.lang.ref.WeakReference

/**
 * @author Tom <rspsmods@gmail.com>
 */
class IfButton1Handler : MessageHandler<IfButtonMessage> {

    override fun handle(client: Client, world: World, message: IfButtonMessage) {
        val interfaceId = message.hash shr 16
        val component = message.hash and 0xFFFF
        val option = message.option + 1

        if (interfaceId == 149) {
            if (message.slot < 0 || message.slot >= client.inventory.capacity) {
                return
            }

            if (!client.lock.canItemInteract()) {
                return
            }

            val item = client.inventory[message.slot] ?: return

            if (item.id != message.item) {
                return
            }

            log(client, "Item action 2: id=%d, slot=%d, component=(%d, %d), inventory=(%d, %d)",
                message.item, message.slot, interfaceId, component, item.id, item.amount)

            client.attr[INTERACTING_ITEM] = WeakReference(item)
            client.attr[INTERACTING_ITEM_ID] = item.id
            client.attr[INTERACTING_ITEM_SLOT] = message.slot
            // IF_BUTTONT = Item on item

            when(option) {
                2 -> {
                    client.writeMessage("Drop / Dismantle")
                }
                3 -> {
                    val result = EquipAction.equip(client, item, message.slot)
                    if (result == EquipAction.Result.UNHANDLED && world.devContext.debugItemActions) {
                        val itemMetaDataService = client.world.getService(ItemMetadataService::class.java)
                        client.writeMessage("Unhandled item action: [item=${item.id}, slot=${message.slot}, option=2]")
                    }
                }
                10 -> {
                    world.sendExamine(client, message.item, ExamineEntityType.ITEM)
                }
            }
        } else {
            if (!client.interfaces.isVisible(interfaceId)) {
                return
            }
            log(client, "Click button: component=[%d:%d], option=%d, slot=%d, item=%d", interfaceId, component, option, message.slot, message.item)
            client.attr[INTERACTING_OPT_ATTR] = option
            client.attr[INTERACTING_ITEM_ID] = message.item
            client.attr[INTERACTING_SLOT_ATTR] = message.slot
            if (world.plugins.executeButton(client, interfaceId, component)) {
                return
            }

            if (world.devContext.debugButtons) {
                client.writeMessage("Unhandled button action: [component=[$interfaceId:$component], option=$option, slot=${message.slot}, item=${message.item}]")
            }
        }
    }
}