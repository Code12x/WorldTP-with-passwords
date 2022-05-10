package com.code12.worldtp.listeners;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.code12.worldtp.WorldTP;
import com.code12.worldtp.files.DataManager;
import com.code12.worldtp.files.References;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerChatListener implements Listener {
	WorldTP plugin;

	public PlayerChatListener(WorldTP plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerChatEvent(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();

		DataManager data = References.data;
		
		if(data.getConfig().getBoolean(player.getName() + ".nextChatMessageIsPassword")) {
			event.setCancelled(true);

			data.getConfig().set(player.getName() + ".nextChatMessageIsPassword", false);
			data.saveConfig();

			String menuGroup = data.getConfig().getString(player.getName() + ".menuGroup");
			
			String password = data.getConfig().getString("menuGroupID." + menuGroup + ".password");
			
			String passwordGuess = event.getMessage();

			if(passwordGuess.equals(password)) {
				Location locationToTP =  data.getConfig().getLocation(player.getName() + ".locationIfApproved");

				new BukkitRunnable() {
					@Override
					public void run() {
						player.teleport(locationToTP);
					}
				}.runTask(plugin);

				player.sendMessage(ChatColor.GREEN + "Password correct!");
			}else {
				player.sendMessage(ChatColor.RED + passwordGuess + " is incorrect");
			}
		}
	}
}
