package org.alter.plugins.content.objects.bookcase

private val BOOKCASES = setOf("object.bookcase_380", "object.bookcase_381")

BOOKCASES.forEach { case ->
    onObjOption(obj = case, option = "search") {
        player.queue {
            search(this, player)
        }
    }
}

suspend fun search(
    it: QueueTask,
    p: Player,
) {
    p.lock()
    p.message("You search the books...")
    it.wait(3)
    p.unlock()
    p.message("You don't find anything that you'd ever want to read.")
}
