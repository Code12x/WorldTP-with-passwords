package com.code12.worldtp.files;

import com.code12.worldtp.WorldTP;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

public class PasswordManager {
    private WorldTP plugin;
    private FileConfiguration fileConfig = null;
    private File file = null;

    public PasswordManager(WorldTP plugin){
        this.plugin = plugin;
        // saves/initializes the config
        saveDefaultConfig();
    }

    public void reloadConfig(){
        if(this.file == null)
            this.file = new File(this.plugin.getDataFolder(), "passwords.yml");
        this.fileConfig = YamlConfiguration.loadConfiguration(this.file);

        InputStream defaultStream = this.plugin.getResource("passwords.yml");
        if(defaultStream != null){
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            this.fileConfig.setDefaults(defaultConfig);
        }
    }

    public FileConfiguration getConfig(){
        if(this.fileConfig == null)
            reloadConfig();

        return this.fileConfig;
    }

    public void saveConfig(){
        if(this.fileConfig == null || this.file == null) {
            plugin.getLogger().warning("this.dataConfig == null || this.configFile == null");
            return;
        }

        try {
            this.getConfig().save(this.file);
        } catch (IOException e) {
            this.plugin.getLogger().log(Level.SEVERE, "Could not save config to " + this.file, e);
        }
    }

    public void saveDefaultConfig(){
        if(this.file == null){
            this.file = new File(this.plugin.getDataFolder(), "passwords.yml");
        }

        if(!this.file.exists()){
            this.plugin.saveResource("passwords.yml", false);
        }
    }
}
