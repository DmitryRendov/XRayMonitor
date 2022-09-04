package me.drendov.XRayMonitor;

import me.drendov.XRayMonitor.lookups.LogBlockLookup;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.logging.Logger;

public class Listeners
        implements Listener {
    private XRayMonitor plugin;
    private static Logger logger;

    Listeners() {
        this.plugin = XRayMonitor.getInstance();
    }

    private LogBlockLookup lb = new LogBlockLookup();

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent evt) {
        Player playName = evt.getPlayer();
        if ((this.plugin.config.isActive("checkOnPlayerJoin")) && (
                (!playName.hasPermission("xcheck.bypasscheck")) || (!playName.isOp()))) {
            new BukkitRunnable() {
                public void run() {
                    String playerName = evt.getPlayer().getName();
                    String world = Config.defaultWorld;
                    int hours = -1;
                    try {
                        if (ClearedPlayerFile.wasPlayerCleared(playerName)) {
                            hours = ClearedPlayerFile.getHoursFromClear(playerName);
                        }
                        int level = 0;

                        int count_stones = 0;
                        int count_stone = 0;
                        int count_andesite = 0;
                        int count_diorite = 0;
                        int count_granite = 0;

                        int diamond_count = 0;
                        int gold_count = 0;
                        int lapis_count = 0;
                        int iron_count = 0;
                        int redstone_count = 0;
                        int coal_count = 0;
                        int mossy_count = 0;
                        int emerald_count = 0;
                        int ancient_debris_count = 0;
                        int spawner_count = 0;
                        int count_netherrack = 0;
                        if (Listeners.this.plugin.getConfig().getString("logging_plugin").equalsIgnoreCase("logblock")) {
                            count_stone = Listeners.this.lb.oreLookup(playerName, "stone", world, hours);
                            count_andesite = Listeners.this.lb.oreLookup(playerName, "andesite", world, hours);
                            count_diorite = Listeners.this.lb.oreLookup(playerName, "diorite", world, hours);
                            count_granite = Listeners.this.lb.oreLookup(playerName, "granite", world, hours);
                            count_stones = count_stone + count_andesite + count_diorite + count_granite;

                            diamond_count = Listeners.this.lb.oreLookup(playerName, "diamond_ore", world, hours);
                            gold_count = Listeners.this.lb.oreLookup(playerName, "gold_ore", world, hours);
                            lapis_count = Listeners.this.lb.oreLookup(playerName, "lapis_ore", world, hours);
                            iron_count = Listeners.this.lb.oreLookup(playerName, "iron_ore", world, hours);
                            redstone_count = Listeners.this.lb.oreLookup(playerName, "redstone_ore", world, hours);
                            coal_count = Listeners.this.lb.oreLookup(playerName, "coal_ore", world, hours);
                            mossy_count = Listeners.this.lb.oreLookup(playerName, "mossy_cobblestone", world, hours);
                            emerald_count = Listeners.this.lb.oreLookup(playerName, "emerald_ore", world, hours);
                            ancient_debris_count = Listeners.this.lb.oreLookup(playerName, "ancient_debris", world, hours);
                            spawner_count = Listeners.this.lb.oreLookup(playerName, "spawner", world, hours);
                            count_netherrack = Listeners.this.lb.oreLookup(playerName, "netherrack", world, hours);

                        }
                        String dia = "";
                        String gld = "";
                        String lap = "";
                        String emr = "";
                        String adr = "";
                        String irn = "";
                        String rds = "";
                        String coa = "";
                        String msy = "";
                        String spn = "";
                        if ((Listeners.this.plugin.config.isActive("diamond")) && (diamond_count > 0)) {
                            float d = (float) (diamond_count * 100.0D / count_stones);
                            if (d > Listeners.this.plugin.config.getRate("confirmed", "diamond")) {
                                dia = "diamond, ";
                            }
                            level = (int) (level + d * 10.0F);
                        }
                        if ((Listeners.this.plugin.config.isActive("gold")) && (gold_count > 0)) {
                            float d = (float) (gold_count * 100.0D / count_stones);
                            if (d > Listeners.this.plugin.config.getRate("confirmed", "gold")) {
                                gld = "gold, ";
                            }
                            level = (int) (level + d * 3.0F);
                        }
                        if ((Listeners.this.plugin.config.isActive("lapis")) && (lapis_count > 0)) {
                            float d = (float) (lapis_count * 100.0D / count_stones);
                            if (d > Listeners.this.plugin.config.getRate("confirmed", "lapis")) {
                                lap = "lapis, ";
                            }
                            level = (int) (level + d * 10.0F);
                        }
                        if ((Listeners.this.plugin.config.isActive("emerald")) && (emerald_count > 0)) {
                            float d = (float) (emerald_count * 100.0D / count_stones);
                            if (d > Listeners.this.plugin.config.getRate("confirmed", "emerald")) {
                                emr = "emerald, ";
                            }
                            level = (int) (level + d * 15.0F);
                        }
                        if ((Listeners.this.plugin.config.isActive("ancient_debris")) && (emerald_count > 0)) {
                            float d = (float) (ancient_debris_count * 100.0D / count_netherrack);
                            if (d > Listeners.this.plugin.config.getRate("confirmed", "ancient_debris")) {
                                adr = "ancient debris, ";
                            }
                            level = (int) (level + d * 15.0F);
                        }
                        if ((Listeners.this.plugin.config.isActive("iron")) && (iron_count > 0)) {
                            float d = (float) (iron_count * 100.0D / count_stones);
                            if (d > Listeners.this.plugin.config.getRate("confirmed", "iron")) {
                                irn = "iron, ";
                            }
                            level = (int) (level + d * 1.0F);
                        }
                        if ((Listeners.this.plugin.config.isActive("redstone")) && (redstone_count > 0)) {
                            float d = (float) (redstone_count * 100.0D / count_stones);
                            if (d > Listeners.this.plugin.config.getRate("confirmed", "redstone")) {
                                rds = "redstone, ";
                            }
                            level = (int) (level + d * 1.0F);
                        }
                        if ((Listeners.this.plugin.config.isActive("coal")) && (coal_count > 0)) {
                            float d = (float) (coal_count * 100.0D / count_stones);
                            if (d > Listeners.this.plugin.config.getRate("confirmed", "coal")) {
                                coa = "redstone, ";
                            }
                            level = (int) (level + d * 1.0F);
                        }
                        if ((Listeners.this.plugin.config.isActive("mossy")) && (mossy_count > 0)) {
                            float d = (float) (mossy_count * 100.0D / count_stones);
                            if (d > Listeners.this.plugin.config.getRate("confirmed", "mossy")) {
                                msy = "mossy, ";
                            }
                            level = (int) (level + d * 7.0F);
                        }
                        if ((Listeners.this.plugin.config.isActive("spawners")) && (spawner_count > 0)) {
                            float d = (float) (spawner_count * 100.0D / count_stones);
                            if (d > Listeners.this.plugin.config.getRate("confirmed", "spawners")) {
                                spn = "spawners, ";
                            }
                            level = (int) (level + d * 9.0F);
                        }
                        if (count_stones < 500) {
                            level = (int) (level * 0.5D);
                        } else if (count_stones > 1000) {
                            level *= 2;
                        }
                        if ((dia != "") || (gld != "") || (lap != "") || (emr != "") || (adr != "") || (irn != "") || (coa != "") || (rds != "") || (msy != "") || (spn != "")) {
                            for (Player staff : Listeners.this.plugin.getServer().getOnlinePlayers()) {
                                if ((staff.hasPermission("xcheck.receive")) || (staff.isOp())) {
                                    staff.sendMessage(ChatColor.RED + "[XRayMonitor] " + Listeners.this.plugin.getConfig().getString("checkOnPlayerJoin.warningMessage").replace("%player%", playerName).replace("%ores%", new StringBuilder(String.valueOf(dia)).append(gld).append(lap).append(emr).append(adr).append(irn).append(rds).append(coa).append(msy).append(spn).toString()));
                                    Listeners.this.plugin.getLogger().info("Player " + Listeners.this.plugin.getConfig().getString("checkOnPlayerJoin.warningMessage").replace("%player%", playerName).replace("%ores%", new StringBuilder(String.valueOf(dia)).append(gld).append(lap).append(emr).append(adr).append(irn).append(rds).append(coa).append(msy).append(spn).toString()));
                                }
                            }
                            if (!Listeners.this.plugin.config.getCmd("commandOnXrayerJoin").equals("none")) {
                                String cmd = Listeners.this.plugin.config.getCmd("commandOnXrayerJoin").replaceAll("%player%", playerName);
                                cmd = cmd.replaceAll("%ores%", dia + gld + lap + emr + adr + irn + rds + coa + msy + spn);
                                cmd = cmd.replaceAll("%xlevel%", String.valueOf(level) );
                                Listeners.this.plugin.getServer().dispatchCommand(Listeners.this.plugin.getServer().getConsoleSender(), cmd);
                            }

                            if (Listeners.this.plugin.config.isActive("notifyConsoleOnJoin")) {
                                logger.severe("Player " + Listeners.this.plugin.getConfig().getString("checkOnPlayerJoin.warningMessage").replace("%player%", playerName).replace("%ores%", new StringBuilder(String.valueOf(dia)).append(gld).append(lap).append(emr).append(adr).append(irn).append(rds).append(coa).append(msy).append(spn).toString()));
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.runTaskAsynchronously(this.plugin);
        }
    }

    @EventHandler
    public void onOreBreak(BlockBreakEvent event) {
        String player = event.getPlayer().getName();
        Material block = event.getBlock().getType();
        if ((block != Material.DIAMOND_ORE) && (block != Material.IRON_ORE) && (block != Material.GOLD_ORE) && (block != Material.LAPIS_ORE) &&
                (block != Material.EMERALD_ORE) && (block != Material.COAL_ORE) && (block != Material.REDSTONE_ORE) &&
                (block != Material.MOSSY_COBBLESTONE) && (block != Material.SPAWNER)) {
            return;
        }
        if ((block == Material.IRON_ORE) && (this.plugin.getConfig().getBoolean("logOreBreaks.iron"))) {
            for (Player staff : this.plugin.getServer().getOnlinePlayers()) {
                if (staff.hasPermission("xcheck.receive")) {
                    staff.sendMessage(ChatColor.RED + "[XRayMonitor] " + ChatColor.AQUA + player + " has just mined an iron ore.");
                }
            }
        }
        if ((block == Material.COAL_ORE) && (this.plugin.getConfig().getBoolean("logOreBreaks.coal"))) {
            for (Player staff : this.plugin.getServer().getOnlinePlayers()) {
                if (staff.hasPermission("xcheck.receive")) {
                    staff.sendMessage(ChatColor.RED + "[XRayMonitor] " + ChatColor.AQUA + player + " has just mined a coal ore.");
                }
            }
        }
        if ((block == Material.REDSTONE_ORE) && (this.plugin.getConfig().getBoolean("logOreBreaks.redstone"))) {
            for (Player staff : this.plugin.getServer().getOnlinePlayers()) {
                if (staff.hasPermission("xcheck.receive")) {
                    staff.sendMessage(ChatColor.RED + "[XRayMonitor] " + ChatColor.AQUA + player + " has just mined a redstone ore.");
                }
            }
        }
        if ((block == Material.GOLD_ORE) && (this.plugin.getConfig().getBoolean("logOreBreaks.gold"))) {
            for (Player staff : this.plugin.getServer().getOnlinePlayers()) {
                if (staff.hasPermission("xcheck.receive")) {
                    staff.sendMessage(ChatColor.RED + "[XRayMonitor] " + ChatColor.AQUA + player + " has just mined a gold ore.");
                }
            }
        }
        if ((block == Material.LAPIS_ORE) && (this.plugin.getConfig().getBoolean("logOreBreaks.lapis"))) {
            for (Player staff : this.plugin.getServer().getOnlinePlayers()) {
                if (staff.hasPermission("xcheck.receive")) {
                    staff.sendMessage(ChatColor.RED + "[XRayMonitor] " + ChatColor.AQUA + player + " has just mined a lapis ore.");
                }
            }
        }
        if ((block == Material.EMERALD_ORE) && (this.plugin.getConfig().getBoolean("logOreBreaks.emerald"))) {
            for (Player staff : this.plugin.getServer().getOnlinePlayers()) {
                if (staff.hasPermission("xcheck.receive")) {
                    staff.sendMessage(ChatColor.RED + "[XRayMonitor] " + ChatColor.AQUA + player + " has just mined a emerald ore.");
                }
            }
        }
        if ((block == Material.ANCIENT_DEBRIS) && (this.plugin.getConfig().getBoolean("logOreBreaks.ancient_debris"))) {
            for (Player staff : this.plugin.getServer().getOnlinePlayers()) {
                if (staff.hasPermission("xcheck.receive")) {
                    staff.sendMessage(ChatColor.RED + "[XRayMonitor] " + ChatColor.AQUA + player + " has just mined a ancient_debris ore.");
                }
            }
        }
        if ((block == Material.DIAMOND_ORE) && (this.plugin.getConfig().getBoolean("logOreBreaks.diamond"))) {
            for (Player staff : this.plugin.getServer().getOnlinePlayers()) {
                if (staff.hasPermission("xcheck.receive")) {
                    staff.sendMessage(ChatColor.RED + "[XRayMonitor] " + ChatColor.AQUA + player + " has just mined a diamond ore.");
                }
            }
        }
        if ((block == Material.MOSSY_COBBLESTONE) && (this.plugin.getConfig().getBoolean("logOreBreaks.mossy"))) {
            for (Player staff : this.plugin.getServer().getOnlinePlayers()) {
                if (staff.hasPermission("xcheck.receive")) {
                    staff.sendMessage(ChatColor.RED + "[XRayMonitor] " + ChatColor.AQUA + player + " has just mined a mossy cobblestone block.");
                }
            }
        }
        if ((block == Material.SPAWNER) && (this.plugin.getConfig().getBoolean("logOreBreaks.spawners"))) {
            for (Player staff : this.plugin.getServer().getOnlinePlayers()) {
                if (staff.hasPermission("xcheck.receive")) {
                    staff.sendMessage(ChatColor.RED + "[XRayMonitor] " + ChatColor.AQUA + player + " has just mined a monster spawner.");
                }
            }
        }
    }
}
