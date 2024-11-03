## Uncategorized List
- [x] Implement weighted drop tables
  - [x] For Npc drops
  - [ ] Rollable tables for like `Barrows Boss Reward Chest`
  - [ ] Improve it, currently it's too spread out need to sort it out, and ofc clean up code.
- [ ]  Level ups
  - [ ] Unique texts and Jingles, information about which sound to play in which state is on Wiki. As for msgs it's on Git.
- [x] Configure Unequip tab.
- [ ] Need to figure out how region music are played, as if i mute music, it won't play anymore. (Need to rejoin the region we could setup a event for that.)
- [x] World map
  - [ ] Full Screen mode
- [x] Fix Tournament interface
- [ ] Ghosted ground items (When changing character height level)
- [ ] rename all KotlinPlugin.kt methods from snake_case to camelCase @Grian
- [ ] Currently, mobs don't aggro player if he does not have slayer level.
- [ ] Bring back HTTP-Api Publish: `JavConfig` and `World_List.ws` Also will require us to support multi worlds.
- [ ] Add load balancer aswell as reject invalid opcode income:

On direct http connection:
```xpath
    io.netty.handler.codec.DecoderException: java.lang.IllegalArgumentException: Opcode 71 is not registered.
	at io.netty.handler.codec.ByteToMessageDecoder.callDecode(ByteToMessageDecoder.java:500)
	at io.netty.handler.codec.ByteToMessageDecoder.channelRead(ByteToMessageDecoder.java:290)
	at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:444)
	at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:420)
	at io.netty.channel.AbstractChannelHandlerContext.fireChannelRead(AbstractChannelHandlerContext.java:412)
	at io.netty.handler.timeout.IdleStateHandler.channelRead(IdleStateHandler.java:289)
	at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:442)
	at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:420)
	at io.netty.channel.AbstractChannelHandlerContext.fireChannelRead(AbstractChannelHandlerContext.java:412)
	at io.netty.channel.DefaultChannelPipeline$HeadContext.channelRead(DefaultChannelPipeline.java:1410)
	at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:440)
	at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:420)
	at io.netty.channel.DefaultChannelPipeline.fireChannelRead(DefaultChannelPipeline.java:919)
	at io.netty.channel.nio.AbstractNioByteChannel$NioByteUnsafe.read(AbstractNioByteChannel.java:166)
	at io.netty.channel.nio.NioEventLoop.processSelectedKey(NioEventLoop.java:788)
	at io.netty.channel.nio.NioEventLoop.processSelectedKeysOptimized(NioEventLoop.java:724)
	at io.netty.channel.nio.NioEventLoop.processSelectedKeys(NioEventLoop.java:650)
	at io.netty.channel.nio.NioEventLoop.run(NioEventLoop.java:562)
	at io.netty.util.concurrent.SingleThreadEventExecutor$4.run(SingleThreadEventExecutor.java:997)
	at io.netty.util.internal.ThreadExecutorMap$2.run(ThreadExecutorMap.java:74)
	at io.netty.util.concurrent.FastThreadLocalRunnable.run(FastThreadLocalRunnable.java:30)
	at java.base/java.lang.Thread.run(Thread.java:833)
```
```xpath
Caused by: java.lang.IllegalArgumentException: Opcode 71 is not registered.
	at net.rsprot.protocol.message.codec.incoming.MessageDecoderRepository.getDecoder(MessageDecoderRepository.kt:15)
	at net.rsprot.protocol.api.login.LoginMessageDecoder.decode(LoginMessageDecoder.kt:66)
	at io.netty.handler.codec.ByteToMessageDecoder.decodeRemovalReentryProtection(ByteToMessageDecoder.java:530)
	at io.netty.handler.codec.ByteToMessageDecoder.callDecode(ByteToMessageDecoder.java:469)
	... 21 more
Caused by: java.lang.IllegalArgumentException: Opcode 71 is not registered
```

After logging out:
```xpath
java.net.SocketException: Connection reset
	at java.base/sun.nio.ch.SocketChannelImpl.throwConnectionReset(SocketChannelImpl.java:394)
	at java.base/sun.nio.ch.SocketChannelImpl.read(SocketChannelImpl.java:426)
	at io.netty.buffer.PooledByteBuf.setBytes(PooledByteBuf.java:255)
	at io.netty.buffer.AbstractByteBuf.writeBytes(AbstractByteBuf.java:1132)
	at io.netty.channel.socket.nio.NioSocketChannel.doReadBytes(NioSocketChannel.java:357)
	at io.netty.channel.nio.AbstractNioByteChannel$NioByteUnsafe.read(AbstractNioByteChannel.java:151)
	at io.netty.channel.nio.NioEventLoop.processSelectedKey(NioEventLoop.java:788)
	at io.netty.channel.nio.NioEventLoop.processSelectedKeysOptimized(NioEventLoop.java:724)
	at io.netty.channel.nio.NioEventLoop.processSelectedKeys(NioEventLoop.java:650)
	at io.netty.channel.nio.NioEventLoop.run(NioEventLoop.java:562)
	at io.netty.util.concurrent.SingleThreadEventExecutor$4.run(SingleThreadEventExecutor.java:997)
	at io.netty.util.internal.ThreadExecutorMap$2.run(ThreadExecutorMap.java:74)
	at io.netty.util.concurrent.FastThreadLocalRunnable.run(FastThreadLocalRunnable.java:30)
	at java.base/java.lang.Thread.run(Thread.java:833)
```

# Combat:
- [x] Implement new defence stuff [Elemental weakness / Melee Attack Types]
  - Implemented properties not functionality
- [ ] Npcs without special combat can be configured from `data` folder.
- [ ] Bind death coffin so that in instances we could do "Set_players_death_coffin_loc" <-- If death is within house player appears at House and revives
- 

## Path Finder / Walk wip: 
- [ ] Remove current one completely : We need to start from scratch <-- Should be better.
  - [ ] Implement new path methods with detection
- [ ] Add Clip flags to Npc/Player, flags if they can go above water / objects
- [x] Change current Path Finder to RSMod's 2.0 path finder
  - It's implemented right now but with it came alot of bugs.
  - [ ] Attack range [Basicaly you can attack from anywhere no range limit.]
  - [x] Walking/Running is fucked
  - [ ] Ground loot interacting
  - [ ] Npc interacting
  - [ ] Item on Entity
- [ ] Pawn clipping either Rule-set within pawn or Attribute keys
  - [ ] In some areas Player should be able to Fly/Swim
  - [ ] Some npcs can only be on water meanwhile others can wander trough out objects like fences
  - 
## Encountered bugs:
- [x] Ran into lumby picked up Mind rune and it did not get removed.
- [ ] Uhh so all this time path finder was not the problem it was the MovementQueue :face_palm:
    - [ ] <-- Figure out way for line of sight ex: Grand Exchange nuggets <-- Will be same as attack range
- [ ] Chat is still some times vanishing on login
- [ ] When player kills NPC , he does not get canceled out of combat
- [ ] Handler for `Caused by: java.lang.IllegalStateException: Invalid RSA check '79'. This typically means the RSA in the client does not match up with the server.`


## Check list:
- [ ] GE Clan hall from tile 3185 3481 <-- Compare how it's on osrs

## Termins:
- Entity - either Npc or GameObject or Player
- Special combat - Npcs like `Aberrant Spectre` use coded attacks, meanwhile npc like `Man` can be left out without code just with configurations.

## Enchant plugin check
- Pass Npc/Player dummies to launch <--- Maybe have a task for that so that it would not clog the boot.


## Feature ideas:
- Instead of Kts make it EventBus
  - Ofc need to learn more...
- Swap Items via TOML
  - DBRows etc..
- Train more
- Shields have block sound

# Plugin Extension ideas: 
- onItemCombat(itemID: Int) --> Execute all scripts if player has that item equipped

## Implement:
- (https://github.com/ultraviolet-jordan/noneofyourbusiness/tree/main)[Jordans Level up message collection]

# Unsure need to decide yet:
- [ ] All plugin methods currently contain alot of `_` for example: `on_npc_option` we could just rename all of them to onNpcOption which looks nicer and easier to write.
- [ ] Move npc combat script into `set_combat_def` but still support separate option to assign combat script to npc <-- Maybe even let overriding it or ye unsure yet.




# Just some notes
- https://youtu.be/zPji4wymUk4?si=aZ0iQmpYUn35Zy3o&t=310
- Auto Retalitation while placing cannon