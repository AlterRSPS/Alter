package dev.openrune.cache.filestore.definition.data

import dev.openrune.cache.filestore.definition.Definition
import dev.openrune.cache.filestore.definition.Parameterized
import dev.openrune.cache.filestore.definition.Recolourable
import it.unimi.dsi.fastutil.bytes.Byte2ByteOpenHashMap

data class ItemType(
    override var id: Int = -1,
    var name: String = "null",
    var examine: String = "null",
    override var originalColours: MutableList<Int>? = null,
    override var modifiedColours: MutableList<Int>? = null,
    override var originalTextureColours: MutableList<Int>? = null,
    override var modifiedTextureColours: MutableList<Int>? = null,
    override var params: Map<Int, Any>? = null,
    var resizeX: Int = 128,
    var resizeY: Int = 128,
    var resizeZ: Int = 128,
    var xan2d: Int = 0,
    var category : Int = -1,
    var yan2d: Int = 0,
    var zan2d: Int = 0,
    var equipSlot: Int = -1,
    var appearanceOverride1: Int = -1,
    var appearanceOverride2: Int = -1,
    var weight: Double = 0.0,
    var cost: Int = 1,
    var isTradeable: Boolean = false,
    var stacks: Int = 0,
    var inventoryModel: Int = 0,
    var members: Boolean = false,
    var zoom2d: Int = 2000,
    var xOffset2d: Int = 0,
    var yOffset2d: Int = 0,
    var ambient: Int = 0,
    var contrast: Int = 0,
    var countCo: MutableList<Int> = MutableList(10) { 0 },
    var countObj: MutableList<Int> = MutableList(10) { 0 },
    var options : MutableList<String?> = mutableListOf(null, null, "Take", null, null),
    var interfaceOptions  : MutableList<String?> = mutableListOf(null, null, null, null, "Drop"),
    var maleModel0: Int = -1,
    var maleModel1: Int = -1,
    var maleModel2: Int = -1,
    var maleOffset: Int = 0,
    var maleHeadModel0: Int = -1,
    var maleHeadModel1: Int = -1,
    var femaleModel0: Int = -1,
    var femaleModel1: Int = -1,
    var femaleModel2: Int = -1,
    var femaleOffset: Int = 0,
    var femaleHeadModel0: Int = -1,
    var femaleHeadModel1: Int = -1,
    var noteLinkId: Int = -1,
    var noteTemplateId: Int = -1,
    var teamCape: Int = 0,
    var dropOptionIndex: Int = -2,
    var unnotedId: Int = -1,
    var notedId: Int = -1,
    var placeholderLink: Int = -1,
    var placeholderTemplate: Int = -1,
    //Custom
    override var inherit: Int = -1,
    var option1: String? = null,
    var option2: String? = null,
    var option3: String = "Take",
    var option4: String? = null,
    var option5: String? = null,
    var ioption1: String? = null,
    var ioption2: String? = null,
    var ioption3: String? = null,
    var ioption4: String? = null,
    var ioption5: String = "Drop",

    ) : Definition, Recolourable, Parameterized {

    init {
        options = listOf(option1,option2,option3,option4,option5).toMutableList()
        interfaceOptions = listOf(ioption1,ioption2,ioption3,ioption4,ioption5).toMutableList()
    }

    lateinit var bonuses: IntArray
    var attackSpeed = -1
    var equipType = 0
    var weaponType = -1
    var renderAnimations: IntArray? = null
    var skillReqs: Byte2ByteOpenHashMap? = null

    val stackable: Boolean
        get() = stacks == 1 || noteTemplateId > 0

    val noted: Boolean
        get() = noteTemplateId > 0

    /**
     * Whether or not the object is a placeholder.
     */
    val isPlaceholder
        get() = placeholderTemplate > 0 && placeholderLink > 0

}