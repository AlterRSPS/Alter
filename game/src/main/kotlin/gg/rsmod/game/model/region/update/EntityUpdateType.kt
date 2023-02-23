package gg.rsmod.game.model.region.update

/**
 *
 * TODO(Tom): externalize the id as it changes every revision
 *
 * @author Tom <rspsmods@gmail.com>
 */
enum class EntityUpdateType(val id: Int) {
    MAP_ANIM(id = 0), // MAP_ANIM
    UNKNOWN(id = 1), // PREFETCH_GAMEOBJECTS
    SPAWN_PROJECTILE(id = 2), //  MAPPROJ_ANIM
    OBJ_DEL_NEW(id = 3),
    UPDATE_GROUND_ITEM(id = 4), // OBJ_COUNT
    ANIMATE_OBJECT(id = 5), // LOC_ANIM
    SPAWN_OBJECT(id = 6), // LOC_ADD_CHANGE
    REMOVE_OBJECT(id = 7), // LOC_DEL
    MAP_ANIM_NEW(id = 8),
    REMOVE_GROUND_ITEM(id = 9), // OBJ_DEL
    SPAWN_GROUND_ITEM(id = 10), // OBJ_ADD
    PLAY_TILE_SOUND(id = 11), // AREA_SOUND
    OBJ_COUNT_2(id = 12)
}