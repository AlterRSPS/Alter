# Alter
[![revision: 223][rev-badge]][patch] [![Discord](https://badgen.net/badge/icon/discord?icon=discord&label)](https://discord.com/invite/sAzCuuwkpN) ![jdk-badge] ![](https://tokei.rs/b1/github/AlterRSPS/Alter)

**Alter** is a modified version of [RSMod](https://github.com/Tomm0017/rsmod) a highly flexible user-friendly game server for use with the OSRS client. Implemented in a modular way,
the framework allows developers to make and create any sort of plugin they wish without having to modify the core game module.
Due to the plugin capabilities, even owners without programming experience can just have others make plugins for them and simply drop them into the plugins module to be automatically loaded on the next startup!

#### I found a bug, where can I report it?
- You can report them by creating Issue on [GitHub](https://github.com/AlterRSPS/Alter/issues) or in Alter's [Discord Server](https://discord.gg/kdhBuRaduw)

#### Setting up the server:
* Youtube tutorial: https://www.youtube.com/watch?v=2Tu-NTzMbf0

* ### First of all Download:
* [2024-07-17-rev223.tar.gz](https://archive.runestats.com/osrs/2024-07-17-rev223.tar.gz) , And make sure you're using correct Java Version.
* Also make sure you have [Intellij](https://www.jetbrains.com/idea/download/#section=windows) installed, how to install intellij: [Youtube](https://www.youtube.com/watch?v=t8T5Qwa5d_o)


> [!NOTE]
> Side note: Better use `Intellij 2023.2.6` : https://download.jetbrains.com/idea/ideaIU-2023.2.6.exe , as most of us noticed alot of flaws with kotlin script on newer Intellij versions

* Secondly open Intellij, (If you already have a project open, either do `File -> Close`, or `File -> New -> Project from Version Control`)
* If you decided to use `Close method` you will see this window, click on `Get from VCS`
  ![tutor1](https://raw.githubusercontent.com/AlterRSPS/Resources/main/docs/resources/ReadMe_Alter/tutor1.png)
* Second paste `https://github.com/AlterRSPS/Alter` in URL Field and hit Clone. </br>
  ![tutor2](https://raw.githubusercontent.com/AlterRSPS/Resources/main/docs/resources/ReadMe_Alter/tuor2.png)
* Hit `Clone` and Let the project fully load.
* Now open the `2024-07-17-rev223.tar.gz` file that you downloaded. and drag out Xteas.json and Cache folder and pull it to `/Data/` Folder.
  ![tutor6](https://raw.githubusercontent.com/AlterRSPS/Resources/main/docs/resources/ReadMe_Alter/tutor6.png)
- `Xtea.json` has to be in same folder like: Data /`api.yml`, etc..
* Now click on `Gradle` button on right side. </br>
  ![tutor4](https://raw.githubusercontent.com/AlterRSPS/Resources/main/docs/resources/ReadMe_Alter/Tutor4.png)
* Expand `Alter` -> `other` and double click `install`
  ![tutor5](https://raw.githubusercontent.com/AlterRSPS/Resources/main/docs/resources/ReadMe_Alter/Tutori5.png)
* And now wait until you get this result in your `Terminal` at the bottom:
  ![tutor7](https://raw.githubusercontent.com/AlterRSPS/Resources/main/docs/resources/ReadMe_Alter/tutor7.png)
* Now instead of `Install` task do Run `Gradle -> Alter -> game -> Tasks -> Application - Run`
* Now the server should be running, you should see inside your terminal:
  ![tutor8](https://raw.githubusercontent.com/AlterRSPS/Resources/main/docs/resources/ReadMe_Alter/Alter_Successfully_initialized.png)
* If you only see `Alter Loaded up in x ms.` you messed up somewhere.
* You can connect what ever client you want (ofc it has to follow OSRS protocols.)

# What client do i use?
* https://github.com/AlterRSPS/devious-client-rsps

* ### Tools:
* [CS2 Editor](https://github.com/blurite/cs2-editor), Big thanks to [Blurite Team](https://github.com/blurite) for maintaining such good tool.
* [Z-Kris's Event Inspector](https://media.z-kris.com/runelite-event-inspector-client.jar) , Want to dump osrs data easily? or see hows what being sent? Use this RL Client it's pretty easy to use.
* That's it for now. If you got something to share feel free to ping me on discord ill inspect it and add it.

### Credits:
* Credits are given out to everyone who helped out with information or contributed in some form to the project. And can be found in: [Here](https://github.com/AlterRSPS)

### Some info:
* Original project founder: [Tomm0017](https://github.com/Tomm0017)


[patch]: https://oldschool.runescape.wiki/w/Update:Deadman:_Armageddon_%26_While_Guthix_Sleeps_Tweaks
[rev-badge]: https://img.shields.io/badge/Revision-223-blueviolet
[license-badge]: https://img.shields.io/badge/license-ISC-informational
[jdk-badge]: https://img.shields.io/badge/JDK-17-blue
