package org.alter.plugins.content.interfaces.bank

import org.alter.api.ext.getVarbit
import org.alter.api.ext.setVarbit
import org.alter.game.model.attr.INTERACTING_ITEM_SLOT
import org.alter.game.model.entity.Player
import org.alter.game.model.item.Item
import org.alter.plugins.content.interfaces.bank.Bank.insert

/**
 * @author bmyte <bmytescape@gmail.com>
 */
object BankTabs {
    const val BANK_TABLIST_ID = 11

    const val SELECTED_TAB_VARBIT = 4150

    /**
     * So when we set example: 4171 -> to 7
     * From top 7 first items get sent to be at that tab[4171 : represents tab 1]
     * When we do collapse we need to send the front items to the end of main Tab
     * I think it would be just better to convert the entire bank tabs to dimensional array
     */
    const val BANK_TAB_ROOT_VARBIT = 4170

    /**
     * Handles the dropping of items into the specified tab of the player's [Bank].
     *
     * @param player
     * The acting [Player] for which the [INTERACTING_ITEM_SLOT] [Item] should
     * be dropped into the specified [dstTab]
     *
     * @param dstTab
     * The bank tab number for which the [INTERACTING_ITEM_SLOT] [Item] is
     * to be dropped into.
     */
    fun dropToTab(
        player: Player,
        dstTab: Int,
    ) {
        val container = player.bank
        val srcSlot = player.attr[INTERACTING_ITEM_SLOT]!!
        val curTab = getCurrentTab(player, srcSlot)
        if (dstTab == curTab) {
            println(" dropToTab was returned as dst is same as curTab")
            return
        } else {
            if (dstTab == 0) { // add to main tab don't insert
                println(":test 1")
                container.insert(srcSlot, container.nextFreeSlot - 1)
                player.setVarbit(BANK_TAB_ROOT_VARBIT + curTab, player.getVarbit(BANK_TAB_ROOT_VARBIT + curTab) - 1)
                // check for empty tab shift
                if (player.getVarbit(BANK_TAB_ROOT_VARBIT + curTab) == 0 && curTab <= numTabsUnlocked(player)) {
                    shiftTabs(player, curTab)
                }
            } else {
                if (dstTab < curTab || curTab == 0) {
                    println(":test 2")
                    container.insert(srcSlot, insertionPoint(player, dstTab))
                } else {
                    println(":test 3")
                    container.insert(srcSlot, insertionPoint(player, dstTab) - 1)
                }
                player.setVarbit(BANK_TAB_ROOT_VARBIT + dstTab, player.getVarbit(BANK_TAB_ROOT_VARBIT + dstTab) + 1)
                if (curTab != 0) {
                    println(":test 4")
                    player.setVarbit(BANK_TAB_ROOT_VARBIT + curTab, player.getVarbit(BANK_TAB_ROOT_VARBIT + curTab) - 1)
                    // check for empty tab shift
                    if (player.getVarbit(BANK_TAB_ROOT_VARBIT + curTab) == 0 && curTab <= numTabsUnlocked(player)) {
                        shiftTabs(player, curTab)
                    }
                }
            }
        }
    }

    /**
     * @TODO If you drop in some new items and have some item w amount => It will override the order of it.
     *
     * Handles the dropping of items into the specified tab of the player's [Bank] from a source slot.
     */
    fun dropToTab(
        player: Player,
        dstTab: Int,
        srcSlot: Int,
        hasEmptySlot: Boolean
    ) {
        val container = player.bank
        val curTab = getCurrentTab(player, srcSlot)

        if (dstTab == curTab) {
            return
        } else {
            if (dstTab == 0) { // add to main tab don't insert
                container.insert(srcSlot, container.nextFreeSlot - 1)
                player.setVarbit(BANK_TAB_ROOT_VARBIT + curTab, player.getVarbit(BANK_TAB_ROOT_VARBIT + curTab) - 1)
                // check for empty tab shift
                if (player.getVarbit(BANK_TAB_ROOT_VARBIT + curTab) == 0 && curTab <= numTabsUnlocked(player)) {
                    shiftTabs(player, curTab)
                }
            } else {
                var insertionPoint = newInsertionPoint(player, dstTab)
                if (dstTab < curTab || curTab == 0) {
                    println("HERE 1")
                    container.insert(srcSlot, insertionPoint.first)
                } else {
                    println("HERE 2")
                    container.insert(srcSlot, insertionPoint.first - 1)
                }
                if (!hasEmptySlot) {
                    player.setVarbit(BANK_TAB_ROOT_VARBIT + dstTab, player.getVarbit(BANK_TAB_ROOT_VARBIT + dstTab) + 1)
                }

                if (curTab != 0) {
                    println("HERE 4")
                    if (player.getVarbit(BANK_TAB_ROOT_VARBIT + curTab) == 0 && curTab <= numTabsUnlocked(player)) {
                        shiftTabs(player, curTab)
                    }
                }
            }
        }
    }
    /**
     * Determines the tab a given slot falls into based on
     * associative varbit analysis.
     *
     * @param player
     * The acting [Player] whose [Bank] tabs are to be checked
     *
     * @param slot
     * The associated slot for a given [Item] within the player's [Bank]
     *
     * @return -> Int
     * The tab which the specified [slot] resides
     */
    fun getCurrentTab(
        player: Player,
        slot: Int,
    ): Int {
        var current = 0
        for (tab in 1..9) {
            current += player.getVarbit(BANK_TAB_ROOT_VARBIT + tab)
            if (slot < current) {
                return tab
            }
        }
        return 0
    }

    /**
     * Tabulates the number of tabs the [player] is currently using
     * based off associative varbit analysis.
     *
     * @param player
     * The acting [Player] to get the number of in-use tabs for
     *
     * @return -> Int
     * The number of tabs the player has in-use/unlocked
     */
    fun numTabsUnlocked(player: Player): Int {
        var tabsUnlocked = 0
        for (tab in 1..9)
            if (player.getVarbit(BANK_TAB_ROOT_VARBIT + tab) > 0) {
                tabsUnlocked++
            }
        return tabsUnlocked
    }

    /**
     * Determines the insertion point for an item being added to
     * a tab based on the tab order and number of items in it and
     * previous tabs.
     *
     * @param player
     * The acting [Player] to find the insertion point for placing
     * an [Item] in the bank tab specified by [tabIndex]
     *
     * @param tabIndex
     * The tab for which the insertion point is desired
     *
     * @return -> Int
     * The insertion index for inserting into the desired tab
     */
    fun insertionPoint(
        player: Player,
        tabIndex: Int = 0,
    ): Int {
        if (tabIndex == 0) {
            return player.bank.nextFreeSlot
        }
        var prevDex = 0
        var dex = 0
        for (tab in 1..tabIndex) {
            prevDex = dex
            dex += player.getVarbit(BANK_TAB_ROOT_VARBIT + tab)
        }

        // truncate empty spots, but stay in current tab
        while (dex != 0 && player.bank[dex - 1] == null && dex > prevDex) {
            dex--
        }
        return dex
    }

    /**
     * Returns if it had to replace null
     */
    fun newInsertionPoint(
        player: Player,
        tabIndex: Int = 0,
    ): Pair<Int, Boolean> {
        var state = false
        if (tabIndex == 0) {
            return Pair(player.bank.nextFreeSlot, state)
        }
        var prevDex = 0
        var dex = 0
        for (tab in 1..tabIndex) {
            prevDex = dex
            dex += player.getVarbit(BANK_TAB_ROOT_VARBIT + tab)
        }

        // truncate empty spots, but stay in current tab
        while (dex != 0 && player.bank[dex - 1] == null && dex > prevDex) {
            dex--
        }
        if (player.bank[dex] == null) {
            state = true
        }
        return Pair(dex, state)
    }

    /**
     * Determines the beginning index for a specified bank tab
     * based on the tab order and number of items in previous tabs.
     *
     * @param player
     * The acting [Player] to find the start point for the bank tab
     * specified by [tabIndex]
     *
     * @param tabIndex
     * The tab for which the start point is desired
     *
     * @return -> Int
     * The start index for the beginning of the desired tab
     */
    fun startPoint(
        player: Player,
        tabIndex: Int = 0,
    ): Int {
        var dex = 0
        if (tabIndex == 0) {
            for (tab in 1..9)
                dex += player.getVarbit(BANK_TAB_ROOT_VARBIT + tab)
        } else {
            for (tab in 1 until tabIndex)
                dex += player.getVarbit(BANK_TAB_ROOT_VARBIT + tab)
        }
        return dex
    }

    /**
     * Performs the shifting of [Bank] tabs' varbit pointers to remove
     * an empty tab, effectively shifting greater tabs down.
     *
     * @param player
     * The acting [Player] which needs bank tabs shifted
     *
     * @param emptyTabIdx
     * The newly emptied bank tab to shift out
     */
    fun shiftTabs(
        player: Player,
        emptyTabIdx: Int,
    ) {
        val numUnlocked = numTabsUnlocked(player)
        for (tab in emptyTabIdx..numUnlocked)
            player.setVarbit(BANK_TAB_ROOT_VARBIT + tab, player.getVarbit(BANK_TAB_ROOT_VARBIT + tab + 1))
        player.setVarbit(BANK_TAB_ROOT_VARBIT + numUnlocked + 1, 0)
    }

    fun getTabsItems(p: Player, tab: Int) : List<Item?> {
        var container = p.bank.toMutableList()
        var tabsItems = mutableListOf<Item?>()
        for (currentTab in BANK_TAB_ROOT_VARBIT + 1.. BANK_TAB_ROOT_VARBIT + tab) {
            if (BANK_TAB_ROOT_VARBIT+tab == currentTab) {
                tabsItems = container.take(p.getVarbit(currentTab)).toMutableList()
            } else {
                container = container.drop(p.getVarbit(currentTab)).toMutableList()
            }
        }
        return tabsItems
    }
}