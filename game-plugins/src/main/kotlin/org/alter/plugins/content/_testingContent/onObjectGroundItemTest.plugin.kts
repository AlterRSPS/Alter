/**
 * @author CloudS3c 11/22/2024
 */


val initialTile = Tile(3158, 3487)
spawn_item(Items.BOWL, 1, initialTile, 3)
spawn_item(Items.KNIFE, 1, initialTile.transform(1, 0), 3)
spawn_obj(12962, initialTile, type = 10)
spawn_obj(14875, initialTile.transform(1, 0), type = 22)
