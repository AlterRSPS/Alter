import gg.rsmod.game.service.game.ItemMetadataService

/**
 * @TODO Finish
 */
on_command("reloaditems") {
    load_service(ItemMetadataService())
    player.message("ItemMetaDataService was reloaded.")
}