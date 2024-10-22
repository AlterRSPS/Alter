package org.alter.api.interf

sealed class InterfaceRenderMode(var colour: Int, var opacity: Int)
data object Default : InterfaceRenderMode(-1, -1)
data object VerticalStretch : InterfaceRenderMode(-1, -1)
data object FullStrech : InterfaceRenderMode(-1, -1)