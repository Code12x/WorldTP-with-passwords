package com.code12.worldtp.commands;

import com.code12.worldtp.WorldTP;
import com.code12.worldtp.worldtpobjects.WorldTPWorldGroup;
import com.code12.worldtp.files.DataManager;
import com.code12.worldtp.files.References;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

public class CommandEditWorld implements CommandExecutor {
    WorldTP plugin;
    public DataManager data = References.data;

    public CommandEditWorld(WorldTP plugin) {
        this.plugin = plugin;
    }

    // The command
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        // makes sure the sender is an admin
        if(!(sender.hasPermission("worldtp.editworld"))){
            sender.sendMessage(ChatColor.YELLOW + "You don't have the necessary permission to use this command.");
            return true;
        }

        if(args.length == 4){ //makes sure it has the proper amount of arguments
            if((!args[3].equals("true")) && (!args[3].equals("false"))){
                sender.sendMessage("This command requires 4 arguments. The first argument is the name of the world to register. The second argument is the display name for the world. The third argument is the material to be displayed in the GUI (this material must be in the spigot Material format | not \"minecraft.iron_sword\", but rather \"iron_sword\". The fourth argument is a true/false value | true allows only admins to enter the world, but false allows anyone to enter the world.");
            }
            String world = args[0];
            String displayName = args[1];
            ItemStack displayItem = new ItemStack(Material.getMaterial(args[2].toUpperCase()));
            String adminOnly = args[3];
            WorldTPWorldGroup worldToRegister = new WorldTPWorldGroup(plugin, world, displayName);
            worldToRegister.setItem(displayItem);
            if(adminOnly.equalsIgnoreCase("true")){
                worldToRegister.setAdminOnly(true);
            }

            // Get the password if applicable
            if(args.length == 5){
                String password = args[4];
                worldToRegister.setPassword(password);
            }

            // Register the edit
            worldToRegister.editWorldGroup();

            sender.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "[!]" + ChatColor.RESET + ChatColor.YELLOW + "Registration of world " + world + " has been completed!");
        }else{
            // in case the sender didn't enter the correct number of arguments or if there were any other errors
            sender.sendMessage("This command requires 5 arguments.\nThe first argument is the name of the world to" +
                    " register.\nThe second argument is the display name for the world.\nThe third argument is the" +
                    " material to be displayed in the GUI (this material must be in the spigot Material format. Not" +
                    " \"minecraft.iron_sword\", but rather \"iron_sword\".\nThe fourth argument is a true/false value" +
                    " | true allows only admins to enter the world, but false allows anyone to enter the world." +
                    "\nThe fifth argument is the password to set for that world. If nothing is entered, the world will" +
                    " not be locked and will act like normal.");
            sender.sendMessage(ChatColor.YELLOW + "Worlds:\n" + ChatColor.RESET +  data.getConfig().getStringList("menuGroupList"));
            return true;
        }
        return true;
    }
}
