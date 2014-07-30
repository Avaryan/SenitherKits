package net.Senither.Kits.Events;

import net.Senither.Kits.Kits;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ServerListener
implements Listener {
    private final Kits _plugin;

    public ServerListener(Kits plugin) {
        this._plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        this._plugin.controller.loadPlayer(e.getPlayer());
        this._plugin.controller.resetPlayer(e.getPlayer(), false);
        this._plugin.scoreboard.createPlayer(e.getPlayer());
        this._plugin.chatManager.sendMessage(e.getPlayer(), this._plugin.chatManager.prefix + "&bYou have joined the map &c&l" + this._plugin.mapHandler.name);
        e.getPlayer().teleport(this._plugin.mapHandler.getSpawnLocation());
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        if (this._plugin.playerLog.get(e.getPlayer().getName()) != 0) {
            this._plugin.controller.resetPlayer(e.getPlayer(), true);
            e.getPlayer().setHealth(0.0);
        }
        this._plugin.controller.unloadPLayer(e.getPlayer());
        this._plugin.controller.removePlayer(e.getPlayer());
    }
}

