/*
 * Decompiled with CFR 0_82.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 */
package net.Senither.Kits.vanish;

import java.util.ArrayList;
import java.util.List;

import net.Senither.Kits.Kits;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class VanishHandler {
    private List<String> hiddenPlayers = new ArrayList<String>();
    @SuppressWarnings("unused")
	private final Kits _plugin;

    public VanishHandler(Kits plugin) {
        this._plugin = plugin;
    }

    public boolean isVanished(Player player) {
        return this.hiddenPlayers.contains(player.getName());
    }

    public void vanishPlayer(Player player) {
        if (this.isVanished(player)) {
            return;
        }
        if (!this.hiddenPlayers.contains(player.getName())) {
            this.hiddenPlayers.add(player.getName());
        }
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (this.isVanished(p)) continue;
            p.hidePlayer(player);
        }
    }

    public void showPlayer(Player player) {
        this.hiddenPlayers.remove(player.getName());
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.showPlayer(player);
        }
    }

    public void showAllPlayers() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!this.isVanished(p)) continue;
            this.showPlayer(p);
        }
    }

    public void setSpectator(Player player) {
        if (!this.hiddenPlayers.contains(player.getName())) {
            this.hiddenPlayers.add(player.getName());
        }
        for (Player p2 : Bukkit.getOnlinePlayers()) {
            if (p2.getName().equals(player.getName())) continue;
            if (!this.isVanished(p2)) continue;
            player.showPlayer(p2);
        }
        for (Player p2 : Bukkit.getOnlinePlayers()) {
            if (this.isVanished(p2)) continue;
            p2.hidePlayer(player);
        }
    }
}

