package com.code12.worldtp.worldtpobjects;

import com.code12.worldtp.WorldTP;
import com.code12.worldtp.files.DataManager;
import com.code12.worldtp.files.References;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class WorldTPWorldGroup {
    WorldTP plugin;
    public DataManager data = References.data;

    private String name;
    private String displayName;
    private ItemStack item = new ItemStack(Material.GRASS_BLOCK);
    private Boolean adminOnly = false;
    private String password = null;

    public WorldTPWorldGroup(WorldTP plugin, String name, String displayName){
        this.plugin = plugin;
        this.name = name;
        this.displayName = displayName;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public void setAdminOnly(Boolean adminOnly) {
        this.adminOnly = adminOnly;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public void registerWorldGroup(){
        //register the world in the "worldList" List in WorldTP data.yml
        List<String> worldList = data.getConfig().getStringList("menuGroupList");
        worldList.add(name);
        data.getConfig().set("menuGroupList", worldList);

        // Set the display name
        data.getConfig().set("menuGroupID." + name + ".displayName", displayName);

        // Set the display item
        data.getConfig().set("menuGroupID." + name + ".item", item);

        // Set the admin-only variable for the world
        data.getConfig().set("menuGroupID." + name + ".admin", adminOnly);

        // Set the password
        if(password != null){
            data.getConfig().set("menuGroupID." + name + ".password", password);
        }else{
            data.getConfig().set("menuGroupID." + name + ".password", null);
        }

        // Save the registry
        data.saveConfig();
    }

    public void deleteWorldGroup(CommandSender sender){
        //Checks if the world exists
        if(!data.getConfig().getStringList("menuGroupList").contains(name)){
            sender.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "[!]" + ChatColor.RESET + ChatColor.YELLOW + "World " + name + " could not be found.");
            sender.sendMessage(ChatColor.YELLOW + "Worlds:\n " + ChatColor.WHITE + data.getConfig().getStringList("menuGroupList"));
            return;
        }

        //remove the world from "worldGroup" in WorldTP data.yml
        List<String> menuGroupList = data.getConfig().getStringList("menuGroupList");
        for (String menuGroup : menuGroupList){
            if(menuGroup.equals(name)){
                data.getConfig().set("worldGroup." + menuGroup, null);
            }
        }

        //remove the world from "menuGroupID" in WorldTP config.yml
        data.getConfig().set("menuGroupID." + name, null);

        //remove the registered world in the "menuGroupList" List in WorldTP data.yml
        List<String> worldList = data.getConfig().getStringList("menuGroupList");
        worldList.remove(name);
        data.getConfig().set("menuGroupList", worldList);

        data.saveConfig();

        sender.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "[!]" + ChatColor.RESET + ChatColor.YELLOW + "NOTICE, the command deleteworld DOES NOT actually delete a world, it only deletes the registry of the world for the WorldTP plugin. To permanently delete a world use the multiverse command: mv delete");

        sender.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "[!]" + ChatColor.RESET + ChatColor.YELLOW + "World " + name + " was successfully deleted.");
        sender.sendMessage(ChatColor.YELLOW + "Remaining Worlds:\n " + ChatColor.WHITE + data.getConfig().getStringList("menuGroupList"));
    }

    public void editWorldGroup(){
        // Edit the display name
        data.getConfig().set("menuGroupID." + name + ".displayName", displayName);

        // Edit the display item
        data.getConfig().set("menuGroupID." + name + ".item", item);

        // Edit the admin-only attribute
        data.getConfig().set("menuGroupID." + name + ".admin", adminOnly);

        // Edit the password
        if(password != null){
            data.getConfig().set("menuGroupID." + name + ".password", password);
        }else{
            data.getConfig().set("menuGroupID." + name + ".password", null);
        }

        // Save the edit
        data.saveConfig();
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public ItemStack getItem() {
        return item;
    }

    public Boolean getAdminOnly() {
        return adminOnly;
    }
}
