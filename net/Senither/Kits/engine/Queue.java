package net.Senither.Kits.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.kitteh.tag.TagAPI;

public class Queue {
    private List<String> players = new ArrayList<String>();
    private HashMap<String, Inventory> inventorys = new HashMap<String, Inventory>();
    private HashMap<String, ItemStack[]> armorSlots = new HashMap<String, ItemStack[]>();

    public Queue(Player challanger, Player challange) {
        this.players.add(challanger.getName());
        this.players.add(challange.getName());
    }

    public List<String> getPlayerNames() {
        return this.players;
    }

    @SuppressWarnings("deprecation")
	public List<Player> getPlayers() {
        ArrayList<Player> players = new ArrayList<Player>();
        players.add(Bukkit.getPlayer((String)this.players.get(0)));
        players.add(Bukkit.getPlayer((String)this.players.get(1)));
        return players;
    }

    @SuppressWarnings("deprecation")
	public Player getChallanger() {
        return Bukkit.getPlayer((String)this.players.get(0));
    }

    @SuppressWarnings("deprecation")
	public Player getChallange() {
        return Bukkit.getPlayer((String)this.players.get(1));
    }

    public void savePlayerData() {
        this.inventorys.clear();
        this.armorSlots.clear();
        for (Player player : this.getPlayers()) {
            String key = player.getName();
            this.inventorys.put(key, (Inventory)player.getInventory());
            this.armorSlots.put(key, player.getInventory().getArmorContents());
        }
    }

    public void resetPlayerData() {
        for (Player player : this.getPlayers()) {
            if (player == null) continue;
            String key = player.getName();
            player.getInventory().setContents(this.inventorys.get(key).getContents());
            player.getInventory().setArmorContents(this.armorSlots.get(key));
        }
    }

    public void cancelQueue(Location spawnPoint) {
        for (Player player : this.getPlayers()) {
            if (player == null) continue;
            player.setHealth(20.0);
            player.setFoodLevel(20);
            player.setFireTicks(0);
            player.teleport(spawnPoint);
            TagAPI.refreshPlayer((Player)player);
        }
    }
}

