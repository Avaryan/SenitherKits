package net.Senither.Kits.engine;

import net.Senither.Kits.Kits;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class ScoreboardManager {
    protected Team playerTeam;
    private final Kits _plugin;

    public ScoreboardManager(Kits plugin) {
        this._plugin = plugin;
        this.updateScoreboards();
    }

    @SuppressWarnings("deprecation")
	public void createPlayer(Player p) {
        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective obj = board.registerNewObjective("Stats", "dummy");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        obj.setDisplayName((Object)ChatColor.GOLD + "   Your Stats   ");
        this.playerTeam = board.registerNewTeam("player");
        this.playerTeam.setAllowFriendlyFire(true);
        this.playerTeam.addPlayer(Bukkit.getOfflinePlayer("Your Worth:"));//getOfflinePlayer((String)"Your Worth:"));
        this.playerTeam.addPlayer(Bukkit.getOfflinePlayer("Credits:"));
        this.playerTeam.addPlayer(Bukkit.getOfflinePlayer("Kills:"));
        this.playerTeam.addPlayer(Bukkit.getOfflinePlayer("Deaths:"));
        this.playerTeam.addPlayer(Bukkit.getOfflinePlayer("Killstreak:"));
        this.playerTeam.addPlayer(Bukkit.getOfflinePlayer("K/D Ratio:"));
        this.playerTeam.addPlayer(Bukkit.getOfflinePlayer("PvP Score:"));
        this.playerTeam.addPlayer(Bukkit.getOfflinePlayer("Duel Score:"));
        p.setScoreboard(board);
        Scoreboard b = p.getScoreboard();
        Objective o = b.getObjective(DisplaySlot.SIDEBAR);
        Score worthScore = o.getScore(Bukkit.getOfflinePlayer((String)"Your Worth:"));
        Score killstreakScore = o.getScore(Bukkit.getOfflinePlayer((String)"Killstreak:"));
        Score killsScore = o.getScore(Bukkit.getOfflinePlayer((String)"Kills:"));
        Score deathsScore = o.getScore(Bukkit.getOfflinePlayer((String)"Deaths:"));
        Score creditsScore = o.getScore(Bukkit.getOfflinePlayer((String)"Credits:"));
        Score kdScore = o.getScore(Bukkit.getOfflinePlayer((String)"K/D Ratio:"));
        Score pvpScore = o.getScore(Bukkit.getOfflinePlayer((String)"PvP Score:"));
        Score duelScore = o.getScore(Bukkit.getOfflinePlayer((String)"Duel Score:"));
        worthScore.setScore(999);
        creditsScore.setScore(999);
        killsScore.setScore(999);
        deathsScore.setScore(999);
        killstreakScore.setScore(999);
        kdScore.setScore(999);
        pvpScore.setScore(999);
        duelScore.setScore(999);
    }

    @SuppressWarnings("deprecation")
	public void updatePlayer(Player player) {
        Objective o;
        Scoreboard b = player.getScoreboard();
        if (b != null && (o = b.getObjective(DisplaySlot.SIDEBAR)) != null) {
            String name = player.getName();
            Score worthScore = o.getScore(Bukkit.getOfflinePlayer((String)"Your Worth:"));
            int worth = (int)Math.floor(this._plugin.playerWorth.get(name));
            Score killstreakScore = o.getScore(Bukkit.getOfflinePlayer((String)"Killstreak:"));
            int killstreak = this._plugin.playerKillstreak.get(name);
            Score killsScore = o.getScore(Bukkit.getOfflinePlayer((String)"Kills:"));
            int kills = this._plugin.playerKills.get(name);
            Score deathsScore = o.getScore(Bukkit.getOfflinePlayer((String)"Deaths:"));
            int deaths = this._plugin.playerDeaths.get(name);
            Score creditsScore = o.getScore(Bukkit.getOfflinePlayer((String)"Credits:"));
            int credits = (int)Math.floor(this._plugin.playerEco.get(name));
            Score kdScore = o.getScore(Bukkit.getOfflinePlayer((String)"K/D Ratio:"));
            int kdRatio = kills;
            if (kills != 0 && deaths != 0) {
                kdRatio = (int)Math.ceil(kills / deaths);
            }
            Score pvpScore = o.getScore(Bukkit.getOfflinePlayer((String)"PvP Score:"));
            Score duelScore = o.getScore(Bukkit.getOfflinePlayer((String)"Duel Score:"));
            int duel = this._plugin.playerDuelScore.get(name);
            worthScore.setScore(worth);
            creditsScore.setScore(credits);
            killsScore.setScore(kills);
            deathsScore.setScore(deaths);
            killstreakScore.setScore(killstreak);
            kdScore.setScore(kdRatio);
            pvpScore.setScore(kills - deaths);
            duelScore.setScore(duel);
            player.setScoreboard(b);
        }
    }

    public void updateScoreboards() {
        this._plugin.getServer().getScheduler().scheduleSyncRepeatingTask((Plugin)this._plugin, (Runnable)new Runnable(){

            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    ScoreboardManager.this.updatePlayer(player);
                }
            }
        }, 0, 40);
    }

}

