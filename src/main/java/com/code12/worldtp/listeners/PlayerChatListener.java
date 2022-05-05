package com.code12.worldtp.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.code12.worldtp.WorldTP;
import com.code12.worldtp.files.DataManager;
import com.code12.worldtp.files.References;

public class PlayerChatListener implements Listener {
	WorldTP plugin;
	
	public void onPlayerChatEvent(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		
		DataManager data = References.data;
		
		if(data.getConfig().getBoolean(player.getName() + ".nextChatMessageIsPassword")) {
			event.setCancelled(true);
			player.teleport(data.getConfig().getLocation(player.getName() + ".locationIfApproved"));
		}
	}
}
