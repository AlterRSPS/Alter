package gg.rsmod.plugins.content.area.edgeville.npcs

import gg.rsmod.game.model.queue.QueueTask
import gg.rsmod.plugins.api.ext.chatNpc
import gg.rsmod.plugins.api.ext.message
import gg.rsmod.plugins.api.ext.options
import gg.rsmod.plugins.api.ext.player

/**
 * @author Fritz <frikkipafi@gmail.com>
 */
class NpcPlaceHolder {

    suspend fun dialog(it: QueueTask) {
        it.chatNpc("<br>Good day<br>How can i help you", title = "Placeholder")
    }

}