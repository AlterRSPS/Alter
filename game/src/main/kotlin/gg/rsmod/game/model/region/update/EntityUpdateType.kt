package gg.rsmod.game.model.region.update

/**
 *
 * TODO(Tom): externalize the id as it changes every revision
 *
 * @author Tom <rspsmods@gmail.com>
 */
enum class EntityUpdateType(val id: Int) {
    PLAY_TILE_SOUND(id = 0), // AREA_SOUND
    SPAWN_OBJECT(id = 1), // LOC_ADD_CHANGE
    SPAWN_GROUND_ITEM(id = 2), // OBJ_ADD
    ANIMATE_OBJECT(id = 3), // LOC_ANIM
    REMOVE_GROUND_ITEM(id = 4), // OBJ_DEL
    LEGACY_REMOVE_GROUND_ITEM(id = 5), // OBJ_DEL_LEGACY
    UPDATE_GROUND_ITEM(id = 6), // OBJ_COUNT
    PREFETCH_GAMEOBJECTS(id = 7), // PREFETCH_GAMEOBJECTS
    SPAWN_PROJECTILE(id = 8), //  MAPPROJ_ANIM
    REMOVE_OBJECT(id = 9), // LOC_DEL
    MAP_ANIM(id = 10), // MAP_ANIM
}