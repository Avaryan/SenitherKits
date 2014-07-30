package net.Senither.Kits.engine;

import java.util.ArrayList;
import java.util.List;

import net.Senither.Kits.Kits;
import net.Senither.Kits.api.events.ChangeMapEvent;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.kitteh.tag.TagAPI;

public class MapHandler
implements CommandExecutor {
    private final Kits _plugin;
    public int countdown;
    public int selectedArenaID = 0;
    public int preSelectedArenaID = 0;
    public List<String> arenas = new ArrayList<String>();
    public String name;
    public int x;
    public int y;
    public int z;
    public int pitch;
    public int yaw;
    public String world;

    @SuppressWarnings("unchecked")
	public MapHandler(Kits plugin) {
        this._plugin = plugin;
        this.arenas = (List<String>) this._plugin.getConfig().getList("arenas");
        this.countdown = this._plugin.getConfig().getInt("mapChange");
        if (this.arenas.size() != 0) {
            this._plugin.chatManager.LogInfo("Arenas Loaded Complete!");
        } else {
            this._plugin.chatManager.LogInfo("Failed to Load the Arenas!");
            this._plugin.getServer().getPluginManager().disablePlugin((Plugin)this._plugin);
        }
        String arenaDefaultName = this.arenas.get(0);
        this.name = this._plugin.getConfig().getString("arena." + arenaDefaultName + ".name");
        this.world = this._plugin.getConfig().getString("arena." + arenaDefaultName + ".world");
        this.x = this._plugin.getConfig().getInt("arena." + arenaDefaultName + ".x");
        this.y = this._plugin.getConfig().getInt("arena." + arenaDefaultName + ".y");
        this.z = this._plugin.getConfig().getInt("arena." + arenaDefaultName + ".z");
        this.pitch = this._plugin.getConfig().getInt("arena." + arenaDefaultName + ".pitch");
        this.yaw = this._plugin.getConfig().getInt("arena." + arenaDefaultName + ".yaw");
        this.MapRotate();
    }

    public void MapRotate() {
        this._plugin.getServer().getScheduler().scheduleSyncRepeatingTask((Plugin)this._plugin, (Runnable)new Runnable(){

            @Override
            public void run() {
                if (MapHandler.this.countdown == 3600) {
                	
                	MapHandler.this._plugin.chatManager.broadcastPluginMessage("&7Cycling to a new map in 60 minutes!");
                } else if (MapHandler.this.countdown == 1800) {
                	MapHandler.this._plugin.chatManager.broadcastPluginMessage("&7Cycling to a new map in 30 minutes!");
                } else if (MapHandler.this.countdown == 600) {
                	MapHandler.this._plugin.chatManager.broadcastPluginMessage("&7Cycling to a new map in 10 minutes!");
                } else if (MapHandler.this.countdown == 300) {
                    MapHandler.this._plugin.chatManager.broadcastPluginMessage("&7Cycling to a new map in 5 minutes!");
                } else if (MapHandler.this.countdown == 60) {
                    MapHandler.this._plugin.chatManager.broadcastPluginMessage("&7Cycling to a new map in 1 minute!");
                } else if (MapHandler.this.countdown == 30) {
                    MapHandler.this._plugin.chatManager.broadcastPluginMessage("&7Cycling to a new map in 30 seconds!");
                } else if (MapHandler.this.countdown == 11) {
                    MapHandler.this._plugin.chatManager.broadcastPluginMessage("&7Cycling to a new map in 10 seconds!");
                } else if (MapHandler.this.countdown == 6) {
                    MapHandler.this._plugin.chatManager.broadcastPluginMessage("&7Cycling to a new map in 5 seconds!");
                } else if (MapHandler.this.countdown == 5) {
                    MapHandler.this._plugin.chatManager.broadcastPluginMessage("&7Cycling to a new map in 4 seconds!");
                } else if (MapHandler.this.countdown == 4) {
                    MapHandler.this._plugin.chatManager.broadcastPluginMessage("&7Cycling to a new map in 3 seconds!");
                } else if (MapHandler.this.countdown == 3) {
                    MapHandler.this._plugin.chatManager.broadcastPluginMessage("&7Cycling to a new map in 2 seconds!");
                } else if (MapHandler.this.countdown == 2) {
                    MapHandler.this._plugin.chatManager.broadcastPluginMessage("&7Cycling to a new map in 1 second!");
                } else if (MapHandler.this.countdown == 1) {
                    MapHandler.this.countdown = MapHandler.this._plugin.getConfig().getInt("mapChange") + 5;
                    MapHandler.this.changeMap();
                }
                --MapHandler.this.countdown;
            }
        }, 40, 20);
    }

    public void changeMap() {
        this.selectedArenaID = this.selectedArenaID == this.arenas.size() - 1 ? 0 : ++this.selectedArenaID;
        String arenaDefaultName = this.arenas.get(this.selectedArenaID);
        this.name = this._plugin.getConfig().getString("arena." + arenaDefaultName + ".name");
        this.world = this._plugin.getConfig().getString("arena." + arenaDefaultName + ".world");
        this.x = this._plugin.getConfig().getInt("arena." + arenaDefaultName + ".x");
        this.y = this._plugin.getConfig().getInt("arena." + arenaDefaultName + ".y");
        this.z = this._plugin.getConfig().getInt("arena." + arenaDefaultName + ".z");
        this.pitch = this._plugin.getConfig().getInt("arena." + arenaDefaultName + ".p");
        this.yaw = this._plugin.getConfig().getInt("arena." + arenaDefaultName + ".w");
        this._plugin.getServer().getPluginManager().callEvent((Event)new ChangeMapEvent(this.getSpawnLocation(), this.name));
        this._plugin.chatManager.broadcastPluginMessage("&5&lNow playing &6&l" + this.name + "!");
        Location location = this.getSpawnLocation();
        location.getWorld().setDifficulty(Difficulty.HARD);
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (this._plugin.duel.isPlayerInBattle(player.getName())) continue;
            player.teleport(location);
            if (!this._plugin.controller.hasPvPEnabled(player.getName())) continue;
            this._plugin.controller.pvpList.add(player.getName());
            TagAPI.refreshPlayer((Player)player);
        }
    }

    public Location getSpawnLocation() {
        Location location = new Location(Bukkit.getWorld((String)this.world), (double)this.x, (double)this.y, (double)this.z);
        location.setPitch((float)this.pitch);
        location.setYaw((float)this.yaw);
        return location;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLable, String[] args) {
        Player player = null;
        if (!(sender instanceof Player)) {
            this._plugin.chatManager.sendMessage(sender, "You can't use that command in-game!");
            return true;
        }
        player = (Player)sender;
        if (!player.hasPermission(this._plugin.permissions.MANAGE_MAP)) {
            this._plugin.chatManager.missingPermission(player, this._plugin.permissions.MANAGE_MAP);
            return true;
        }
        this._plugin.chatManager.sendMessage(player, " &eThis feature has not yet been implemented!");
        return true;
    }

}

