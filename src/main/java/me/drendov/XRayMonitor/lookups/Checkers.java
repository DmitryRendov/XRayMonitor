package me.drendov.XRayMonitor.lookups;

import me.drendov.XRayMonitor.Messages;
import me.drendov.XRayMonitor.TextMode;
import me.drendov.XRayMonitor.XRayMonitor;

import java.sql.SQLException;
import java.util.*;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Checkers {
    private static XRayMonitor plugin;
    private static LogBlockLookup lb;
    private static Logger logger;

    // Static initializer to set up defaults
    static {
        try {
            plugin = XRayMonitor.getInstance();
            lb = new LogBlockLookup();
            logger = plugin != null ? plugin.getLogger() : null;
        } catch (Exception e) {
            // Handle case where plugin is not initialized (e.g., during testing)
            plugin = null;
            lb = null;
            logger = null;
        }
    }

    // Define ore configurations
    private static final Map<String, OreConfig> ORE_CONFIGS = new HashMap<String, OreConfig>() {{
        put("diamond", new OreConfig("diamond", Messages.Diamond, 10.0f, Arrays.asList("diamond_ore", "deepslate_diamond_ore")));
        put("emerald", new OreConfig("emerald", Messages.Emerald, 10.0f, Arrays.asList("emerald_ore", "deepslate_emerald_ore")));
        put("gold", new OreConfig("gold", Messages.Gold, 3.0f, Arrays.asList("gold_ore", "deepslate_gold_ore", "nether_gold_ore")));
        put("lapis", new OreConfig("lapis", Messages.Lapis, 10.0f, Arrays.asList("lapis_ore", "deepslate_lapis_ore")));
        put("copper", new OreConfig("copper", Messages.Copper, 1.0f, Arrays.asList("copper_ore", "deepslate_copper_ore")));
        put("iron", new OreConfig("iron", Messages.Iron, 1.0f, Arrays.asList("iron_ore", "deepslate_iron_ore")));
        put("redstone", new OreConfig("redstone", Messages.Redstone, 1.0f, Arrays.asList("redstone_ore", "deepslate_redstone_ore")));
        put("coal", new OreConfig("coal", Messages.Coal, 1.0f, Arrays.asList("coal_ore", "deepslate_coal_ore")));
        put("mossy", new OreConfig("mossy", Messages.Mossy, 7.0f, Arrays.asList("mossy_cobblestone")));
        put("spawners", new OreConfig("spawners", Messages.Spawners, 9.0f, Arrays.asList("spawner")));
        put("ancient_debris", new OreConfig("ancient_debris", Messages.AncientDebris, 10.0f, Arrays.asList("ancient_debris"), true));
    }};

    // Define stone types for base calculation
    private static final List<String> STONE_TYPES = Arrays.asList("stone", "diorite", "andesite", "granite", "deepslate", "blackstone");
    private static final List<String> NETHER_STONE_TYPES = Arrays.asList("netherrack", "basalt");

    /**
     * Configuration class for ore types
     */
    private static class OreConfig {
        final String configKey;
        final Messages message;
        final float levelMultiplier;
        final List<String> oreVariants;
        final boolean useNetherStones;

        OreConfig(String configKey, Messages message, float levelMultiplier, List<String> oreVariants) {
            this(configKey, message, levelMultiplier, oreVariants, false);
        }

        OreConfig(String configKey, Messages message, float levelMultiplier, List<String> oreVariants, boolean useNetherStones) {
            this.configKey = configKey;
            this.message = message;
            this.levelMultiplier = levelMultiplier;
            this.oreVariants = oreVariants;
            this.useNetherStones = useNetherStones;
        }
    }

    /**
     * Data class to hold ore lookup results
     */
    private static class OreLookupResult {
        final int count;
        final int stoneCount;

        OreLookupResult(int count, int stoneCount) {
            this.count = count;
            this.stoneCount = stoneCount;
        }
    }

    public void checkGlobal(final String name, final CommandSender sender, final String world, final int hours) {
        new BukkitRunnable() {
            @Override
            public void run() {
                Player player = (sender instanceof Player) ? (Player) sender : null;

                if (!plugin.checkWorld(world)) {
                    XRayMonitor.sendMessage(player, TextMode.Err, Messages.DefaultWorldNotFound);
                    return;
                }

                try {
                    XRayMonitor.sendMessage(player, TextMode.Info, Messages.CalcPlayerOre, ChatColor.GOLD + name);
                    XRayMonitor.sendMessage(player, TextMode.Info, Messages.PleaseBePatient);

                    // Get base stone counts
                    Map<String, Integer> stoneCounts = getOreCounts(name, world, hours, STONE_TYPES);
                    Map<String, Integer> netherStoneCounts = getOreCounts(name, world, hours, NETHER_STONE_TYPES);
                    
                    int totalStones = stoneCounts.values().stream().mapToInt(Integer::intValue).sum();
                    int totalNetherStones = netherStoneCounts.values().stream().mapToInt(Integer::intValue).sum();

                    sender.sendMessage(plugin.msgBorder);
                    XRayMonitor.sendMessage(player, TextMode.Info, Messages.Stones, String.valueOf(totalStones));

                    int level = 0;

                    // Process each ore type
                    for (Map.Entry<String, OreConfig> entry : ORE_CONFIGS.entrySet()) {
                        OreConfig config = entry.getValue();
                        
                        OreLookupResult result = processOreType(name, world, hours, config, totalStones, totalNetherStones);
                        level += calculateAndDisplayOre(player, config, result);
                    }

                    // Adjust level for new players
                    level = adjustLevelForExperience(level, totalStones);
                    notifyXrayLevel(player, level);
                    sender.sendMessage(plugin.msgBorder);

                } catch (SQLException e) {
                    logger.severe("SQL Exception during global check: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    public void checkSingle(final String name, final CommandSender sender, final String oreName, final String world, final int hours) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    Player player = (sender instanceof Player) ? (Player) sender : null;

                    XRayMonitor.sendMessage(player, TextMode.Info, Messages.CalcPlayerOre, TextMode.Warn + name);
                    XRayMonitor.sendMessage(player, TextMode.Info, Messages.PleaseBePatient);

                    Map<String, Integer> stoneCounts = getOreCounts(name, world, hours, STONE_TYPES);
                    int totalStones = stoneCounts.values().stream().mapToInt(Integer::intValue).sum();
                    int oreCount = getSingleOreCount(name, oreName, world, hours);

                    sender.sendMessage(plugin.msgBorder);
                    XRayMonitor.sendMessage(player, TextMode.Info, Messages.Stones, String.valueOf(totalStones));

                    displaySingleOreResult(sender, oreName, oreCount, totalStones);

                } catch (SQLException e) {
                    logger.severe("SQL Exception during single check: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    public void listAllXRayers(CommandSender sender, String world, String oreName, float maxrate, int hours) {
        Player player = (sender instanceof Player) ? (Player) sender : null;
        final Material mat = Material.matchMaterial(oreName);
        
        if (mat == null) {
            throw new IllegalArgumentException("No material matching: '" + oreName + "'");
        }

        List<String[]> playerOreStone = new ArrayList<>();
        
        if (Objects.requireNonNull(plugin.getConfig().getString("logging_plugin")).equalsIgnoreCase("logblock")) {
            XRayMonitor.sendMessage(player, TextMode.Info, Messages.CalcAllPlayersOreRate, mat.toString(), String.valueOf(maxrate));
            playerOreStone = lb.playerLookup(sender, oreName, world);
        }

        displayXRayersList(sender, player, playerOreStone, mat, maxrate);
    }

    // Helper Methods

    /**
     * Get ore counts for specified ore types
     */
    private Map<String, Integer> getOreCounts(String playerName, String world, int hours, List<String> oreTypes) throws SQLException {
        Map<String, Integer> counts = new HashMap<>();
        
        if (plugin.getConfig().getString("logging_plugin").equalsIgnoreCase("logblock")) {
            for (String oreType : oreTypes) {
                counts.put(oreType, lb.oreLookup(playerName, oreType, world, hours));
            }
        }
        
        return counts;
    }

    /**
     * Process a single ore type and return lookup result
     */
    private OreLookupResult processOreType(String playerName, String world, int hours, OreConfig config, int totalStones, int totalNetherStones) throws SQLException {
        int totalCount = 0;
        
        if (plugin.getConfig().getString("logging_plugin").equalsIgnoreCase("logblock")) {
            for (String variant : config.oreVariants) {
                totalCount += lb.oreLookup(playerName, variant, world, hours);
            }
        }
        
        int stoneCount = config.useNetherStones ? totalNetherStones : totalStones;
        return new OreLookupResult(totalCount, stoneCount);
    }

    /**
     * Calculate ore percentage, determine color, and display result
     */
    private int calculateAndDisplayOre(Player player, OreConfig config, OreLookupResult result) {
        if (!plugin.config.isActive(config.configKey) || result.count <= 0) {
            XRayMonitor.sendMessage(player, ChatColor.WHITE, config.message, "-");
            if (config.useNetherStones) {
                XRayMonitor.sendMessage(player, ChatColor.WHITE, config.message, "-", "-");
            }
            return 0;
        }

        float percentage = (float) (result.count * 100.0D / result.stoneCount);
        ChatColor color = determineOreColor(config.configKey, percentage);
        String formattedPercentage = formatPercentage(percentage);
        
        if (config.useNetherStones) {
            XRayMonitor.sendMessage(player, color, config.message, 
                formattedPercentage + "% (" + result.count + ")", 
                String.valueOf(result.stoneCount));
        } else {
            XRayMonitor.sendMessage(player, color, config.message, 
                formattedPercentage + "% (" + result.count + ")");
        }

        return (int) (percentage * config.levelMultiplier);
    }

    /**
     * Determine color based on ore rates
     */
    private ChatColor determineOreColor(String oreType, float percentage) {
        if (percentage > plugin.config.getRate("confirmed", oreType)) {
            return TextMode.Err;
        } else if (percentage > plugin.config.getRate("warn", oreType)) {
            return TextMode.Instr;
        } else {
            return TextMode.Success;
        }
    }

    /**
     * Format percentage to 2 decimal places
     */
    private String formatPercentage(float percentage) {
        String s = percentage + "000000000";
        return String.valueOf(Float.parseFloat(s.substring(0, s.lastIndexOf('.') + 3)));
    }

    /**
     * Get count for a single ore type
     */
    private int getSingleOreCount(String playerName, String oreName, String world, int hours) throws SQLException {
        if (plugin.getConfig().getString("logging_plugin").equalsIgnoreCase("logblock")) {
            return lb.oreLookup(playerName, oreName, world, hours);
        }
        return 0;
    }

    /**
     * Display result for single ore check
     */
    private void displaySingleOreResult(CommandSender sender, String oreName, int count, int totalStones) {
        if (count > 0) {
            float percentage = (float) (count * 100.0D / totalStones);
            String formattedPercentage = formatPercentage(percentage);
            sender.sendMessage(oreName + ": " + formattedPercentage + "% (" + count + ")");
        } else {
            sender.sendMessage(oreName + ": -");
        }
    }

    /**
     * Display the list of potential X-rayers
     */
    private void displayXRayersList(CommandSender sender, Player player, List<String[]> playerOreStone, Material mat, float maxrate) {
        sender.sendMessage(plugin.msgBorder);
        XRayMonitor.sendMessage(player, TextMode.Info, Messages.AllPlayersOnOre, mat.toString());
        sender.sendMessage(plugin.msgBorder);
        
        if (playerOreStone == null) {
            sender.sendMessage(ChatColor.RED + "playerOreStone is null");
            return;
        }

        Set<String> processedPlayers = new HashSet<>();
        
        for (String[] row : playerOreStone) {
            int stones = Integer.parseInt(row[2]);
            int oreCount = Integer.parseInt(row[1]);
            String playerName = row[0];
            
            if (stones >= 100 && !processedPlayers.contains(playerName)) {
                float percentage = (float) (oreCount * 100.0D / stones);
                if (percentage > maxrate) {
                    sender.sendMessage(playerName + " " + percentage + "%");
                    processedPlayers.add(playerName);
                }
            }
        }
        
        sender.sendMessage(plugin.msgBorder);
    }

    /**
     * Adjust experience level based on stone count
     */
    private int adjustLevelForExperience(int level, int stones) {
        if (stones < 500) {
            return (int) (level * 0.5D);
        } else if (stones > 1000) {
            return level * 2;
        }
        return level;
    }

    /**
     * Notify about X-ray level
     */
    private void notifyXrayLevel(Player player, int level) {
        if (level < 45) {
            XRayMonitor.sendMessage(player, TextMode.Info, Messages.VeryLowChanceXRay, String.valueOf(level));
        } else if (level >= 45 && level < 85) {
            XRayMonitor.sendMessage(player, TextMode.Info, Messages.LowChanceXRay, String.valueOf(level));
        } else if (level >= 85 && level < 130) {
            XRayMonitor.sendMessage(player, TextMode.Instr, Messages.MediumChanceXRay, String.valueOf(level));
        } else if (level >= 130 && level < 170) {
            XRayMonitor.sendMessage(player, TextMode.Err, Messages.HighChanceXRay, String.valueOf(level));
        } else if (level >= 170) {
            XRayMonitor.sendMessage(player, ChatColor.DARK_RED, Messages.VeryHighChanceXRay, String.valueOf(level));
        }
    }
}
