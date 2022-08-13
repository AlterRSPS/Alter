import gg.rsmod.game.model.priv.Privilege
import gg.rsmod.plugins.content.cmd.Commands_plugin

val OpenShopCommands = listOf("openshop", "shop", "store")
OpenShopCommands.forEach {
    on_command(it, Privilege.ADMIN_POWER, description = "Open shop , use: ::listshop for shop id's") {
        val args = player.getCommandArgs()
        Commands_plugin.Command.tryWithUsage(player, args, "Invalid format! Example of proper command <col=801700>::openshop Gem Trader.</col>") { values ->
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
    get_all_shops().forEach {
        player.message("id: ${id++} shop name: ${it.key}" )
    }
}