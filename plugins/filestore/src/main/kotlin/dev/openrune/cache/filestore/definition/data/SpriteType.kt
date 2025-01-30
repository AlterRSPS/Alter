package dev.openrune.cache.filestore.definition.data

import dev.openrune.cache.filestore.definition.Definition
import dev.openrune.game.IndexedSprite
import java.awt.image.BufferedImage
import kotlin.math.ceil


data class SpriteType(
    override var id: Int,
    var sprites: Array<IndexedSprite>? = null,
    override var inherit: Int = -1
) : Definition {

    var spriteSheet : BufferedImage? = null

    fun toSprite(subIndex: Int = -1): BufferedImage {
        val spriteList = sprites ?: return BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB)

        return if (spriteList.size == 1) {
            spriteList.first().toBufferedImage()
        } else {
            when {
                subIndex in spriteList.indices -> {
                    return spriteList[subIndex].toBufferedImage()
                }
                else -> {
                    spriteSheet = createSpriteSheet(spriteList,200)
                    return spriteSheet!!
                }
            }
        }
    }

    private fun createSpriteSheet(spriteList: Array<IndexedSprite>, sheetWidth: Int): BufferedImage {
        // Determine the maximum width and height of all sprites
        val maxSpriteWidth = spriteList.maxOf { it.width }
        val maxSpriteHeight = spriteList.maxOf { it.height }

        // Calculate the number of columns that fit in the specified sheet width
        var cols = sheetWidth / maxSpriteWidth
        if (cols == 0) cols = 1 // Avoid division by zero

        // Calculate the number of rows needed based on the total number of sprites
        val rows = ceil(spriteList.size.toDouble() / cols).toInt()

        // Create a new BufferedImage to hold the entire sprite sheet
        val spriteSheet = BufferedImage(
            cols * maxSpriteWidth, rows * maxSpriteHeight, BufferedImage.TYPE_INT_ARGB
        )

        // Get the graphics context for drawing
        val g2d = spriteSheet.createGraphics()

        // Iterate over the spriteList and draw each sprite at the appropriate position
        var spriteIndex = 0
        for (row in 0 until rows) {
            for (col in 0 until cols) {
                if (spriteIndex >= spriteList.size) break

                val sprite = spriteList[spriteIndex]
                val x = col * maxSpriteWidth
                val y = row * maxSpriteHeight

                // Center the sprite within its "cell" if it's smaller than the max dimensions
                val offsetX = (maxSpriteWidth - sprite.width) / 2
                val offsetY = (maxSpriteHeight - sprite.height) / 2

                // Draw the sprite onto the sprite sheet, centered in its grid cell
                g2d.drawImage(sprite.toBufferedImage(), x + offsetX, y + offsetY, null)

                spriteIndex++
            }
        }

        // Dispose of the graphics context
        g2d.dispose()

        return spriteSheet
    }

}