package net.Senither.Kits.Events;

import java.util.Random;

import net.Senither.Kits.Kits;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.kitteh.tag.AsyncPlayerReceiveNameTagEvent;
import org.kitteh.tag.TagAPI;

public class PlayerListener implements Listener {
	private final Kits _plugin;

	public PlayerListener(Kits plugin) {
		this._plugin = plugin;
	}

	@EventHandler
	public void onNameTag(AsyncPlayerReceiveNameTagEvent e) {
		String name;
		String useName = name = e.getNamedPlayer().getName();
		if (useName.length() > 14) {
			useName = useName.substring(0, 12);
		}
		if (this._plugin.controller.hasPvPEnabled(name)) {
			if (this._plugin.playerWorth.get(e.getNamedPlayer().getName()) >= 22.0) {
				e.setTag((Object) ChatColor.RED + useName);
			} else if (this._plugin.playerWorth.get(e.getNamedPlayer()
					.getName()) <= 7.0) {
				e.setTag((Object) ChatColor.YELLOW + useName);
			} else {
				e.setTag((Object) ChatColor.AQUA + useName);
			}
		} else {
			e.setTag((Object) ChatColor.WHITE + useName);
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
		Player killer;
		e.setDeathMessage((String) null);
		e.getDrops().clear();
		Player player = e.getEntity();
		if (e.getEntity().getKiller() instanceof Player) {
			killer = e.getEntity().getKiller();
		} else if (e.getEntity().getKiller() instanceof Arrow) {
			Arrow arrow = (Arrow) e.getEntity().getKiller();
			killer = (Player) arrow.getShooter();
		} else {
			if (this._plugin.duel.isPlayerInBattle(player.getName())) {
				Player duelKiller = Bukkit.getPlayer((String) this._plugin.duel
						.getBattlePartner(player.getName()));
				this._plugin.chatManager.sendMessage(duelKiller,
						"&a&lYou have won the duel!");
				this._plugin.chatManager.sendMessage(player,
						"&a&lYou have lost the duel!");
				this._plugin.controller.manageDuelRewards(duelKiller, player);
				this._plugin.chatManager.broadcastPluginMessage("&b"
						+ duelKiller.getName() + " &3has won a duel against &b"
						+ player.getName() + "&3!");
				this._plugin.duel.endDuel();
				return;
			}
			return;
		}
		if (this._plugin.duel.isPlayerInBattle(player.getName())) {
			Player duelKiller = Bukkit.getPlayer((String) this._plugin.duel
					.getBattlePartner(player.getName()));
			this._plugin.chatManager.sendMessage(duelKiller,
					"&a&lYou have won the duel!");
			this._plugin.chatManager.sendMessage(player,
					"&a&lYou have lost the duel!");
			this._plugin.controller.manageDuelRewards(duelKiller, player);
			this._plugin.chatManager.broadcastPluginMessage("&b"
					+ duelKiller.getName() + " &3has won a duel against &b"
					+ player.getName() + "&3!");
			this._plugin.duel.endDuel();
			return;
		}
		this._plugin.playerDeaths.put(player.getName(),
				this._plugin.playerDeaths.get(player.getName()) + 1);
		this._plugin.playerKills.put(killer.getName(),
				this._plugin.playerKills.get(killer.getName()) + 1);
		this._plugin.playerLog.put(player.getName(), 0);
		double worth = this._plugin.playerWorth.get(player.getName());
		double add = this._plugin.playerWorth.get(killer.getName());
		if (add <= 4.0) {
			worth += 10.35;
		} else if (add <= 6.0) {
			worth += 6.55;
		} else if (add <= 8.0) {
			worth += 2.61;
		} else if (add > 10.0) {
			worth = add <= 12.0 ? (worth -= 1.09)
					: (add <= 14.0 ? (worth -= 2.57)
							: (add <= 18.0 ? (worth -= 5.23)
									: (add <= 22.0 ? (worth -= 8.74)
											: (worth -= 11.11))));
		}
		if (worth <= 1.0) {
			worth = 0.59;
		}
		add += 0.31;
		this._plugin.playerEco.put(killer.getName(),
				this._plugin.playerEco.get(killer.getName()) + worth);
		String nworth = "" + worth;
		String[] aworth = nworth.split("\\.");
		if (aworth[1].length() < 2) {
			String[] arrstring = aworth;
			arrstring[1] = arrstring[1] + "0";
		}
		int playerKS = this._plugin.playerKillstreak.get(player.getName());
		this._plugin.playerKillstreak.put(player.getName(), 0);
		int killerKS = this._plugin.playerKillstreak.get(killer.getName()) + 1;
		this._plugin.playerKillstreak.put(killer.getName(), killerKS);
		if (this._plugin.playerUsingKits.contains(player.getName())) {
			this._plugin.playerUsingKits.remove(player.getName());
		}
		boolean broadcastKillstreak = false;
		for (int i = 5; i < 1000; i += 5) {
			if (killerKS != i)
				continue;
			broadcastKillstreak = true;
		}
		if (broadcastKillstreak) {
			this._plugin.chatManager.broadcastPluginMessage("&6"
					+ killer.getName() + "&a is on a &6" + killerKS
					+ "&a killstreak!");
			this._plugin.controller.giveKillstreakReward(killer);
		}
		this._plugin.chatManager.sendMessage(killer, "&bYou received &e"
				+ aworth[0] + "." + aworth[1].substring(0, 2)
				+ " &bcredits for killing &e" + player.getName());
		this._plugin.chatManager.sendMessage(killer,
				"&bYou now have a &eKill Streak &bof &e" + killerKS);
		this._plugin.chatManager.sendMessage(player,
				"&e" + killer.getName() + " &breceived &e" + aworth[0] + "."
						+ aworth[1].substring(0, 2) + " &bfor killing you");
		this._plugin.chatManager.sendMessage(player,
				"&bYou died with a &eKill Streak &bof &e" + playerKS);
		this._plugin.playerWorth.put(killer.getName(), add);
		TagAPI.refreshPlayer((Player) killer);
	}

	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent e) {
		e.setRespawnLocation(this._plugin.mapHandler.getSpawnLocation());
		this._plugin.controller.resetPlayer(e.getPlayer(), true);
	}

	@EventHandler
	public void onPlayerDamage(EntityDamageByEntityEvent e) {
		Snowball snowball;
		Player target = null;
		Player damager = null;
		if (e.getEntity() instanceof Player
				&& this._plugin.controller.pvpList
						.contains((target = (Player) e.getEntity()).getName())) {
			e.setCancelled(true);
			return;
		}
		if (e.getDamager() instanceof Player) {
			damager = (Player) e.getDamager();
		} else if (e.getDamager() instanceof Arrow) {
			Arrow arrow;
			if ((arrow = (Arrow) e.getDamager()).getShooter() instanceof Player) {
				damager = (Player) arrow.getShooter();
			}
		} else if (e.getDamager() instanceof Snowball
				&& (snowball = (Snowball) e.getDamager()).getShooter() instanceof Player) {
			damager = (Player) snowball.getShooter();
			if (target.hasPotionEffect(PotionEffectType.SLOW)) {
				Random random = new Random();
				e.setDamage((double) random.nextInt(3) + 2.0);
				target.removePotionEffect(PotionEffectType.SLOW);
				target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,
						100, 1));
			} else {
				target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,
						100, 1));
				this._plugin.chatManager.sendMessage(damager,
						"&bYou have slowed " + target.getName()
								+ " for &35 &bseconds!");
			}
		}
		if (damager != null
				&& this._plugin.controller.pvpList.contains(damager.getName())) {
			this._plugin.controller.pvpList.remove(damager.getName());
			this._plugin.chatManager.sendMessage(damager,
					"You no longer have spawn protection!");
			TagAPI.refreshPlayer((Player) damager);
		}
		if (damager != null) {
			this._plugin.playerLog.put(damager.getName(), 5);
		}
		this._plugin.playerLog.put(target.getName(), 5);
	}

	@EventHandler
	public void onRegainHealth(EntityRegainHealthEvent e) {
		if (e.getEntity() instanceof Player
				&& e.getRegainReason() == EntityRegainHealthEvent.RegainReason.SATIATED) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerFoodLevelChange(FoodLevelChangeEvent e) {
		if (!this._plugin.controller.hasHungerEnabled(e.getEntity().getName())) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onItemDrop(PlayerDropItemEvent e) {
		if (e.getItemDrop().getItemStack().getType() == Material.BOWL) {
			e.getItemDrop().remove();
		} else {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onItemClick(InventoryClickEvent e) {
		int id;
		if (e.getCurrentItem() == null || e.getCursor() == null
				|| e.getWhoClicked() == null) {
			return;
		}
		if (e.getInventory().getTitle().equals("Player Stats")) {
			e.setCancelled(true);
			e.getWhoClicked().closeInventory();
			this._plugin.chatManager.sendMessage((Player) e.getWhoClicked(),
					"&cYou can't move that item!");
		}
		if (!(this._plugin.controller.canBuild(e.getWhoClicked().getName()) || (id = e
				.getSlot()) != 0
				&& id != 36
				&& id != 37
				&& id != 38
				&& id != 39)) {
			e.setCancelled(true);
			e.getWhoClicked().closeInventory();
			this._plugin.chatManager.sendMessage((Player) e.getWhoClicked(),
					"&cYou can't move that item!");
		}
	}

	@EventHandler
	public void onRightClick(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if (p.getItemInHand().getType() == Material.MUSHROOM_SOUP) {
			if (e.getAction() == Action.RIGHT_CLICK_AIR
					|| e.getAction() == Action.RIGHT_CLICK_BLOCK) {
				if (p.getHealth() != p.getMaxHealth()) {
					e.setCancelled(true);
					int healAmount = 6;
					p.setHealth(p.getHealth() + healAmount > p.getMaxHealth() ? p
							.getMaxHealth() : p.getHealth() + healAmount);
					p.getItemInHand().setType(Material.BOWL);
				}
				if (p.getFoodLevel() < 20) {
					p.setFoodLevel(20);
					p.getItemInHand().setType(Material.BOWL);
				}
			}
		}
		if (p.getItemInHand().getType() == Material.MILK_BUCKET) {
			if (e.getAction() == Action.RIGHT_CLICK_AIR
					|| e.getAction() == Action.RIGHT_CLICK_BLOCK) {
				e.setCancelled(true);
				if (p.getHealth() != p.getMaxHealth()) {
					e.setCancelled(true);
					int healAmount = 6;
					p.setHealth(p.getHealth() + healAmount > p.getMaxHealth() ? p
							.getMaxHealth() : p.getHealth() + healAmount);
					p.getItemInHand().setType(Material.BOWL);
				}
				if (p.getFoodLevel() < 20) {
					p.setFoodLevel(20);
					p.getItemInHand().setType(Material.AIR);
				}
			}
		}
	}
}
