package net.Senither.Kits.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.Senither.Kits.Kits;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.kitteh.tag.TagAPI;

public class DuelHandler {
    private final Kits _plugin;
    private List<Queue> queue = new ArrayList<Queue>();
    private List<Location> spawnPoints = new ArrayList<Location>();
    private HashMap<String, String> challangeList = new HashMap<String, String>();
    protected Queue inProgress = null;
    private int stage = 0;
    private int counter = 5;

    public DuelHandler(Kits plugin) {
        this._plugin = plugin;
        Location spawn1 = new Location(Bukkit.getWorld((String)this._plugin.getConfig().getString("duel.1.world")), this._plugin.getConfig().getDouble("duel.1.x"), this._plugin.getConfig().getDouble("duel.1.y"), this._plugin.getConfig().getDouble("duel.1.z"));
        spawn1.setPitch((float)this._plugin.getConfig().getInt("duel.1.pitch"));
        spawn1.setYaw((float)this._plugin.getConfig().getInt("duel.1.yaw"));
        Location spawn2 = new Location(Bukkit.getWorld((String)this._plugin.getConfig().getString("duel.2.world")), this._plugin.getConfig().getDouble("duel.2.x"), this._plugin.getConfig().getDouble("duel.2.y"), this._plugin.getConfig().getDouble("duel.2.z"));
        spawn2.setPitch((float)this._plugin.getConfig().getInt("duel.2.pitch"));
        spawn2.setYaw((float)this._plugin.getConfig().getInt("duel.2.yaw"));
        this.spawnPoints.add(spawn1);
        this.spawnPoints.add(spawn2);
        this._plugin.getServer().getScheduler().scheduleSyncRepeatingTask((Plugin)plugin, (Runnable)new Runnable(){

            @Override
            public void run() {
                if (DuelHandler.this.stage == 0) {
                    if (DuelHandler.this.queue.size() != 0) {
                        DuelHandler.this.inProgress = (Queue)DuelHandler.this.queue.get(0);
                        DuelHandler.this.queue.remove(0);
                        for (Player player : DuelHandler.this.inProgress.getPlayers()) {
                            DuelHandler.access$200((DuelHandler)DuelHandler.this).chatManager.sendMessage(player, "&3You're first in line for your duel!");
                            DuelHandler.access$200((DuelHandler)DuelHandler.this).chatManager.sendMessage(player, "&3You will be teleported shortly!");
                        }
                        DuelHandler.this.stage = 1;
                        DuelHandler.this.counter = 5;
                    }
                } else if (DuelHandler.this.stage == 1) {
                    if (DuelHandler.this.counter == 0) {
                        DuelHandler.this.inProgress.savePlayerData();
                        int index = 0;
                        for (Player player : DuelHandler.this.inProgress.getPlayers()) {
                            DuelHandler.access$200((DuelHandler)DuelHandler.this).chatManager.sendMessage(player, "&3Teleporting...");
                            DuelHandler.access$200((DuelHandler)DuelHandler.this).controller.resetPlayer(player, true);
                            player.setHealth(20.0);
                            player.setFoodLevel(20);
                            player.teleport((Location)DuelHandler.this.spawnPoints.get(index));
                            ++index;
                        }
                        DuelHandler.this.counter = 6;
                        DuelHandler.this.stage = 2;
                    }
                    DuelHandler.this.counter--;
                } else if (DuelHandler.this.stage == 2) {
                    switch (DuelHandler.this.counter) {
                        case 1: 
                        case 2: 
                        case 3: 
                        case 4: 
                        case 5: {
                            for (Player player : DuelHandler.this.inProgress.getPlayers()) {
                                DuelHandler.access$200((DuelHandler)DuelHandler.this).chatManager.sendMessage(player, "&3The battle will begin in &b" + DuelHandler.this.counter + " &3second" + (DuelHandler.this.counter == 1 ? "" : "s") + "!");
                            }
                            break;
                        }
                        case 0: {
                            for (Player player : DuelHandler.this.inProgress.getPlayers()) {
                                DuelHandler.access$200((DuelHandler)DuelHandler.this).chatManager.sendMessage(player, "&3The battle has begun!");
                                DuelHandler.access$200((DuelHandler)DuelHandler.this).controller.pvpList.remove(player.getName());
                                TagAPI.refreshPlayer((Player)player);
                            }
                            DuelHandler.this.counter = 301;
                            DuelHandler.this.stage = 3;
                        }
                    }
                    DuelHandler.this.counter--;
                } else if (DuelHandler.this.stage == 3) {
                    if (DuelHandler.this.counter == 0) {
                        for (Player player : DuelHandler.this.inProgress.getPlayers()) {
                            DuelHandler.access$200((DuelHandler)DuelHandler.this).chatManager.sendMessage(player, "&3Times over, resetting inventorys and teleporting you back..");
                        }
                        DuelHandler.this.endDuel();
                    } else if (DuelHandler.this.counter == 60) {
                        for (Player player : DuelHandler.this.inProgress.getPlayers()) {
                            DuelHandler.access$200((DuelHandler)DuelHandler.this).chatManager.sendMessage(player, "&3You have &b1 &3minute left before the match will end in a tie!");
                        }
                    }
                    DuelHandler.this.counter--;
                }
            }
        }, 40, 20);
    }

    public boolean addPlayers(Player challanger, Player challange) {
        if (this.isPlayersInQueue(challanger.getName(), challange.getName())) {
            return false;
        }
        this.queue.add(new Queue(challanger, challange));
        return true;
    }

    public boolean isPlayerInQueue(String player) {
        for (Queue q : this.queue) {
            for (String p : q.getPlayerNames()) {
                if (!p.equals(player)) continue;
                return true;
            }
        }
        return false;
    }

    public boolean isPlayersInQueue(String challanger, String challange) {
        for (Queue q : this.queue) {
            for (String p : q.getPlayerNames()) {
                if (!p.equals(challanger) && !p.equals(challange)) continue;
                return true;
            }
        }
        return false;
    }

    public boolean isPlayerInBattle(String player) {
        if (this.inProgress == null) {
            return false;
        }
        for (String p : this.inProgress.getPlayerNames()) {
            if (!p.equals(player)) continue;
            return true;
        }
        return false;
    }

    public boolean isPlayersInBattle(String challanger, String challange) {
        if (this.inProgress == null) {
            return false;
        }
        for (String p : this.inProgress.getPlayerNames()) {
            if (!p.equals(challanger) && !p.equals(challange)) continue;
            return true;
        }
        return false;
    }

    public Queue getQueue(String player) {
        for (Queue q : this.queue) {
            for (String p : q.getPlayerNames()) {
                if (!p.equals(player)) continue;
                return q;
            }
        }
        return null;
    }

    public String getBattlePartner(String player) {
        for (String name : this.inProgress.getPlayerNames()) {
            if (name.equals(player)) continue;
            return name;
        }
        return null;
    }

    public void endDuel() {
        if (this.inProgress == null) {
            return;
        }
        for (String player : this.inProgress.getPlayerNames()) {
            if (this._plugin.controller.pvpList.contains(player)) continue;
            this._plugin.controller.pvpList.add(player);
        }
        this.inProgress.cancelQueue(this._plugin.mapHandler.getSpawnLocation());
        this.inProgress.resetPlayerData();
        this.inProgress = null;
        this.counter = 6;
        this.stage = 0;
    }

    @SuppressWarnings("deprecation")
	public void removePlayer(String player) {
        Queue q = this.getQueue(player);
        if (q == null) {
            if (this.inProgress == null) {
                return;
            }
            boolean check = false;
            String other = null;
            for (String sender : this.inProgress.getPlayerNames()) {
                if (sender.equals(player)) {
                    check = true;
                    continue;
                }
                other = sender;
            }
            if (check) {
                this._plugin.chatManager.sendMessage(Bukkit.getPlayer((String)other), "&b" + player + " &3has logged out!");
                this._plugin.chatManager.sendMessage(Bukkit.getPlayer((String)other), "&3Seems like you win!");
                this._plugin.controller.manageDuelRewards(Bukkit.getPlayer((String)other), Bukkit.getPlayer((String)player));
                this._plugin.chatManager.broadcastPluginMessage("&b" + other + " &3has won a duel against &b" + player + "&3!");
                this.endDuel();
            }
            return;
        }
        for (String sender : q.getPlayerNames()) {
            if (sender.equals(player)) continue;
            this._plugin.chatManager.sendMessage(Bukkit.getPlayer((String)sender), "&b" + player + " &3has logged out!");
            this._plugin.chatManager.sendMessage(Bukkit.getPlayer((String)sender), "&3You have been kicked from the duel queue!");
        }
        this.queue.remove(q);
    }

    public void challangePlayer(Player challanger, Player challange) {
        if (this.challangeList.containsValue(challange.getName()) && this.challangeList.get(challanger.getName()).equals(challange.getName())) {
            this._plugin.chatManager.sendMessage(challanger, "&aYou have accepted " + challange.getName() + "'s duel request!");
            this._plugin.chatManager.sendMessage(challanger, "&aYou have been added to the queue!");
            this._plugin.chatManager.sendMessage(challange, "&a" + challanger.getName() + " has accepted your duel request!");
            this._plugin.chatManager.sendMessage(challange, "&aYou have been added to the queue!");
            this.challangeList.remove(challanger.getName());
            this.addPlayers(challanger, challange);
            return;
        }
        if (this.isPlayerInBattle(challange.getName()) || this.isPlayerInQueue(challange.getName())) {
            this._plugin.chatManager.sendMessage(challanger, "&c" + challange.getName() + " is already in a duel queue!");
            return;
        }
        if (this.challangeList.containsKey(challange.getName())) {
            this._plugin.chatManager.sendMessage(challanger, "&c" + challange.getName() + " has already been challanged by another player!");
            return;
        }
        this.challangeList.put(challange.getName(), challanger.getName());
        this._plugin.chatManager.sendMessage(challange, "&b" + challanger.getName() + " &3has challanged you to a duel!");
        this._plugin.chatManager.sendMessage(challange, "&3Use &b/duel " + challanger.getName() + " &3to accept it!");
        this._plugin.chatManager.sendMessage(challanger, "&3You have challanged &b" + challange.getName() + " &3to a duel!");
        final String challangerF = challanger.getName();
        final String challangeF = challange.getName();
        Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin)this._plugin, (Runnable)new Runnable(){

            @SuppressWarnings("deprecation")
			@Override
            public void run() {
                if (DuelHandler.this.challangeList.containsKey(challangeF)) {
                    DuelHandler.this.challangeList.remove(challangeF);
                    Player playerChallangerF = Bukkit.getPlayer((String)challangerF);
                    if (playerChallangerF != null) {
                        DuelHandler.access$200((DuelHandler)DuelHandler.this).chatManager.sendMessage(playerChallangerF, "&c" + challangeF + " didn't accept your duel request!");
                    }
                }
            }
        }, 300);
    }

    static /* synthetic */ Kits access$200(DuelHandler x0) {
        return x0._plugin;
    }

}

