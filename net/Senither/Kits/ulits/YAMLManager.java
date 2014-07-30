package net.Senither.Kits.ulits;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class YAMLManager {
    private final JavaPlugin plugin;
    private File configFile;
    private FileConfiguration fileConfiguration;
    private final File folder;

    @SuppressWarnings("deprecation")
	public YAMLManager(JavaPlugin plugin, String fileName) {
        if (plugin == null) {
            throw new IllegalArgumentException("plugin cannot be null");
        }
        if (!plugin.isInitialized()) {
            throw new IllegalArgumentException("plugin must be initiaized");
        }
        this.plugin = plugin;
        this.folder = new File(plugin.getDataFolder(), "playerData");
        if (!this.folder.exists()) {
            this.folder.mkdirs();
        }
        this.configFile = new File(this.folder, fileName);
        if (!this.configFile.exists()) {
            try {
                this.configFile.createNewFile();
                Bukkit.getLogger().log(Level.INFO, "[Kits] Creating a new player config file ({0})", fileName);
                this.fileConfiguration = YamlConfiguration.loadConfiguration((File)this.configFile);
                this.fileConfiguration.set("kills", (Object)0);
                this.fileConfiguration.set("deaths", (Object)0);
                this.fileConfiguration.set("credits", (Object)10);
                this.fileConfiguration.set("dualscore", (Object)0);
                this.fileConfiguration.set("higestKillstreak", (Object)0);
                this.fileConfiguration.set("lastlogin", (Object)0);
                this.fileConfiguration.set("achivements.killstreak.10", (Object)false);
                this.fileConfiguration.set("achivements.killstreak.25", (Object)false);
                this.fileConfiguration.set("achivements.killstreak.50", (Object)false);
                this.fileConfiguration.set("achivements.killstreak.75", (Object)false);
                this.fileConfiguration.set("achivements.killstreak.100", (Object)false);
                this.fileConfiguration.set("achivements.enchants.sharpness4", (Object)false);
                this.fileConfiguration.set("achivements.enchants.protection3", (Object)false);
                this.fileConfiguration.set("achivements.armour.diamond", (Object)false);
                this.fileConfiguration.set("achivements.armour.leather", (Object)false);
                this.fileConfiguration.set("achivements.kit.ninja", (Object)false);
                this.fileConfiguration.set("achivements.kit.medic", (Object)false);
                this.fileConfiguration.set("achivements.kills.100", (Object)false);
                this.fileConfiguration.set("achivements.kills.500", (Object)false);
                this.fileConfiguration.set("achivements.kills.1000", (Object)false);
                this.fileConfiguration.set("achivements.kills.2500", (Object)false);
                this.fileConfiguration.set("achivements.kills.5000", (Object)false);
                this.fileConfiguration.set("achivements.kills.10000", (Object)false);
                this.fileConfiguration.save(this.configFile);
            }
            catch (IOException ex) {
                plugin.getLogger().log(Level.SEVERE, "Could not save config to " + this.configFile, ex);
            }
        } else {
            this.fileConfiguration = YamlConfiguration.loadConfiguration((File)this.configFile);
        }
    }

    public FileConfiguration getConfig() {
        return this.fileConfiguration;
    }

    public void saveConfig() {
        if (this.fileConfiguration == null || this.configFile == null) {
            return;
        }
        try {
            this.getConfig().save(this.configFile);
        }
        catch (IOException ex) {
            this.plugin.getLogger().log(Level.SEVERE, "Could not save config to " + this.configFile, ex);
        }
    }
}

