# XRayMonitor
[![CI](https://github.com/DmitryRendov/XRayMonitor/actions/workflows/xraymonitor-ci.yml/badge.svg)](https://github.com/DmitryRendov/XRayMonitor/actions/workflows/xraymonitor-ci.yml)
[![Java](https://img.shields.io/badge/Java-21%2B-orange.svg)](https://java.com)
[![License](https://img.shields.io/github/license/DmitryRendov/XRayMonitor)](LICENSE)

Let's fight X-Ray cheat with simple Math!

![XRayMonitor Logo](XRayMonitor.png?raw=true "XRayMonitor Logo")

This simple plugin monitors players' mining activity and detects potential X-Ray cheating through statistical analysis. When a player mines significantly more valuable ores (like diamonds, gold, emeralds) compared to common blocks (like stone, coal), the plugin flags them for potential cheating. The detection is based on configurable thresholds, allowing server admins to customize sensitivity according to their needs, and uses [LogBlock](https://www.spigotmc.org/resources/logblock.67333/) data to analyze mining patterns over time.

This plugin requires Java 21 or higher and is compatible with Minecraft server versions 1.20.x and above. Also, it supports both Paper and Spigot server implementations.


## Download
XRayMonitor available on 
- GitHub: https://github.com/DmitryRendov/XRayMonitor/releases
- SpigotMC: https://www.spigotmc.org/resources/xraymonitor.131488/

## Installation
1. Download the plugin.
2. Place the plugin in your server's plugins directory.
3. Restart your server.
4. Use the /xrm check <player> command to check a particular player for X-ray cheating.

## Configuration 
The configuration file `config.yml` is created in the plugin's folder after the first run.

## Commands
- `/xrm all` - Check the mining activity of all online players on the server.
- `/xrm <player>` - Check the mining activity of a specific player, even offline.
- `/xrm reload` - Reload the plugin configuration.
- `/xrm Player world:survival` - Check a specific player's mining activity in a specific world.
- `/xrm all ore:diamond_ore rate:3` - Check all players' mining activity for diamond ore with a rate of 3.

![XRayMonitor Player check](XRayMonitor-player-check.png?raw=true "XRayMonitor Player check")

![XRayMonitor Admin notification](XRayMonitor-admin-notification.png?raw=true "XRayMonitor Admin notification")

## Adding New Materials in the code

### Easy Extension Process:
1. **Add Message Entry** (in Messages.java):
   ```java
   Quartz("quartz", "§6Quartz: §a%s"),
   ```

2. **Add Configuration Entry** (in Checkers.java):
   ```java
   put("quartz", new OreConfig("quartz", Messages.Quartz, 2.0f, 
       Arrays.asList("nether_quartz_ore"), true));
   ```

### Example Future Materials:
```java
// Future Minecraft materials
put("ruby", new OreConfig("ruby", Messages.Ruby, 12.0f, 
    Arrays.asList("ruby_ore", "deepslate_ruby_ore")));
put("amethyst", new OreConfig("amethyst", Messages.Amethyst, 5.0f, 
    Arrays.asList("amethyst_cluster", "budding_amethyst")));
```

## Support
If you encounter a bug, please open an issue with detailed information. I maintain this in my spare time, so fixes may take a while, but I'll address them when possible.

## Donate
If you appreciate my work, feel free to:
- Buy Me a Coffee at [ko-fi.com](https://ko-fi.com/dmitryrendov)
