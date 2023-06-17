package gg.rsmod.game.model.region.update

/**
 *
 * TODO(Tom): externalize the id as it changes every revision
 *
 * @author Tom <rspsmods@gmail.com>
 */
enum class EntityUpdateType(val id: Int) {
    REMOVE_OBJECT(id = 0), // LOC_DEL
    PREFETCH_GAMEOBJECTS(id = 1), // PREFETCH_GAMEOBJECTS
    MAP_ANIM(id = 2), // MAP_ANIM
    ANIMATE_OBJECT(id = 3), // LOC_ANIM
    SPAWN_PROJECTILE(id = 4), //  MAPPROJ_ANIM
    SPAWN_OBJECT(id = 5), // LOC_ADD_CHANGE
    PLAY_TILE_SOUND(id = 6), // AREA_SOUND
    LEGACY_REMOVE_GROUND_ITEM(id = 7), // OBJ_DEL_LEGACY
    REMOVE_GROUND_ITEM(id = 8), // OBJ_DEL
    SPAWN_GROUND_ITEM(id = 9), // OBJ_ADD
    UPDATE_GROUND_ITEM(id = 10), // OBJ_COUNT
}