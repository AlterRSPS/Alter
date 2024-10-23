## TODO List
- [x] Implement weighted drop tables
  - [x] For Npc drops
  - [ ] Rollable tables for like `Barrows Boss Reward Chest`
- [x] Implement new defence stuff [Elemental weakness / Melee Attack Types]
  - Implemented properties not functionality
- [ ] Skill level ups logic, for jingles and etc..
- [ ] Clean up AlterRSPS Organization repositories + Update main header
- [x] Configure Unequip tab.
- [ ] Need to figure out how region music are played, as if i mute music, it won't play anymore. (Need to rejoin the region we could setup a event for that.)
- [x] World map
  - [ ] Full Screen mode
- [x] Fix Tournament interface
- [ ] Ghosted ground items (When changing character height level)
- [ ] Npcs that don't have anything special registration from `Data/`
- [ ] Clipping
- [x] Change current Path Finder to RSMod's 2.0 path finder
  - It's implemented right now but withit came alot of bugs.
  - [ ] Attack range [Basicaly you can attack from anywhere no range limit.]
  - [x] Walking/Running is fucked
- [ ] Handler for `Caused by: java.lang.IllegalStateException: Invalid RSA check '79'. This typically means the RSA in the client does not match up with the server.`
- [ ] rename all KotlinPlugin.kt methods from snake_case to camelCase @Grian
- [ ] Add Clip flags to Npc/Player, flags if they can go above water / objects
## Path Finder / Walk wip
- [ ] Remove current one completely : We need to start from scratch <-- Should be better.
  - [ ] Implement new path methods with detection
## Encountered bugs:
- [x] Ran into lumby picked up Mind rune and it did not get removed.
- [ ] Bind death coffen so that in instances we could do "Set_players_death_coffen_loc" <-- If death is within house player appears at House and revives
- 
- [ ] Uhh so all this time path finder was not the problem it was the MovementQueue :face_palm:
    - [ ] <-- Figure out way for line of sight ex: Grand Exchange nuggets <-- Will be same as attack range
##
- Instead of Kts make it EventBus
  - Ofc need to learn more...
- Swap Items via TOML
  - DBRows etc..
- Train more
- Shields have block sound

## Implement:
- (https://github.com/ultraviolet-jordan/noneofyourbusiness/tree/main)[Jordans Level up message collection]

# Unsure need to decide yet:
- [ ] All plugin methods currently contain alot of `_` for example: `on_npc_option` we could just rename all of them to onNpcOption which looks nicer and easier to write.
- [ ] Move npc combat script into `set_combat_def` but still support separate option to assign combat script to npc <-- Maybe even let overriding it or ye unsure yet.











----
|   '
|
|


















# Just some notes
- https://youtu.be/zPji4wymUk4?si=aZ0iQmpYUn35Zy3o&t=310


- Auto Retalitation while placing cannon