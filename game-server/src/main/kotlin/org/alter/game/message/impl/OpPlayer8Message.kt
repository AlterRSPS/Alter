package org.alter.game.message.impl

import org.alter.game.message.Message

/**
 * @author Triston Plummer ("Dread")
 *
 * Represents the eighth player interaction option
 *
 * @param index The index of the player that the client is interacting with
 */
class OpPlayer8Message(val index: Int) : Message