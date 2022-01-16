package gg.rsmod.plugins.content.objs.bankbooth

import gg.rsmod.plugins.content.inter.bank.openBank

private val BOOTHS = with(Objs) { setOf(
         BANK_BOOTH,
         //BANK_BOOTH_10083,
         //BANK_BOOTH_10355,
         //BANK_BOOTH_10357,
         //BANK_BOOTH_10517,
         //BANK_BOOTH_10527,
         //BANK_BOOTH_10583,
         //BANK_BOOTH_10584,
         //BANK_BOOTH_11338,
         //BANK_BOOTH_12798,
         //BANK_BOOTH_12799,
         //BANK_BOOTH_12800,
         //BANK_BOOTH_12801,
         //BANK_BOOTH_14367,
         //BANK_BOOTH_14368,
         //BANK_BOOTH_16642,
         //BANK_BOOTH_16700,
         //BANK_BOOTH_18491,
         //BANK_BOOTH_20325,
         //BANK_BOOTH_20326,
         //BANK_BOOTH_20327,
         //BANK_BOOTH_20328,
         //BANK_BOOTH_22819,
         //BANK_BOOTH_24101,
         //BANK_BOOTH_24347,
         //BANK_BOOTH_25808,
         //BANK_BOOTH_27254,
         //BANK_BOOTH_27260,
         //BANK_BOOTH_27263,
         //BANK_BOOTH_27265,
         //BANK_BOOTH_27267,
         //BANK_BOOTH_27292,
         //BANK_BOOTH_27718,
         //BANK_BOOTH_27719,
         //BANK_BOOTH_27720,
         //BANK_BOOTH_27721,
         //BANK_BOOTH_28429,
         //BANK_BOOTH_28430,
         //BANK_BOOTH_28431,
         //BANK_BOOTH_28432,
         //BANK_BOOTH_28433,
         //BANK_BOOTH_28546,
         //BANK_BOOTH_28547,
         //BANK_BOOTH_28548,
         //BANK_BOOTH_28549,
         //BANK_BOOTH_32666,
         //BANK_BOOTH_36559,
         //BANK_BOOTH_37959,
         //BANK_BOOTH_39238,
         //BANK_BOOTH_42837
     ) }

BOOTHS.forEach { booth ->
    on_obj_option(obj = booth, option = "bank") {
        player.openBank()
    }

    on_obj_option(obj = booth, option = "collect") {
        open_collect(player)
    }
}

fun open_collect(p: Player) {
    p.setInterfaceUnderlay(color = -1, transparency = -1)
    p.openInterface(interfaceId = 402, dest = InterfaceDestination.MAIN_SCREEN)
}