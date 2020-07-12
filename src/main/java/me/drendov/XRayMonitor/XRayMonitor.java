package me.drendov.XRayMonitor;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;
import java.util.logging.Logger;

import de.diddiz.LogBlock.LogBlock;
import org.bukkit.ChatColor;
import org.bstats.bukkit.Metrics;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class XRayMonitor extends JavaPlugin {
    private static XRayMonitor instance;

    //for logging to the console
    private static Logger logger;

    public final Config config = new Config(this);
    protected final static String pluginFolderPath = "plugins" + File.separator + "XRayMonitor";
    final static String configFilePath = pluginFolderPath + File.separator + "config.yml";
    final static String messagesFilePath = pluginFolderPath + File.separator + "messages.yml";
    final static String clearedPlayerFilePath = pluginFolderPath + File.separator + "ClearedPlayers.yml";

    private String version;
    private String[] messages;
    public final String msgBorder = new String(new char[10]).replace("\0", ChatColor.GREEN + "-" + ChatColor.DARK_GREEN + "-");

    @Override
    public void onEnable() {
        instance = this;
        logger = instance.getLogger();

        // load up all the messages from messages.yml
        this.loadMessages();
        logger.info("Customizable messages loaded.");

        this.config.load();
        this.detectLogger();

        // load cleared players
        ClearedPlayerFile.loadClearedPlayers();

        // register for events
        PluginManager pluginManager = this.getServer().getPluginManager();

        // register player events
        pluginManager.registerEvents(new Listeners(), this);
        Objects.requireNonNull(this.getCommand("xrm")).setExecutor(new Cmd());

        try
        {
            int pluginId = 8153;
            Metrics metrics = new Metrics(this, pluginId);
        }
        catch (Throwable ignored){}
        PluginDescriptionFile pdfFile = this.getDescription();
        this.version = pdfFile.getVersion();
        logger.info("XRayMonitor v" + this.version + " enabled.");
    }

    @Override
    public void onDisable() {
        logger.info("XRayMonitor disabled.");
    }

    public static XRayMonitor getInstance() {
        return instance;
    }

    private void detectLogger() {
        Plugin p = this.getServer().getPluginManager().getPlugin("LogBlock");
        if (!(p instanceof LogBlock)) {
            logger.severe("LogBlock has not been found. Disabling XRayMonitor.");
            this.getServer().getPluginManager().disablePlugin(this);
        }
        this.config.setLogger("LogBlock");
    }

    public boolean isWorldExist(String world) {
        return this.getServer().getWorld(world) != null;
    }

    void showInfo(CommandSender sender) {
        Player player = isSenderPlayer(sender);
        XRayMonitor.sendMessage(player, TextMode.Instr, Messages.PluginTitle, this.version);
        XRayMonitor.sendMessage(player, TextMode.Instr, Messages.HelperOne);
        XRayMonitor.sendMessage(player, TextMode.Instr, Messages.HelperTwo);
        XRayMonitor.sendMessage(player, TextMode.Instr, Messages.HelperThree);
        XRayMonitor.sendMessage(player, TextMode.Instr, Messages.HelperFour);
        XRayMonitor.sendMessage(player, TextMode.Instr, Messages.HelperFive);
    }

    void showHelp(CommandSender sender) {
        Player player = isSenderPlayer(sender);
        XRayMonitor.sendMessage(player, TextMode.Instr, Messages.HelperTitle);
        XRayMonitor.sendMessage(player, TextMode.Instr, "/xrm " + ChatColor.WHITE + "<player> " + ChatColor.RED + " [required]");
        XRayMonitor.sendMessage(player, TextMode.Instr, "/xrm " + ChatColor.WHITE + "world:<world name> " + ChatColor.GREEN + " [optional]");
        XRayMonitor.sendMessage(player, TextMode.Instr, "/xrm " + ChatColor.WHITE + "ore:<ore name> " + ChatColor.GREEN + " [optional, required on /xrm all]");
        XRayMonitor.sendMessage(player, TextMode.Instr, "/xrm " + ChatColor.WHITE + "rate:<percentage> " + ChatColor.GREEN + " [optional, required on /xrm all]");
        XRayMonitor.sendMessage(player, TextMode.Instr, "/xrm " + ChatColor.WHITE + "since:<time in hours> " + ChatColor.GREEN + " [optional]");
        XRayMonitor.sendMessage(player, TextMode.Instr, "example: " + ChatColor.WHITE + "/xrm PlayerName world:survival ore:diamond_ore since:30");
        XRayMonitor.sendMessage(player, TextMode.Instr, "example for mass check: " + ChatColor.WHITE + "/xrm all ore:diamond_ore rate:3");
    }

    void clearPlayer(CommandSender sender, String playerName) throws Exception {
        Player player = isSenderPlayer(sender);
        ClearedPlayerFile.clearPlayer(playerName);
        XRayMonitor.sendMessage(player, TextMode.Success, Messages.ClearedPlayer, playerName);
    }

    public static Player isSenderPlayer(CommandSender sender) {
        Player player = null;
        if (sender instanceof Player) {
            player = (Player) sender;
        }
        return player;
    }

    //sends a color-coded message to a player
    public static void sendMessage(Player player, ChatColor color, Messages messageID, String... args) {
        sendMessage(player, color, messageID, 0, args);
    }

    //sends a color-coded message to a player
    public static void sendMessage(Player player, ChatColor color, Messages messageID, long delayInTicks, String... args) {
        String message = XRayMonitor.getInstance().getMessage(messageID, args);
        sendMessage(player, color, message, delayInTicks);
    }

    //sends a color-coded message to a player
    public static void sendMessage(Player player, ChatColor color, String message) {
        if (message == null || message.length() == 0) return;

        if (player == null) {
            logger.info(color + message);
        } else {
            player.sendMessage(ChatColor.DARK_AQUA + "[" + ChatColor.DARK_GREEN + "XRM" + ChatColor.DARK_AQUA + "] " + color + message);
        }
    }

    public static void sendMessage(Player player, ChatColor color, String message, long delayInTicks) {
        SendPlayerMessageTask task = new SendPlayerMessageTask(player, color, message);

        //Only schedule if there should be a delay. Otherwise, send the message right now, else the message will appear out of order.
        if (delayInTicks > 0) {
            XRayMonitor.getInstance().getServer().getScheduler().runTaskLater(XRayMonitor.getInstance(), task, delayInTicks);
        } else {
            task.run();
        }
    }

    private void loadMessages() {
        Messages[] messageIDs = Messages.values();
        this.messages = new String[Messages.values().length];

        HashMap<String, CustomizableMessage> defaults = new HashMap<String, CustomizableMessage>();
        // initialize defaults
        this.addDefault(defaults, Messages.Reloaded, "Config reloaded.", null);
        this.addDefault(defaults, Messages.NoPermissionForCommand, "You don't have permission to do that.", null);
        this.addDefault(defaults, Messages.WorldNotFound, "World not found.", null);
        this.addDefault(defaults, Messages.DefaultWorldNotFound, "Default world does not exist. Please check your configuration file.", null);
        this.addDefault(defaults, Messages.CalcPlayerOre, "Calculating ore ratios for {0}.", "0: a player");
        this.addDefault(defaults, Messages.AllPlayersOnOre, "All players on {0}.", "0: ore name");
        this.addDefault(defaults, Messages.CalcAllPlayersOreRate, "Searching for players with a {0} rate higher than {1}.", "0: ore name; 1: max rate in percent");
        this.addDefault(defaults, Messages.PleaseBePatient, "Please be patient, this may take a minute.", null);
        this.addDefault(defaults, Messages.Stones, "Stones: {0}", "0: number of stone-like blocks (incl. granite/andesite/diorite).");

        this.addDefault(defaults, Messages.Diamond, "Diamond: {0}", "0: number of diamond ore blocks.");
        this.addDefault(defaults, Messages.Emerald, "Emerald: {0}", "0: number of emerald ore blocks.");
        this.addDefault(defaults, Messages.AncientDebris, "AncientDebris: {0} ({1})", "0: number of ancient debris blocks; 1:number of Netherrack destroyed");
        this.addDefault(defaults, Messages.Gold, "Gold: {0}", "0: number of gold ore blocks.");
        this.addDefault(defaults, Messages.Lapis, "Lapis: {0}", "0: number of lapis ore blocks.");
        this.addDefault(defaults, Messages.Iron, "Iron: {0}", "0: number of iron ore  blocks.");
        this.addDefault(defaults, Messages.Redstone, "Redstone: {0}", "0: number of redstone ore blocks.");
        this.addDefault(defaults, Messages.Coal, "Coal: {0}", "0: number of coal ore blocks.");
        this.addDefault(defaults, Messages.Mossy, "Mossy: {0}", "0: number of mossy blocks.");
        this.addDefault(defaults, Messages.Spawners, "Spawners: {0}", "0: number of spawners.");
        this.addDefault(defaults, Messages.CustomOre, "{0}: {1}", "0: name of custom ore; 1:number of custom ore blocks.");
        this.addDefault(defaults, Messages.NoMaterial, "No material matching: {0}", "0: ore name");

        this.addDefault(defaults, Messages.VeryLowChanceXRay, "xLevel: {0}. (x-ray use is very unlikely)", "0: level of x-ray usage");
        this.addDefault(defaults, Messages.LowChanceXRay, "xLevel: {0}. (x-ray use is unlikely).", "0: level of x-ray usage");
        this.addDefault(defaults, Messages.MediumChanceXRay, "xLevel: {0}. (medium chance of x-ray).", "0: level of x-ray usage");
        this.addDefault(defaults, Messages.HighChanceXRay, "xLevel: {0}. (high chance of x-ray).", "0: level of x-ray usage");
        this.addDefault(defaults, Messages.VeryHighChanceXRay, "xLevel: {0}. (very high chance of x-ray).", "0: level of x-ray usage");
        this.addDefault(defaults, Messages.PotentialXrayerWarning, "{0} has higher than average stats for {} and may be a cheater. Watch carefully.", "0: a player; 1: ores list");
        this.addDefault(defaults, Messages.ErrRatePositive, "Rate should be positive number greater than 0.", null);
        this.addDefault(defaults, Messages.ClearedPlayer, "X-ray stats of the player {0} have been successfully cleared.", "0: a player");

        this.addDefault(defaults, Messages.HelperOne, "/xrm <PlayerName>$f - Calculate a player's x-ray stats.", null);
        this.addDefault(defaults, Messages.HelperTwo, "/xrm all$f - Calculate all online players' x-ray stats.", null);
        this.addDefault(defaults, Messages.HelperThree, "/xrm clear <PlayerName>$f - Clears a player's x-ray stats.", null);
        this.addDefault(defaults, Messages.HelperFour, "/xrm reload$f - Reloads the config.", null);
        this.addDefault(defaults, Messages.HelperFive, "/xrm help$f - Displays more detailed command usage.", null);
        this.addDefault(defaults, Messages.HelperTitle, "$f----- $2XRayMonitor commands: $f-----", null);
        this.addDefault(defaults, Messages.PluginTitle, "$f----- $2XRayMonitor $av{0} $f-----", "0: the plugin version");
        this.addDefault(defaults, Messages.MsgBorder, "$2-$a-$2-$a-$2-$a-$2-$a-$2-$a-$2-$a-$2-$a-$2-$a-$2-$a-$2-$a-", null);


        // load the config file
        FileConfiguration config = YamlConfiguration.loadConfiguration(new File(messagesFilePath));

        // for each message ID
        for (Messages messageID : messageIDs) {
            //get default for this message
            CustomizableMessage messageData = defaults.get(messageID.name());

            // if default is missing, log an error and use some fake data for now so that the plugin can run
            if (messageData == null) {
                logger.info("Missing message for " + messageID.name() + ".  Please contact the developer.");
                messageData = new CustomizableMessage(messageID, "Missing message!  ID: " + messageID.name() + ".  Please contact a server admin.", null);
            }

            // read the message from the file, use default if necessary
            this.messages[messageID.ordinal()] = config.getString("Messages." + messageID.name() + ".Text", messageData.text);
            config.set("Messages." + messageID.name() + ".Text", this.messages[messageID.ordinal()]);

            this.messages[messageID.ordinal()] = this.messages[messageID.ordinal()].replace('$', (char) 0x00A7);

            if (messageData.notes != null) {
                messageData.notes = config.getString("Messages." + messageID.name() + ".Notes", messageData.notes);
                config.set("Messages." + messageID.name() + ".Notes", messageData.notes);
            }
        }

        //save any changes
        try {
            config.options().header("Use a YAML editor like NotepadPlusPlus to edit this file.  \nAfter editing, back up your changes before reloading the server in case you made a syntax error.  \nUse dollar signs ($) for formatting codes, which are documented here: http://minecraft.gamepedia.com/Formatting_codes");
            config.save(XRayMonitor.messagesFilePath);
        } catch (IOException exception) {
            logger.info("Unable to write to the configuration file at \"" + XRayMonitor.messagesFilePath + "\"");
        }
        defaults.clear();
        System.gc();
    }

    private void addDefault(HashMap<String, CustomizableMessage> defaults,
                            Messages id, String text, String notes) {
        CustomizableMessage message = new CustomizableMessage(id, text, notes);
        defaults.put(id.name(), message);
    }

    synchronized public String getMessage(Messages messageID, String... args) {
        String message = messages[messageID.ordinal()];

        for (int i = 0; i < args.length; i++) {
            String param = args[i];
            message = message.replace("{" + i + "}", param);
        }

        return message;
    }

}
