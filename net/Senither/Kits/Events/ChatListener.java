package net.Senither.Kits.Events;

import net.Senither.Kits.Kits;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener
implements Listener {
    private final Kits _plugin;
    @SuppressWarnings("unused")
	private final String prefix = "&8&l[";
    @SuppressWarnings("unused")
	private final String sufix = "&8&l] &r";

    public ChatListener(Kits plugin) {
        this._plugin = plugin;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        String pvpScore = this.getPlayerScore(e.getPlayer().getName());
        String duelScore = this.getDuelScore(e.getPlayer().getName());
        if (pvpScore == null && duelScore != null) {
            e.setFormat(this._plugin.chatManager.colorize(new StringBuilder().append("&8&l[").append(duelScore).append("&8&l] &r").toString()) + e.getFormat());
        } else if (pvpScore != null && duelScore == null) {
            e.setFormat(this._plugin.chatManager.colorize(new StringBuilder().append("&8&l[").append(pvpScore).append("&8&l] &r").toString()) + e.getFormat());
        } else if (pvpScore != null && duelScore != null) {
            e.setFormat(this._plugin.chatManager.colorize(new StringBuilder().append("&8&l[").append(pvpScore).append("&6-").append(duelScore).append("&8&l] &r").toString()) + e.getFormat());
        }
    }

    private String getPlayerScore(String player) {
        int kills = this._plugin.playerKills.get(player);
        int deaths = this._plugin.playerDeaths.get(player);
        int score = kills - deaths;
        if (score >= 200) {
            return this._plugin.chatManager.colorize("&a" + score);
        }
        return null;
    }

    private String getDuelScore(String name) {
        int duelScore = this._plugin.playerDuelScore.get(name);
        if (duelScore >= 50) {
            return this._plugin.chatManager.colorize("&c" + duelScore);
        }
        return null;
    }
}

