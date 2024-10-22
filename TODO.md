## TODO List
- [x] Implement weighted drop tables
  - [x] For Npc drops
  - [ ] Rollable tables for like `Barrows Boss Reward Chest`
- [x] Implement new defence stuff [Elemental weakness / Melee Attack Types]
  - [ ] Implemented properties not functionality
- [ ] Skill level ups logic, for jingles and etc..
- [ ] Refactor codebase for Ktlint / Update deprecated methods.
- [ ] Clean up AlterRSPS Organization repositories + Update main header



## Implement:
- (https://github.com/rsmod/rsmod/tree/master/game/pathfinder)[RSMod2.0 Path Finder]
- (https://github.com/ultraviolet-jordan/noneofyourbusiness/tree/main)[Jordans Level up message collection]


# Unsure need to decide yet:
- [ ] All plugin methods currently contain alot of `_` for example: `on_npc_option` we could just rename all of them to onNpcOption which looks nicer and easier to write.
- [ ] Move npc combat script into `set_combat_def` but still support separate option to assign combat script to npc <-- Maybe even let overriding it or ye unsure yet.

