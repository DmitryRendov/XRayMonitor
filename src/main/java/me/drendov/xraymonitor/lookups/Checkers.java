package me.drendov.xraymonitor.lookups;

import me.drendov.xraymonitor.Messages;
import me.drendov.xraymonitor.TextMode;
import me.drendov.xraymonitor.XRayMonitor;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Checkers {
    private static XRayMonitor instance = XRayMonitor.getInstance();
    private static LogBlockLookup lb = new LogBlockLookup();

    public void checkGlobal(final String name, final CommandSender sender, final String world, final int hours) {

        new BukkitRunnable() {
            @Override
            public void run() {

                Player player = XRayMonitor.isSenderPlayer(sender);

                if (!instance.isWorldExist(world)) {
                    XRayMonitor.sendMessage(player, TextMode.Err, Messages.DefaultWorldNotFound);
                    return;
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
                    int spawner_count = 0;

                    if (instance.getConfig().getString("logging_plugin").equalsIgnoreCase("logblock")) {
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
                        spawner_count = Checkers.lb.oreLookup(name, "spawner", world, hours);
                    }

                    int stones = stone + diorite + andesite + granite;
                    XRayMonitor.sendMessage(player, TextMode.Info, Messages.MsgBorder);
                    XRayMonitor.sendMessage(player, TextMode.Info, Messages.Stones, String.valueOf(stones));

                    String s;
                    ChatColor ccolor;

                    if ((Checkers.instance.config.isActive("diamond")) && (diamond_count > 0)) {
                        float d = (float) (diamond_count * 100.0D / stones);
                        if (d > Checkers.instance.config.getRate("confirmed", "diamond")) {
                            ccolor = TextMode.Err;
                        } else if (d > Checkers.instance.config.getRate("warn", "diamond")) {
                            ccolor = TextMode.Instr;
                        } else {
                            ccolor = TextMode.Success;
                        }
                        level = (int) (level + d * 10.0F);

                        s = d + "000000000";
                        XRayMonitor.sendMessage(player, ccolor, Messages.Diamond, Float.parseFloat(s.substring(0, s.lastIndexOf('.') + 3)) + "% (" + diamond_count + ")");
                    } else {
                        XRayMonitor.sendMessage(player, ChatColor.GRAYz, Messages.Diamond, "-");
                    }

                    if ((Checkers.instance.config.isActive("gold")) && (gold_count > 0)) {
                        float d = (float) (gold_count * 100.0D / stones);
                        if (d > Checkers.instance.config.getRate("confirmed", "gold")) {
                            ccolor = TextMode.Err;
                        } else if (d > Checkers.instance.config.getRate("warn", "gold")) {
                            ccolor = TextMode.Instr;
                        } else {
                            ccolor = TextMode.Success;
                        }
                        level = (int) (level + d * 3.0F);

                        s = d + "000000000";
                        XRayMonitor.sendMessage(player, ccolor, Messages.Gold, Float.parseFloat(s.substring(0, s.lastIndexOf('.') + 3)) + "% (" + gold_count + ")");
                    } else {
                        XRayMonitor.sendMessage(player, ChatColor.GRAY, Messages.Gold, "-");
                    }
                    if ((Checkers.instance.config.isActive("lapis")) && (lapis_count > 0)) {
                        float d = (float) (lapis_count * 100.0D / stones);
                        if (d > Checkers.instance.config.getRate("confirmed", "lapis")) {
                            ccolor = TextMode.Err;
                        } else if (d > Checkers.instance.config.getRate("warn", "lapis")) {
                            ccolor = TextMode.Instr;
                        } else {
                            ccolor = TextMode.Success;
                        }
                        level = (int) (level + d * 10.0F);

                        s = d + "000000000";
                        XRayMonitor.sendMessage(player, ccolor, Messages.Lapis, Float.parseFloat(s.substring(0, s.lastIndexOf('.') + 3)) + "% (" + lapis_count + ")");
                    } else {
                        XRayMonitor.sendMessage(player, ChatColor.GRAY, Messages.Lapis, "-");
                    }
                    if ((Checkers.instance.config.isActive("emerald")) && (emerald_count > 0)) {
                        float d = (float) (emerald_count * 100.0D / stones);
                        if (d > Checkers.instance.config.getRate("confirmed", "emerald")) {
                            ccolor = TextMode.Err;
                        } else if (d > Checkers.instance.config.getRate("warn", "emerald")) {
                            ccolor = TextMode.Instr;
                        } else {
                            ccolor = TextMode.Success;
                        }
                        level = (int) (level + d * 10.0F);

                        s = d + "000000000";
                        XRayMonitor.sendMessage(player, ccolor, Messages.Emerald, Float.parseFloat(s.substring(0, s.lastIndexOf('.') + 3)) + "% (" + emerald_count + ")");

                    } else {
                        XRayMonitor.sendMessage(player, ChatColor.GRAY, Messages.Emerald, "-");
                    }
                    if ((Checkers.instance.config.isActive("iron")) && (iron_count > 0)) {
                        float d = (float) (iron_count * 100.0D / stones);
                        if (d > Checkers.instance.config.getRate("confirmed", "iron")) {
                            ccolor = TextMode.Err;
                        } else if (d > Checkers.instance.config.getRate("warn", "iron")) {
                            ccolor = TextMode.Instr;
                        } else {
                            ccolor = TextMode.Success;
                        }
                        level = (int) (level + d * 1.0F);

                        s = d + "000000000";
                        XRayMonitor.sendMessage(player, ccolor, Messages.Iron, Float.parseFloat(s.substring(0, s.lastIndexOf('.') + 3)) + "% (" + iron_count + ")");
                    } else {
                        XRayMonitor.sendMessage(player, ChatColor.GRAY, Messages.Iron, "-");
                    }
                    if ((Checkers.instance.config.isActive("redstone")) && (redstone_count > 0)) {
                        float d = (float) (redstone_count * 100.0D / stones);
                        if (d > Checkers.instance.config.getRate("confirmed", "redstone")) {
                            ccolor = TextMode.Err;
                        } else if (d > Checkers.instance.config.getRate("warn", "redstone")) {
                            ccolor = TextMode.Instr;
                        } else {
                            ccolor = TextMode.Success;
                        }
                        level = (int) (level + d * 1.0F);

                        s = d + "000000000";
                        XRayMonitor.sendMessage(player, ccolor, Messages.Redstone, Float.parseFloat(s.substring(0, s.lastIndexOf('.') + 3)) + "% (" + redstone_count + ")");

                    } else {
                        XRayMonitor.sendMessage(player, ChatColor.GRAY, Messages.Redstone, "-");
                    }
                    if ((Checkers.instance.config.isActive("coal")) && (coal_count > 0)) {
                        float d = (float) (coal_count * 100.0D / stones);
                        if (d > Checkers.instance.config.getRate("confirmed", "coal")) {
                            ccolor = TextMode.Err;
                        } else if (d > Checkers.instance.config.getRate("warn", "coal")) {
                            ccolor = TextMode.Instr;
                        } else {
                            ccolor = TextMode.Success;
                        }
                        level = (int) (level + d * 1.0F);

                        s = d + "000000000";
                        XRayMonitor.sendMessage(player, ccolor, Messages.Coal, Float.parseFloat(s.substring(0, s.lastIndexOf('.') + 3)) + "% (" + coal_count + ")");
                    } else {
                        XRayMonitor.sendMessage(player, ChatColor.GRAY, Messages.Coal, "-");
                    }
                    if ((Checkers.instance.config.isActive("mossy")) && (mossy_count > 0)) {
                        float d = (float) (mossy_count * 100.0D / stones);
                        if (d > Checkers.instance.config.getRate("confirmed", "mossy")) {
                            ccolor = TextMode.Err;
                        } else if (d > Checkers.instance.config.getRate("warn", "mossy")) {
                            ccolor = TextMode.Instr;
                        } else {
                            ccolor = TextMode.Success;
                        }
                        level = (int) (level + d * 7.0F);

                        s = d + "000000000";
                        XRayMonitor.sendMessage(player, ccolor, Messages.Mossy, Float.parseFloat(s.substring(0, s.lastIndexOf('.') + 3)) + "% (" + mossy_count + ")");
                    } else {
                        XRayMonitor.sendMessage(player, ChatColor.GRAY, Messages.Mossy, "-");
                    }
                    if ((Checkers.instance.config.isActive("spawners")) && (spawner_count > 0)) {
                        float d = (float) (spawner_count * 100.0D / stones);
                        if (d > Checkers.instance.config.getRate("confirmed", "spawners")) {
                            ccolor = TextMode.Err;
                        } else if (d > Checkers.instance.config.getRate("warn", "spawners")) {
                            ccolor = TextMode.Instr;
                        } else {
                            ccolor = TextMode.Success;
                        }
                        level = (int) (level + d * 9.0F);

                        s = d + "000000000";
                        XRayMonitor.sendMessage(player, ccolor, Messages.Spawners, Float.parseFloat(s.substring(0, s.lastIndexOf('.') + 3)) + "% (" + spawner_count + ")");
                    } else {
                        XRayMonitor.sendMessage(player, ChatColor.GRAY, Messages.Spawners, "-");
                    }

                    // Adjust level for new players
                    if (stones < 500) {
                        level = (int) (level * 0.5D);
                    } else if (stones > 1000) {
                        level *= 2;
                    }
                    notifyXrayLevel(player, level);
                    XRayMonitor.sendMessage(player, TextMode.Info, Messages.MsgBorder);
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

        }.runTaskAsynchronously(instance);
    }

    public void checkSingle(final String name, final CommandSender sender, final String oreName, final String world, final int hours) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    final Material mat = Material.matchMaterial(oreName);
                    Player player = XRayMonitor.isSenderPlayer(sender);
                    if (mat == null) {
                        XRayMonitor.sendMessage(player, TextMode.Err, Messages.NoMaterial, oreName);
                        throw new IllegalArgumentException("No material matching: '" + oreName + "'");
                    }

                    XRayMonitor.sendMessage(player, TextMode.Info, Messages.CalcPlayerOre, TextMode.Warn + name);
                    XRayMonitor.sendMessage(player, TextMode.Info, Messages.PleaseBePatient);
                    int stone = 0;
                    int diorite = 0;
                    int andesite = 0;
                    int granite = 0;
                    int count_xyz = 0;
                    if (Checkers.instance.getConfig().getString("logging_plugin").equalsIgnoreCase("logblock")) {
                        stone = Checkers.lb.oreLookup(name, "stone", world, hours);
                        diorite = Checkers.lb.oreLookup(name, "diorite", world, hours);
                        andesite = Checkers.lb.oreLookup(name, "andesite", world, hours);
                        granite = Checkers.lb.oreLookup(name, "granite", world, hours);

                        count_xyz = Checkers.lb.oreLookup(name, oreName, world, hours);
                    }

                    int stones = stone + diorite + andesite + granite;
                    XRayMonitor.sendMessage(player, TextMode.Info, Messages.MsgBorder);
                    XRayMonitor.sendMessage(player, TextMode.Info, Messages.Stones, String.valueOf(stones));


                    String s = "";
                    if (count_xyz > 0) {
                        float d = (float) (count_xyz * 100.0D / stones);
                        s = d + "000000000";
                        XRayMonitor.sendMessage(player, TextMode.Info, Messages.CustomOre, oreName, Float.parseFloat(s.substring(0, s.lastIndexOf('.') + 3)) + "% (" + String.valueOf(count_xyz) + ")");
                    } else {
                        XRayMonitor.sendMessage(player, ChatColor.WHITE, Messages.CustomOre, oreName, "-");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(instance);
    }

    public void checkAllOnlinePlayers(CommandSender sender, String world, String oreName, float maxrate, int hours) {
        List<String[]> playerOreStone = new ArrayList();
        final Material mat = Material.matchMaterial(oreName);
        Player player = XRayMonitor.isSenderPlayer(sender);
        if (mat == null) {
            XRayMonitor.sendMessage(player, TextMode.Err, Messages.NoMaterial, oreName);
            throw new IllegalArgumentException("No material matching: '" + oreName + "'");
        }
        if (Objects.requireNonNull(instance.getConfig().getString("logging_plugin")).equalsIgnoreCase("logblock")) {
            XRayMonitor.sendMessage(player, TextMode.Info, Messages.CalcAllPlayersOreRate, mat.toString(), String.valueOf(maxrate));
            playerOreStone = lb.playerLookup(sender, oreName, world);
        }
        XRayMonitor.sendMessage(player, TextMode.Info, Messages.MsgBorder);
        XRayMonitor.sendMessage(player, TextMode.Info, Messages.AllPlayersOnOre, mat.toString());
        XRayMonitor.sendMessage(player, TextMode.Info, Messages.MsgBorder);
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
        XRayMonitor.sendMessage(player, TextMode.Info, Messages.MsgBorder);
    }
}
