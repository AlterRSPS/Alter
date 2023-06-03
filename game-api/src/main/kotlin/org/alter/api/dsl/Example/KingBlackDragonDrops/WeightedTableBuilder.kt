//package org.alter.api.dsl.Example.KingBlackDragonDrops
//
//import org.apollo.cache.def.ItemDef
//import org.apollo.cache.def.ItemDefBase
//import org.apollo.game.BaseItem
//import org.apollo.model.skills.herblore.Herb
//import org.apollo.plugin.api.id
//import org.apollo.plugin.api.noted
//import org.alter.api.dsl.Example.KingBlackDragonDrops.WeightedTableType.*
//import org.apollo.plugin.api.ui.noneItemType
//import org.apollo.table.WeightedTableDenominatorInteger
//import org.apollo.table.WeightedTableInteger
//import org.apollo.utility.random
//
//import org.apollo.utility.constants.ItemTypeString
//import org.apollo.utility.constants.generated.ItemTypeConstants
//import kotlin.math.roundToInt
//
//
//val Int.noted: Int
//    get() = ItemDefBase.note(this)
//val Int.unnoted: Int
//    get() = ItemDefBase.unnote(this)
//
//enum class WeightedTableType {
//    ALWAYS,
//    PRE_ROLL,
//    MAIN,
//    TERTIARY
//}
//
//class ConditionalItem<T>(
//    val id: ItemTypeString,
//    val minAmount: Int,
//    val maxAmount: Int,
//    val numerator: Int,
//    val denominator: Int,
//    description: String?,
//    block: T.() -> Boolean
//) :
//    WeightedPredicate<T>(description, { block(this) }) {
//    constructor(id: ItemTypeString, minAmount: Int, maxAmount: Int) : this(id, minAmount, maxAmount, 1, 1, null, { true })
//}
//
//open class WeightedPredicate<T>(val description: String?, val block: T.(list: MutableList<ConditionalItem<T>>) -> Boolean)
//
//open class WeightedTableEntriesBase<T>(private val rolls: Int) : Rollable<T>() {
//
//    val builder = WeightedTableDenominatorInteger<WeightedPredicate<T>>()
//    lateinit var table: WeightedTableInteger<WeightedPredicate<T>>
//
//    override fun roll(list: MutableList<ConditionalItem<T>>, type: T): Boolean {
//        var retry = false
//        repeat(rolls) {
//            val next = table.next()
//            if (next == null || next.isNone()) {
//                return@repeat
//            }
//
//            if (next.block(type, list)) {
//                val index = next.tableIndex()
//                if (index == -2) {
//
//                } else
//                    if (index == -1) {
//                        list.add(next as ConditionalItem<T>)
//                    } else {
//                        val table = linked[index]
//                        if (!table.roll(list, type)) {
//                            retry = true
//                        }
//                    }
//            } else {
//                retry = true
//            }
//        }
//        return retry
//    }
//
//    override fun build(type: WeightedTableType) {
//        table = builder.build(type == MAIN)
//    }
//
//    override fun all(block: (entry: WeightedPredicate<T>, numerator: Int, denominator: Int) -> Unit) {
//        builder.iterate { entry, numerator, denominator ->
//            block(entry, numerator, denominator)
//        }
//    }
//}
//
//open class WeightedTableEntries<T>(rolls: Int) : WeightedTableEntriesBase<T>(rolls) {
//
//    fun add(numerator: Int, denominator: Int, description: String? = null, block: T.(list: MutableList<ConditionalItem<T>>) -> Boolean) {
//        builder.add(numerator = numerator, denominator = denominator, item = WeightedPredicate(description, block))
//    }
//
//    fun add(
//        id: ItemTypeString,
//        minAmount: Int,
//        maxAmount: Int,
//        numerator: Int,
//        denominator: Int,
//        description: String? = null,
//        block: T.() -> Boolean = { true }
//    ) {
//        builder.add(
//            numerator = numerator,
//            denominator = denominator,
//            item = ConditionalItem(id, minAmount, maxAmount, numerator, denominator, description, block)
//        )
//    }
//
//    fun add(
//        table: WeightedTableBuilder<T>,
//        type: WeightedTableType,
//        numerator: Int,
//        denominator: Int,
//        description: String? = null,
//        block: T.() -> Boolean = { true }
//    ) {
//        val main = table[type] as Rollable<T>
//        add(entries = main, numerator = numerator, denominator = denominator, description = description, block = block)
//    }
//
//    private fun add(
//        entries: Rollable<T>,
//        numerator: Int,
//        denominator: Int,
//        description: String? = null,
//        block: T.() -> Boolean = { true }
//    ) {
//        add(
//            id = noneItemType,
//            minAmount = linked.size,
//            maxAmount = linked.size,
//            numerator = numerator,
//            denominator = denominator,
//            description = description,
//            block = block
//        )
//        linked.add(entries)
//    }
//
//
//    fun add(id: ItemTypeString, minAmount: Int, maxAmount: Int, denominator: Int, description: String? = null, block: T.() -> Boolean) =
//        add(id, minAmount, maxAmount, 1, denominator, description, block)
//
//    fun add(id: ItemTypeString, minAmount: Int, maxAmount: Int, numerator: Int, denominator: Int) =
//        add(id, minAmount, maxAmount, numerator, denominator) { true }
//
//    fun add(id: ItemTypeString, minAmount: Int, maxAmount: Int, denominator: Int) = add(id, minAmount, maxAmount, 1, denominator)
//
//    fun add(id: ItemTypeString, amount: Int, denominator: Int, description: String? = null, block: T.() -> Boolean) =
//        add(id, amount, amount, denominator, description, block)
//
//    fun add(id: ItemTypeString, amount: Int, denominator: Int) = add(id, amount, amount, denominator)
//
//}
//
//class MainWeightedTableEntries<T>(private val total: Int, rolls: Int) : WeightedTableEntriesBase<T>(rolls) {
//
//    fun add(weight: Int, description: String? = null, block: T.(list: MutableList<ConditionalItem<T>>) -> Boolean) {
//        builder.add(weight, total, WeightedPredicate(description, block))
//    }
//
//    fun add(
//        table: WeightedTableBuilder<T>,
//        type: WeightedTableType = MAIN,
//        weight: Int,
//        description: String? = null,
//        block: T.() -> Boolean = { true }
//    ) {
//        add(table[type]!!, weight, description, block)
//    }
//
//    private fun add(main: Rollable<T>, weight: Int, description: String? = null, block: T.() -> Boolean = { true }) {
//        add(noneItemType, linked.size, linked.size, weight, description, block)
//        linked.add(main)
//    }
//
//    fun add(id: ItemTypeString, minAmount: Int, maxAmount: Int, weight: Int, description: String? = null, block: T.() -> Boolean = { true }) {
//        builder.add(weight, total, ConditionalItem(id, minAmount, maxAmount, weight, total, description, block))
//    }
//
//    fun add(id: ItemTypeString, minAmount: Int, maxAmount: Int, weight: Int) = add(id, minAmount, maxAmount, weight) { true }
//    fun add(id: ItemTypeString, amount: Int, weight: Int, description: String? = null, block: T.() -> Boolean) =
//        add(id, amount, amount, weight, description, block)
//
//    fun add(id: ItemTypeString, amount: Int, weight: Int) = add(id, amount, amount, weight)
//
//    fun nothing(weight: Int) {
//        add(noneItemType, -1 -1, weight)
//    }
//
//    override fun build(type: WeightedTableType) {
//        super.build(type)
//
//        if (total != table.total) {
//            error("Backing ($total) must match specified total ${table.total}")
//        }
//    }
//
//}
//
//class TertiaryTableBuilder<T>(private val rolls: Int) : WeightedTableEntries<T>(rolls) {
//
//    private val tables = mutableListOf<TertiaryTableEntries<T>>()
//
//    fun table(name: String, predicate: T.() -> Boolean = { true }, block: TertiaryTableEntries<T>.() -> Unit) {
//        val entry = TertiaryTableEntries(name, rolls, predicate)
//        block(entry)
//        tables.add(entry)
//    }
//
//    override fun roll(list: MutableList<ConditionalItem<T>>, type: T): Boolean {
//        repeat(rolls) {
//            builder.iterate { next, numerator, denominator ->
//                val ratio = numerator.toDouble() / denominator.toDouble()
//                if (random() > ratio) {
//                    return@iterate
//                }
//
//                if (next.block(type, list)) {
//                    when (val index = next.tableIndex()) {
//                        -2 -> {}
//                        -1 -> {
//                            list.add(next as ConditionalItem<T>)
//                        }
//                        else -> {
//                            linked[index].roll(list, type)
//                        }
//                    }
//                }
//            }
//
//            tables.forEach {
//                it.roll(list, type)
//            }
//        }
//        return false
//    }
//
//    override fun build(type: WeightedTableType) {
//        tables.forEach { it.build(type) }
//    }
//}
//
//class TertiaryTableEntries<T>(val name: String, rolls: Int, private val predicate: T.() -> Boolean) : WeightedTableEntries<T>(rolls) {
//
//    override fun roll(list: MutableList<ConditionalItem<T>>, type: T): Boolean {
//        if (!predicate(type)) {
//            return false
//        }
//
//        return super.roll(list, type)
//    }
//
//
//}
//
//open class AlwaysTableEntries<T> : Rollable<T>() {
//
//    protected val backing = mutableListOf<ConditionalItem<T>>()
//
//    fun add(id: ItemTypeString) = add(id, 1)
//    fun add(id: ItemTypeString, amount: Int) = add(id, amount, amount)
//
//    fun add(table: WeightedTableBuilder<T>, type: WeightedTableType) {
//        val main = table[type] as Rollable<T>
//        add(main)
//    }
//
//    private fun add(entries: Rollable<T>) {
//        add(noneItemType, linked.size, linked.size)
//        linked.add(entries)
//    }
//
//    open fun add(id: ItemTypeString, minAmount: Int, maxAmount: Int) {
//        backing.add(ConditionalItem(id, minAmount, maxAmount))
//    }
//
//    override fun roll(list: MutableList<ConditionalItem<T>>, type: T): Boolean {
//        list.addAll(backing)
//        linked.forEach {
//            it.roll(list, type)
//        }
//        return false
//    }
//
//    override fun build(type: WeightedTableType) {}
//    override fun all(block: (entry: WeightedPredicate<T>, numerator: Int, denominator: Int) -> Unit) {
//        backing.forEach { block(it, 1, 1) }
//    }
//}
//
//abstract class Rollable<T> {
//    internal val linked: MutableList<Rollable<T>> by lazy { mutableListOf() }
//
//    abstract fun roll(list: MutableList<ConditionalItem<T>>, type: T): Boolean
//    abstract fun build(type: WeightedTableType)
//    abstract fun all(block: (entry: WeightedPredicate<T>, numerator: Int, denominator: Int) -> Unit)
//}
//
//class WeightedTableBuilder<T> {
//
//    private val entries: MutableMap<WeightedTableType, Rollable<T>> = mutableMapOf()
//
//    operator fun get(type: WeightedTableType) = entries[type]
//
//    fun always(rolls: Int = 1, block: AlwaysTableEntries<T>.() -> Unit) = entry(ALWAYS, block)
//    fun preroll(rolls: Int = 1, block: WeightedTableEntries<T>.() -> Unit) = entry(PRE_ROLL, block)
//
//    fun always(block: AlwaysTableEntries<T>.() -> Unit) = always(1, block)
//    fun preroll(block: WeightedTableEntries<T>.() -> Unit) = preroll(1, block)
//
//    fun main(total: Int, rolls: Int = 1, block: MainWeightedTableEntries<T>.() -> Unit) = entry(rolls, total, MAIN, block)
//    fun tertiary(rolls: Int, block: TertiaryTableBuilder<T>.() -> Unit) = entry(rolls, TERTIARY, block)
//    fun tertiary(block: TertiaryTableBuilder<T>.() -> Unit) = tertiary(1, block)
//
//    private fun entry(type: WeightedTableType, block: WeightedTableEntries<T>.() -> Unit) = entry(1, type, block)
//
//    private fun entry(rolls: Int, total: Int, type: WeightedTableType, block: MainWeightedTableEntries<T>.() -> Unit) {
//        entries[type] = MainWeightedTableEntries<T>(total, rolls).apply {
//            block(this)
//            build(type)
//        }
//    }
//
//    @JvmName("entry1")
//    private fun entry(rolls: Int, type: WeightedTableType, block: TertiaryTableBuilder<T>.() -> Unit) {
//        entries[type] = TertiaryTableBuilder<T>(rolls).apply {
//            block(this)
//            build(type)
//        }
//    }
//
//    private fun entry(rolls: Int, type: WeightedTableType, block: WeightedTableEntries<T>.() -> Unit) {
//        entries[type] = WeightedTableEntries<T>(rolls).apply {
//            block(this)
//            build(type)
//        }
//    }
//
//    @JvmName("baseEntry")
//    private fun entry(type: WeightedTableType, block: AlwaysTableEntries<T>.() -> Unit) {
//        entries[type] = AlwaysTableEntries<T>().apply {
//            block(this)
//            build(type)
//        }
//    }
//
//    fun roll(type: T) = roll(always = 1, preroll = 1, main = 1, tertiary = 1, type = type)
//
//
//    fun roll(always: Int = 1, preroll: Int = 1, main: Int = 1, tertiary: Int = 1, type: T): List<BaseItem> {
//        val list = mutableListOf<BaseItem>()
//        rollFull(always = always, preroll = preroll, main = main, tertiary = tertiary, type = type).forEach {
//            if (it.id == noneItemType) {
//                return@forEach
//            }
//
//            val amount = random(it.minAmount, it.maxAmount)
//            val id = when {
//                Herb.isHerb(it.id.id) -> it.id.noted
//                else -> when (it.id) {
//                    ItemTypeConstants.UNCUT_DIAMOND,  ItemTypeConstants.UNCUT_RUBY,  ItemTypeConstants.UNCUT_EMERALD,  ItemTypeConstants.UNCUT_SAPPHIRE,  ItemTypeConstants.UNCUT_OPAL,  ItemTypeConstants.UNCUT_JADE,  ItemTypeConstants.UNCUT_RED_TOPAZ,  ItemTypeConstants.UNCUT_DRAGONSTONE -> it.id.noted
//                    else -> it.id
//                }
//            }
//
//            val def = ItemDef[id.id]
//            val multiplier = when {
//                id ==  ItemTypeConstants.COINS -> 10.0
//                id ==  ItemTypeConstants.TRADING_STICKS || id == ItemTypeConstants.TOKKUL -> 5.0
//                def?.isStackable ?: false || def?.isNote ?: false -> 2.0
//                else -> 1.0
//            }
//            list += BaseItem(
//                id.id,
//                ((amount * multiplier).roundToInt())
//            )
//        }
//        return list
//    }
//
//    fun rollFull(rolls: Int, type: T) = rollFull(always = rolls, preroll = rolls, main = rolls, tertiary = rolls, type = type)
//    fun rollFull(always: Int, preroll: Int, main: Int, tertiary: Int, type: T): List<ConditionalItem<T>> {
//        fun add(rollable: Rollable<T>?, type: T, mutable: MutableList<ConditionalItem<T>>): Boolean {
//            rollable ?: return false
//
//            if (!rollable.roll(list = mutable, type = type) || !rollable.roll(list = mutable, type = type)) {
//                return false
//            }
//            return true
//        }
//
//        val mutable = mutableListOf<ConditionalItem<T>>()
//        repeat(always) {
//            add(entries[ALWAYS], type, mutable)
//        }
//
//        var complete = false
//        repeat(preroll) {
//            complete = complete.or(add(entries[PRE_ROLL], type, mutable))
//        }
//
//        if (!complete) {
//            repeat(main) {
//                add(entries[MAIN], type, mutable)
//            }
//        }
//
//        repeat(tertiary) {
//            add(entries[TERTIARY], type, mutable)
//        }
//
//        return mutable
//    }
//
//    fun all(block: (entry: WeightedPredicate<T>, numerator: Int, denominator: Int) -> Unit) {
//        val tables = mutableSetOf<Rollable<T>>()
//
//        fun iterate(rollable: Rollable<T>) {
//            if (tables.contains(rollable)) {
//                return
//            }
//            tables.add(rollable)
//
//            rollable.all(block)
//            rollable.linked.forEach {
//                iterate(it)
//            }
//        }
//
//        entries[ALWAYS]?.let { iterate(it) }
//        entries[PRE_ROLL]?.let { iterate(it) }
//        entries[MAIN]?.let { iterate(it) }
//        entries[TERTIARY]?.let { iterate(it) }
//    }
//}
//
//fun <T> WeightedPredicate<T>.isNone(): Boolean {
//    if (this is ConditionalItem) {
//        return id == noneItemType && minAmount == -1 && maxAmount == -1
//    }
//    return false
//}
//
//private fun <T> WeightedPredicate<T>.tableIndex(): Int {
//    if (this is ConditionalItem) {
//        if (id != noneItemType) return -1
//        return minAmount
//    }
//    return -2
//}
//
//fun <T> mainTable(total: Int, rolls: Int = 1, block: MainWeightedTableEntries<T>.() -> Unit): WeightedTableBuilder<T> {
//    val builder = WeightedTableBuilder<T>()
//    builder.main(total, rolls, block)
//    return builder
//}
//
//fun <T> table(block: WeightedTableBuilder<T>.() -> Unit): WeightedTableBuilder<T> {
//    val builder = WeightedTableBuilder<T>()
//    block(builder)
//    return builder
//}
