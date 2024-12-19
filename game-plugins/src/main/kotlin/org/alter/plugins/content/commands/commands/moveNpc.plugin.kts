//import org.alter.game.model.move.walkTo
//
//val paths =
//    listOf(
//        Tile(3219, 3222),
//        Tile(3221, 3222),
//        Tile(3221, 3212),
//        Tile(3214, 3205),
//        Tile(3202, 3205),
//        Tile(3202, 3233),
//        Tile(3203, 3233),
//        Tile(3207, 3233),
//        Tile(3210, 3230),
//        Tile(3210, 3230),
//        Tile(3219, 3220),
//    )
//val PATROL_DELAY = TimerKey()
//on_npc_spawn(Npcs.HANS) {
//    npc.timers[PATROL_DELAY] = 1
//}
//on_timer(PATROL_DELAY) {
//    npc.queue {
//        npc.walkTo(paths[npc.pathsIndex])
//        npc.pathsIndex = npc.pathsIndex++ % paths.size
//    }
//    npc.timers[PATROL_DELAY] = 1
//}
