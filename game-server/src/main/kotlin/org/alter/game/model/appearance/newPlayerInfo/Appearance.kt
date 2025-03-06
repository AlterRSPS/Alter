package org.alter.game.model.appearance.newPlayerInfo

import org.alter.game.type.bas.BasType
import org.bson.Document

/**
 * Taken from RSMod 2.0
 * @author Tomm0017
 */
class Appearance {
    var rebuild: Boolean = true
        internal set

    var bas: BasType? = null
        set(value) {
            field = value
            rebuild = true
        }

    // @TODO
    // var transmog: UnpackedNpcType? = null
    //    set(value) {
    //        field = value
    //        rebuild = true
    //    }

    var skullIcon: Int? = null
        set(value) {
            field = value
            rebuild = true
        }

    var overheadIcon: Int? = null
        set(value) {
            field = value
            rebuild = true
        }

    var bodyType: Int = 0
        set(value) {
            field = value
            rebuild = true
        }

    var pronoun: Int = 0
        set(value) {
            field = value
            rebuild = true
        }

    var namePrefix: String? = null
        set(value) {
            field = value
            rebuild = true
        }

    var nameSuffix: String? = null
        set(value) {
            field = value
            rebuild = true
        }

    var combatLvlSuffix: String? = null
        set(value) {
            field = value
            rebuild = true
        }

    var softHidden: Boolean = false
        set(value) {
            field = value
            rebuild = true
        }

    var combatLevel: Int = 3
        set(value) {
            field = value
            rebuild = true
        }

    private val colours: ByteArray = ByteArray(5)
    private val identKit: ShortArray = ShortArray(7) { -1 }

    // TODO: Move default colours/identkit assignment to a relevant plugin and
    //  delete this init block.
    init {
        assignDefaultColours()
        assignDefaultIdentKit()
    }

    fun setColour(index: Int, colour: Int) {
        require(colour in 0..255) { "colour must be in range [0..255]. ($colour)" }
        this.colours[index] = colour.toByte()
        this.rebuild = true
    }

    fun setIdentKit(index: Int, identKit: Int) {
        require(identKit in 0..65535) { "identKit must be in range [0..65535]. ($identKit)" }
        this.identKit[index] = identKit.toShort()
        this.rebuild = true
    }

    fun coloursSnapshot(): List<Byte> = colours.toList()

    fun identKitSnapshot(): List<Short> = identKit.toList()

    fun clearRebuildFlag() {
        rebuild = false
    }

    private fun assignDefaultColours() {
        colours[0] = 0
        colours[1] = 0
        colours[2] = 0
        colours[3] = 0
        colours[4] = 0
    }

    private fun assignDefaultIdentKit() {
        identKit[0] = 0
        identKit[1] = 10
        identKit[2] = 18
        identKit[3] = 26
        identKit[4] = 33
        identKit[5] = 36
        identKit[6] = 42
    }

    fun asDocument(): Document {
        return Document()
            .append("looks", identKit.toList())
            .append("colors", colours.toList())
            .append("bodyType", bodyType)
    }

    companion object {
        fun fromDocument(doc: Document): Appearance {
            val appearance = Appearance()
            doc.getList("colors", Integer::class.java).forEachIndexed { index, color ->
                appearance.setColour(index, color.toInt())
            }


            doc.getList("looks", Integer::class.java).forEachIndexed { index, looks ->
                appearance.setIdentKit(index, looks.toInt())
            }
            doc.getOrDefault("bodyType", 0)
            return appearance
        }
    }
}