//package org.alter.plugins.content.testzone;
//
//import org.alter.game.model.Tile
//
///**
// *  @author <a href="https://github.com/CloudS3c">Cl0ud</a>
// *  @author <a href="https://www.rune-server.ee/members/376238-cloudsec/">Cl0ud</a>
// *
// */
//val testzone = Tile(1805, 4509, 0)
//on_command("testzone") {
//    player.moveTo(testzone)
//}
//
//
//val npc_spawn_tile = Tile(1806, 4502)
//val npc_spawn_tile_end = Tile(1799, 4511)
//
//
//for (z in npc_spawn_tile_end.z downTo npc_spawn_tile.z) {
//    for (x in npc_spawn_tile_end.x until npc_spawn_tile.x) {
//        val npc = Npc(Npcs.COW, Tile(x,z, 0), world)
//        world.spawn(npc)
//    }
//}
//
//
