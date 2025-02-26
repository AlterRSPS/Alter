# Alter
[![revision: 228][rev-badge]][patch] [![Discord](https://badgen.net/badge/icon/discord?icon=discord&label)](https://discord.com/invite/sAzCuuwkpN) ![jdk-badge] ![](https://tokei.rs/b1/github/AlterRSPS/Alter)

> [!Warning]
> When you have Intellij Open, navigate to `File` -> `Project Structure` -> And make sure SDK is set to `17 java version 17.x.x`
> It is recommended to install the [rscm-plugin](https://github.com/blurite/rscm-plugin) in IntelliJ for enhanced Entity references support.


**Alter** is a modified version of [RSMod](https://github.com/Tomm0017/rsmod) a highly flexible user-friendly game server for use with the OSRS client. Implemented in a modular way,
the framework allows developers to make and create any sort of plugin they wish without having to modify the core game module.
Due to the plugin capabilities, even owners without programming experience can just have others make plugins for them and simply drop them into the plugins module to be automatically loaded on the next startup!

#### I found a bug, where can I report it?
- You can report them by creating Issue on [GitHub](https://github.com/AlterRSPS/Alter/issues) or in Alter's [Discord Server](https://discord.gg/kdhBuRaduw)

#### Setting up the server:
* Youtube tutorial: https://www.youtube.com/watch?v=2Tu-NTzMbf0

* ### First of all Download:
* Make sure you have [Intellij](https://www.jetbrains.com/idea/download/#section=windows) installed, how to install intellij: [Youtube](https://www.youtube.com/watch?v=t8T5Qwa5d_o)

> [!IMPORTANT]
> The cache and XTEA keys are **NOT** automatically installed when running the `install` task. You must first download the cache using one of these methods:
> 1. Run the `downloadOSRSCache` task to automatically download the correct revision cache and XTEA keys
> 2. Manually download the cache and XTEA keys from OpenRS2 (instructions below)

> [!TIP]
> To manually download the cache and XTEA files:
> * Download the [XTEA keys](https://archive.openrs2.org/caches/oldschool/228/keys.json) and save as `xteas.json` inside the `/data/` directory
> * Download the [cache revision 228](https://archive.openrs2.org/caches/oldschool/228/disk.zip), extract it and place the .dat2 and .idx files in the `/data/cache/` directory

> [!TIP]
> Side note: Recommended to use `Intellij 2024.3.x+` It seems that it's more stable for Kotlin Script than older versions. 

* Secondly open Intellij, (If you already have a project open, either do `File -> Close`, or `File -> New -> Project from Version Control`)
* If you decided to use `Close method` you will see this window, click on `Get from VCS`
  ![tutor1](https://raw.githubusercontent.com/AlterRSPS/Resources/main/docs/resources/ReadMe_Alter/tutor1.png)
* Second paste `https://github.com/AlterRSPS/Alter` in URL Field and hit Clone. </br>
  ![tutor2](https://raw.githubusercontent.com/AlterRSPS/Resources/main/docs/resources/ReadMe_Alter/tuor2.png)
* Hit `Clone` and Let the project fully load.
* Now click on `Gradle` button on right side. </br>
  ![tutor4](https://raw.githubusercontent.com/AlterRSPS/Resources/main/docs/resources/ReadMe_Alter/Tutor4.png)
* Expand `Alter` -> `other` and double click `downloadOSRSCache` (This will download revision 228 cache files and XTEA keys)
  ![tutor5](https://raw.githubusercontent.com/AlterRSPS/Resources/main/docs/resources/ReadMe_Alter/Tutori5.png)
* Wait for the download to complete, then run the `install` task from the same menu
* And now wait until you get this result in your `Terminal` at the bottom:
  ![tutor7](https://raw.githubusercontent.com/AlterRSPS/Resources/main/docs/resources/ReadMe_Alter/tutor7.png)
* Now instead of `Install` task do Run `Gradle -> Alter -> game -> Tasks -> Application - Run`
* Now the server should be running, you should see inside your terminal:
  ![tutor8](https://raw.githubusercontent.com/AlterRSPS/Resources/main/docs/resources/ReadMe_Alter/Alter_Successfully_initialized.png)
* If you only see `Alter Loaded up in x ms.` you messed up somewhere.
* You can connect what ever client you want (ofc it has to follow OSRS protocols.)

> [!TIP]
> As for now, there aren't that many public OSRS clients, would recommend using you can find how to patch it yourself https://github.com/blurite/rsprox/blob/master/PATCHER.md:
> https://github.com/blurite/rsprox

> [!WARNING]
> And stay away from client's like Devious, as they have been caught adding Account Stealer into their client.

* ### Tools:
* [CS2 Editor](https://github.com/blurite/cs2-editor), Big thanks to [Blurite Team](https://github.com/blurite) for maintaining such good tool.
* [Z-Kris's Event Inspector](https://media.z-kris.com/runelite-event-inspector-client.jar) , Want to dump osrs data easily? or see hows what being sent? Use this RL Client it's pretty easy to use.
* That's it for now. If you got something to share feel free to ping me on discord ill inspect it and add it.

## Acknowledgments
- This project uses [OpenRune-FileStore](https://github.com/OpenRune/OpenRune-FileStore) for cache management.
- RsMod1 project founder: [Tomm0017](https://github.com/Tomm0017)
- RsMod2 Route finder: [RouteFinder](https://github.com/rsmod/rsmod/tree/main/engine/routefinder)

### Credits:
* Credits are given out to everyone who helped out with information or contributed in some form to the project. And can be found in: [Here](https://github.com/AlterRSPS)


[patch]: https://oldschool.runescape.wiki/w/Update:Leagues_V:_Raging_Echos_Rewards_Are_Here
[rev-badge]: https://img.shields.io/badge/Revision-228-blueviolet
[license-badge]: https://img.shields.io/badge/license-ISC-informational
[jdk-badge]: https://img.shields.io/badge/JDK-17-blue
