package org.alter.plugins.content.mechanics.run

onLogin {
    player.timers[RunEnergy.RUN_DRAIN] = 1
}

onTimer(RunEnergy.RUN_DRAIN) {
    player.timers[RunEnergy.RUN_DRAIN] = 1
    RunEnergy.drain(player)
}

/**
 * Button by minimap.
 */
onButton(interfaceId = 160, component = 27) {
    RunEnergy.toggle(player)
}

/**
 * Settings button.
 */
onButton(interfaceId = 116, component = 71) {
    RunEnergy.toggle(player)
}
