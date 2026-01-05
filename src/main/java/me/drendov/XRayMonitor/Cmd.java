package me.drendov.XRayMonitor;

import me.drendov.XRayMonitor.lookups.Checkers;

import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Cmd
        implements CommandExecutor {
    private static XRayMonitor plugin = XRayMonitor.getInstance();
    private Checkers checker = new Checkers();
    private static Logger logger = plugin.getLogger();

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        Player player = null;
        if (sender instanceof Player) {
            player = (Player) sender;
        }

        // xrm
        if (cmd.getName().equalsIgnoreCase("xrm")) {
            if (sender.hasPermission("xrm.check") || sender.isOp()) {
                
                // Handle subcommands
                if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
                    plugin.config.load();
                    XRayMonitor.sendMessage(player, TextMode.Success, Messages.Reloaded);
                    return true;
                }
                if (args.length == 1 && args[0].equalsIgnoreCase("help")) {
                    plugin.showHelp(sender);
                    return true;
                }
                if (args.length == 2 && args[0].equalsIgnoreCase("clear")) {
                    if (sender.hasPermission("xrm.clear") || sender.isOp()) {
                        try {
                            plugin.clearPlayer(sender, args[1]);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return true;
                    }
                    XRayMonitor.sendMessage(player, TextMode.Err, Messages.NoPermissionForCommand);
                    return true;
                }

                // Handle "check" subcommand - shift args if it's present
                String[] shiftedArgs = args;
                if (args.length > 0 && args[0].equalsIgnoreCase("check")) {
                    if (args.length < 2) {
                        plugin.showInfo(sender);
                        return true;
                    }
                    // Remove "check" from args, shift everything down
                    shiftedArgs = new String[args.length - 1];
                    System.arraycopy(args, 1, shiftedArgs, 0, args.length - 1);
                }

                String playerName = "";
                if (shiftedArgs.length > 0) {
                    if (!shiftedArgs[0].contains(":")) {
                        playerName = shiftedArgs[0];
                    }
                } else {
                    plugin.showInfo(sender);
                    return true;
                }

                String world = "";
                int hours = -1;
                String oreName = "";
                float rate = 0.0f;
                HashMap<String, String> hm = new HashMap<String, String>();
                String[] nonPlayerArgs = new String[shiftedArgs.length];
                try {
                    int i;
                    if (!shiftedArgs[0].contains(":")) {
                        for (i = 1; i < shiftedArgs.length; ++i) {
                            nonPlayerArgs[i - 1] = shiftedArgs[i];
                        }
                    } else {
                        for (i = 0; i < shiftedArgs.length; ++i) {
                            nonPlayerArgs[i] = shiftedArgs[i];
                        }
                    }
                    for (String arg : nonPlayerArgs) {
                        if (arg == null) break;
                        String[] tokens = arg.split(":");
                        if (tokens.length == 2) {
                            hm.put(tokens[0], tokens[1]);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (hm.containsKey("rate")) {
                    rate = Float.parseFloat(hm.get("rate"));
                    if (plugin.config.isDebug()) {
                        logger.info(ChatColor.RED + "[DEBUG]" + ChatColor.WHITE + " rate=" + rate);
                    }
                    if ( rate <= 0 ) {
                        XRayMonitor.sendMessage(player, TextMode.Err, Messages.ErrRatePositive);
                        return true;
                    }
                }
                if (hm.containsKey("since")) {
                    hours = Integer.parseInt(hm.get("since"));
                    if (plugin.config.isDebug()) {
                        logger.info(ChatColor.RED + "[DEBUG]" + ChatColor.WHITE + " hours=" + hours);
                    }
                }
                if (hm.containsKey("world")) {
                    world = hm.get("world");
                    if (plugin.config.isDebug()) {
                        logger.info(ChatColor.RED + "[DEBUG]" + ChatColor.WHITE + " world=" + world);
                    }
                    if (!plugin.checkWorld(world)) {
                        XRayMonitor.sendMessage(player, TextMode.Err, Messages.WorldNotFound);
                        return true;
                    }
                }
                if (hm.containsKey("ore")) {
                    oreName = hm.get("ore");
                    if (plugin.config.isDebug()) {
                        logger.info(ChatColor.RED + "[DEBUG]" + ChatColor.WHITE + " oreName=" + oreName);
                    }
                }

                if (playerName.length() == 0) {
                    this.plugin.showInfo(sender);
                    return true;
                }
                if (world.length() == 0 && oreName.isEmpty()) {
                    try {
                        if (ClearedPlayerFile.wasPlayerCleared(playerName)) {
                            hours = ClearedPlayerFile.getHoursFromClear(playerName);
                            if (plugin.config.isDebug()) {
                                logger.info(ChatColor.RED + "[DEBUG]" + ChatColor.WHITE + " hours for cleared playerName=" + hours);
                            }
                        }
                        world = Config.defaultWorld;
                        if (world != null && !plugin.checkWorld(world)) {
                            XRayMonitor.sendMessage(player, TextMode.Err, Messages.DefaultWorldNotFound);
                            return true;
                        }

                        if (plugin.config.isDebug()) {
                            logger.info(ChatColor.RED + "[DEBUG]" + ChatColor.WHITE + " Run global check for " + playerName + " world=" + world + " hours=" + hours);
                        }
                        this.checker.checkGlobal(playerName, sender, world, hours);
                        return true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (world.length() > 0 && oreName.isEmpty()) {
                    try {
                        if (!plugin.checkWorld(world)) {
                            XRayMonitor.sendMessage(player, TextMode.Err, Messages.WorldNotFound);
                            return true;
                        }
                        if (ClearedPlayerFile.wasPlayerCleared(playerName)) {
                            hours = ClearedPlayerFile.getHoursFromClear(playerName);
                        }
                        if (plugin.config.isDebug()) {
                            logger.info(ChatColor.RED + "[DEBUG]" + ChatColor.WHITE + " Run global check for " + playerName + " sender=" + sender + " world=" + world + " hours=" + hours);
                        }
                        this.checker.checkGlobal(playerName, sender, world, hours);
                        return true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (world.length() > 0 && !oreName.isEmpty()) {
                    if (playerName.equalsIgnoreCase("all") && rate > 0.0f) {
                        if (plugin.config.isDebug()) {
                            logger.info(ChatColor.RED + "[DEBUG]" + ChatColor.WHITE + " List All XRay-ers check for world=" + world + " oreName=" + oreName + " rate=" + rate + " hours=" + hours);
                        }
                        new Thread(new CustomRunnable(sender, world, oreName, rate, hours) {

                            @Override
                            public void run() {
                                if (plugin.config.isDebug()) {
                                    logger.info(ChatColor.RED + "[DEBUG]" + ChatColor.WHITE + " List All XRay-ers check for world=" + this.world + " this.oreName=" + this.oreName + " this.rate=" + this.rate + " this.hours=" + this.hours);
                                }
                                Cmd.this.checker.listAllXRayers(this.sender, this.world, this.oreName, this.rate, this.hours);
                            }
                        }).start();
                        return true;
                    }
                    if (ClearedPlayerFile.wasPlayerCleared(playerName)) {
                        hours = ClearedPlayerFile.getHoursFromClear(playerName);
                    }
                    if (plugin.config.isDebug()) {
                        logger.info(ChatColor.RED + "[DEBUG]" + ChatColor.WHITE + " Run checkSingle for " + playerName + " oreName=" + oreName + " world=" + world + " hours=" + hours);
                    }
                    this.checker.checkSingle(playerName, sender, oreName, world, hours);
                    return true;
                }
                if (world.length() == 0 && !oreName.isEmpty()) {
                    world = Config.defaultWorld;
                    if (world != null && !plugin.checkWorld(world)) {
                        XRayMonitor.sendMessage(player, TextMode.Err, Messages.DefaultWorldNotFound);
                        return true;
                    }
                    if (playerName.equalsIgnoreCase("all") && rate > 0.0f) {
                        final CommandSender s = sender;
                        final String w = world;
                        final String on = oreName;
                        final float mr = rate;
                        final int h = hours;
                        new BukkitRunnable() {
                            public void run() {
                                Cmd.this.checker.listAllXRayers(s, w, on, mr, h);
                            }
                        }.runTaskAsynchronously(plugin);
                        return true;
                    }
                    if (ClearedPlayerFile.wasPlayerCleared(playerName)) {
                        hours = ClearedPlayerFile.getHoursFromClear(playerName);
                    }
                    this.checker.checkSingle(playerName, sender, oreName, world, hours);
                    return true;
                }
            } else {
                XRayMonitor.sendMessage(player, TextMode.Err, Messages.NoPermissionForCommand);
                return true;
            }
        }
        return false;
    }

}

