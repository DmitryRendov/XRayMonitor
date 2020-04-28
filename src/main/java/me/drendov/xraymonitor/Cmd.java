package me.drendov.xraymonitor;

import me.drendov.xraymonitor.lookups.Checkers;

import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Cmd
        implements CommandExecutor {
    private static XRayMonitor plugin = XRayMonitor.getInstance();
    private Checkers checker = new Checkers();
    private static Logger logger = plugin.getLogger();

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
                        logger.info(ChatColor.RED + "[DEBUG]" + ChatColor.WHITE + "  TK0=" + tokens[0] + "  TK1=" + tokens[1]);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (hm.containsKey("maxrate")) {
                    maxrate = Float.parseFloat(hm.get("maxrate"));
                    logger.info(ChatColor.RED + "[DEBUG]" + ChatColor.WHITE + " maxrate=" + maxrate);
                }
                if (hm.containsKey("since")) {
                    hours = Integer.parseInt(hm.get("since"));
                    logger.info(ChatColor.RED + "[DEBUG]" + ChatColor.WHITE + " hours=" + hours);
                }
                if (hm.containsKey("world")) {
                    world = hm.get("world");
                    logger.info(ChatColor.RED + "[DEBUG]" + ChatColor.WHITE + " world=" + world);
                    if ( ! plugin.checkWorld(world) ) {
                        sender.sendMessage(ChatColor.RED + "[XRayMonitor]" + ChatColor.WHITE + " This world does not exist. Please check your world-parameter.");
                        return true;
                    }
                }
                if (hm.containsKey("ore")) {
                    oreName = hm.get("ore");
                    logger.info("DEBUG: oreName=" + oreName);
                }
                if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
                    plugin.config.load();
                    sender.sendMessage( ChatColor.RED + "[XRayMonitor]" +  ChatColor.WHITE + " Config reloaded.");
                    return true;
                }
                if (args.length == 1 && args[0].equalsIgnoreCase("help")) {
                    plugin.showHelp(sender);
                    return true;
                }
                if (args.length == 2 && args[0].equalsIgnoreCase("clear")) {
                    if (sender.hasPermission("xcheck.clear") || sender.isOp()) {
                        try {
                            plugin.clearPlayer(sender, args[1]);
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
                            logger.info(ChatColor.RED + "[DEBUG]" + ChatColor.WHITE + " hours for cleared player=" + hours);
                        }
                        world = Config.defaultWorld;
                        if ( world != null && ! plugin.checkWorld(world) ) {
                            sender.sendMessage(ChatColor.RED + "[XRayMonitor]" + ChatColor.WHITE + " Default world does not exist. Please check your configuration file.");
                            return true;
                        }

                        logger.info(ChatColor.RED + "[DEBUG]" + ChatColor.WHITE + " Run checkGlobal with: player=" + player + " sender=" + sender + " world=" + world + " hours=" + hours);
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
                        logger.info(ChatColor.RED + "[DEBUG]" + ChatColor.WHITE + " Run checkGlobal with: player=" + player + " sender=" + sender + " world=" + world + " hours=" + hours);
                        this.checker.checkGlobal(player, sender, world, hours);
                        return true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (world.length() > 0 && !oreName.isEmpty()) {
                    if (player.equalsIgnoreCase("all") && maxrate > 0.0f) {
                        logger.info(ChatColor.RED + "[DEBUG]" + ChatColor.WHITE + " Run listAllXRayers with: sender=" + sender + " world=" + world + " oreName=" + oreName + " maxrate=" + maxrate + " hours=" + hours);
                        new Thread(new CustomRunnable(sender, world, oreName, maxrate, hours) {

                            @Override
                            public void run() {
                                logger.info(ChatColor.RED + "[DEBUG]" + ChatColor.WHITE + " Run listAllXRayers with: sender=" + this.sender + " this.world=" + this.world + " this.oreName=" + this.oreName + " this.maxrate=" + this.maxrate + " this.hours=" + this.hours);
                                Cmd.this.checker.listAllXRayers(this.sender, this.world, this.oreName, this.maxrate, this.hours);
                            }
                        }).start();
                        return true;
                    }
                    if (ClearedPlayerFile.wasPlayerCleared(player)) {
                        hours = ClearedPlayerFile.getHoursFromClear(player);
                    }
                    logger.info(ChatColor.RED + "[DEBUG]" + ChatColor.WHITE + " Run checkSingle with: player=" + player + " sender=" + sender + " oreName=" + oreName + " world=" + world + " hours=" + hours);
                    this.checker.checkSingle(player, sender, oreName, world, hours);
                    return true;
                }
                if (world.length() == 0 && !oreName.isEmpty()) {
                    world = Config.defaultWorld;
                    if ( world != null && ! plugin.checkWorld(world) ) {
                        sender.sendMessage(ChatColor.RED + "[XRayMonitor]" + ChatColor.WHITE + " Default world does not exist. Please check your configuration file.");
                        return true;
                    }
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

