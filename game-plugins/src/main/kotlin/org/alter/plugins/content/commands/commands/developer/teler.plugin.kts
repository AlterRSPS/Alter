import org.alter.game.model.move.moveTo
import org.alter.game.model.priv.Privilege

onCommand("teler", Privilege.DEV_POWER, description = "Teleport to region") {
    val args = player.getCommandArgs()
    val region = args[0].toInt()
    val tile = Tile.fromRegion(region)
    player.moveTo(tile)
}
