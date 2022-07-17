package gg.rsmod.plugins.content.area.edgeville.handlers

import gg.rsmod.plugins.api.cfg.Items
import gg.rsmod.plugins.content.mechanics.shops.ItemCurrency

/**
 * @author Fritz <frikkipafi@gmail.com>
 */
class VoteCurrency : ItemCurrency(Items.VOTE_TICKET, singularCurrency = "coin", pluralCurrency = "coins")