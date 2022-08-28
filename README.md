# Alter
[![revision: 205][rev-badge]][patch] [![Discord](https://badgen.net/badge/icon/discord?icon=discord&label)](https://discord.com/invite/sAzCuuwkpN) ![](https://tokei.rs/b1/github/AlterRSPS/Alter)

**Alter** is a modified version of [RSMod](https://github.com/Tomm0017/rsmod) a highly flexible user-friendly game server for use with the OSRS client. Implemented in a modular way,
the framework allows developers to make and create any sort of plugin they wish without having to modify the core game module.
Due to the plugin capabilities, even owners without programming experience can just have others make plugins for them and simply drop them into the plugins module to be automatically loaded on the next startup!

#### I found a bug, where can I report it?
- You can report them by creating Issue on [GitHub](https://github.com/AlterRSPS/Alter/issues) or in Alter's [Discord Server](https://discord.gg/kdhBuRaduw)

### Installation:
First download: [2022-06-08-rev205.tar.gz](https://archive.runestats.com/osrs/2022-06-08-rev205.tar.gz)
Also make sure you have [Intellij](https://www.jetbrains.com/idea/download/#section=windows) installed. 
* Firstly open up Intellij, (If you already have a project open, either do File -> Close, or File -> New -> Project from Version Control)
  ![tutor1](https://raw.githubusercontent.com/AlterRSPS/Resources/main/docs/resources/ReadMe_Alter/tutor1.png)
  ![tutor2](https://raw.githubusercontent.com/AlterRSPS/Resources/main/docs/resources/ReadMe_Alter/tuor2.png)
* Second paste `https://github.com/AlterRSPS/Alter` in URL Field and hit Clone.
* Let the project fully load.
* Now open the 2022-06-08-rev205.tar.gz and drag out Xteas.json and Cache folder and pull it to /Data/ Folder.
  ![tutor6](https://raw.githubusercontent.com/AlterRSPS/Resources/main/docs/resources/ReadMe_Alter/tutor6.png)
  - Xtea.json has to be in same folder like: Data /`api.yml` , `blocks.yml` , etc..
* Now click on Gradle word on right side.
  ![tutor4](https://raw.githubusercontent.com/AlterRSPS/Resources/main/docs/resources/ReadMe_Alter/Tutor4.png)
* Expand gg.rsmod -> other and double click install
  ![tutor5](https://raw.githubusercontent.com/AlterRSPS/Resources/main/docs/resources/ReadMe_Alter/Tutori5.png)
* Wait till you get this result:
  ![tutor7](https://raw.githubusercontent.com/AlterRSPS/Resources/main/docs/resources/ReadMe_Alter/tutor7.png)
* Now instead of Install task do Run `Gradle -> gg.rsmod -> game -> Tasks -> Application - Run`
* Now the server is running.
  - DO NO CLOSE/TURN OFF THE SERVER YOU NEED IT TO BE RUNNING. CLIENT HAS TO VERIFY REVISIONS WHEN YOU OPEN THE CLIENT.
* Now you need to go File -> New -> Project from Version Control
* And now clone this: https://github.com/AlterRSPS/Runelite let it fully clone, and wait till Gradle finishes initializing.
* Now Click Add Configuration button on your right side. Click the plus icon -> select gradle -> In that long field bellow run enter:
  - `run --args="--developer-mode --debug --jav_config "https://raw.githubusercontent.com/AlterRSPS/Runelite/master/jav_config.ws"" -x checkStyleMain -x checkStyleTest -x test` hit apply and done.
* Now you shouldnt see Add Configuration button and next in that same area there should be Green arrow. click that. Or just Shift + F10
* When you will see the client fully loaded -> go to settings (Wrench icon) -> Find Private Server plugin -> Paste in the `Key (Modulus)` From your server directory MODULUS File.
  ![tutor9](https://raw.githubusercontent.com/AlterRSPS/Resources/main/docs/resources/ReadMe_Alter/tutor9.png)
  ![tutor8](https://raw.githubusercontent.com/AlterRSPS/Resources/main/docs/resources/ReadMe_Alter/tutor8.png)
* Disable/Enable the plugin and login into game and done enjoy making content or w.e you will do.

### Credits:
* Credits are given out to everyone who helped out with information or contributed in some form to the project. And can be found in: [Here](https://github.com/AlterRSPS)

### Some info:
* Original project founder: [Tomm0017](https://github.com/Tomm0017)

[patch]: https://oldschool.runescape.wiki/w/Update:Revenant_Caves_%26_Deadman_Changes
[rev-badge]: https://img.shields.io/badge/Revision-205-blueviolet
[license-badge]: https://img.shields.io/badge/license-ISC-informational
