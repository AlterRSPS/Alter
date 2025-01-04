import dev.openrune.cache.CacheManager.getItem
import dev.openrune.cache.CacheManager.itemSize
import org.alter.game.model.priv.Privilege
import org.alter.plugins.content.commands.Commands_plugin.Command.tryWithUsage
import java.text.DecimalFormat

onCommand("item", Privilege.ADMIN_POWER, description = "Spawn items") {
    val args = player.getCommandArgs()
    tryWithUsage(
        player,
        args,
        "Invalid format! Example of proper command <col=801700>::item 4151 1</col> or <col=801700>::item 4151</col>",
    ) { values ->
        try {
            val item = values[0].toInt()
            val amount = if (values.size > 1) Math.min(Int.MAX_VALUE.toLong(), values[1].parseAmount()).toInt() else 1
            if (item < itemSize()) {
                val def = getItem(Item(item).toUnnoted().id)
                val result = player.inventory.add(item = item, amount = amount, assureFullInsertion = false)
                player.message(
                    "You have spawned <col=801700>${DecimalFormat().format(result.completed)} x ${def.name}</col></col> ($item).",
                )
            } else {
                player.message("Item $item does not exist in cache.")
            }
        } catch (e: Exception) {
            player.queue(TaskPriority.STRONG) {
                val item = spawn() ?: return@queue
                if (item.amount > 0) {
                    player.message("You have spawned ${item.amount} x ${item.getName()}.")
                } else {
                    player.message("You don't have enough inventory space.")
                }
            }
        }
    }
}

suspend fun QueueTask.spawn(): Item? {
    val item = searchItemInput("Select an item to spawn:")
    if (item == -1) {
        return null
    }
    val amount =
        when (options("1", "5", "X", "Max", title = "How many would you like to spawn?")) {
            1 -> 1
            2 -> 5
            3 -> inputInt("Enter amount to spawn")
            4 -> Int.MAX_VALUE
            else -> return null
        }
    val add = player.inventory.add(item, amount, assureFullInsertion = false)
    return Item(item, add.completed)
}
