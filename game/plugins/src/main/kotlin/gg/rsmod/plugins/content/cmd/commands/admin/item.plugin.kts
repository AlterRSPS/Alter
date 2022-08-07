import gg.rsmod.game.model.priv.Privilege
import gg.rsmod.plugins.content.cmd.Commands_plugin.Command.tryWithUsage
import java.text.DecimalFormat

on_command("item", Privilege.ADMIN_POWER, description = "Spawn items") {
    val args = player.getCommandArgs()
    tryWithUsage(player, args, "Invalid format! Example of proper command <col=801700>::item 4151 1</col> or <col=801700>::item 4151</col>") { values ->
        val item = values[0].toInt()
        val amount = if (values.size > 1) Math.min(Int.MAX_VALUE.toLong(), values[1].parseAmount()).toInt() else 1
        if (item < world.definitions.getCount(ItemDef::class.java)) {
            val def = world.definitions.get(ItemDef::class.java, Item(item).toUnnoted(world.definitions).id)
            val result = player.inventory.add(item = item, amount = amount, assureFullInsertion = false)
            player.message("You have spawned <col=801700>${DecimalFormat().format(result.completed)} x ${def.name}</col></col> ($item).")
        } else {
            player.message("Item $item does not exist in cache.")
        }
    }
}