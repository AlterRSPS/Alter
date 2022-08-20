package gg.rsmod.plugins.content.inter.social

on_button(interfaceId = Social.FRIENDS_LIST_INTERFACE_ID, component = Social.SWITCH_LIST_ID) {
    player.openInterface(interfaceId = Social.IGNORES_LIST_INTERFACE_ID, dest = InterfaceDestination.SOCIAL)
    player.setVarbit(Varbits.FRIEND_FACE_ID_VARBIT, 1)
}

on_login {
    if (player.getInterfaceAt(InterfaceDestination.SOCIAL) == Social.FRIENDS_LIST_INTERFACE_ID)
        player.setVarbit(Varbits.FRIEND_FACE_ID_VARBIT, 0)
    else
        player.setVarbit(Varbits.FRIEND_FACE_ID_VARBIT, 1)
}