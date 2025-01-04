import org.alter.plugins.content.interfaces.bank.openBank
import org.alter.plugins.content.interfaces.itemsets.ItemSets.openSets
import org.alter.plugins.content.interfaces.tournament_supplies.Tournament_Supplies.openTournamentSupplies
import org.alter.rscm.RSCM.getRSCM
/**
 * @author CloudS3c 12/30/2024
 */
val bankChestTile = Tile(1829, 3095)
var bObj = DynamicObject(id = getRSCM("object.bank_box"), type = 10, tile = bankChestTile, rot = 0)
// @TODO make so that when player moves next to it. It would do the [Queue] block
onEnterRegion(7216) {
    world.queue {
        world.spawn(bObj)
        wait(1)
        world.remove(bObj)
        world.spawn(DynamicObject(id = getRSCM("object.bank_box_31949"), type = 10, tile = bankChestTile, rot = 0))
    }
}

onObjOption("object.bank_box_31949", "Bank") {
    player.openBank()
}
onObjOption("object.bank_box_31949", "Venerate") {
    player.queue {
        when (options("Sets", "Tournament", title = "What youd like to open:")) {
            1 -> player.openSets()
            2 -> player.openTournamentSupplies()
        }
    }
}

