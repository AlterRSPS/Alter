import org.alter.game.model.priv.Privilege

listOf("openshop", "shop", "store").forEach {
    onCommand(it, Privilege.ADMIN_POWER, description = "Open shop , use: ::listshop for shop id's") {
        val args = player.getCommandArgs()
        try {
            player.openShop(args[0].toInt())
        } catch (e: NumberFormatException) {
            player.openShop(args.joinToString().replace(",", ""))
        } catch (e: Exception) {
            player.message(e.toString())
        }
    }
}