package gg.rsmod.game.model.region.update

/**
 *
 * TODO(Tom): externalize the id as it changes every revision
 *
 * @author Tom <rspsmods@gmail.com>
 */
enum class EntityUpdateType(val id: Int) {
    SPAWN_PROJECTILE(id = 0), //  MAP_ANIM
    UPDATE_GROUND_ITEM(id = 1), // ITEM_COUNT
    ANIMATE_OBJECT(id = 2), // OBJECT_ANIM
    SPAWN_GROUND_ITEM(id = 3), // ITEM_ADD
    REMOVE_OBJECT(id = 4), // OBJ_DEL
    MAP_ANIM(id = 5), // MAPPROJ_ANIM
    UNKNOWN(id = 6), // PREFETCH_GAMEOBJECTS ✔
    PLAY_TILE_SOUND(id = 7), // AREA_SOUND
    REMOVE_GROUND_ITEM(id = 8), // ITEM_DEL
    SPAWN_OBJECT(id = 9), // OBJ_ADD
}