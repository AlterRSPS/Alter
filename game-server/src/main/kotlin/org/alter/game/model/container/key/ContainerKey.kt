package org.alter.game.model.container.key

import org.alter.game.model.container.ContainerStackType

/**
 * A unique key used for an [org.alter.game.model.container.ItemContainer].
 *
 * @author Tom <rspsmods@gmail.com>
 */
data class ContainerKey(val name: String, val capacity: Int, val stackType: ContainerStackType) {
    override fun equals(other: Any?): Boolean = (other as? ContainerKey)?.name == name

    override fun hashCode(): Int = name.hashCode()
}
