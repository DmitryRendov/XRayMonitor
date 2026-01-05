package me.drendov.XRayMonitor.lookups;

import me.drendov.XRayMonitor.ClearedPlayerFile;
import me.drendov.XRayMonitor.Config;
import me.drendov.XRayMonitor.XRayMonitor;
import de.diddiz.LogBlock.BlockChange;
import de.diddiz.LogBlock.LogBlock;
import de.diddiz.LogBlock.QueryParams;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;

public class LogBlockLookup {
    private XRayMonitor plugin = XRayMonitor.getInstance();
    private Logger logger = plugin.getLogger();

    public int oreLookup(String player, String oreName, String world, int hours) throws SQLException {
        try {
            LogBlock logBlock = (LogBlock)this.plugin.getServer().getPluginManager().getPlugin("LogBlock");
            if (logBlock == null) {
                this.plugin.getLogger().warning("LogBlockLookup: LogBlock plugin not found!");
                return 0;
            }

            QueryParams params = new QueryParams(logBlock);
            params.setPlayer(player);
            params.bct = QueryParams.BlockChangeType.DESTROYED;
            params.limit = -1;
            params.since = hours * 60;
            params.world = this.plugin.getServer().getWorld(world);

            if (params.world == null) {
                this.plugin.getLogger().warning("LogBlockLookup: World '" + world + "' not found!");
                return 0;
            }

            final Material mat = Material.matchMaterial(oreName);
            if (mat == null) {
                this.plugin.getLogger().warning("LogBlockLookup: No material matching '" + oreName + "' for player " + player);
                return 0;
            }

            ArrayList<Material> lookupListTypes = new ArrayList<>();
            lookupListTypes.add(mat);
            params.types = lookupListTypes;

            params.needCount = true;
            int count = logBlock.getCount(params);

            // Debug logging
            if (this.plugin.config.isDebug()) {
                logger.info(ChatColor.RED + "[DEBUG]" + ChatColor.WHITE + " LogBlock lookup - Player: " + player + ", Ore: " + oreName + " (" + mat + "), World: " + world + ", Hours: " + hours + ", Count: " + count);
            }

            return count;
        } catch (Exception e) {
            this.plugin.getLogger().severe("Error in oreLookup for player " + player + ", ore " + oreName + ": " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

    public List<String[]> playerLookup(CommandSender sender, String oreName, String world) {
        LogBlock logBlock = (LogBlock)this.plugin.getServer().getPluginManager().getPlugin("LogBlock");
        QueryParams params = new QueryParams(logBlock);
        params.bct = QueryParams.BlockChangeType.DESTROYED;
        params.limit = -1;
        params.world = this.plugin.getServer().getWorld(Config.defaultWorld);

        final Material mat = Material.matchMaterial(oreName);
        if (mat == null) {
            throw new IllegalArgumentException("No material matching: '" + oreName + "'");
        }
        ArrayList<Material> lookupListTypes = new ArrayList<>();
        lookupListTypes.add(mat);
        params.types = lookupListTypes;

        params.needPlayer = true;
        params.sum = QueryParams.SummarizationMode.PLAYERS;
        ArrayList<String[]> namesAndOresList = new ArrayList<>();
        try {
            for (BlockChange bc : logBlock.getBlockChanges(params)) {
                String[] nameOreStoneString = new String[3];
                int since = -1;
                if (ClearedPlayerFile.wasPlayerCleared(bc.playerName)) {
                    since = ClearedPlayerFile.getHoursFromClear(bc.playerName);
                }
                nameOreStoneString[0] = bc.playerName;
                nameOreStoneString[1] = Integer.toString(this.oreLookup(bc.playerName, oreName, world, since));
                nameOreStoneString[2] = Integer.toString(this.oreLookup(bc.playerName, "stone", world, since));
                namesAndOresList.add(nameOreStoneString);
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        return namesAndOresList;
    }
}

