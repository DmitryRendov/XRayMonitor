package me.drendov.xraymonitor;

import de.diddiz.LogBlock.LogBlock;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class XRayMonitor extends JavaPlugin {
    public final Config config = new Config(this);
    private static XRayMonitor instance;
    public Boolean banned = false;
    private String version;
    public final String msgBorder = (Object) ChatColor.DARK_GREEN + "-" + (Object) ChatColor.GREEN + "-" + (Object) ChatColor.DARK_GREEN + "-" + (Object) ChatColor.GREEN + "-" + (Object) ChatColor.DARK_GREEN + "-" + (Object) ChatColor.GREEN + "-" + (Object) ChatColor.DARK_GREEN + "-" + (Object) ChatColor.GREEN + "-" + (Object) ChatColor.DARK_GREEN + "-" + (Object) ChatColor.GREEN + "-" + (Object) ChatColor.DARK_GREEN + "-" + (Object) ChatColor.GREEN + "-" + (Object) ChatColor.DARK_GREEN + "-" + (Object) ChatColor.GREEN + "-" + (Object) ChatColor.DARK_GREEN + "-" + (Object) ChatColor.GREEN + "-" + (Object) ChatColor.DARK_GREEN + "-" + (Object) ChatColor.GREEN + "-" + (Object) ChatColor.DARK_GREEN + "-" + (Object) ChatColor.GREEN + "-" + (Object) ChatColor.DARK_GREEN + "-" + (Object) ChatColor.GREEN + "-" + (Object) ChatColor.DARK_GREEN + "-" + (Object) ChatColor.GREEN + "-" + (Object) ChatColor.DARK_GREEN + "-" + (Object) ChatColor.GREEN + "-" + (Object) ChatColor.DARK_GREEN + "-" + (Object) ChatColor.GREEN + "-" + (Object) ChatColor.DARK_GREEN + "-" + (Object) ChatColor.GREEN + "-" + (Object) ChatColor.DARK_GREEN + "-";

    @Override
    public void onEnable() {
        instance = this;
        this.config.load();
        this.detectLogger();
        ClearedPlayerFile.loadClearedPlayers();
        this.getServer().getPluginManager().registerEvents((Listener) new Listeners(), (Plugin) this);
        this.getCommand("xcheck").setExecutor((CommandExecutor) new Cmd());

        PluginDescriptionFile pdfFile = this.getDescription();
        this.version = pdfFile.getVersion();
        this.getLogger().info("XRayMonitor v" + this.version + " enabled.");
    }

    @Override
    public void onDisable() {
        this.getLogger().info("XRayMonitor disabled");
    }

    public static XRayMonitor getInstance() {
        return instance;
    }

    private void detectLogger() {
        Plugin p = this.getServer().getPluginManager().getPlugin("LogBlock");
        if (!(p instanceof LogBlock)) {
            this.getLogger().severe("LogBlock has not been detected. Disabling XRayMonitor.");
            p = this.getServer().getPluginManager().getPlugin("XRayMonitor");
            this.getServer().getPluginManager().disablePlugin(p);
        }
        this.config.setLogger("LogBlock");
    }

    void showInfo(CommandSender sender) {
        sender.sendMessage(ChatColor.WHITE + "----- " + ChatColor.GREEN + "XRayMonitor v" + this.version + ChatColor.WHITE + " -----");
        sender.sendMessage(ChatColor.YELLOW + "/xcheck <player> " + ChatColor.WHITE + "- Calculate a player's x-ray stats");
        sender.sendMessage(ChatColor.YELLOW + "/xcheck clear <player> " + ChatColor.WHITE + "- Clears a player's x-ray stats");
        sender.sendMessage(ChatColor.YELLOW + "/xcheck reload " + ChatColor.WHITE + "- Reloads the config");
        sender.sendMessage(ChatColor.YELLOW + "/xcheck help " + ChatColor.WHITE + "- Displays more detailed command usage");
    }

    void showHelp(CommandSender sender) {
        sender.sendMessage(ChatColor.WHITE + "----- " + ChatColor.GREEN + "XRayMonitor commands" + ChatColor.WHITE + " -----");
        sender.sendMessage((Object) ChatColor.YELLOW + "/xcheck " + (Object) ChatColor.WHITE + "<player>|all " + (Object) ChatColor.RED + " [required]");
        sender.sendMessage((Object) ChatColor.YELLOW + "/xcheck " + (Object) ChatColor.WHITE + "world:<world name> " + (Object) ChatColor.GREEN + " [optional]");
        sender.sendMessage((Object) ChatColor.YELLOW + "/xcheck " + (Object) ChatColor.WHITE + "ore:<ore code #> " + (Object) ChatColor.GREEN + " [optional, required on player:all]");
        sender.sendMessage((Object) ChatColor.YELLOW + "/xcheck " + (Object) ChatColor.WHITE + "maxrate:<percentage> " + (Object) ChatColor.RED + " [required on player:all]");
        sender.sendMessage((Object) ChatColor.YELLOW + "/xcheck " + (Object) ChatColor.WHITE + "since:<time in hours> " + (Object) ChatColor.GREEN + " [optional]");
        sender.sendMessage((Object) ChatColor.YELLOW + "/xcheck " + (Object) ChatColor.WHITE + "banned:true " + (Object) ChatColor.GREEN + " [optional], hides banned players from /xcheck all");
        sender.sendMessage((Object) ChatColor.YELLOW + "example: " + (Object) ChatColor.WHITE + "/xcheck player123 world:survival ore:diamond_ore since:30");
        sender.sendMessage((Object) ChatColor.YELLOW + "example for mass check: " + (Object) ChatColor.WHITE + "/xcheck all ore:diamond_ore maxrate:3");
    }

    void clearPlayer(CommandSender sender, String player) throws Exception {
        ClearedPlayerFile.clearPlayer(player);
        sender.sendMessage(ChatColor.RED + "[XRayMonitor]" + ChatColor.WHITE + " x-ray stats of the player " + player + " have been successfully cleared.");
    }
}

