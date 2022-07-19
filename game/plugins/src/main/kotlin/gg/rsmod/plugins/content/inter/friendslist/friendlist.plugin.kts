package gg.rsmod.plugins.content.inter.friendslist

import gg.rsmod.plugins.api.ext.*
import java.util.*

    //switch between Friendlist and Ignorelist
    on_button(InterfaceDestination.SOCIAL.interfaceId, Friendlist.SWITCH_LIST_ID){
        if (player.getVarbit(Varbits.FRIEND_FACE_ID_VARBIT) == 0)
            player.openInterface(interfaceId = Friendlist.IGNORELIST_ID, dest = InterfaceDestination.TAB_AREA)
            player.setVarbit(Varbits.FRIEND_FACE_ID_VARBIT, 1)
    }
    on_button(Friendlist.IGNORELIST_ID, Friendlist.SWITCH_LIST_ID){
        if (player.getVarbit(Varbits.FRIEND_FACE_ID_VARBIT) == 1)
            player.openInterface(interfaceId = Friendlist.FRIENDLIST_ID, dest = InterfaceDestination.TAB_AREA)
            player.setVarbit(Varbits.FRIEND_FACE_ID_VARBIT, 0)
    }
    /**
    //Friendlist buttons
    on_button(InterfaceDestination.SOCIAL.interfaceId, Friendlist.ADD_FRIEND_ID){

    }
    on_button(InterfaceDestination.SOCIAL.interfaceId, Friendlist.DELETE_FRIEND_ID){

    }
    //Ignorelist buttons
    on_button(InterfaceDestination.SOCIAL.interfaceId, Friendlist.ADD_IGNORE_ID){

    }
    on_button(InterfaceDestination.SOCIAL.interfaceId, Friendlist.DELETE_IGNORE_ID){

    }
    */

fun SendPrivateMessage()  {

    /**
    - message: gg.rsmod.game.message.impl.IgnoreMessage # TODO: MESSAGE_PRIVATE
    type: VARIABLE_SHORT
    opcode: 84
    ignore: true
     */

}
fun AddFriend(){

    /**
    - message: gg.rsmod.game.message.impl.IgnoreMessage # TODO: FRIENDLIST_ADD
    type: VARIABLE_BYTE
    opcode: 50
    length: -1
    ignore: true
     */

}
fun DeleteFriend()  {


   /**
    - message: gg.rsmod.game.message.impl.IgnoreMessage # TODO: FRIENDLIST_DEL
    type: VARIABLE_BYTE
    opcode: 43
    ignore: true
    */
}

fun AddIgnore()  {

    /**
    - message: gg.rsmod.game.message.impl.IgnoreMessage # TODO: IGNORELIST_ADD
    type: VARIABLE_BYTE
    opcode: 11
    length: -1
    ignore: true
     */

}
fun DeleteIgnore()  {

    /**
    - message: gg.rsmod.game.message.impl.IgnoreMessage # TODO: IGNORELIST_DEL
    type: VARIABLE_BYTE
    opcode: 86
    ignore: true
     */
}




