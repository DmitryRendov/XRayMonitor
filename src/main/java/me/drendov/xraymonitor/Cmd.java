package me.drendov.xraymonitor;

import me.drendov.xraymonitor.lookups.Checkers;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Cmd
        implements CommandExecutor {
    private XRayMonitor plugin = XRayMonitor.getInstance();
    private Checkers checker = new Checkers();

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        // xcheck
        if (cmd.getName().equalsIgnoreCase("xcheck")) {
            if (sender.hasPermission("xcheck.check") || sender.isOp()) {
                String player = "";
                if (args.length > 0) {
                    if (!args[0].contains(":")) {
                        player = args[0];
                    }
                } else {
                    this.plugin.showInfo(sender);
                    return true;
                }
                String world = "";
                int hours = -1;
                String oreName = "";
                String bantype = "none";
                float maxrate = 0.0f;
                HashMap<String, String> hm = new HashMap<String, String>();
                String[] nonPlayerArgs = new String[args.length];
                try {
                    int i;
                    if (!args[0].contains(":")) {
                        if (args[0].equals("clear")) {
                            for (i = 2; i < args.length; ++i) {
                                nonPlayerArgs[i - 2] = args[i];
                            }
                        } else {
                            for (i = 1; i < args.length; ++i) {
                                nonPlayerArgs[i - 1] = args[i];
                            }
                        }
                    } else {
                        for (i = 0; i < args.length; ++i) {
                            nonPlayerArgs[i] = args[i];
                        }
                    }
                    for (String arg : nonPlayerArgs) {
                        if (arg == null) break;
                        String[] tokens = arg.split(":");
                        hm.put(tokens[0], tokens[1]);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (hm.containsKey("maxrate")) {
                    maxrate = Float.parseFloat(hm.get("maxrate"));
                }
                if (hm.containsKey("since")) {
                    hours = Integer.parseInt(hm.get("since"));
                }

                if (hm.containsKey("world")) {
                    world = hm.get("world");
                    if ( ! plugin.checkWorld(world) ) {
                        sender.sendMessage(ChatColor.RED + "[XRayMonitor]" + ChatColor.WHITE + " This world does not exist. Please check your world-parameter.");
                        return true;
                    }
                }
                if (hm.containsKey("ore")) {
                    oreName = hm.get("ore");
                }
                if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
                    this.plugin.config.load();
                    sender.sendMessage( ChatColor.RED + "[XRayMonitor]" +  ChatColor.WHITE + " Config reloaded.");
                    return true;
                }
                if (args.length == 1 && args[0].equalsIgnoreCase("help")) {
                    this.plugin.showHelp(sender);
                    return true;
                }
                if (args.length == 2 && args[0].equalsIgnoreCase("clear")) {
                    if (sender.hasPermission("xcheck.clear") || sender.isOp()) {
                        try {
                            this.plugin.clearPlayer(sender, args[1]);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return true;
                    }
                    sender.sendMessage( ChatColor.RED + "[XRayMonitor]" +  ChatColor.WHITE + " You do not have permission for this command.");
                    return true;
                }
                if (player.length() == 0) {
                    this.plugin.showInfo(sender);
                    return true;
                }
                if (world.length() == 0 && oreName.isEmpty()) {
                    try {
                        if (ClearedPlayerFile.wasPlayerCleared(player)) {
                            hours = ClearedPlayerFile.getHoursFromClear(player);
                        }
                        world = Config.defaultWorld;
                        if ( ! plugin.checkWorld(world) ) {
                            sender.sendMessage(ChatColor.RED + "[XRayMonitor]" + ChatColor.WHITE + " Default world does not exist. Please check your configuration file.");
                            return true;
                        }
                        this.checker.checkGlobal(player, sender, world, hours);
                        return true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (world.length() > 0 && oreName.isEmpty()) {
                    try {
                        if ( ! plugin.checkWorld(world) ) {
                            sender.sendMessage(ChatColor.RED + "[XRayMonitor]" + ChatColor.WHITE + " This world does not exist. Please check your world-parameter.");
                            return true;
                        }
                        if (ClearedPlayerFile.wasPlayerCleared(player)) {
                            hours = ClearedPlayerFile.getHoursFromClear(player);
                        }
                        this.checker.checkGlobal(player, sender, world, hours);
                        return true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (world.length() > 0 && !oreName.isEmpty()) {
                    if (player.equalsIgnoreCase("all") && maxrate > 0.0f) {
                        new Thread(new CustomRunnable(sender, world, oreName, maxrate, hours) {

                            @Override
                            public void run() {
                                Cmd.this.checker.listAllXRayers(this.sender, this.world, this.oreName, this.maxrate, this.hours);
                            }
                        }).start();
                        return true;
                    }
                    if (ClearedPlayerFile.wasPlayerCleared(player)) {
                        hours = ClearedPlayerFile.getHoursFromClear(player);
                    }
                    this.checker.checkSingle(player, sender, oreName, world, hours);
                    return true;
                }
                if (world.length() == 0 && !oreName.isEmpty()) {
                    world = Config.defaultWorld;
                    if (player.equalsIgnoreCase("all") && maxrate > 0.0f) {
                        final CommandSender s = sender;
                        final String w = world;
                        final String on = oreName;
                        final float mr = maxrate;
                        final int h = hours;
                        new BukkitRunnable() {
                            public void run() {
                                Cmd.this.checker.listAllXRayers(s, w, on, mr, h);
                            }
                        }.runTaskAsynchronously(this.plugin);
                        return true;
                    }
                    if (ClearedPlayerFile.wasPlayerCleared(player)) {
                        hours = ClearedPlayerFile.getHoursFromClear(player);
                    }
                    this.checker.checkSingle(player, sender, oreName, world, hours);
                    return true;
                }
            } else {
                sender.sendMessage( ChatColor.RED + "[XRayMonitor]" +  ChatColor.WHITE + " You do not have permission for this command.");
                return true;
            }
        }
        return false;
    }

}

