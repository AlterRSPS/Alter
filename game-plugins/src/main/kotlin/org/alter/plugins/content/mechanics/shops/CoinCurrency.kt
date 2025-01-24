package org.alter.plugins.content.mechanics.shops

import org.alter.rscm.RSCM.getRSCM


/**
 * @author Tom <rspsmods@gmail.com>
 */
class CoinCurrency : ItemCurrency(getRSCM("item.coins_995"), singularCurrency = "coin", pluralCurrency = "coins")
