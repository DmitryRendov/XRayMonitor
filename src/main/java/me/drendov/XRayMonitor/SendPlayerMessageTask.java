package me.drendov.XRayMonitor;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.logging.Logger;

class SendPlayerMessageTask implements Runnable
{
	private Player player;
	private ChatColor color;
	private String message;
	private static XRayMonitor plugin = XRayMonitor.getInstance();
	private static Logger logger = plugin.getLogger();

	
	public SendPlayerMessageTask(Player player, ChatColor color, String message)
	{
		this.player = player;
		this.color = color;
		this.message = message;
	}

	@Override
	public void run()
	{
		if(player == null)
		{
		    logger.info(color + message);
		    return;
		}
	    
	    //if the player is dead, save it for after his respawn
//	    if(this.player.isDead())
//	    {
//	        PlayerData playerData = GriefPrevention.instance.dataStore.getPlayerData(this.player.getUniqueId());
//	        playerData.messageOnRespawn = this.color + this.message;
//	    }
//
//	    //otherwise send it immediately
//	    else
	    {
	        XRayMonitor.sendMessage(this.player, this.color, this.message);
	    }
	}	
}
