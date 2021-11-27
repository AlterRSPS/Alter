package gg.rsmod.game.model.region.update

/**
 *
 * TODO(Tom): externalize the id as it changes every revision
 *
 * @author Tom <rspsmods@gmail.com>
 */
enum class EntityUpdateType(val id: Int) {
    MAP_ANIM(id = 0), // MAPPROJ_ANIM
    UPDATE_GROUND_ITEM(id = 1), // ITEM_COUNT
    SPAWN_PROJECTILE(id = 2), //  MAP_ANIM
    SPAWN_OBJECT(id = 3), // OBJ_ADD
    REMOVE_GROUND_ITEM(id = 4), // ITEM_DEL
    ANIMATE_OBJECT(id = 5), // OBJECT_ANIM
    UNKNOWN(id = 6), // PREFETCH_GAMEOBJECTS
    PLAY_TILE_SOUND(id = 7), // AREA_SOUND
    REMOVE_OBJECT(id = 8), // OBJ_DEL
    SPAWN_GROUND_ITEM(id = 9), // ITEM_ADD
}