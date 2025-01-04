package org.alter.plugins.content._testingContent.olds

/**
 * @author CloudS3c 12/18/2024
 */
    
onCommand("lock") {
    if (player.isLocked()) {
        player.unlock()
        player.message("You are unlocked.")
    } else {
        player.lock()
        player.message("You are locked")
    }
}