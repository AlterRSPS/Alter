package gg.rsmod.game.model.region.update

/**
 *
 * TODO(Tom): externalize the id as it changes every revision
 *
 * @author Tom <rspsmods@gmail.com>
 */
enum class EntityUpdateType(val id: Int) {
    PLAY_TILE_SOUND(id = 0), // AREA_SOUND*
    SPAWN_GROUND_ITEM(id = 1),  // ITEM_ADD
    REMOVE_OBJECT(id = 2), // OBJ_DEL
    SPAWN_PROJECTILE(id = 3), //  MAP_ANIM?
    MAP_ANIM(id = 4), // MAPPROJ_ANIM
    REMOVE_GROUND_ITEM(id = 5), // ITEM_DEL
    UNKNOWN(id = 6), // PREFETCH_GAMEOBJECTS
    SPAWN_OBJECT(id = 7), // OBJ_ADD
    UPDATE_GROUND_ITEM(id = 8), // ITEM_COUNT
    ANIMATE_OBJECT(id = 9); // OBJECT_ANIM
}