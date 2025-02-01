package org.alter.plugins.content.interfaces.gameframe.tabs.friends_list

import org.alter.api.*
import org.alter.api.cfg.*
import org.alter.api.dsl.*
import org.alter.api.ext.*
import org.alter.game.*
import org.alter.game.model.*
import org.alter.game.model.attr.*
import org.alter.game.model.container.*
import org.alter.game.model.container.key.*
import org.alter.game.model.entity.*
import org.alter.game.model.item.*
import org.alter.game.model.queue.*
import org.alter.game.model.shop.*
import org.alter.game.model.timer.*
import org.alter.game.plugin.*
import org.alter.plugins.content.interfaces.social.Social

class FriendsListPlugin(
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {
        
    init {
        onButton(interfaceId = Social.FRIENDS_LIST_INTERFACE_ID, component = Social.SWITCH_LIST_ID) {
            player.openInterface(interfaceId = Social.IGNORES_LIST_INTERFACE_ID, dest = InterfaceDestination.SOCIAL)
            player.setVarbit(Varbit.FRIEND_FACE_ID_VARBIT, 1)
        }

        onLogin {
            if (player.getInterfaceAt(InterfaceDestination.SOCIAL) == Social.FRIENDS_LIST_INTERFACE_ID) {
                player.setVarbit(Varbit.FRIEND_FACE_ID_VARBIT, 0)
            } else {
                player.setVarbit(Varbit.FRIEND_FACE_ID_VARBIT, 1)
            }
        }
    }
}
