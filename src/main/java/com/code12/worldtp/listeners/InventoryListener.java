package com.code12.worldtp.listeners;

import com.code12.worldtp.WorldTP;
import com.code12.worldtp.worldtpobjects.WorldTPWorld;
import com.code12.worldtp.files.ConfigManager;
import com.code12.worldtp.files.DataManager;
import com.code12.worldtp.files.References;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.List;

public class InventoryListener implements Listener {
    WorldTP plugin;

    public DataManager data = References.data;
    public ConfigManager config = References.config;

    public InventoryListener(WorldTP plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInventoryInteractEvent(InventoryClickEvent event) {
        List<String> menuGroupList = data.getConfig().getStringList("menuGroupList");

        Player player = (Player) event.getWhoClicked();

        if (event.getView().getTitle().contains("World Menu")) {
            if (event.getCurrentItem() == null) {
                return;
            }
            if (event.getCurrentItem().getItemMeta() != null) {
                for (String menuGroup : menuGroupList) {
                    if (event.getCurrentItem().getItemMeta().getDisplayName().equals(data.getConfig().getString("menuGroupID." + menuGroup + ".displayName"))) {

                        Boolean spawn = config.getConfig().getBoolean(menuGroup + ".Spawn_Teleporting");
                        Boolean nether = config.getConfig().getBoolean(menuGroup + ".Nether_Teleporting");
                        Boolean end = config.getConfig().getBoolean(menuGroup + ".End_Teleporting");

                        String worldToLeaveName = player.getWorld().getName();
                        WorldTPWorld worldToLeave = new WorldTPWorld(plugin, worldToLeaveName);
                        String worldGroupToLeave = worldToLeave.getWorldGroup();


                        String worldToEnterName;
                        WorldTPWorld worldToEnter;
                        String worldToEnterWorldGroup;

                        Location playerLocation = player.getLocation();

                        Location locationToTP = null;


                        if(spawn || nether || end){


                            event.getView().close();
                            Inventory dimensionChoice = Bukkit.createInventory(null, 9, "Dimension");
                            if (spawn) {
                                dimensionChoice.addItem(newItem("GRASS_BLOCK", menuGroup + " Spawn"));
                            }
                            if (nether) {
                                dimensionChoice.addItem(newItem("NETHERRACK", menuGroup + " Nether"));
                            }
                            if (end) {
                                dimensionChoice.addItem(newItem("END_STONE", menuGroup + " End"));
                            }
                            dimensionChoice.addItem(newItem("LEATHER_BOOTS", menuGroup + " Resume"));

                            player.openInventory(dimensionChoice);
                            return;


                        }else {

                            if (data.getConfig().getLocation("playerLocations." + player.getName() + "." + menuGroup) != null) {
                                locationToTP = data.getConfig().getLocation("playerLocations." + player.getName() + "." + menuGroup);
                            }

                            if (data.getConfig().getLocation("menuGroupID." + menuGroup + ".WorldTPWorldSpawnPoint") != null) {
                                locationToTP = data.getConfig().getLocation("menuGroupID." + menuGroup + ".WorldTPWorldSpawnPoint");
                            }

                            if (playerLocation.getWorld().getName().startsWith(menuGroup)){
                                player.sendMessage(ChatColor.YELLOW + "You are already in the world: " + worldGroupToLeave);
                                event.setCancelled(true);
                                break;
                            }
                            
                            if(locationToTP == null) {
                            	World world = Bukkit.getWorld(menuGroup);
                            	locationToTP = world.getSpawnLocation();
                            	if (data.getConfig().getLocation("menuGroupID." + menuGroup + ".WorldTPWorldSpawnPoint") != null)
                                    locationToTP = data.getConfig().getLocation("menuGroupID." + menuGroup + ".WorldTPWorldSpawnPoint");
                            	if (player.getBedSpawnLocation() != null) {
                            		locationToTP = player.getBedSpawnLocation();
                            	}
                            }
                                                        
                            boolean locked = false;
                            
                            if(data.getConfig().getString("menuGroupID." + menuGroup + ".password") != null) {
                            	locked = true;
                            }
                                                        
                            if(locked) {
                            	data.getConfig().set(player.getName() + ".nextChatMessageIsPassword", true);
                            	
                            	data.getConfig().set(player.getName() + ".locationIfApproved", locationToTP);
                            	
                            	player.sendMessage(ChatColor.YELLOW + "Enter the password into chat. NOTE: The password will not be displayed in the chat.");
                            	
                            	break;
                            }

                            player.teleport(locationToTP);
                        }


                        data.getConfig().set("playerLocations." + player.getName() + "." + worldGroupToLeave, playerLocation);

                        player.sendMessage(ChatColor.DARK_AQUA + "You have been teleported to " + menuGroup + ".");
                        data.saveConfig();
                        event.setCancelled(true);
                        
                        break;
                    }
                }
            }
        }else if (event.getView().getTitle().contains("Dimension")) {
            if (event.getCurrentItem() == null) {
                return;
            }
            if (event.getCurrentItem().getItemMeta() != null) {
                for (String menuGroup : menuGroupList) {
                    if (event.getCurrentItem().getItemMeta().getDisplayName().startsWith(menuGroup)) {
                        String worldToLeaveName = player.getWorld().getName();
                        WorldTPWorld worldToLeave = new WorldTPWorld(plugin, worldToLeaveName);
                        String worldGroupToLeave = worldToLeave.getWorldGroup();

                        Location playerLocation = player.getLocation();

                        Location locationToTP = null;


                        if (event.getCurrentItem().getItemMeta().getDisplayName().equals(menuGroup + " Spawn")) {

                            if (data.getConfig().getString("worldGroup." + menuGroup + ".overworld") == null) {
                                player.sendMessage(ChatColor.YELLOW + "That place does not exist.");
                                event.setCancelled(true);
                                return;
                            }
                            locationToTP = Bukkit.getWorld(data.getConfig().getString("worldGroup." + menuGroup + ".overworld")).getSpawnLocation();

                            if (data.getConfig().getLocation("menuGroupID." + menuGroup + ".WorldTPWorldSpawnPoint") != null) {
                                locationToTP = data.getConfig().getLocation("menuGroupID." + menuGroup + ".WorldTPWorldSpawnPoint");
                            }

                        } else if (event.getCurrentItem().getItemMeta().getDisplayName().equals(menuGroup + " Nether")) {
                            if (data.getConfig().getString("worldGroup." + menuGroup + ".nether") == null) {
                                player.sendMessage(ChatColor.YELLOW + "That place does not exist.");
                                event.setCancelled(true);
                                return;
                            }

                            locationToTP = Bukkit.getWorld(data.getConfig().getString("worldGroup." + menuGroup + ".nether")).getSpawnLocation();
                        } else if (event.getCurrentItem().getItemMeta().getDisplayName().equals(menuGroup + " End")) {
                            if (data.getConfig().getString("worldGroup." + menuGroup + ".the_end") == null) {
                                player.sendMessage(ChatColor.YELLOW + "That place does not exist.");
                                event.setCancelled(true);
                                return;
                            }
                            locationToTP = Bukkit.getWorld(data.getConfig().getString("worldGroup." + menuGroup + ".the_end")).getSpawnLocation();

                        } else if (event.getCurrentItem().getItemMeta().getDisplayName().equals(menuGroup + " Resume")) {
                            if (data.getConfig().getLocation("playerLocations." + player.getName() + "." + menuGroup) != null) {
                                locationToTP = data.getConfig().getLocation("playerLocations." + player.getName() + "." + menuGroup);
                            }

                            if (data.getConfig().getLocation("menuGroupID." + menuGroup + ".WorldTPWorldSpawnPoint") != null) {
                                locationToTP = data.getConfig().getLocation("menuGroupID." + menuGroup + ".WorldTPWorldSpawnPoint");
                            }

                            if (playerLocation.getWorld().getName().startsWith(menuGroup)){
                                player.sendMessage(ChatColor.YELLOW + "You are already in the world: " + worldGroupToLeave);
                                event.setCancelled(true);
                                break;
                            }
                        }

                        if(locationToTP == null) {
                        	World world = Bukkit.getWorld(menuGroup);
                        	locationToTP = world.getSpawnLocation();
                        	if (data.getConfig().getLocation("menuGroupID." + menuGroup + ".WorldTPWorldSpawnPoint") != null)
                                locationToTP = data.getConfig().getLocation("menuGroupID." + menuGroup + ".WorldTPWorldSpawnPoint");
                        	if (player.getBedSpawnLocation() != null) {
                        		locationToTP = player.getBedSpawnLocation();
                        	}
                        }
                                                    
                        boolean locked = false;
                        
                        if(data.getConfig().getString("menuGroupID." + menuGroup + ".password") != null) {
                        	locked = true;
                        }
                                                    
                        if(locked) {
                        	data.getConfig().set(player.getName() + ".nextChatMessageIsPassword", true);
                        	
                        	data.getConfig().set(player.getName() + ".locationIfApproved", locationToTP);
                        	
                        	player.sendMessage(ChatColor.YELLOW + "Enter the password into chat. NOTE: The password will not be displayed in the chat.");
                        	
                        	break;
                        }

                        player.teleport(locationToTP);

                        data.getConfig().set("playerLocations." + player.getName() + "." + worldGroupToLeave, playerLocation);

                        player.sendMessage(ChatColor.DARK_AQUA + "You have been teleported to " + menuGroup + ".");
                        data.saveConfig();
                        event.setCancelled(true);
                        
                        break;
                    }
                }
            }
        }
    }

    private ItemStack newItem (String material, String name){
        ItemStack item = new ItemStack(Material.valueOf(material));
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(name);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(itemMeta);
        return item;
    }    
}
