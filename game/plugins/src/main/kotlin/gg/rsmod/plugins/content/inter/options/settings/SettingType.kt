package gg.rsmod.plugins.content.inter.options.settings

enum class SettingType(val typeId : Int) {
    TOGGLE(0),
    SLIDER(1),
    DROPDOWN(2),
    KEYBIND(3),
    INPUT(4),
    HEADER(5),
    BUTTON(6),
    SEE_MORE(7),
    INFO(8),
    COLOUR(9);

    companion object {
        private val VALUES = values()
        fun getByValue(value: Int) = VALUES.firstOrNull { it.typeId == value }
    }
}