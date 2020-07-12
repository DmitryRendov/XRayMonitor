package me.drendov.XRayMonitor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class ClearedPlayerFile {
    private static XRayMonitor instance = XRayMonitor.getInstance();
    private static Logger logger = instance.getLogger();
    private static File clearedPlayers = new File(XRayMonitor.clearedPlayerFilePath);
    private static FileConfiguration cpinfo;
    private static List<String> cpList;

    static void loadClearedPlayers() {
        if (!clearedPlayers.exists()) try {
            final boolean clearedPlayersNewFile = clearedPlayers.createNewFile();
            if (!clearedPlayersNewFile) {
                logger.log(Level.SEVERE, "[XRayMonitor] An error was encountered while trying to create the ClearedPlayers file.");
            }
            cpinfo.set("cleared_players", cpList);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "[XRayMonitor] An error was encountered while trying to create the ClearedPlayers file.");
            e.printStackTrace();
        }
        ClearedPlayerFile.saveClearedPlayers();
    }

    private static void saveClearedPlayers() {
        try {
            cpinfo.save(clearedPlayers);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "[XRayMonitor] An error was encountered while trying to save the ClearedPlayers file.");
            e.printStackTrace();
        }
    }

    static void clearPlayer(String player) {
        cpList = cpinfo.getStringList("cleared_players");
        String clearString = player + " " + System.currentTimeMillis();
        cpList.add(clearString);
        cpinfo.set("cleared_players", cpList);
        ClearedPlayerFile.saveClearedPlayers();
    }

    public static boolean wasPlayerCleared(String player) {
        cpList = cpinfo.getStringList("cleared_players");
        for (String s : cpList) {
            if (!s.contains(player)) continue;
            return true;
        }
        return false;
    }

    public static int getHoursFromClear(String player) {
        long clearTime = 0L;
        cpList = cpinfo.getStringList("cleared_players");
        for (String s : cpList) {
            if (!s.contains(player)) continue;
            String[] temp = s.split(" ");
            clearTime = Long.parseLong(temp[1]);
        }
        long now = System.currentTimeMillis();
        long difference = (now - clearTime) / 1000L / 60L;
        int hours = (int) (difference / 60L);
        if (hours < 1) {
            hours = 1;
        }
        return hours;
    }

    static {
        cpinfo = YamlConfiguration.loadConfiguration(clearedPlayers);
        cpList = new ArrayList<>();
    }
}

