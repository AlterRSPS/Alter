/**
 * @author Fritz <frikkipafi@gmail.com>
 */

on_command("yell") {

    val rank: String
    val color: String

    when (player.privilege.id) {
        0 -> { rank = "Player"
               color = ""}
        1 -> { rank = "<img=1>Donator"
               color = "<shad=16711680>"}
        2 -> { rank = "<img=0>Admin"
               color = "<shad=65280>"}
        3 -> { rank = "<img=2>Developer"
               color = "<shad=53247>"}
        4 -> { rank = "<img=3>Owner"
               color = "<shad=16777215>"}

    else -> { rank = "unidendified"
              color = ""}
    }

    val args = player.getCommandArgs()
    val name = player.username
    val text = args[0]
    player.world.players.forEach {
        it.message("${color}[${rank}]${name}:${text}", ChatMessageType.ENGINE)
    }
}

