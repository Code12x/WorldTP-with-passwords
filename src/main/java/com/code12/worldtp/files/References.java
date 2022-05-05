package com.code12.worldtp.files;

import com.code12.worldtp.WorldTP;
import com.onarandombox.MultiverseCore.MultiverseCore;
import org.bukkit.Bukkit;

public class References {
    public static DataManager data;
    public static ConfigManager config;
    public static PasswordManager passwords;
    public static MultiverseCore core = (MultiverseCore) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");

    public static void loadData(WorldTP plugin){
        data = new DataManager(plugin);
    }

    public static void loadConfig(WorldTP plugin){
        config = new ConfigManager(plugin);
    }

    public static void loadPasswords(WorldTP plugin){
        passwords = new PasswordManager(plugin);
    }
}
