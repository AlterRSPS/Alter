package gg.rsmod.game.model.region.update

/**
 *
 * TODO(Tom): externalize the id as it changes every revision
 *
 * @author Tom <rspsmods@gmail.com>
 */
enum class EntityUpdateType(val id: Int) {
    SPAWN_PROJECTILE(id = 2), //  MAPPROJ_ANIM
    UPDATE_GROUND_ITEM(id = 1), // OBJ_COUNT
    ANIMATE_OBJECT(id = 9), // LOC_ANIM
    SPAWN_GROUND_ITEM(id = 8), // OBJ_ADD
    REMOVE_OBJECT(id = 6), // LOC_DEL
    MAP_ANIM(id = 0), // MAP_ANIM
    UNKNOWN(id = 3), // PREFETCH_GAMEOBJECTS ✔
    PLAY_TILE_SOUND(id = 7), // AREA_SOUND
    REMOVE_GROUND_ITEM(id = 5), // OBJ_DEL
    SPAWN_OBJECT(id = 4), // LOC_ADD_CHANGE
}