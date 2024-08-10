package dev.openrune.cache.tools.tasks.impl

import cc.ekblad.toml.decode
import cc.ekblad.toml.tomlMapper
import com.displee.cache.CacheLibrary
import dev.openrune.cache.SPRITES
import dev.openrune.cache.tools.tasks.CacheTask
import dev.openrune.cache.tools.tasks.impl.sprites.Sprite
import dev.openrune.cache.tools.tasks.impl.sprites.SpriteManifest
import dev.openrune.cache.tools.tasks.impl.sprites.SpriteSet
import dev.openrune.cache.util.getFiles
import dev.openrune.cache.util.progress
import io.netty.buffer.Unpooled
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

/**
 * The `PackSprites` class is responsible for packing sprite images into an OSRS game cache.
 * It supports both single sprite files and spritesheets, automatically handling different packing strategies based
 * on the provided manifest file. This class is a part of the cache update process, ensuring that all new or modified
 * sprites are correctly encoded and stored in the game's cache system.
 *
 * @param spritesDirectory The directory where sprite files are stored. This should include all `.png` files that
 *                         need to be processed.
 * @param spriteManifest Optional parameter specifying the manifest file that contains metadata for sprites. If not
 *                       provided, only unnamed sprites will be packed.
 *
 */
class PackSprites(
    private val spritesDirectory: File,
    private val spriteManifest: File = File(spritesDirectory, "manifest.toml")
) : CacheTask() {

    private val mapper = tomlMapper { }

    companion object {
        val customSprites: MutableMap<Int, SpriteSet> = mutableMapOf()
    }
    private var manifest: Map<String, SpriteManifest> = emptyMap()

    override fun init(library: CacheLibrary) {
        val files = getFiles(spritesDirectory, "png")
        val alreadyPacked = mutableListOf<String>()
        val progress = progress("Packing OSRS Sprites", files.filter { it.extension == "png" }.size)

        if (spriteManifest.exists()) {
            manifest = mapper.decode<Map<String,SpriteManifest>>(spriteManifest.toPath())
        }

        files.forEach { spriteFile ->
            progress.extraMessage = spriteFile.name
            processSpriteFile(spriteFile, alreadyPacked, library)
            progress.step()
        }

        customSprites.forEach { (id, spriteSet) ->
            library.put(SPRITES, id, spriteSet.encode().array())
        }
        progress.close()
    }

    private fun processSpriteFile(spriteFile: File, alreadyPacked: MutableList<String>, library: CacheLibrary) {
        val fileName = spriteFile.nameWithoutExtension.lowercase()
        manifest[fileName.lowercase()]?.let { data ->
            if (data.atlas != null) {
                packFromAtlas(spriteFile, data)
            } else {
                packNamedSprite(spriteFile, data)
            }
            alreadyPacked.add(spriteFile.name)
        } ?: handleUnNamedSprite(spriteFile, library)
    }

    private fun packFromAtlas(spriteFile: File, data: SpriteManifest) {
        data.atlas?.let { atlas ->
            val sprites = extractSpriteSheet(loadImage(spriteFile), atlas.width, atlas.height)
            sprites.forEachIndexed { index, image ->
                val sprite = Sprite(data.offsetX, data.offsetY, image)
                addSpriteToSet(data.id, sprite, image.width, image.height, index)
            }
        }
    }

    private fun packNamedSprite(spriteFile: File, data: SpriteManifest) {
        val image = loadImage(spriteFile)
        val sprite = Sprite(data.offsetX, data.offsetY, image)
        addSpriteToSet(data.id, sprite, image.width, image.height, 0)

    }

    private fun handleUnNamedSprite(spriteFile: File, library: CacheLibrary) {
        if (spriteFile.name.matches(Regex("^[_0-9]+\\.png$"))) {
            val (group, index) = spriteFile.nameWithoutExtension.split("_").let {
                it[0].toInt() to it.getOrElse(1) { "0" }.toInt()
            }
            var sprites = emptyList<Sprite>().toMutableList()
            if (index != 0) {
                sprites = SpriteSet.decode(group, Unpooled.wrappedBuffer(library.data(SPRITES, group))).sprites

                sprites[index] = Sprite(0, 0, loadImage(spriteFile))
            } else {
                sprites.add(Sprite(0, 0, loadImage(spriteFile)))
            }

            sprites.forEachIndexed { pos, sprite ->
                addSpriteToSet(group, sprite, sprite.width, sprite.height, pos)
            }
        }
    }

    private fun extractSpriteSheet(sheet : BufferedImage, spriteWidth : Int, spriteHeight : Int): MutableList<BufferedImage> {
        val sprites = emptyList<BufferedImage>().toMutableList()
        val spritesAcross = sheet.width / spriteWidth
        val spritesDown = sheet.height / spriteHeight

        for (y in 0 until spritesDown) {
            for (x in 0 until spritesAcross) {
                val sprite = sheet.getSubimage(x * spriteWidth, y * spriteHeight, spriteWidth, spriteHeight)
                sprites.add(sprite)
            }
        }
        return sprites
    }

    private fun addSpriteToSet(key: Int, sprite: Sprite, width: Int, height: Int, index: Int) {
        customSprites.getOrPut(key) {
            SpriteSet(id = key, width = width, height = height, sprites = mutableListOf(),0)
        }.sprites.add(index, sprite)
    }

    private fun loadImage(path: File): BufferedImage = ImageIO.read(path)

}