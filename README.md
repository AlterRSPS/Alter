# Alter
[![revision: 213][rev-badge]][patch] [![Discord](https://badgen.net/badge/icon/discord?icon=discord&label)](https://discord.com/invite/sAzCuuwkpN) ![jdk-badge] ![](https://tokei.rs/b1/github/AlterRSPS/Alter)

**Alter** is a modified version of [RSMod](https://github.com/Tomm0017/rsmod) a highly flexible user-friendly game server for use with the OSRS client. Implemented in a modular way,
the framework allows developers to make and create any sort of plugin they wish without having to modify the core game module.
Due to the plugin capabilities, even owners without programming experience can just have others make plugins for them and simply drop them into the plugins module to be automatically loaded on the next startup!

#### I found a bug, where can I report it?
- You can report them by creating Issue on [GitHub](https://github.com/AlterRSPS/Alter/issues) or in Alter's [Discord Server](https://discord.gg/kdhBuRaduw)

#### Setting up the server:
* ### First of all Download:
* [2023-05-03-rev213.tar.gz](https://archive.runestats.com/osrs/2023-05-03-rev213.tar.gz) , And make sure you're using correct Java Version.
* Also make sure you have [Intellij](https://www.jetbrains.com/idea/download/#section=windows) installed, how to install intellij: [Youtube](https://www.youtube.com/watch?v=t8T5Qwa5d_o).
* Secondly open Intellij, (If you already have a project open, either do `File -> Close`, or `File -> New -> Project from Version Control`)
* If you decided to use `Close method` you will see this window, click on `Get from VCS`
  ![tutor1](https://raw.githubusercontent.com/AlterRSPS/Resources/main/docs/resources/ReadMe_Alter/tutor1.png)
* Second paste `https://github.com/AlterRSPS/Alter` in URL Field and hit Clone. </br>
  ![tutor2](https://raw.githubusercontent.com/AlterRSPS/Resources/main/docs/resources/ReadMe_Alter/tuor2.png)
* Hit `Clone` and Let the project fully load.
* Now open the `2023-05-03-rev213.tar.gz` file that you downloaded. and drag out Xteas.json and Cache folder and pull it to `/Data/` Folder.
  ![tutor6](https://raw.githubusercontent.com/AlterRSPS/Resources/main/docs/resources/ReadMe_Alter/tutor6.png)
- Xtea.json has to be in same folder like: Data /`api.yml` , `blocks.yml` , etc..
* Now click on `Gradle` button on right side. </br>
  ![tutor4](https://raw.githubusercontent.com/AlterRSPS/Resources/main/docs/resources/ReadMe_Alter/Tutor4.png)
* Expand `gg.rsmod` -> `other` and double click `install`
  ![tutor5](https://raw.githubusercontent.com/AlterRSPS/Resources/main/docs/resources/ReadMe_Alter/Tutori5.png)
* And now wait until you get this result in your `Terminal` at the bottom:
  ![tutor7](https://raw.githubusercontent.com/AlterRSPS/Resources/main/docs/resources/ReadMe_Alter/tutor7.png)
* Now instead of `Install` task do Run `Gradle -> gg.rsmod -> game -> Tasks -> Application - Run`
* Now the server should be running, you should see inside your terminal:
  ![tutor8](https://raw.githubusercontent.com/AlterRSPS/Resources/main/docs/resources/ReadMe_Alter/Alter_Successfully_initialized.png)
* If you only see `Alter Loaded up in x ms.` you messed up somewhere.
* You can connect what ever client you want (ofc it has to follow OSRS protocols.)
* ### Some clients that can be used to connected to Alter Server:
* [MeteorLite](https://github.com/MeteorLite/meteor-client)
  * Simply run it, and you will have Private Server Plugin which is pretty easy to use.
  * If you will need help with it, come to their discord server [MeteorLiteDiscord](https://discord.com/invite/Y85d6NH6z3)

* ### Tools:
* [CS2 Editor](https://github.com/blurite/cs2-editor), Big thanks to [Blurite Team](https://github.com/blurite) for maintaining such good tool.
* [Z-Kris's Event Inspector](https://media.z-kris.com/runelite-event-inspector-client.jar) , Want to dump osrs data easily? or see hows what being sent? Use this RL Client it's pretty easy to use.
* That's it for now. If you got something to share feel free to ping me on discord ill inspect it and add it.

### Credits:
* Credits are given out to everyone who helped out with information or contributed in some form to the project. And can be found in: [Here](https://github.com/AlterRSPS)

### Some info:
* Original project founder: [Tomm0017](https://github.com/Tomm0017)

[patch]: https://oldschool.runescape.wiki/w/Update:Points-Based_Combat_Achievements
[rev-badge]: https://img.shields.io/badge/Revision-213-blueviolet
[license-badge]: https://img.shields.io/badge/license-ISC-informational
[jdk-badge]: https://img.shields.io/badge/JDK-11-blue
