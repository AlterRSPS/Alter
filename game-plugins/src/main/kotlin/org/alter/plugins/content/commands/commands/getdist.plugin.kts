onCommand("getdist") {
    val args = player.getCommandArgs()
    val x = args[0].toInt()
    val z = args[1].toInt()
    val tile = Tile(x,z)
    player.message("Player:[${player.tile}], Destination: [${tile}], Distance: [${player.tile.getDistance(tile)}]" )
}