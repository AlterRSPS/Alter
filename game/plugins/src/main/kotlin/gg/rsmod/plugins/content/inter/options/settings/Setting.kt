package gg.rsmod.plugins.content.inter.options.settings

import gg.rsmod.game.fs.StructDefinitions

class Setting(private val struct : StructDefinitions, position : Int) {
    val IS_DESKTOP_PARAM = 739;
    val IS_MOBILE_PARAM = 740;
    val IS_NON_IRONMAN_PARAM = 741;
    val IS_IRONMAN_PARAM = 742;
    val ID_PARAM = 1077;
    val TYPE_PARAM = 1078;
    val PRE_REQUIREMENTS_ENUM_ID_PARAM = 1080;
    val PRE_REQUIREMENTS_VALUES_ENUM_ID_PARAM = 1081;
    val PRE_REQUIREMENTS_INVERSED_ENUM_ID_PARAM = 1082;
    val PRE_REQUIREMENTS_INVERSED_VALUES_ENUM_ID_PARAM = 1083;
    val NAME_PARAM = 1086;
    val SEARCH_KEYWORDS_PARAM = 1088;
    val DROPDOWN_ENTRIES_PARAM = 1091;
    val DROPDOWN_ENTRIES_MOBILE_PARAM = 1092;
    val SLIDER_NOTCH_COUNT_PARAM = 1101;
    val IS_SLIDER_TRANSMITTED_PARAM = 1105;
    val HAS_CUSTOM_REQUIREMENTS_PARAM = 1115;
    val HAS_TOGGLE_INVERSED_PARAM = 1084;
    val CHOOSE_TRANSMIT_PARAM = 1085;
    val MOBILE_NAME_PARAM = 1087;
    val DESCRIPTION_PARAM = 1096;
    val KEYBIND_SPITE_PARAM = 1098;
    val KEYBIND_SPRITE_SIZE_COORDGRID_PARAM = 1099;
    val SLIDER_SECTORS_PARAM = 1102;
    val SLIDER_SECTOR_TEXT_ENUM_ID_PARAM = 1103;
    val SLIDER_CUSTOM_ON_OP_PARAM = 1106;
    val SLIDER_CUSTOM_SETPOS_PARAM = 1107;
    val IS_SLIDER_DRAGGABLE_PARAM = 1108;
    val SLIDER_DEADZONE_PARAM = 1109;
    val SLIDER_DEADTIME_PARAM = 1110;
    val SLIDER_INPUT_SINGULAR_PARAM = 1111;
    val SLIDER_INPUT_PLURAL_PARAM = 1112;
    val SLIDER_INPUT_ZERO_PARAM = 1113;
    val SLIDER_OP_CHECKER_MESSAGE_PARAM = 1116;
    val SLIDER_MOBILE_OP_CHECKER_MESSAGE_PARAM = 1117;
    val HAS_COLLAPSIBLE_INFOBOX_PARAM = 1118;
    val HIDE_DESCRIPTION_PARAM = 1119;
    val IS_ENHANCED_CLIENT_PARAM = 1157;
    val CUSTOM_NAME_EXTRA_TEXT_PARAM = 1158;
    val IS_MOBILE_ALWAYS_ENABLED_PARAM = 1186;
    val HAS_CUSTOM_CHECK_PARAM = 1229;
    val DEFAULT_COLOUR_PARAM = 1230;
    val IS_NON_DESKTOP_ONLY_PARAM = 1271
    val IS_LEAGUE_WORLD_ONLY_PARAM = 1272
    val IS_LEAGUE_ENHANCED_CLIENT_ONLY_PARAM = 1273

    private val position: Int = position
    private val structId : Int = struct.getId()
    private val id : Int? = struct.getParamAsInt(ID_PARAM)
    private val typeId : Int? = struct.getParamAsInt(TYPE_PARAM)
    private val name : String? = struct.getParamAsString(NAME_PARAM)
    private val searchKeywords : String? = struct.getParamAsString(SEARCH_KEYWORDS_PARAM)
    private val sliderTransmitted : Boolean = struct.getParamAsBoolean(IS_SLIDER_TRANSMITTED_PARAM)
    private val sliderNotchCount : Int? = struct.getParamAsInt(SLIDER_NOTCH_COUNT_PARAM)
    private val desktop : Boolean = struct.getParamAsBoolean(IS_DESKTOP_PARAM)
    private val mobile : Boolean = struct.getParamAsBoolean(IS_MOBILE_PARAM)
    private val ironman : Boolean = struct.getParamAsBoolean(IS_IRONMAN_PARAM)
    private val nonIronman : Boolean = struct.getParamAsBoolean(IS_NON_IRONMAN_PARAM)
    private val hasCustomRequirements : Boolean = struct.getParamAsBoolean(HAS_CUSTOM_REQUIREMENTS_PARAM)
    private val preRequirementsEnumId : Int? = struct.getParamAsInt(PRE_REQUIREMENTS_ENUM_ID_PARAM)
    private val preRequirementsValuesEnumId : Int? = struct.getParamAsInt(PRE_REQUIREMENTS_VALUES_ENUM_ID_PARAM);
    private val inversedPreRequirementsEnumId : Int? = struct.getParamAsInt(PRE_REQUIREMENTS_INVERSED_ENUM_ID_PARAM);
    private val inversedPreRequirementsValuesEnumId : Int? = struct.getParamAsInt(PRE_REQUIREMENTS_INVERSED_VALUES_ENUM_ID_PARAM);
    private val toggleInversed : Boolean = struct.getParamAsBoolean(HAS_TOGGLE_INVERSED_PARAM);
    private val chooseTransmit : Boolean = struct.getParamAsBoolean(CHOOSE_TRANSMIT_PARAM);
    private val mobileName : String? = struct.getParamAsString(MOBILE_NAME_PARAM);
    private val description : String? = struct.getParamAsString(DESCRIPTION_PARAM);
    private val keyBindSprite : Int? = struct.getParamAsInt(KEYBIND_SPITE_PARAM);
    private val keyBindSpriteCoordGrid : Int? = struct.getParamAsInt(KEYBIND_SPRITE_SIZE_COORDGRID_PARAM);
    private val sliderSectors: Int? = struct.getParamAsInt(SLIDER_SECTORS_PARAM);
    private val sliderSectorsTextEnumId: Int? = struct.getParamAsInt(SLIDER_SECTOR_TEXT_ENUM_ID_PARAM);
    private val sliderCustomOnOpScript: Boolean = struct.getParamAsBoolean(SLIDER_CUSTOM_ON_OP_PARAM);
    private val sliderCustomSetPos : Boolean = struct.getParamAsBoolean(SLIDER_CUSTOM_SETPOS_PARAM);
    private val sliderDraggable : Boolean = struct.getParamAsBoolean(IS_SLIDER_DRAGGABLE_PARAM);
    private val sliderDeadZone : Int? = struct.getParamAsInt(SLIDER_DEADZONE_PARAM);
    private val sliderDeadTime : Int? = struct.getParamAsInt(SLIDER_DEADTIME_PARAM);
    private val inputSingular : String? = struct.getParamAsString(SLIDER_INPUT_SINGULAR_PARAM);
    private val inputPlural : String? = struct.getParamAsString(SLIDER_INPUT_PLURAL_PARAM);
    private val inputZero : String? = struct.getParamAsString(SLIDER_INPUT_ZERO_PARAM);
    private val opCheckerMessage : String? = struct.getParamAsString(SLIDER_OP_CHECKER_MESSAGE_PARAM);
    private val mobileOpCheckerMessage : String? = struct.getParamAsString(SLIDER_MOBILE_OP_CHECKER_MESSAGE_PARAM);
    private val collapsibleInfobox : Boolean = struct.getParamAsBoolean(HAS_COLLAPSIBLE_INFOBOX_PARAM);
    private val hideDescription : Boolean = struct.getParamAsBoolean(HIDE_DESCRIPTION_PARAM);
    private val enhancedClientOnly : Boolean = struct.getParamAsBoolean(IS_ENHANCED_CLIENT_PARAM);
    private val customNameExtraText : Boolean = struct.getParamAsBoolean(CUSTOM_NAME_EXTRA_TEXT_PARAM);
    private val mobileAlwaysEnabled : Boolean = struct.getParamAsBoolean(IS_MOBILE_ALWAYS_ENABLED_PARAM);
    private val hasCustomCheck : Boolean = struct.getParamAsBoolean(HAS_CUSTOM_CHECK_PARAM);
    private val defaultColour : Int? = struct.getParamAsInt(DEFAULT_COLOUR_PARAM);
    private val nonDesktopOnly : Boolean = struct.getParamAsBoolean(IS_NON_DESKTOP_ONLY_PARAM);
    private val leagueWorldOnly : Boolean = struct.getParamAsBoolean(IS_LEAGUE_WORLD_ONLY_PARAM);
    private val leagueWorldEnhancedClientOnly : Boolean = struct.getParamAsBoolean(IS_LEAGUE_ENHANCED_CLIENT_ONLY_PARAM);
    private val dropdownEntriesEnumId : Int? = struct.getParamAsInt(DROPDOWN_ENTRIES_PARAM);
    private val mobileDropDownEntriesEnumId : Int? = struct.getParamAsInt(DROPDOWN_ENTRIES_MOBILE_PARAM);

    private val type : SettingType? = this.typeId?.let { SettingType.getByValue(it) }

    fun getPosition() : Int {
        return position
    }

    fun getStructId() : Int {
        return structId
    }

    fun getId() : Int? {
        return id
    }

    fun getTypeId() : Int? {
        return typeId
    }

    fun getType() : SettingType? {
        return this.type
    }

    fun getName() : String? {
        return name
    }

    fun isDesktop() : Boolean {
        return desktop
    }

    fun isMobile() : Boolean {
        return mobile
    }

    fun isIronman() : Boolean {
        return ironman
    }

    fun isNonIronman() : Boolean {
        return nonIronman
    }
}