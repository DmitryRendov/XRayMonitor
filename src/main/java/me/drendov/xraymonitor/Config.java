package me.drendov.xraymonitor;

import org.bukkit.configuration.file.FileConfiguration;

public class Config {
    private XRayMonitor plugin;
    private FileConfiguration config;
    public static String defaultWorld;

    public Config(XRayMonitor plugin) {
        this.plugin = plugin;
    }

    public void load() {
        this.plugin.reloadConfig();
        this.config = this.plugin.getConfig();
        this.config.addDefault("logging_plugin", (Object)"logblock");
        this.config.addDefault("default_world", (Object)"world");
        this.config.addDefault("checkOnPlayerJoin", (Object)true);
        this.config.addDefault("checkOnPlayerJoin.warningMessage", (Object)"%player% has higher than average stats for %ores% and may be a cheater. Watch carefully.");
        this.config.addDefault("commandOnXrayerJoin", (Object)"none");
        this.config.addDefault("diamond", (Object)true);
        this.config.addDefault("gold", (Object)true);
        this.config.addDefault("lapis", (Object)true);
        this.config.addDefault("iron", (Object)true);
        this.config.addDefault("redstone", (Object)true);
        this.config.addDefault("coal", (Object)true);
        this.config.addDefault("mossy", (Object)true);
        this.config.addDefault("emerald", (Object)true);
        this.config.addDefault("spawners", (Object)true);
        this.config.addDefault("diamond_warn", (Object)3.2);
        this.config.addDefault("diamond_confirmed", (Object)3.8);
        this.config.addDefault("gold_warn", (Object)8.0);
        this.config.addDefault("gold_confirmed", (Object)10.0);
        this.config.addDefault("emerald_warn", (Object)0.3);
        this.config.addDefault("emerald_confirmed", (Object)0.5);
        this.config.addDefault("lapis_warn", (Object)3.2);
        this.config.addDefault("lapis_confirmed", (Object)3.8);
        this.config.addDefault("iron_warn", (Object)40.0);
        this.config.addDefault("iron_confirmed", (Object)100.0);
        this.config.addDefault("redstone_warn", (Object)25.0);
        this.config.addDefault("redstone_confirmed", (Object)65.0);
        this.config.addDefault("coal_warn", (Object)50.0);
        this.config.addDefault("coal_confirmed", (Object)100.0);
        this.config.addDefault("mossy_warn", (Object)35.0);
        this.config.addDefault("mossy_confirmed", (Object)90.0);
        this.config.addDefault("spawners_warn", (Object)1.6);
        this.config.addDefault("spawners_confirmed", (Object)2.2);
        this.config.addDefault("logOreBreaks.diamond", (Object)true);
        this.config.addDefault("logOreBreaks.emerald", (Object)false);
        this.config.addDefault("logOreBreaks.iron", (Object)false);
        this.config.addDefault("logOreBreaks.gold", (Object)false);
        this.config.addDefault("logOreBreaks.redstone", (Object)false);
        this.config.addDefault("logOreBreaks.coal", (Object)false);
        this.config.addDefault("logOreBreaks.lapis", (Object)false);
        this.config.addDefault("logOreBreaks.mossy", (Object)false);
        this.config.addDefault("logOreBreaks.spawners", (Object)false);
        this.config.options().copyDefaults(true);
        this.plugin.saveConfig();
        defaultWorld = this.config.getString("default_world");
    }

    public boolean isActive(String ore) {
        return this.config.getBoolean(ore);
    }

    public double getRate(String type, String ore) {
        return this.config.getDouble(String.valueOf(ore) + "_" + type);
    }

    public String getCmd(String name) {
        return this.config.getString(name);
    }

    public void setLogger(String logger) {
        this.config.set("logging_plugin", (Object)logger.toLowerCase());
        this.plugin.getLogger().info(String.valueOf(logger) + " detected and in use.");
        this.plugin.saveConfig();
    }
}

