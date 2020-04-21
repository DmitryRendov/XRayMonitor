package me.drendov.xraymonitor;

import java.util.Objects;
import java.util.logging.Logger;
import de.diddiz.LogBlock.LogBlock;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class XRayMonitor extends JavaPlugin {
    private static XRayMonitor instance;

    //for logging to the console
    private static Logger logger;

    public final Config config = new Config(this);

    private String version;
    public final String msgBorder = (Object) ChatColor.DARK_GREEN + "-" + (Object) ChatColor.GREEN + "-" + (Object) ChatColor.DARK_GREEN + "-" + (Object) ChatColor.GREEN + "-" + (Object) ChatColor.DARK_GREEN + "-" + (Object) ChatColor.GREEN + "-" + (Object) ChatColor.DARK_GREEN + "-" + (Object) ChatColor.GREEN + "-" + (Object) ChatColor.DARK_GREEN + "-" + (Object) ChatColor.GREEN + "-" + (Object) ChatColor.DARK_GREEN + "-" + (Object) ChatColor.GREEN + "-" + (Object) ChatColor.DARK_GREEN + "-" + (Object) ChatColor.GREEN + "-" + (Object) ChatColor.DARK_GREEN + "-" + (Object) ChatColor.GREEN + "-" + (Object) ChatColor.DARK_GREEN + "-" + (Object) ChatColor.GREEN + "-" + (Object) ChatColor.DARK_GREEN + "-" + (Object) ChatColor.GREEN + "-" + (Object) ChatColor.DARK_GREEN + "-" + (Object) ChatColor.GREEN + "-" + (Object) ChatColor.DARK_GREEN + "-" + (Object) ChatColor.GREEN + "-" + (Object) ChatColor.DARK_GREEN + "-" + (Object) ChatColor.GREEN + "-" + (Object) ChatColor.DARK_GREEN + "-" + (Object) ChatColor.GREEN + "-" + (Object) ChatColor.DARK_GREEN + "-" + (Object) ChatColor.GREEN + "-" + (Object) ChatColor.DARK_GREEN + "-";

    @Override
    public void onEnable() {
        instance = this;
        logger = instance.getLogger();


        this.config.load();
        this.detectLogger();

        // load cleared players
        ClearedPlayerFile.loadClearedPlayers();

        // register for events
        PluginManager pluginManager = this.getServer().getPluginManager();

        // player events
        pluginManager.registerEvents((Listener) new Listeners(), (Plugin) this);
        Objects.requireNonNull(this.getCommand("xcheck")).setExecutor(new Cmd());

//        //cache offline players
//        OfflinePlayer [] offlinePlayers = this.getServer().getOfflinePlayers();
//        CacheOfflinePlayerNamesThread namesThread = new CacheOfflinePlayerNamesThread(offlinePlayers, this.playerNameToIDMap);
//        namesThread.setPriority(Thread.MIN_PRIORITY);
//        namesThread.start();
//        //load ignore lists for any already-online players
//        @SuppressWarnings("unchecked")
//        Collection<Player> players = (Collection<Player>)GriefPrevention.instance.getServer().getOnlinePlayers();
//        for(Player player : players)
//        {
//            new IgnoreLoaderThread(player.getUniqueId(), this.dataStore.getPlayerData(player.getUniqueId()).ignoredPlayers).start();
//        }

        PluginDescriptionFile pdfFile = this.getDescription();
        this.version = pdfFile.getVersion();
        logger.info("XRayMonitor v" + this.version + " enabled.");
    }

    @Override
    public void onDisable() {
        logger.info("XRayMonitor disabled");
    }

    public static XRayMonitor getInstance() {
        return instance;
    }

    private void detectLogger() {
        Plugin p = this.getServer().getPluginManager().getPlugin("LogBlock");
        if (!(p instanceof LogBlock)) {
            logger.severe("LogBlock has not been detected. Disabling XRayMonitor.");
            this.getServer().getPluginManager().disablePlugin(this);
        }
        this.config.setLogger("LogBlock");
    }

    public boolean checkWorld(String world) {
        return this.getServer().getWorld(world) != null;
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

