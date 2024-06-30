package org.alter.plugins.content.interfaces.social

on_button(interfaceId = Social.IGNORES_LIST_INTERFACE_ID, component = Social.SWITCH_LIST_ID) {
    player.openInterface(interfaceId = Social.FRIENDS_LIST_INTERFACE_ID, dest = InterfaceDestination.SOCIAL)
    player.setVarbit(Varbit.FRIEND_FACE_ID_VARBIT, 0)
}
