package org.alter.api

/**
 * Enum class for client-side scripts commonly used in different interfaces.
 * Each enum value corresponds to a specific functionality and provides an identifier
 * to help create [ClientScript] objects easily.
 *
 * @see ClientScript
 */
enum class CommonClientScripts(identifier : String = "", scriptID : Int = -1) {
    FOCUS_TAB("toplevel_sidebutton_switch"),
    WORLD_MAP_TILE("worldmap_transmitdata"),
    COMABT_LEVEL_SUMMARY("summary_sidepanel_combat_level_transmit"),
    SET_TEXT_ALIGN("if_settextalign"),
    CHATBOX_RESET_BACKGROUND("toplevel_chatbox_resetbackground"),
    SET_OPTIONS("objbox_setbuttons"),
    GE_SEARCH_ITEMS("meslayer_mode14"),
    INTERFACE_MENU("menu"),
    CHATBOX_MULTI("chatbox_multi_init"),
    SHOP_INIT("shop_main_init"),
    MAIN_MODAL_BACKGROUND("toplevel_mainmodal_background"),
    MAIN_MODAL_OPEN("toplevel_mainmodal_open"),
    INTERFACE_INV_INIT("interface_inv_init"),
    SKILL_MULTI_SETUP("skillmulti_setup"),
    INTRO_MUSIC_RESTORE("league_3_intro_music_restore"),
    GE_OFFER_SET_DESC("ge_offers_setdesc"),
    LOOTING_BAG_SETUP("wilderness_lootingbag_setup"),
    TIME_PLAYED("TimePlayed", 3970)
    ;

    val script : ClientScript = ClientScript(identifier, scriptID)
}