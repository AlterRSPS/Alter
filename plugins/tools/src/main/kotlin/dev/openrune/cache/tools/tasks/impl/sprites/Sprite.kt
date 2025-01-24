package dev.openrune.cache.tools.tasks.impl.sprites

import java.awt.image.BufferedImage

data class Sprite(val offsetX: Int, val offsetY: Int, val image: BufferedImage) {
    val width: Int get() = image.width
    val height: Int get() = image.height
    fun getRGB(x: Int, y: Int): Int = image.getRGB(x, y)
    fun setRGB(x: Int, y: Int, rgb: Int) {
        image.setRGB(x, y, rgb)
    }
}