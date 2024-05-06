package org.alter.plugins.content.skills.cooking

import net.rsprot.protocol.game.incoming.resumed.ResumePauseButton
import org.alter.api.ext.*
import org.alter.game.fs.def.ItemDef
import org.alter.game.model.entity.Player
import org.alter.game.model.queue.QueueTask
import org.alter.plugins.content.skills.cooking.data.CookingObj

private val closeCookingDialog: QueueTask.() -> Unit = {
    player.closeComponent(parent = 162, child = 561)
}

suspend fun QueueTask.cookingMessageBox(vararg items: Int, title: String = "What would you like to cook?", maxItems: Int = player.inventory.capacity, obj: CookingObj?, logic: Player.(Int, Int, CookingObj?) -> Unit) {
    val defs = player.world.definitions
    val itemDefs = items.map { defs.get(ItemDef::class.java, it) }

    val itemArray = Array(10) { -1 }
    val nameArray = Array(10) { "|" }

    itemDefs.withIndex().forEach{
        val def = it.value
        itemArray[it.index] = def.id
        nameArray[it.index] = "|${def.name}"
    }

    player.sendTempVarbit(5983, 1)
    player.runClientScript(2379)
    player.openInterface(parent = 162, child = 559, interfaceId = 270, isModal = true)
    player.setInterfaceEvents(interfaceId = 270, component = 14, range = (1..10), setting = 1)
    player.setInterfaceEvents(interfaceId = 270, component = 15, range = (1..10), setting = 1)
    player.runClientScript(2046, 6, "$title${nameArray.joinToString("")}", maxItems, *itemArray, maxItems)

    terminateAction = closeCookingDialog
    waitReturnValue()
    terminateAction!!(this)

    val result = requestReturnValue as? ResumePauseButton ?: return
    val child = result.componentId

    if(child < 14 || child >= 14 + items.size) {
        return
    }

    val item = items[child - 14]
    val qty = result.sub

    logic(player, item, qty, obj)
}