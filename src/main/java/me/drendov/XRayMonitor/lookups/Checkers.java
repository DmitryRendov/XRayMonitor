package me.drendov.XRayMonitor.lookups;

import me.drendov.XRayMonitor.Messages;
import me.drendov.XRayMonitor.TextMode;
import me.drendov.XRayMonitor.XRayMonitor;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Checkers {
    private static XRayMonitor plugin = XRayMonitor.getInstance();
    private static LogBlockLookup lb = new LogBlockLookup();
    private static Logger logger = plugin.getLogger();

    public void checkGlobal(final String name, final CommandSender sender, final String world, final int hours) {

        logger.info(ChatColor.RED + "DEBUG: checkGlobal");

        new BukkitRunnable() {
            @Override
            public void run() {

                Player player = null;
                if (sender instanceof Player) {
                    player = (Player) sender;
                }

                if (!Checkers.plugin.checkWorld(world)) {
                    XRayMonitor.sendMessage(player, TextMode.Err, Messages.DefaultWorldNotFound);
                }

                try {
                    XRayMonitor.sendMessage(player, TextMode.Info, Messages.CalcPlayerOre, ChatColor.GOLD + name);
                    XRayMonitor.sendMessage(player, TextMode.Info, Messages.PleaseBePatient);
                    int level = 0;
                    int stone = 0;
                    int diorite = 0;
                    int andesite = 0;
                    int granite = 0;

                    int diamond_count = 0;
                    int gold_count = 0;
                    int lapis_count = 0;
                    int iron_count = 0;
                    int coal_count = 0;
                    int redstone_count = 0;
                    int mossy_count = 0;
                    int emerald_count = 0;
                    int ancient_debris_count = 0;
                    int spawner_count = 0;
                    int netherrack_count = 0;

                    if (plugin.getConfig().getString("logging_plugin").equalsIgnoreCase("logblock")) {
                        stone = Checkers.lb.oreLookup(name, "stone", world, hours);
                        diorite = Checkers.lb.oreLookup(name, "diorite", world, hours);
                        andesite = Checkers.lb.oreLookup(name, "andesite", world, hours);
                        granite = Checkers.lb.oreLookup(name, "granite", world, hours);
                        diamond_count = Checkers.lb.oreLookup(name, "diamond_ore", world, hours);
                        gold_count = Checkers.lb.oreLookup(name, "gold_ore", world, hours);
                        lapis_count = Checkers.lb.oreLookup(name, "lapis_ore", world, hours);
                        iron_count = Checkers.lb.oreLookup(name, "iron_ore", world, hours);
                        redstone_count = Checkers.lb.oreLookup(name, "redstone_ore", world, hours);
                        coal_count = Checkers.lb.oreLookup(name, "coal_ore", world, hours);
                        mossy_count = Checkers.lb.oreLookup(name, "mossy_cobblestone", world, hours);
                        emerald_count = Checkers.lb.oreLookup(name, "emerald_ore", world, hours);
                        ancient_debris_count = Checkers.lb.oreLookup(name, "ancient_debris", world, hours);
                        spawner_count = Checkers.lb.oreLookup(name, "spawner", world, hours);
                        netherrack_count = Checkers.lb.oreLookup(name, "netherrack", world, hours);
                    }

                    sender.sendMessage(Checkers.plugin.msgBorder);
                    int stones = stone + diorite + andesite + granite;
                    XRayMonitor.sendMessage(player, TextMode.Info, Messages.Stones, String.valueOf(stones));

                    String s;
                    ChatColor ccolor;

                    if ((Checkers.plugin.config.isActive("diamond")) && (diamond_count > 0)) {
                        float d = (float) (diamond_count * 100.0D / stones);
                        if (d > Checkers.plugin.config.getRate("confirmed", "diamond")) {
                            ccolor = TextMode.Err;
                        } else if (d > Checkers.plugin.config.getRate("warn", "diamond")) {
                            ccolor = TextMode.Instr;
                        } else {
                            ccolor = TextMode.Success;
                        }
                        level = (int) (level + d * 10.0F);

                        s = d + "000000000";
                        XRayMonitor.sendMessage(player, ccolor, Messages.Diamond, Float.parseFloat(s.substring(0, s.lastIndexOf('.') + 3)) + "% (" + diamond_count + ")");
                    } else {
                        XRayMonitor.sendMessage(player, ChatColor.WHITE, Messages.Diamond, "-");
                    }
                    if ((Checkers.plugin.config.isActive("emerald")) && (emerald_count > 0)) {
                        float d = (float) (emerald_count * 100.0D / stones);
                        if (d > Checkers.plugin.config.getRate("confirmed", "emerald")) {
                            ccolor = TextMode.Err;
                        } else if (d > Checkers.plugin.config.getRate("warn", "emerald")) {
                            ccolor = TextMode.Instr;
                        } else {
                            ccolor = TextMode.Success;
                        }
                        level = (int) (level + d * 10.0F);

                        s = d + "000000000";
                        XRayMonitor.sendMessage(player, ccolor, Messages.Emerald, Float.parseFloat(s.substring(0, s.lastIndexOf('.') + 3)) + "% (" + emerald_count + ")");

                    } else {
                        XRayMonitor.sendMessage(player, ChatColor.WHITE, Messages.Emerald, "-");
                    }

                    if ((Checkers.plugin.config.isActive("ancient_debris")) && (ancient_debris_count > 0)) {
                        float d = (float) (ancient_debris_count * 100.0D / netherrack_count);
                        if (d > Checkers.plugin.config.getRate("confirmed", "ancient_debris")) {
                            ccolor = TextMode.Err;
                        } else if (d > Checkers.plugin.config.getRate("warn", "ancient_debris")) {
                            ccolor = TextMode.Instr;
                        } else {
                            ccolor = TextMode.Success;
                        }
                        level = (int) (level + d * 10.0F);

                        s = d + "000000000";
                        XRayMonitor.sendMessage(player, ccolor, Messages.AncientDebris, Float.parseFloat(s.substring(0, s.lastIndexOf('.') + 3)) + "% (" + ancient_debris_count + ")", String.valueOf(netherrack_count));

                    } else {
                        XRayMonitor.sendMessage(player, ChatColor.WHITE, Messages.AncientDebris, "-", "-");
                    }

                    if ((Checkers.plugin.config.isActive("gold")) && (gold_count > 0)) {
                        float d = (float) (gold_count * 100.0D / stones);
                        if (d > Checkers.plugin.config.getRate("confirmed", "gold")) {
                            ccolor = TextMode.Err;
                        } else if (d > Checkers.plugin.config.getRate("warn", "gold")) {
                            ccolor = TextMode.Instr;
                        } else {
                            ccolor = TextMode.Success;
                        }
                        level = (int) (level + d * 3.0F);

                        s = d + "000000000";
                        XRayMonitor.sendMessage(player, ccolor, Messages.Gold, Float.parseFloat(s.substring(0, s.lastIndexOf('.') + 3)) + "% (" + gold_count + ")");
                    } else {
                        XRayMonitor.sendMessage(player, ChatColor.WHITE, Messages.Gold, "-");
                    }
                    if ((Checkers.plugin.config.isActive("lapis")) && (lapis_count > 0)) {
                        float d = (float) (lapis_count * 100.0D / stones);
                        if (d > Checkers.plugin.config.getRate("confirmed", "lapis")) {
                            ccolor = TextMode.Err;
                        } else if (d > Checkers.plugin.config.getRate("warn", "lapis")) {
                            ccolor = TextMode.Instr;
                        } else {
                            ccolor = TextMode.Success;
                        }
                        level = (int) (level + d * 10.0F);

                        s = d + "000000000";
                        XRayMonitor.sendMessage(player, ccolor, Messages.Lapis, Float.parseFloat(s.substring(0, s.lastIndexOf('.') + 3)) + "% (" + lapis_count + ")");
                    } else {
                        XRayMonitor.sendMessage(player, ChatColor.WHITE, Messages.Lapis, "-");
                    }

                    if ((Checkers.plugin.config.isActive("iron")) && (iron_count > 0)) {
                        float d = (float) (iron_count * 100.0D / stones);
                        if (d > Checkers.plugin.config.getRate("confirmed", "iron")) {
                            ccolor = TextMode.Err;
                        } else if (d > Checkers.plugin.config.getRate("warn", "iron")) {
                            ccolor = TextMode.Instr;
                        } else {
                            ccolor = TextMode.Success;
                        }
                        level = (int) (level + d * 1.0F);

                        s = d + "000000000";
                        XRayMonitor.sendMessage(player, ccolor, Messages.Iron, Float.parseFloat(s.substring(0, s.lastIndexOf('.') + 3)) + "% (" + iron_count + ")");
                    } else {
                        XRayMonitor.sendMessage(player, ChatColor.WHITE, Messages.Iron, "-");
                    }
                    if ((Checkers.plugin.config.isActive("redstone")) && (redstone_count > 0)) {
                        float d = (float) (redstone_count * 100.0D / stones);
                        if (d > Checkers.plugin.config.getRate("confirmed", "redstone")) {
                            ccolor = TextMode.Err;
                        } else if (d > Checkers.plugin.config.getRate("warn", "redstone")) {
                            ccolor = TextMode.Instr;
                        } else {
                            ccolor = TextMode.Success;
                        }
                        level = (int) (level + d * 1.0F);

                        s = d + "000000000";
                        XRayMonitor.sendMessage(player, ccolor, Messages.Redstone, Float.parseFloat(s.substring(0, s.lastIndexOf('.') + 3)) + "% (" + redstone_count + ")");

                    } else {
                        XRayMonitor.sendMessage(player, ChatColor.WHITE, Messages.Redstone, "-");
                    }
                    if ((Checkers.plugin.config.isActive("coal")) && (coal_count > 0)) {
                        float d = (float) (coal_count * 100.0D / stones);
                        if (d > Checkers.plugin.config.getRate("confirmed", "coal")) {
                            ccolor = TextMode.Err;
                        } else if (d > Checkers.plugin.config.getRate("warn", "coal")) {
                            ccolor = TextMode.Instr;
                        } else {
                            ccolor = TextMode.Success;
                        }
                        level = (int) (level + d * 1.0F);

                        s = d + "000000000";
                        XRayMonitor.sendMessage(player, ccolor, Messages.Coal, Float.parseFloat(s.substring(0, s.lastIndexOf('.') + 3)) + "% (" + coal_count + ")");
                    } else {
                        XRayMonitor.sendMessage(player, ChatColor.WHITE, Messages.Coal, "-");
                    }
                    if ((Checkers.plugin.config.isActive("mossy")) && (mossy_count > 0)) {
                        float d = (float) (mossy_count * 100.0D / stones);
                        if (d > Checkers.plugin.config.getRate("confirmed", "mossy")) {
                            ccolor = TextMode.Err;
                        } else if (d > Checkers.plugin.config.getRate("warn", "mossy")) {
                            ccolor = TextMode.Instr;
                        } else {
                            ccolor = TextMode.Success;
                        }
                        level = (int) (level + d * 7.0F);

                        s = d + "000000000";
                        XRayMonitor.sendMessage(player, ccolor, Messages.Mossy, Float.parseFloat(s.substring(0, s.lastIndexOf('.') + 3)) + "% (" + mossy_count + ")");
                    } else {
                        XRayMonitor.sendMessage(player, ChatColor.WHITE, Messages.Mossy, "-");
                    }
                    if ((Checkers.plugin.config.isActive("spawners")) && (spawner_count > 0)) {
                        float d = (float) (spawner_count * 100.0D / stones);
                        if (d > Checkers.plugin.config.getRate("confirmed", "spawners")) {
                            ccolor = TextMode.Err;
                        } else if (d > Checkers.plugin.config.getRate("warn", "spawners")) {
                            ccolor = TextMode.Instr;
                        } else {
                            ccolor = TextMode.Success;
                        }
                        level = (int) (level + d * 9.0F);

                        s = d + "000000000";
                        XRayMonitor.sendMessage(player, ccolor, Messages.Spawners, Float.parseFloat(s.substring(0, s.lastIndexOf('.') + 3)) + "% (" + spawner_count + ")");
                    } else {
                        XRayMonitor.sendMessage(player, ChatColor.WHITE, Messages.Spawners, "-");
                    }

                    // Adjust level for new players
                    if (stones < 500) {
                        level = (int) (level * 0.5D);
                    } else if (stones > 1000) {
                        level *= 2;
                    }
                    notifyXrayLevel(player, level);
                    sender.sendMessage(Checkers.plugin.msgBorder);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            private void notifyXrayLevel(Player player, int level) {
                if (level < 45) {
                    XRayMonitor.sendMessage(player, TextMode.Info, Messages.VeryLowChanceXRay, String.valueOf(level));
                }
                if ((level >= 45) && (level < 85)) {
                    XRayMonitor.sendMessage(player, TextMode.Info, Messages.LowChanceXRay, String.valueOf(level));
                }
                if ((level >= 85) && (level < 130)) {
                    XRayMonitor.sendMessage(player, TextMode.Instr, Messages.MediumChanceXRay, String.valueOf(level));
                }
                if ((level >= 130) && (level < 170)) {
                    XRayMonitor.sendMessage(player, TextMode.Err, Messages.HighChanceXRay, String.valueOf(level));
                }
                if (level >= 170) {
                    XRayMonitor.sendMessage(player, ChatColor.DARK_RED, Messages.VeryHighChanceXRay, String.valueOf(level));
                }
            }

        }.runTaskAsynchronously(plugin);
    }

    public void checkSingle(final String name, final CommandSender sender, final String oreName, final String world, final int hours) {
        logger.info("DEBUG: getting into checkSingle");
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    Player player = null;
                    if (sender instanceof Player) {
                        player = (Player) sender;
                    }

                    XRayMonitor.sendMessage(player, TextMode.Info, Messages.CalcPlayerOre, TextMode.Warn + name);
                    XRayMonitor.sendMessage(player, TextMode.Info, Messages.PleaseBePatient);
                    int stone = 0;
                    int diorite = 0;
                    int andesite = 0;
                    int granite = 0;
                    int count_xyz = 0;
                    if (Checkers.plugin.getConfig().getString("logging_plugin").equalsIgnoreCase("logblock")) {
                        stone = Checkers.lb.oreLookup(name, "stone", world, hours);
                        diorite = Checkers.lb.oreLookup(name, "diorite", world, hours);
                        andesite = Checkers.lb.oreLookup(name, "andesite", world, hours);
                        granite = Checkers.lb.oreLookup(name, "granite", world, hours);

                        count_xyz = Checkers.lb.oreLookup(name, oreName, world, hours);
                    }

                    logger.info("DEBUG: checking single material=" + oreName);
                    int stones = stone + diorite + andesite + granite;
                    sender.sendMessage(Checkers.plugin.msgBorder);
                    XRayMonitor.sendMessage(player, TextMode.Info, Messages.Stones, String.valueOf(stones));


                    String s = "";
                    if (count_xyz > 0) {
                        float d = (float) (count_xyz * 100.0D / stones);
                        s = d + "000000000";
                        sender.sendMessage(oreName + ": " + String.valueOf(Float.parseFloat(s.substring(0, s.lastIndexOf('.') + 3))) + "% (" + String.valueOf(count_xyz) + ")");
                    } else {
                        sender.sendMessage(oreName + ": -");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    public void listAllXRayers(CommandSender sender, String world, String oreName, float maxrate, int hours) {
        logger.info("DEBUG: getting into listAllXRayers");
        List<String[]> playerOreStone = new ArrayList();
        final Material mat = Material.matchMaterial(oreName);
        Player player = null;
        if (sender instanceof Player) {
            player = (Player) sender;
        }
        if (mat == null) {
            throw new IllegalArgumentException("No material matching: '" + oreName + "'");
        }
        if (Objects.requireNonNull(plugin.getConfig().getString("logging_plugin")).equalsIgnoreCase("logblock")) {
            XRayMonitor.sendMessage(player, TextMode.Info, Messages.CalcAllPlayersOreRate, mat.toString(), String.valueOf(maxrate));
            playerOreStone = lb.playerLookup(sender, oreName, world);
        }
        sender.sendMessage(plugin.msgBorder);
        XRayMonitor.sendMessage(player, TextMode.Info, Messages.AllPlayersOnOre, mat.toString());
        sender.sendMessage(plugin.msgBorder);
        if (playerOreStone == null) {
            sender.sendMessage(ChatColor.RED + "playerOreStone is null");
        }
        ArrayList<String> preventRepeat = new ArrayList();
        for (String[] row : playerOreStone) {
            if (Integer.valueOf(row[2]).intValue() >= 100) {
                float d = (float) (Integer.valueOf(row[1]).intValue() * 100.0D / Integer.valueOf(row[2]).intValue());
                if (d > maxrate) {
                    if (!preventRepeat.contains(row[0])) {
                        sender.sendMessage(row[0] + " " + d + "%");
                        preventRepeat.add(row[0]);
                    }
                }
            }
        }
        sender.sendMessage(plugin.msgBorder);
    }
}
