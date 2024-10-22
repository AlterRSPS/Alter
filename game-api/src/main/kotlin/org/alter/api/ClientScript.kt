package org.alter.api

import dev.openrune.cache.CacheManager


/**
 * Represents a client-side script, identifiable either by an identifier or directly by an ID.
 * If the `id` parameter is passed as -1, the script ID is looked up using the provided identifier.
 * Identifiers like 'closebutton_leave' can be found in the cs2-scripts repository on GitHub
 * (https://github.com/Joshua-F/cs2-scripts?tab=readme-ov-file), within entries like '28 [clientscript,closebutton_leave]'.
 * Here, 'closebutton_leave' specifically corresponds to a script managing a close button's leave event.
 *
 * @property identifier a unique identifier for the script; used for fetching script ID if `id` is -1
 * @property id the unique numeric ID of the script; if -1, script ID is fetched using an identifier
 * @author Mark7625
 */
class ClientScript(identifier: String = "", id: Int = -1) {

    /**
     * The unique ID of the client script. If not set (-1), it is determined by looking up the identifier.
     * If the identifier lookup fails, it defaults back to -1 indicating that no valid script was found.
     */
    var id: Int = if (id == -1) CacheManager.findScriptId(identifier) else id
        private set
}