import org.alter.game.model.priv.Privilege
import org.alter.plugins.content.commands.Commands_plugin.Command.tryWithUsage

val OpenShopCommands = listOf("openshop", "shop", "store")

OpenShopCommands.forEach {
    on_command(it, Privilege.ADMIN_POWER, description = "Open shop , use: ::listshop for shop id's") {
        val args = player.getCommandArgs()
        tryWithUsage(player, args, "Invalid format! Example of proper command <col=801700>::openshop Gem Trader.</col>") {
            try {
                player.openShop(args[0].toInt())
            } catch (e: NumberFormatException) {
                player.openShop(args.joinToString().replace(",", ""))
            } catch (e: Exception) {
                player.message(e.toString())
            }
        }
    }
}

on_command("listshop", Privilege.ADMIN_POWER) {
    player.message("Shop List: ")
    var id = 0
    getPluginRepository().shops.forEach {
        player.message("id: ${id++} shop name: ${it.key}")
    }
}
