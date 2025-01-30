package dev.openrune.cache.tools.tasks.impl.sprites

internal class SpriteManifest(
    val id: Int,
    val offsetX: Int = 0,
    val offsetY: Int = 0,
    val atlas: SpriteAtlas? = null,
) {
    internal class SpriteAtlas(val width: Int, val height: Int)
}