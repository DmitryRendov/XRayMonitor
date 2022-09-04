package me.drendov.XRayMonitor;

import org.bukkit.configuration.file.FileConfiguration;

public class Config {
    private XRayMonitor plugin;
    private FileConfiguration config;
    public static String defaultWorld;

    Config(XRayMonitor plugin) {
        this.plugin = plugin;
    }

    void load() {
        this.plugin.reloadConfig();
        this.config = this.plugin.getConfig();
        this.config.addDefault("logging_plugin", "logblock");
        this.config.addDefault("default_world", "world");
        this.config.addDefault("checkOnPlayerJoin", true);
        this.config.addDefault("checkOnPlayerJoin.warningMessage", "%player% has higher than average stats for %ores% and may be a cheater. Watch carefully.");
        this.config.addDefault("commandOnXrayerJoin", "none");
        this.config.addDefault("notifyConsoleOnJoin", "true");
        this.config.addDefault("diamond", true);
        this.config.addDefault("gold", true);
        this.config.addDefault("lapis", true);
        this.config.addDefault("iron", true);
        this.config.addDefault("redstone", true);
        this.config.addDefault("coal", true);
        this.config.addDefault("mossy", true);
        this.config.addDefault("emerald", true);
        this.config.addDefault("ancient_debris", true);
        this.config.addDefault("spawners", true);
        this.config.addDefault("diamond_warn", 3.2);
        this.config.addDefault("diamond_confirmed", 3.8);
        this.config.addDefault("gold_warn", 8.0);
        this.config.addDefault("gold_confirmed", 10.0);
        this.config.addDefault("emerald_warn", 0.3);
        this.config.addDefault("emerald_confirmed", 0.5);
        this.config.addDefault("ancient_debris_warn", 0.3);
        this.config.addDefault("ancient_debris_confirmed", 0.5);
        this.config.addDefault("lapis_warn", 3.2);
        this.config.addDefault("lapis_confirmed", 3.8);
        this.config.addDefault("iron_warn", 40.0);
        this.config.addDefault("iron_confirmed", 100.0);
        this.config.addDefault("redstone_warn", 25.0);
        this.config.addDefault("redstone_confirmed", 65.0);
        this.config.addDefault("coal_warn", 50.0);
        this.config.addDefault("coal_confirmed", 100.0);
        this.config.addDefault("mossy_warn", 35.0);
        this.config.addDefault("mossy_confirmed", 90.0);
        this.config.addDefault("spawners_warn", 1.6);
        this.config.addDefault("spawners_confirmed", 2.2);
        this.config.addDefault("logOreBreaks.diamond", true);
        this.config.addDefault("logOreBreaks.emerald", true);
        this.config.addDefault("logOreBreaks.ancient_debris", true);
        this.config.addDefault("logOreBreaks.iron", false);
        this.config.addDefault("logOreBreaks.gold", false);
        this.config.addDefault("logOreBreaks.redstone", false);
        this.config.addDefault("logOreBreaks.coal", false);
        this.config.addDefault("logOreBreaks.lapis", false);
        this.config.addDefault("logOreBreaks.mossy", false);
        this.config.addDefault("logOreBreaks.spawners", false);
        this.config.options().copyDefaults(true);
        this.plugin.saveConfig();
        defaultWorld = this.config.getString("default_world");
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
        this.plugin.getLogger().info(String.valueOf(logger) + " detected and in use.");
        this.plugin.saveConfig();
    }
}

