package net.Senither.Kits.Events;

import java.util.ArrayList;
import java.util.List;

import net.Senither.Kits.Kits;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.material.Wool;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.kitteh.tag.TagAPI;

public class BoosterBlockListener
implements Listener {
    private final Kits _plugin;
    private List<String> speedBoost = new ArrayList<String>();
    private List<String> jumpBoost = new ArrayList<String>();
    private List<String> slowDebuff = new ArrayList<String>();
    private List<String> witherDebuff = new ArrayList<String>();
    private List<String> fireDebuff = new ArrayList<String>();
    private List<String> nauseaDebuff = new ArrayList<String>();
    private List<String> explodeBoom = new ArrayList<String>();
    private List<String> teleportUP = new ArrayList<String>();
    private List<String> teleportDOWN = new ArrayList<String>();

    public BoosterBlockListener(Kits plugin) {
        this._plugin = plugin;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Block block = e.getPlayer().getLocation().getBlock().getRelative(BlockFace.DOWN);
        if (block.getState().getData() instanceof Wool) {
            DyeColor color = ((Wool)block.getState().getData()).getColor();
            final Player player = e.getPlayer();
            if (color == DyeColor.RED) {
                if (this._plugin.controller.pvpList.contains(player.getName())) {
                    this._plugin.controller.pvpList.remove(player.getName());
                    this._plugin.chatManager.sendMessage(player, "You no longer have spawn protection!");
                    TagAPI.refreshPlayer((Player)player);
                }
            } else if (color == DyeColor.LIME) {
                if (!this.speedBoost.contains(player.getName())) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 120, 4));
                    this.speedBoost.add(player.getName());
                    this._plugin.chatManager.sendMessage(player, "&aSpeed Block &7has been activated for 6 seconds!");
                    this._plugin.getServer().getScheduler().scheduleSyncDelayedTask((Plugin)this._plugin, (Runnable)new Runnable(){

                        @Override
                        public void run() {
                            BoosterBlockListener.this.speedBoost.remove(player.getName());
                        }
                    }, 1800);
                }
            } else if (color == DyeColor.YELLOW) {
                if (!this.jumpBoost.contains(player.getName())) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 70, 6));
                    this.jumpBoost.add(player.getName());
                    this._plugin.chatManager.sendMessage(player, "&eJump Block &7has been activated for 3 seconds!");
                    this._plugin.getServer().getScheduler().scheduleSyncDelayedTask((Plugin)this._plugin, (Runnable)new Runnable(){

                        @Override
                        public void run() {
                            BoosterBlockListener.this.jumpBoost.remove(player.getName());
                        }
                    }, 600);
                }
            } else if (color == DyeColor.BLACK) {
                if (!this.slowDebuff.contains(player.getName())) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40, 100));
                    this.slowDebuff.add(player.getName());
                    this._plugin.chatManager.sendMessage(player, "&0Trap Block &7has been activated for 2 seconds!");
                    this._plugin.getServer().getScheduler().scheduleSyncDelayedTask((Plugin)this._plugin, (Runnable)new Runnable(){

                        @Override
                        public void run() {
                            BoosterBlockListener.this.slowDebuff.remove(player.getName());
                        }
                    }, 3000);
                }
            } else if (color == DyeColor.GRAY) {
                if (!this.witherDebuff.contains(player.getName())) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 100, 0));
                    this.witherDebuff.add(player.getName());
                    this._plugin.chatManager.sendMessage(player, "&8Wither Block &7has been adtivated for 5 seconds!");
                    this._plugin.getServer().getScheduler().scheduleSyncDelayedTask((Plugin)this._plugin, (Runnable)new Runnable(){

                        @Override
                        public void run() {
                            BoosterBlockListener.this.witherDebuff.remove(player.getName());
                        }
                    }, 1210);
                }
            } else if (color == DyeColor.MAGENTA) {
                if (!this.nauseaDebuff.contains(player.getName())) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 200, 2));
                    this.nauseaDebuff.add(player.getName());
                    this._plugin.chatManager.sendMessage(player, "&5Nasuea Block &7has been activated for 10 seconds");
                    this._plugin.getServer().getScheduler().scheduleSyncDelayedTask((Plugin)this._plugin, (Runnable)new Runnable(){

                        @Override
                        public void run() {
                            BoosterBlockListener.this.nauseaDebuff.remove(player.getName());
                        }
                    }, 2400);
                }
            } else if (color == DyeColor.PINK) {
                if (!this.fireDebuff.contains(player.getName())) {
                    player.setFireTicks(70);
                    this.fireDebuff.add(player.getName());
                    this._plugin.chatManager.sendMessage(player, "&dFire Block &7has been activated for a few seconds!");
                    this._plugin.getServer().getScheduler().scheduleSyncDelayedTask((Plugin)this._plugin, (Runnable)new Runnable(){

                        @Override
                        public void run() {
                            BoosterBlockListener.this.fireDebuff.remove(player.getName());
                        }
                    }, 600);
                }
            } else if (color == DyeColor.GREEN) {
                if (!this.explodeBoom.contains(player.getName())) {
                    Location l = player.getLocation();
                    l.getWorld().createExplosion((double)l.getBlockX(), (double)(l.getBlockY() + 1), (double)l.getBlockZ(), 2.0f, false, false);
                    this.explodeBoom.add(player.getName());
                    this._plugin.chatManager.sendMessage(player, "&2TNT Block &7just went BOOM!");
                    this._plugin.getServer().getScheduler().scheduleSyncDelayedTask((Plugin)this._plugin, (Runnable)new Runnable(){

                        @Override
                        public void run() {
                            BoosterBlockListener.this.explodeBoom.remove(player.getName());
                        }
                    }, 600);
                }
            } else if (color == DyeColor.LIGHT_BLUE) {
                if (!this.teleportUP.contains(player.getName())) {
                    player.teleport(player.getLocation().add(0.0, 10.0, 0.0));
                    this.teleportDOWN.add(player.getName());
                    this._plugin.chatManager.sendMessage(player, "&bYou just got teleported &310&b blocks up!");
                    this._plugin.getServer().getScheduler().scheduleSyncDelayedTask((Plugin)this._plugin, (Runnable)new Runnable(){

                        @Override
                        public void run() {
                            BoosterBlockListener.this.teleportDOWN.remove(player.getName());
                        }
                    }, 100);
                }
            } else if (!(color != DyeColor.BLUE || this.teleportDOWN.contains(player.getName()))) {
                player.teleport(player.getLocation().subtract(0.0, 10.0, 0.0));
                this.teleportUP.add(player.getName());
                this._plugin.chatManager.sendMessage(player, "&bYou just got teleported &310&b blocks down!");
                this._plugin.getServer().getScheduler().scheduleSyncDelayedTask((Plugin)this._plugin, (Runnable)new Runnable(){

                    @Override
                    public void run() {
                        BoosterBlockListener.this.teleportUP.remove(player.getName());
                    }
                }, 100);
            }
        }
    }

}

