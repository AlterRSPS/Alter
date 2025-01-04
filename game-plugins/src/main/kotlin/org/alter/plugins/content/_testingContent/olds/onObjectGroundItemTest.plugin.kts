package org.alter.plugins.content._testingContent.olds

/**
 * @author CloudS3c 11/22/2024
 */


val initialTile = Tile(3158, 3487)
spawnItem("item.bowl", 1, initialTile, 3)
spawnItem("item.knife", 1, initialTile.transform(1, 0), 3)
spawnObj("object.table_12962", initialTile, type = 10)
spawnObj("object.null_14875", initialTile.transform(1, 0), type = 22)
