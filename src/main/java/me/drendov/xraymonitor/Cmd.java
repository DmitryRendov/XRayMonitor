package me.drendov.xraymonitor;

import me.drendov.xraymonitor.lookups.Checkers;

import java.util.HashMap;

import org.bukkit.ChatColor;
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
        boolean succeed = false;
        if (cmd.getName().equalsIgnoreCase("xcheck")) {
            if (sender.hasPermission("xcheck.check") || sender.isOp()) {
                String playername = "";
                if (args.length > 0) {
                    if (!args[0].contains(":")) {
                        playername = args[0];
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
                    maxrate = Float.parseFloat(hm.get("maxrate").toString());
                }
                if (hm.containsKey("since")) {
                    hours = Integer.parseInt(hm.get("since").toString());
                }
                this.plugin.banned = hm.containsKey("banned") && (hm.get("banned")).toString().equalsIgnoreCase("true");
                if (hm.containsKey("world")) {
                    world = hm.get("world");
                    if (this.plugin.getServer().getWorld(world) == null) {
                        sender.sendMessage(ChatColor.RED + "[XRayMonitor]" + ChatColor.WHITE + " This world does not exist. Please check your world-parameter.");
                        return true;
                    }
                }
                if (hm.containsKey("ore")) {
                    oreName = hm.get("ore").toString();
                }
                if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
                    this.plugin.config.load();
                    sender.sendMessage((Object) ChatColor.RED + "[XRayMonitor]" + (Object) ChatColor.WHITE + " Config reloaded.");
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
                    sender.sendMessage((Object) ChatColor.RED + "[XRayMonitor]" + (Object) ChatColor.WHITE + " You do not have permission for this command.");
                    return true;
                }
                if (playername.length() == 0) {
                    this.plugin.showInfo(sender);
                    return true;
                }
                if (world.length() == 0 && oreName.isEmpty()) {
                    try {
                        if (ClearedPlayerFile.wasPlayerCleared(playername)) {
                            hours = ClearedPlayerFile.getHoursFromClear(playername);
                        }
                        world = Config.defaultWorld;
                        this.checker.checkGlobal(playername, sender, world, hours);
                        return true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (world.length() > 0 && oreName.isEmpty()) {
                    try {
                        if (ClearedPlayerFile.wasPlayerCleared(playername)) {
                            hours = ClearedPlayerFile.getHoursFromClear(playername);
                        }
                        this.checker.checkGlobal(playername, sender, world, hours);
                        return true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (world.length() > 0 && !oreName.isEmpty()) {
                    if (playername.equalsIgnoreCase("all") && maxrate > 0.0f) {
                        new Thread(new CustomRunnable(sender, world, oreName, bantype, maxrate, this.plugin.banned, hours) {

                            @Override
                            public void run() {
                                Cmd.this.checker.listAllXRayers(this.sender, this.world, this.oreName, this.bantype, this.maxrate, this.banned, this.hours);
                            }
                        }).start();
                        return true;
                    }
                    if (ClearedPlayerFile.wasPlayerCleared(playername)) {
                        hours = ClearedPlayerFile.getHoursFromClear(playername);
                    }
                    this.checker.checkSingle(playername, sender, oreName, world, hours);
                    return true;
                }
                if (world.length() == 0 && !oreName.isEmpty()) {
                    world = Config.defaultWorld;
                    if (playername.equalsIgnoreCase("all") && maxrate > 0.0f) {
                        final CommandSender s = sender;
                        final String w = world;
                        final String oname = oreName;
                        final String bt = bantype;
                        final float mr = maxrate;
                        final int h = hours;
                        final boolean ban = this.plugin.banned;
                        new BukkitRunnable() {
                            public void run() {
                                Cmd.this.checker.listAllXRayers(s, w, oname, bt, mr, ban, h);
                            }
                        }.runTaskAsynchronously((Plugin) this.plugin);
                        return true;
                    }
                    if (ClearedPlayerFile.wasPlayerCleared(playername)) {
                        hours = ClearedPlayerFile.getHoursFromClear(playername);
                    }
                    this.checker.checkSingle(playername, sender, oreName, world, hours);
                    return true;
                }
            } else {
                sender.sendMessage((Object) ChatColor.RED + "[XRayMonitor]" + (Object) ChatColor.WHITE + " You do not have permission for this command.");
                return true;
            }
        }
        return succeed;
    }

}

