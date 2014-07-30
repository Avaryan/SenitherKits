package net.Senither.Kits.ulits;

import java.util.logging.Level;
import java.util.logging.Logger;

import net.Senither.Kits.Kits;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChatManager {
    public final Kits _plugin;
    public String pluginName;
    public String logName;
    public String prefix;
    public ChatColor defaultChatColor = ChatColor.GRAY;
    private final Logger log = Logger.getLogger("Minecraft");

    public ChatManager(Kits plugin) {
        this._plugin = plugin;
        this.pluginName = this._plugin.getName();
        this.logName = "[" + this.pluginName + "] ";
        this.prefix = (Object)ChatColor.DARK_GRAY + "[" + (Object)ChatColor.DARK_AQUA + this.pluginName + (Object)ChatColor.DARK_GRAY + "] ";
    }

    public void LogInfo(String message) {
        this.log.log(Level.INFO, "{0}{1}", new Object[]{this.logName, message});
    }

    public void LogSevere(String message) {
        this.log.log(Level.SEVERE, "{0}{1}", new Object[]{this.logName, message});
    }

    public void LogWarning(String message) {
        this.log.log(Level.WARNING, "{0}{1}", new Object[]{this.logName, message});
    }

    public void debugMessage(String message) {
        if (message == null) {
            message = "Husten, we got a problem!";
        }
        this.LogSevere("DEBUG MESSAGE : \n " + message);
    }

    public void enableMessage() {
    }

    public void disableMessage() {
        this.LogInfo("v" + this._plugin.v + " disabled.");
    }

    public void sendMessage(CommandSender player, String message) {
        player.sendMessage((Object)this.defaultChatColor + this.colorize(message));
    }

    public void sendMessage(Player player, String message) {
        player.sendMessage((Object)this.defaultChatColor + this.colorize(message));
    }

    public void broadcastPluginMessage(String message) {
        Bukkit.broadcastMessage((String)(this.prefix + (Object)this.defaultChatColor + this.colorize(message)));
    }

    public void broadcastMessage(String message) {
        Bukkit.broadcastMessage((String)((Object)this.defaultChatColor + this.colorize(message)));
    }

    public void missingPermission(CommandSender player, String permission) {
        player.sendMessage((Object)ChatColor.RED + "Influent permissions to execute this command.");
        player.sendMessage((Object)ChatColor.RED + "You're missing the permission node " + (Object)ChatColor.ITALIC + permission);
    }

    public void missingPermission(Player player, String permission) {
        player.sendMessage((Object)ChatColor.RED + "Influent permissions to execute this command.");
        player.sendMessage((Object)ChatColor.RED + "You're missing the permission node " + (Object)ChatColor.ITALIC + permission);
    }

    public void errorMessage(Exception e) {
        this.LogWarning("An error occurred " + e);
    }

    public String colorize(String message) {
        return ChatColor.translateAlternateColorCodes((char)'&', (String)message);
    }

    public String decolorize(String message) {
        return ChatColor.stripColor((String)message);
    }

    public void splitChat(Player player) {
        player.sendMessage((Object)ChatColor.DARK_GRAY + "=====================================================");
    }

    public void clearChat(Player player) {
        for (int i = 0; i < 35; ++i) {
            player.sendMessage("");
        }
    }
}

