package org.alter.plugins.content.interfaces.skillguides

val SKILL_ID_VARBIT = 4371
val SUB_SECTION_VARBIT = 4372
val NEW_SKILL_GUIDE_INTERFACE = 860
val NEW_SKILL_GUIDE_INTERFACE_CLOSE_COMPONENT = 4

SkillGuide.values.forEach { guide ->
    on_button(320, guide.child) {
        if (!player.lock.canInterfaceInteract()) {
            return@on_button
        }
        if (player.getVarbit(Varbit.NEW_STYLE_SKILL_GUIDE_INTERFACE) == 0) {
            player.setVarbit(SUB_SECTION_VARBIT, 0)
            player.setVarbit(SKILL_ID_VARBIT, guide.bit)
            player.setInterfaceEvents(interfaceId = 214, component = 25, from = -1, to = -1, setting = 0)
            player.setInterfaceUnderlay(color = -1, transparency = -1)
            player.openInterface(interfaceId = 214, dest = InterfaceDestination.MAIN_SCREEN)
        } else {
            player.openInterface(interfaceId = NEW_SKILL_GUIDE_INTERFACE, dest = InterfaceDestination.MAIN_SCREEN)
            player.runClientScript(1902, 1, 0)
            //player.setInterfaceEvents(interfaceId = 860, component = 8, 0..2000) /* @TODO support setInterfaceEvents w empty InterfaceEvent list. */
            player.setInterfaceEvents(interfaceId = 860, component = 7, 0..200, InterfaceEvent.ClickOp1)
        }
    }
}
on_button(NEW_SKILL_GUIDE_INTERFACE, NEW_SKILL_GUIDE_INTERFACE_CLOSE_COMPONENT) {
    player.closeInterface(InterfaceDestination.MAIN_SCREEN)
}

for (section in 11..24) {
    on_button(214, section) {
        player.setVarbit(SUB_SECTION_VARBIT, section - 11)
    }
}
