package gg.rsmod.game.model.region.update

/**
 *
 * TODO(Tom): externalize the id as it changes every revision
 *
 * @author Tom <rspsmods@gmail.com>
 */
enum class EntityUpdateType(val id: Int) {
    REMOVE_GROUND_ITEM(id = 5), //
    PLAY_TILE_SOUND(id = 8), // 0
    UNKNOWN(id = 3), // 6
    UPDATE_GROUND_ITEM(id = 7),
    SPAWN_PROJECTILE(id = 9),
    SPAWN_GROUND_ITEM(id = 6), //
    SPAWN_OBJECT(id = 0),
    MAP_ANIM(id = 1),
    REMOVE_OBJECT(id = 2), // 2
    ANIMATE_OBJECT(id = 4);
}