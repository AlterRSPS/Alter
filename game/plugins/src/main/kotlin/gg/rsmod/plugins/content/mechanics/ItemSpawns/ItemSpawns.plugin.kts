import gg.rsmod.plugins.content.mechanics.itemspawns.ItemSpawnsService

load_service(ItemSpawnsService())
on_world_init {
    world.getService(ItemSpawnsService::class.java)!!.let { service ->
        service.spawns.forEach {
            spawn_item(it.id, it.amount, it.x, it.z, it.height, it.respawnTime)
        }
    }
}
