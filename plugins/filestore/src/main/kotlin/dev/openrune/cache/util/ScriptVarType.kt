package dev.openrune.cache.util
/*
 * Copyright (c) 2018, Joshua Filby <joshua@filby.me>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
enum class ScriptVarType(
    /**
     * The type id when encoding or decoding types from some data structures.
     */
    val id: Int,
    /**
     * The character used when encoding or decoding types.
     */
    val keyChar: Char,
    /**
     * The full name of the var type.
     */
    val fullName: String
) {
    // Primitive Types
    INTEGER(0, 'i', "integer"),
    BOOLEAN(1, '1', "boolean"),
    STRING(36, 's', "string"),
    LONG(110, 'Ï', "long"),

    // Numeric Types
    HASH32(2, '2', "hash32"),
    QUEST(3, ':', "quest"),
    QUESTHELP(4, ';', "questhelp"),
    CURSOR(5, '@', "cursor"),
    SEQ(6, 'A', "seq"),
    COLOUR(7, 'C', "colour"),
    LOC_SHAPE(8, 'H', "locshape"),
    COMPONENT(9, 'I', "component"),
    IDKIT(10, 'K', "idkit"),
    MIDI(11, 'M', "midi"),
    NPC_MODE(12, 'N', "npc_mode"),
    NAMEDOBJ(13, 'O', "namedobj"),
    SYNTH(14, 'P', "synth"),
    AI_QUEUE(15, 'Q', "ai_queue"),
    AREA(16, 'R', "area"),
    STAT(17, 'S', "stat"),
    NPC_STAT(18, 'T', "npc_stat"),
    WRITEINV(19, 'V', "writeinv"),
    MESH(20, '^', "mesh"),
    MAPAREA(21, '`', "maparea"),
    COORDGRID(22, 'c', "coordgrid"),
    GRAPHIC(23, 'd', "graphic"),
    CHATPHRASE(24, 'e', "chatphrase"),
    FONTMETRICS(25, 'f', "fontmetrics"),
    ENUM(26, 'g', "enum"),
    HUNT(27, 'h', "hunt"),
    JINGLE(28, 'j', "jingle"),
    CHATCAT(29, 'k', "chatcat"),
    LOC(30, 'l', "loc"),
    MODEL(31, 'm', "model"),
    NPC(32, 'n', "npc"),
    OBJ(33, 'o', "obj"),
    PLAYER_UID(34, 'p', "player_uid"),
    REGION_UID(35, 'r', "region_uid"),
    SPOTANIM(37, 't', "spotanim"),
    NPC_UID(38, 'u', "npc_uid"),
    INV(39, 'v', "inv"),
    TEXTURE(40, 'x', "texture"),
    CATEGORY(41, 'y', "category"),
    CHAR(42, 'z', "char"),
    LASER(43, '|', "laser"),
    BAS(44, '€', "bas"),
    CONTROLLER(45, 'ƒ', "controller"),
    COLLISION_GEOMETRY(46, '‡', "collision_geometry"),
    PHYSICS_MODEL(47, '‰', "physics_model"),
    PHYSICS_CONTROL_MODIFIER(48, 'Š', "physics_control_modifier"),
    CLANHASH(49, 'Œ', "clanhash"),
    CUTSCENE(51, 'š', "cutscene"),
    ITEMCODE(53, '¡', "itemcode"),
    PVPKILLS(54, '¢', "pvp_kills"),
    MAPSCENEICON(55, '£', "mapsceneicon"),
    CLANFORUMQFC(56, '§', "clanforumqfc"),
    VORBIS(57, '«', "vorbis"),
    VERIFY_OBJECT(58, '®', "verify_object"),
    MAPELEMENT(59, 'µ', "mapelement"),
    CATEGORYTYPE(60, '¶', "categorytype"),
    SOCIAL_NETWORK(61, 'Æ', "social_network"),
    HITMARK(62, '×', "hitmark"),
    PACKAGE(63, 'Þ', "package"),
    PARTICLE_EFFECTOR(64, 'á', "particle_effector"),
    CONTROLLER_UID(65, 'æ', "controller_uid"),
    PARTICLE_EMITTER(66, 'é', "particle_emitter"),
    PLOGTYPE(67, 'í', "plogtype"),
    UNSIGNED_INT(68, 'î', "unsigned_int"),
    SKYBOX(69, 'ó', "skybox"),
    SKYDECOR(70, 'ú', "skydecor"),
    HASH64(71, 'û', "hash64"),
    INPUTTYPE(72, 'Î', "inputtype"),
    STRUCT(73, 'J', "struct"),
    DBROW(74, 'Ð', "dbrow"),
    STORABLELABEL(75, '¤', "storablelabel"),
    STORABLEPROC(76, '¥', "storableproc"),
    GAMELOGEVENT(77, 'è', "gamelogevent"),
    ANIMATIONCLIP(78, '¹', "animationclip"),
    SKELETON(79, '°', "skeleton"),
    REGIONVISIBILITY(80, 'ì', "region_visibility"),
    FMODHANDLE(81, 'ë', "fmodhandle"),
    REGION_ALLOWLOGIN(83, 'þ', "region_allowlogin"),
    REGION_INFO(84, 'ý', "region_info"),
    REGION_INFO_FAILURE(85, 'ÿ', "region_info_failure"),
    SERVER_ACCOUNT_CREATION_STEP(86, 'õ', "server_account_creation_step"),
    CLIENT_ACCOUNT_CREATION_STEP(87, 'ô', "client_account_creation_step"),
    LOBBY_ACCOUNT_CREATION_STEP(88, 'ö', "lobby_account_creation_step"),
    GWC_PLATFORM(89, 'ò', "gwc_platform"),
    CURRENCY(90, 'Ü', "currency"),
    KEYBOARD_KEY(91, 'ù', "keyboard_key"),
    MOUSEEVENT(92, 'ï', "mouseevent"),
    HEADBAR(93, '¯', "headbar"),
    BUG_TEMPLATE(94, 'ê', "bug_template"),
    BILLING_AUTH_FLAG(95, 'ð', "billing_auth_flag"),
    ACCOUNT_FEATURE_FLAG(96, 'å', "account_feature_flag"),
    INTERFACE(97, 'a', "interface"),
    TOPLEVELINTERFACE(98, 'F', "toplevelinterface"),
    OVERLAYINTERFACE(99, 'L', "overlayinterface"),
    CLIENTINTERFACE(100, '©', "clientinterface"),
    MOVESPEED(101, 'Ý', "movespeed"),
    MATERIAL(102, '¬', "material"),
    SEQGROUP(103, 'ø', "seqgroup"),
    TEMP_HISCORE(104, 'ä', "temp_hiscore"),
    TEMP_HISCORE_LENGTH_TYPE(105, 'ã', "temp_hiscore_length_type"),
    TEMP_HISCORE_DISPLAY_TYPE(106, 'â', "temp_hiscore_display_type"),
    TEMP_HISCORE_CONTRIBUTE_RESULT(107, 'à', "temp_hiscore_contribute_result"),
    AUDIOGROUP(108, 'À', "audiogroup"),
    AUDIOMIXBUSS(109, 'Ò', "audiomixbuss"),

    // Special Types
    TYPE_SPECIAL_1(-1, '#', "special_1"),
    TYPE_SPECIAL_2(-1, '(', "special_2"),
    TYPE_SPECIAL_3(-1, '%', "special_3"),
    TYPE_SPECIAL_4(-1, '&', "special_4"),
    TYPE_SPECIAL_5(-1, ')', "special_5"),
    TYPE_SPECIAL_6(-1, '3', "special_6"),
    TYPE_SPECIAL_7(-1, '5', "special_7"),
    TYPE_SPECIAL_8(-1, '7', "special_8"),
    TYPE_SPECIAL_9(-1, '8', "special_9"),
    TYPE_SPECIAL_10(-1, '9', "special_10"),
    TYPE_SPECIAL_11(-1, 'D', "special_11"),
    TYPE_SPECIAL_12(-1, 'G', "special_12"),
    TYPE_SPECIAL_13(-1, 'U', "special_13"),
    TYPE_SPECIAL_14(-1, 'Á', "special_14"),
    TYPE_SPECIAL_15(-1, 'Z', "special_15"),
    TYPE_SPECIAL_16(-1, '~', "special_16"),
    TYPE_SPECIAL_17(-1, '±', "special_17"),
    TYPE_SPECIAL_18(-1, '»', "special_18"),
    TYPE_SPECIAL_19(-1, '¿', "special_19"),
    TYPE_SPECIAL_20(-1, 'Ç', "special_20"),
    TYPE_SPECIAL_21(-1, 'Ñ', "special_21"),
    TYPE_SPECIAL_22(-1, 'ñ', "special_22"),
    TYPE_SPECIAL_23(-1, 'Ù', "special_23"),
    TYPE_SPECIAL_24(-1, 'ß', "special_24"),
    TYPE_SPECIAL_25(-1, 'E', "special_25"),
    TRANSMIT_LIST(-1, 'Y', "transmit_list"),
    TYPE_SPECIAL_27(-1, 'Ä', "special_27"),
    TYPE_SPECIAL_28(-1, 'ü', "special_28"),
    TYPE_SPECIAL_29(-1, 'Ú', "special_29"),
    TYPE_SPECIAL_30(-1, 'Û', "special_30"),
    TYPE_SPECIAL_31(-1, 'Ó', "special_31"),
    TYPE_SPECIAL_32(-1, 'È', "special_32"),
    TYPE_SPECIAL_33(-1, 'Ô', "special_33"),
    TYPE_SPECIAL_34(-1, '¾', "special_34"),
    TYPE_SPECIAL_35(-1, 'Ö', "special_35"),
    TYPE_SPECIAL_36(-1, '³', "special_36"),
    TYPE_SPECIAL_37(-1, '·', "special_37"),
    TYPE_SPECIAL_38(-1, 0.toChar(), "special_38"),
    TYPE_SPECIAL_39(-1, 0.toChar(), "special_39"),
    TYPE_SPECIAL_40(-1, 0.toChar(), "special_40"),
    TYPE_SPECIAL_41(-1, 'º', "special_41"),
    TYPE_SPECIAL_42(-1, 0.toChar(), "special_42"),
    TYPE_SPECIAL_43(-1, 0.toChar(), "special_43"),
    TYPE_SPECIAL_44(-1, 0.toChar(), "special_44"),
    TYPE_SPECIAL_45(-1, 0.toChar(), "special_45"),
    TYPE_SPECIAL_46(-1, '!', "special_46"),
    TYPE_SPECIAL_47(-1, '$', "special_47"),
    TYPE_SPECIAL_48(-1, '?', "special_48"),
    TYPE_SPECIAL_49(-1, 'ç', "special_49"),
    TYPE_SPECIAL_50(-1, '*', "special_50"),

    // Unknown Types
    STRINGVECTOR(-1, '¸', "stringvector"),
    MESANIM(-1, 0.toChar(), "mesanim"),
    UNDERLAY(-1, 0.toChar(), "underlay"),
    OVERLAY(-1, 0.toChar(), "overlay"),
    WORLD_AREA(-1, 0.toChar(), "world_area"),

    // Special
    TYPE(-1, 0.toChar(), "type"),
    BASEVARTYPE(-1, 0.toChar(), "basevartype"),
    PARAM(-1, 0.toChar(), "param"),
    CLIENTSCRIPT(-1, 0.toChar(), "clientscript"),
    ONSHIFTCLICKNPC(-1, 0.toChar(), "onshiftclicknpc"),
    ONSHIFTCLICKLOC(-1, 0.toChar(), "onshiftclickloc"),
    ONSHIFTCLICKOBJ(-1, 0.toChar(), "onshiftclickobj"),
    ONSHIFTCLICKPLAYER(-1, 0.toChar(), "onshiftclickplayer"),
    ONSHIFTCLICKTILE(-1, 0.toChar(), "onshiftclicktile"),
    DBCOLUMN(-1, 0.toChar(), "dbcolumn"),
    VAR_PLAYER(-1, 0.toChar(), "var_player"),
    VAR_PLAYER_BIT(-1, 0.toChar(), "var_player_bit"),
    VAR_CLIENT(-1, 0.toChar(), "var_client"),
    VAR_CLIENT_STRING(-1, 0.toChar(), "var_client_string"),
    VAR_CLAN_SETTING(-1, 0.toChar(), "var_clan_setting"),
    VAR_CLAN(-1, 0.toChar(), "var_clan"),
    VAR_CONTROLLER(-1, 0.toChar(), "var_controller"),
    VAR_CONTROLLER_BIT(-1, 0.toChar(), "var_controller_bit"),
    VAR_GLOBAL(-1, 0.toChar(), "var_global"),
    VAR_NPC(-1, 0.toChar(), "var_npc"),
    VAR_NPC_BIT(-1, 0.toChar(), "var_npc_bit"),
    VAR_OBJ(-1, 0.toChar(), "var_obj"),
    VAR_SHARED(-1, 0.toChar(), "var_shared"),
    VAR_SHARED_STRING(-1, 0.toChar(), "var_shared_string"),

    // Fake Subtypes
    INT_INT(-1, 0.toChar(), "int_int"),
    INT_BOOLEAN(-1, 0.toChar(), "int_boolean"),
    INT_CHATFILTER(-1, 0.toChar(), "int_chatfilter"),
    INT_CHATTYPE(-1, 0.toChar(), "int_chattype"),
    INT_CLIENTTYPE(-1, 0.toChar(), "int_clienttype"),
    INT_PLATFORMTYPE(-1, 0.toChar(), "int_platformtype"),
    INT_IFTYPE(-1, 0.toChar(), "int_iftype"),
    INT_KEY(-1, 0.toChar(), "int_key"),
    INT_SETPOSH(-1, 0.toChar(), "int_setposh"),
    INT_SETPOSV(-1, 0.toChar(), "int_setposv"),
    INT_SETSIZE(-1, 0.toChar(), "int_setsize"),
    INT_SETTEXTALIGNH(-1, 0.toChar(), "int_settextalignh"),
    INT_SETTEXTALIGNV(-1, 0.toChar(), "int_settextalignv"),
    INT_WINDOWMODE(-1, 0.toChar(), "int_windowmode"),
    INT_GAMEOPTION(-1, 0.toChar(), "int_gameoption"),
    INT_DEVICEOPTION(-1, 0.toChar(), "int_deviceoption"),
    INT_MENUENTRYTYPE(-1, 0.toChar(), "int_menuentrytype"),
    INT_GRADIENTMODE(-1, 0.toChar(), "int_gradientmode"),
    INT_OBJOWNER(-1, 0.toChar(), "int_objowner"),
    INT_RGB(-1, 0.toChar(), "int_rgb"),
    INT_OPKIND(-1, 0.toChar(), "int_opkind"),
    INT_OPMODE(-1, 0.toChar(), "int_opmode"),

    // For Decompiler
    HOOK(-1, 0.toChar(), "hook"),
    UNKNOWN(-1, 0.toChar(), "unknown"),
    UNKNOWN_INT(-1, 0.toChar(), "unknown_int"),
    UNKNOWN_INT_NOTBOOLEAN(-1, 0.toChar(), "unknown_int_notboolean"),
    UNKNOWN_INT_NOTINT(-1, 0.toChar(), "unknown_int_notint"),
    UNKNOWN_INT_NOTINT_NOTBOOLEAN(-1, 0.toChar(), "unknown_int_notint_notboolean"),
    CONDITION(-1, 0.toChar(), "condition"),
    VARP(209, 7.toChar(), "integer")
    ;

    companion object {
        private val idToTypeMap: MutableMap<Int, ScriptVarType> = HashMap()
        private val keyToTypeMap: MutableMap<Char, ScriptVarType> = HashMap()

        init {
            for (type in values()) {
                if (type.id != -1) {
                    idToTypeMap[type.id] = type
                }
                keyToTypeMap[type.keyChar] = type
            }
        }

        fun forId(id: Int): ScriptVarType? {
            return idToTypeMap[id]
        }

        fun forCharKey(key: Char): ScriptVarType? {
            return keyToTypeMap[key]
        }
    }
}