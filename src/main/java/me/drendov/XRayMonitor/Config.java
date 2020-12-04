package me.drendov.XRayMonitor;

import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.Material;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;


public class Config {
    private static final XRayMonitor instance = XRayMonitor.getInstance();
    //for logging to the console
    private static Logger logger;
    private FileConfiguration config;

    // configured worlds
    public String[] worlds;

    public String defaultWorld = "world";

    // log driver
    public String logEngine;

    // Notify Console when a player break watched ores or potential cheater log in
    public Boolean notifyConsole;

    // Notify Staff team when a player break watched ores or potential cheater log in
    public Boolean notifyStaff;

    // Stone-like blocks
    public ConcurrentHashMap<World, ArrayList<Material>> stoneBlocks;

    // Ore list to watch and notify
    public ArrayList<Material> notifyOnOreBreaks;

    // Ore list and rates per world
    WorldOreRatesManager worldSettingsManager = new WorldOreRatesManager();

    void load() {

        this.worldSettingsManager = new WorldOreRatesManager();
        //load the config if it exists
        FileConfiguration config = YamlConfiguration.loadConfiguration(new File(XRayMonitor.configFilePath));
        FileConfiguration outConfig = new YamlConfiguration();
        // outConfig.options().header("Default values fit most servers. If you want to customize them -- have a look at our wiki: https://github.com/DmitryRendov/XRayMonitor/wiki/");

        // Driver to log blocks history
        this.logEngine = config.getString("XRayMonitor.LogEngine", "logblock");

        // Notify Console when a player break watched ores or potential cheater log in
        this.notifyConsole = config.getBoolean("XRayMonitor.NotifyConsole", false);

        outConfig.set("XRayMonitor.LogEngine", this.logEngine);
        outConfig.set("XRayMonitor.NotifyConsole", this.notifyConsole);


        List<World> worlds = this.instance.getServer().getWorlds();
        ArrayList<String> worldSettingsKeys = new ArrayList<String>();
        for (World world : worlds) {
            worldSettingsKeys.add(world.getName());
        }
        worldSettingsKeys.add(this.worldSettingsManager.worldName);

        for (String worldName : worldSettingsKeys) {
            WorldOreRates settings = this.worldSettingsManager.Create(worldName);

            // ArrayList<Material> stoneBlocks;
            //ConcurrentHashMap<Material, ArrayList<Object>> oreRates;

            settings.oreRates.put(Material.DIAMOND_ORE, new OreRate(config.getDouble(worldName + "DIAMOND_ORE.warn", 0.0),
                    config.getDouble(worldName + "DIAMOND_ORE.confirmed", 0.0),
                    config.getInt(worldName + "DIAMOND_ORE.weight", 0)));

            outConfig.set(worldName + "DIAMOND_ORE.warn", settings.oreRates.get(Material.DIAMOND_ORE));
        }


        try {
            outConfig.save(XRayMonitor.configFilePath);
            logger.info("Finished loading configuration.");
        } catch (IOException exception) {
            logger.info("Unable to write to the configuration file at \"" + XRayMonitor.configFilePath + "\"");
        }

        logger.info("Finished loading data.");
    }

    public boolean isActive(String ore) {
        return this.config.getBoolean(ore);
    }

    public double getRate(String type, String ore) {
        return this.config.getDouble(ore + "_" + type);
    }

    String getCmd(String name) {
        return this.config.getString(name);
    }

    void setLogger(String logger) {
        this.config.set("logging_plugin", logger.toLowerCase());
        instance.getLogger().info(String.valueOf(logger) + " detected and in use.");
    }
}

