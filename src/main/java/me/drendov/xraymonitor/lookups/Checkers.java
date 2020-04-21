package me.drendov.xraymonitor.lookups;

import me.drendov.xraymonitor.XRayMonitor;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

public class Checkers
{
    private static XRayMonitor plugin = XRayMonitor.getInstance();
    private static LogBlockLookup lb = new LogBlockLookup();

    public void checkGlobal(final String name, final CommandSender sender, final String world, final int hours)
    {
        new BukkitRunnable()
        {
            @Override
            public void run()
            {

                if ( ! Checkers.plugin.checkWorld(world)) {
                    sender.sendMessage("Please check config.yml - your configured world seems not to exist?");
                }
                try
                {
                    sender.sendMessage(ChatColor.GREEN + "[XRayMonitor] Calculating ore ratios for " + ChatColor.GOLD + name + ".");
                    sender.sendMessage(ChatColor.GREEN + "[XRayMonitor] Please be patient, this may take a minute.");
                    int level = 0;
                    int count_stone = 0;

                    int diamond_count = 0;
                    int gold_count = 0;
                    int lapis_count = 0;
                    int iron_count = 0;
                    int coal_count = 0;
                    int redstone_count = 0;
                    int mossy_count = 0;
                    int emerald_count = 0;
                    int spawner_count = 0;
                    if (plugin.getConfig().getString("logging_plugin").equalsIgnoreCase("logblock"))
                    {
                        count_stone = Checkers.lb.oreLookup(name, "stone", world, hours);
                        diamond_count = Checkers.lb.oreLookup(name, "diamond_ore", world, hours);
                        gold_count = Checkers.lb.oreLookup(name, "gold_ore", world, hours);
                        lapis_count = Checkers.lb.oreLookup(name, "lapis_ore", world, hours);
                        iron_count = Checkers.lb.oreLookup(name, "iron_ore", world, hours);
                        redstone_count = Checkers.lb.oreLookup(name, "redstone_ore", world, hours);
                        coal_count = Checkers.lb.oreLookup(name, "coal_ore", world, hours);
                        mossy_count = Checkers.lb.oreLookup(name, "mossy_cobblestone", world, hours);
                        emerald_count = Checkers.lb.oreLookup(name,"emerald_ore", world, hours);
                        spawner_count = Checkers.lb.oreLookup(name, "spawner", world, hours);
                    }

                    //sender.sendMessage(ChatColor.GREEN + "xraymonitor: " + ChatColor.GOLD + name);
                    sender.sendMessage(Checkers.plugin.msgBorder);
                    sender.sendMessage("Stones: " + String.valueOf(count_stone));

                    String s = "";
                    ChatColor ccolor = ChatColor.GREEN;
                    if ((Checkers.plugin.config.isActive("diamond")) && (diamond_count > 0))
                    {
                        float d = (float)(diamond_count * 100.0D / count_stone);
                        if (d > Checkers.plugin.config.getRate("confirmed", "diamond")) {
                            ccolor = ChatColor.RED;
                        } else if (d > Checkers.plugin.config.getRate("warn", "diamond")) {
                            ccolor = ChatColor.YELLOW;
                        } else {
                            ccolor = ChatColor.GREEN;
                        }
                        level = (int)(level + d * 10.0F);

                        s = String.valueOf(d) + "000000000";
                        sender.sendMessage(ccolor + "Diamond: " + String.valueOf(Float.parseFloat(s.substring(0, s.lastIndexOf('.') + 3))) + "% (" + String.valueOf(diamond_count) + ")");
                    }
                    else
                    {
                        sender.sendMessage("Diamond: -");
                    }
                    if ((Checkers.plugin.config.isActive("gold")) && (gold_count > 0))
                    {
                        float d = (float)(gold_count * 100.0D / count_stone);
                        if (d > Checkers.plugin.config.getRate("confirmed", "gold")) {
                            ccolor = ChatColor.RED;
                        } else if (d > Checkers.plugin.config.getRate("warn", "gold")) {
                            ccolor = ChatColor.YELLOW;
                        } else {
                            ccolor = ChatColor.GREEN;
                        }
                        level = (int)(level + d * 3.0F);

                        s = String.valueOf(d) + "000000000";
                        sender.sendMessage(ccolor + "Gold: " + String.valueOf(Float.parseFloat(s.substring(0, s.lastIndexOf('.') + 3))) + "% (" + String.valueOf(gold_count) + ")");
                    }
                    else
                    {
                        sender.sendMessage("Gold: -");
                    }
                    if ((Checkers.plugin.config.isActive("lapis")) && (lapis_count > 0))
                    {
                        float d = (float)(lapis_count * 100.0D / count_stone);
                        if (d > Checkers.plugin.config.getRate("confirmed", "lapis")) {
                            ccolor = ChatColor.RED;
                        } else if (d > Checkers.plugin.config.getRate("warn", "lapis")) {
                            ccolor = ChatColor.YELLOW;
                        } else {
                            ccolor = ChatColor.GREEN;
                        }
                        level = (int)(level + d * 10.0F);

                        s = String.valueOf(d) + "000000000";
                        sender.sendMessage(ccolor + "Lapis: " + String.valueOf(Float.parseFloat(s.substring(0, s.lastIndexOf('.') + 3))) + "% (" + String.valueOf(lapis_count) + ")");
                    }
                    else
                    {
                        sender.sendMessage("Lapis: -");
                    }
                    if ((Checkers.plugin.config.isActive("emerald")) && (emerald_count > 0))
                    {
                        float d = (float)(emerald_count * 100.0D / count_stone);
                        if (d > Checkers.plugin.config.getRate("confirmed", "emerald")) {
                            ccolor = ChatColor.RED;
                        } else if (d > Checkers.plugin.config.getRate("warn", "emerald")) {
                            ccolor = ChatColor.YELLOW;
                        } else {
                            ccolor = ChatColor.GREEN;
                        }
                        level = (int)(level + d * 10.0F);

                        s = String.valueOf(d) + "000000000";
                        sender.sendMessage(ccolor + "Emerald: " + String.valueOf(Float.parseFloat(s.substring(0, s.lastIndexOf('.') + 3))) + "% (" + String.valueOf(emerald_count) + ")");
                    }
                    else
                    {
                        sender.sendMessage("Emerald: -");
                    }
                    if ((Checkers.plugin.config.isActive("iron")) && (iron_count > 0))
                    {
                        float d = (float)(iron_count * 100.0D / count_stone);
                        if (d > Checkers.plugin.config.getRate("confirmed", "iron")) {
                            ccolor = ChatColor.RED;
                        } else if (d > Checkers.plugin.config.getRate("warn", "iron")) {
                            ccolor = ChatColor.YELLOW;
                        } else {
                            ccolor = ChatColor.GREEN;
                        }
                        level = (int)(level + d * 1.0F);

                        s = String.valueOf(d) + "000000000";
                        sender.sendMessage(ccolor + "Iron: " + String.valueOf(Float.parseFloat(s.substring(0, s.lastIndexOf('.') + 3))) + "% (" + String.valueOf(iron_count) + ")");
                    }
                    else
                    {
                        sender.sendMessage("Iron: -");
                    }
                    if ((Checkers.plugin.config.isActive("redstone")) && (redstone_count > 0))
                    {
                        float d = (float)(redstone_count * 100.0D / count_stone);
                        if (d > Checkers.plugin.config.getRate("confirmed", "redstone")) {
                            ccolor = ChatColor.RED;
                        } else if (d > Checkers.plugin.config.getRate("warn", "redstone")) {
                            ccolor = ChatColor.YELLOW;
                        } else {
                            ccolor = ChatColor.GREEN;
                        }
                        level = (int)(level + d * 1.0F);

                        s = String.valueOf(d) + "000000000";
                        sender.sendMessage(ccolor + "Redstone: " + String.valueOf(Float.parseFloat(s.substring(0, s.lastIndexOf('.') + 3))) + "% (" + String.valueOf(redstone_count) + ")");
                    }
                    else
                    {
                        sender.sendMessage("Redstone: -");
                    }
                    if ((Checkers.plugin.config.isActive("coal")) && (coal_count > 0))
                    {
                        float d = (float)(coal_count * 100.0D / count_stone);
                        if (d > Checkers.plugin.config.getRate("confirmed", "coal")) {
                            ccolor = ChatColor.RED;
                        } else if (d > Checkers.plugin.config.getRate("warn", "coal")) {
                            ccolor = ChatColor.YELLOW;
                        } else {
                            ccolor = ChatColor.GREEN;
                        }
                        level = (int)(level + d * 1.0F);

                        s = String.valueOf(d) + "000000000";
                        sender.sendMessage(ccolor + "Coal: " + String.valueOf(Float.parseFloat(s.substring(0, s.lastIndexOf('.') + 3))) + "% (" + String.valueOf(coal_count) + ")");
                    }
                    else
                    {
                        sender.sendMessage("Iron: -");
                    }
                    if ((Checkers.plugin.config.isActive("mossy")) && (mossy_count > 0))
                    {
                        float d = (float)(mossy_count * 100.0D / count_stone);
                        if (d > Checkers.plugin.config.getRate("confirmed", "mossy")) {
                            ccolor = ChatColor.RED;
                        } else if (d > Checkers.plugin.config.getRate("warn", "mossy")) {
                            ccolor = ChatColor.YELLOW;
                        } else {
                            ccolor = ChatColor.GREEN;
                        }
                        level = (int)(level + d * 7.0F);

                        s = String.valueOf(d) + "000000000";
                        sender.sendMessage(ccolor + "Mossy: " + String.valueOf(Float.parseFloat(s.substring(0, s.lastIndexOf('.') + 3))) + "% (" + String.valueOf(mossy_count) + ")");
                    }
                    else
                    {
                        sender.sendMessage("Mossy: -");
                    }
                    if ((Checkers.plugin.config.isActive("spawners")) && (spawner_count > 0))
                    {
                        float d = (float)(spawner_count * 100.0D / count_stone);
                        if (d > Checkers.plugin.config.getRate("confirmed", "spawners")) {
                            ccolor = ChatColor.RED;
                        } else if (d > Checkers.plugin.config.getRate("warn", "spawners")) {
                            ccolor = ChatColor.YELLOW;
                        } else {
                            ccolor = ChatColor.GREEN;
                        }
                        level = (int)(level + d * 9.0F);

                        s = String.valueOf(d) + "000000000";
                        sender.sendMessage(ccolor + "Spawners: " + String.valueOf(Float.parseFloat(s.substring(0, s.lastIndexOf('.') + 3))) + "% (" + String.valueOf(spawner_count) + ")");
                    }
                    else
                    {
                        sender.sendMessage("Spawners: -");
                    }
                    if (count_stone < 500) {
                        level = (int)(level * 0.5D);
                    } else if (count_stone > 1000) {
                        level *= 2;
                    }
                    if (level < 45) {
                        sender.sendMessage(ChatColor.GREEN + "xLevel: " + level + " (Xray use is very unlikely)");
                    }
                    if ((level >= 45) && (level < 85)) {
                        sender.sendMessage(ChatColor.GREEN + "xLevel: " + level + " (Xray use is unlikely)");
                    }
                    if ((level >= 85) && (level < 130)) {
                        sender.sendMessage(ChatColor.YELLOW + "xLevel: " + level + " (Medium Chance of Xray)");
                    }
                    if ((level >= 130) && (level < 170)) {
                        sender.sendMessage(ChatColor.RED + "xLevel: " + level + " (High Chance of Xray)");
                    }
                    if (level >= 170) {
                        sender.sendMessage(ChatColor.DARK_RED + "xLevel: " + level + " (Very High Chance of Xray)");
                    }
                    sender.sendMessage(Checkers.plugin.msgBorder);
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    public void checkSingle(final String name, final CommandSender sender, final String oreName, final String world, final int hours)
    {
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                try
                {
                    sender.sendMessage(ChatColor.GREEN + "[XRayMonitor] Calculating ore ratios for " + ChatColor.GOLD + name + ".");
                    sender.sendMessage(ChatColor.GREEN + "[XRayMonitor] Please be patient, this may take a minute.");
                    int count_stone = 0;
                    int count_xyz = 0;
                    if (Checkers.plugin.getConfig().getString("logging_plugin").equalsIgnoreCase("logblock"))
                    {
                        count_stone = Checkers.lb.oreLookup(name, "stone", world, hours);
                        count_xyz = Checkers.lb.oreLookup(name, oreName, world, hours);
                    }

                    String mat_1_name = Material.getMaterial(oreName).toString();

                    sender.sendMessage(Checkers.plugin.msgBorder);
                    sender.sendMessage(ChatColor.GREEN + "[xraymonitor: " + ChatColor.GOLD + name);
                    sender.sendMessage(Checkers.plugin.msgBorder);
                    sender.sendMessage("Stones: " + String.valueOf(count_stone));

                    String s = "";
                    if (count_xyz > 0)
                    {
                        float d = (float)(count_xyz * 100.0D / count_stone);
                        s = String.valueOf(d) + "000000000";
                        sender.sendMessage(mat_1_name + ": " + String.valueOf(Float.parseFloat(s.substring(0, s.lastIndexOf('.') + 3))) + "% (" + String.valueOf(count_xyz) + ")");
                    }
                    else
                    {
                        sender.sendMessage(mat_1_name + ": -");
                    }
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    public void listAllXRayers(CommandSender sender, String world, String oreName, float maxrate, int hours)
    {
        List<String[]> playerOreStone = new ArrayList();
        if (plugin.getConfig().getString("logging_plugin").equalsIgnoreCase("logblock"))
        {
            sender.sendMessage(ChatColor.GREEN + "[XRayMonitor] Searching for players with a " + Material.getMaterial(oreName).toString() + " rate higher than " + maxrate + ".");
            playerOreStone = lb.playerLookup(sender, oreName, world);
        }
        sender.sendMessage(plugin.msgBorder);
        sender.sendMessage(ChatColor.GREEN + "[XRayMonitor] All players on " + Material.getMaterial(oreName).toString());
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
