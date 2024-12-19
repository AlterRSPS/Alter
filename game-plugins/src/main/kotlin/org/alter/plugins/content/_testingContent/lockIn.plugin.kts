/**
 * @author CloudS3c 12/18/2024
 */
    
on_command("lock") {
    if (player.isLocked()) {
        player.unlock()
        player.message("You are unlocked.")
    } else {
        player.lock()
        player.message("You are locked")
    }
}