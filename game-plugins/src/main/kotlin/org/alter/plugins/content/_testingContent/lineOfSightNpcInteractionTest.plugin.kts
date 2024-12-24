import org.alter.plugins.content.interfaces.bank.openBank
import org.alter.plugins.content.interfaces.itemsets.ItemSets

/**
 * @author CloudS3c 11/21/2024
 */
    
on_npc_option(Npcs.GRAND_EXCHANGE_CLERK, 1, 3) {
    player.openBank()
}
on_npc_option(Npcs.GRAND_EXCHANGE_CLERK, 5, 3) {
    ItemSets.open(player)
}
on_obj_option(Objs.GRAND_EXCHANGE_BOOTH, 1) {
    player.openBank()
}

spawn_npc(Npcs.GRAND_EXCHANGE_CLERK, 3164, 3488)
spawn_npc(Npcs.GRAND_EXCHANGE_CLERK, 3165, 3488)
spawn_npc(Npcs.GRAND_EXCHANGE_CLERK, 3166, 3489)
spawn_npc(Npcs.GRAND_EXCHANGE_CLERK, 3166, 3490)
spawn_npc(Npcs.GRAND_EXCHANGE_CLERK, 3165, 3491)
spawn_npc(Npcs.GRAND_EXCHANGE_CLERK, 3164, 3491)
spawn_npc(Npcs.GRAND_EXCHANGE_CLERK, 3163, 3490)
spawn_npc(Npcs.GRAND_EXCHANGE_CLERK, 3163, 3489)