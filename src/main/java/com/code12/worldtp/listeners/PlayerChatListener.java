package com.code12.worldtp.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.code12.worldtp.WorldTP;
import com.code12.worldtp.files.DataManager;
import com.code12.worldtp.files.References;

import net.md_5.bungee.api.ChatColor;

public class PlayerChatListener implements Listener {
	WorldTP plugin;
	
	public void onPlayerChatEvent(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		
		DataManager data = References.data;
		
		if(data.getConfig().getBoolean(player.getName() + ".nextChatMessageIsPassword")) {
			event.setCancelled(true);
			
			String menuGroup = data.getConfig().getString(player.getName() + ".menuGroup");
			
			String password = data.getConfig().getString("menuGroupID." + menuGroup + ".password");
			
			String passwordGuess = event.getMessage();
			
			if(passwordGuess.equals(password)) {
				player.teleport(data.getConfig().getLocation(player.getName() + ".locationIfApproved"));
				player.sendMessage(ChatColor.GREEN + "Password correct!");
			}else {
				player.sendMessage(ChatColor.RED + "Incorrect");
			}
			
			data.getConfig().set(player.getName() + ".nextChatMessageIsPassword", false);
			
			data.saveConfig();			
		}
	}
}
