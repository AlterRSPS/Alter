package dev.openrune.cache.filestore.definition.data

import dev.openrune.cache.filestore.definition.Definition

data class OverlayType(
    override var id: Int = -1,
    var rgbColor: Int = 0,
    var secondaryRgbColor: Int = -1,
    var textureId: Int = -1,
    var hideUnderlay: Boolean = true,
    override var inherit: Int = -1
) : Definition {
    var hue: Int = 0
    var saturation: Int = 0
    var lightness: Int = 0
    var otherHue: Int = 0
    var otherSaturation: Int = 0
    var otherLightness: Int = 0

    fun calculateHsl() {
        if (secondaryRgbColor != -1) {
            calculateHsl(secondaryRgbColor)
            otherHue = hue
            otherSaturation = saturation
            otherLightness = lightness
        }
        calculateHsl(rgbColor)
    }

    private fun calculateHsl(var1: Int) {
        val var2 = (var1 shr 16 and 255).toDouble() / 256.0
        val var4 = (var1 shr 8 and 255).toDouble() / 256.0
        val var6 = (var1 and 255).toDouble() / 256.0
        var var8 = var2
        if (var4 < var2) {
            var8 = var4
        }
        if (var6 < var8) {
            var8 = var6
        }
        var var10 = var2
        if (var4 > var2) {
            var10 = var4
        }
        if (var6 > var10) {
            var10 = var6
        }
        var var12 = 0.0
        var var14 = 0.0
        val var16 = (var8 + var10) / 2.0
        if (var10 != var8) {
            if (var16 < 0.5) {
                var14 = (var10 - var8) / (var10 + var8)
            }
            if (var16 >= 0.5) {
                var14 = (var10 - var8) / (2.0 - var10 - var8)
            }
            if (var2 == var10) {
                var12 = (var4 - var6) / (var10 - var8)
            } else if (var4 == var10) {
                var12 = 2.0 + (var6 - var2) / (var10 - var8)
            } else if (var10 == var6) {
                var12 = 4.0 + (var2 - var4) / (var10 - var8)
            }
        }
        var12 /= 6.0
        hue = (256.0 * var12).toInt()
        saturation = (var14 * 256.0).toInt()
        lightness = (var16 * 256.0).toInt()
        if (saturation < 0) {
            saturation = 0
        } else if (saturation > 255) {
            saturation = 255
        }
        if (lightness < 0) {
            lightness = 0
        } else if (lightness > 255) {
            lightness = 255
        }
    }
}