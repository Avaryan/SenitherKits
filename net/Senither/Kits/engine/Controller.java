package net.Senither.Kits.engine;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.Senither.Kits.Kits;
import net.Senither.Kits.ulits.YAMLManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.kitteh.tag.TagAPI;

public class Controller {
    private final Kits _plugin;
    public List<String> pvpList = new ArrayList<String>();
    public List<String> buildList = new ArrayList<String>();
    public List<String> hungerList = new ArrayList<String>();

    public Controller(final Kits plugin) {
        this._plugin = plugin;
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable(){

            @Override
            public void run() {
                if (Bukkit.getOnlinePlayers().length != 0) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        String key = player.getName();
                        String useName = player.getName();
                        if (plugin.playerLog.get(key) != 0) {
                            plugin.playerLog.put(key, plugin.playerLog.get(player.getName()) - 1);
                        }
                        if (key.length() > 14) {
                            useName = useName.substring(0, 12);
                        }
                        if (plugin.controller.hasPvPEnabled(key)) {
                            if (plugin.playerWorth.get(key) >= 22.0) {
                                player.setPlayerListName(ChatColor.RED + useName);
                                continue;
                            }
                            if (plugin.playerWorth.get(key) <= 7.0) {
                                player.setPlayerListName(ChatColor.YELLOW + useName);
                                continue;
                            }
                            player.setPlayerListName(ChatColor.AQUA + useName);
                            continue;
                        }
                        player.setPlayerListName(ChatColor.GRAY + useName);
                    }
                }
            }
        }, 40, 20);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable(){

            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    for (ItemStack item : player.getInventory().getArmorContents()) {
                        item.setDurability((short) -32768);
                    }
                    if (player.getInventory().getItem(0) == null) continue;
                    player.getInventory().getItem(0).setDurability((short) -32768);
                }
            }
        }, 300, 300);
    }

    public void loadPlayer(Player player) {
        if (!this._plugin.playerConfig.containsKey(player.getName())) {
            this._plugin.playerConfig.put(player.getName(), new YAMLManager(this._plugin, player.getName() + ".yml"));
        }
        YAMLManager config = this._plugin.playerConfig.get(player.getName());
        this._plugin.playerKillstreak.put(player.getName(), 0);
        this._plugin.playerEco.put(player.getName(), config.getConfig().getDouble("credits"));
        this._plugin.playerDeaths.put(player.getName(), config.getConfig().getInt("deaths"));
        this._plugin.playerKills.put(player.getName(), config.getConfig().getInt("kills"));
        this._plugin.playerDuelScore.put(player.getName(), config.getConfig().getInt("dualscore"));
        this._plugin.playerWorth.put(player.getName(), 10.0);
        this._plugin.playerLog.put(player.getName(), 0);
        player.setFoodLevel(20);
        player.setHealth(20.0);
        player.setAllowFlight(false);
    }

    public void unloadPLayer(Player player) {
        YAMLManager config = this._plugin.playerConfig.get(player.getName());
        config.getConfig().set("credits", this._plugin.playerEco.get(player.getName()));
        config.getConfig().set("kills", this._plugin.playerKills.get(player.getName()));
        config.getConfig().set("deaths", this._plugin.playerDeaths.get(player.getName()));
        config.getConfig().set("dualscore", this._plugin.playerDuelScore.get(player.getName()));
        config.getConfig().set("lastlogin", 0);
        config.getConfig().set("achivements.killstreak.10", false);
        config.getConfig().set("achivements.killstreak.25", false);
        config.getConfig().set("achivements.killstreak.50", false);
        config.getConfig().set("achivements.killstreak.75", false);
        config.getConfig().set("achivements.killstreak.100", false);
        config.getConfig().set("achivements.enchants.sharpness4", false);
        config.getConfig().set("achivements.enchants.protection3", false);
        config.getConfig().set("achivements.armour.diamond", false);
        config.getConfig().set("achivements.armour.leather", false);
        config.getConfig().set("achivements.kit.ninja", false);
        config.getConfig().set("achivements.kit.medic", false);
        config.getConfig().set("achivements.kills.100", false);
        config.getConfig().set("achivements.kills.500", false);
        config.getConfig().set("achivements.kills.1000", false);
        config.getConfig().set("achivements.kills.2500", false);
        config.getConfig().set("achivements.kills.5000", false);
        config.getConfig().set("achivements.kills.10000", false);
        config.saveConfig();
        this._plugin.playerConfig.remove(player.getName());
    }

    public void removePlayer(Player player) {
        this._plugin.duel.removePlayer(player.getName());
        this._plugin.playerKillstreak.remove(player.getName());
        this._plugin.playerEco.remove(player.getName());
        this._plugin.playerDeaths.remove(player.getName());
        this._plugin.playerKills.remove(player.getName());
        this._plugin.playerWorth.remove(player.getName());
        this._plugin.slowList.remove(player.getName());
        this._plugin.weakList.remove(player.getName());
        this._plugin.playerLog.remove(player.getName());
        this._plugin.playerDuelScore.remove(player.getName());
        this.hungerList.remove(player.getName());
        this.buildList.remove(player.getName());
        this.pvpList.remove(player.getName());
        if (player.hasPotionEffect(PotionEffectType.SLOW)) {
            player.removePotionEffect(PotionEffectType.SLOW);
        }
        if (player.hasPotionEffect(PotionEffectType.WEAKNESS)) {
            player.removePotionEffect(PotionEffectType.WEAKNESS);
        }
        if (player.hasPotionEffect(PotionEffectType.SPEED)) {
            player.removePotionEffect(PotionEffectType.SPEED);
        }
        if (player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
            player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
        }
        if (player.hasPotionEffect(PotionEffectType.REGENERATION)) {
            player.removePotionEffect(PotionEffectType.REGENERATION);
        }
        if (player.hasPotionEffect(PotionEffectType.POISON)) {
        	player.removePotionEffect(PotionEffectType.POISON);
        }
        if (this._plugin.vanish.isVanished(player)) {
            this._plugin.vanish.showPlayer(player);
        }
    }

    public void resetPlayer(Player player, boolean completeReset) {
        if (player.hasPotionEffect(PotionEffectType.SLOW)) {
            player.removePotionEffect(PotionEffectType.SLOW);
        }
        if (player.hasPotionEffect(PotionEffectType.WEAKNESS)) {
            player.removePotionEffect(PotionEffectType.WEAKNESS);
        }
        if (player.hasPotionEffect(PotionEffectType.POISON)) {
        	player.removePotionEffect(PotionEffectType.POISON);
        }
        if (completeReset) {
            PlayerInventory i = player.getInventory();
            i.clear();
            i.addItem(new ItemStack[]{new ItemStack(Material.DIAMOND_SWORD, 1)});
            for (int s = 1; s <= 26; ++s) {
                i.addItem(new ItemStack[]{new ItemStack(Material.MILK_BUCKET, 1)});
            }
            player.getInventory().setHelmet(new ItemStack(Material.IRON_HELMET, 1));
            player.getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE, 1));
            player.getInventory().setLeggings(new ItemStack(Material.IRON_LEGGINGS, 1));
            player.getInventory().setBoots(new ItemStack(Material.IRON_BOOTS, 1));
            this._plugin.playerKillstreak.put(player.getName(), 0);
            this._plugin.playerWorth.put(player.getName(), 10.0);
            if (!this.pvpList.contains(player.getName())) {
                this.pvpList.add(player.getName());
            }
            final String name = player.getName();
            Bukkit.getScheduler().scheduleSyncDelayedTask(this._plugin, new Runnable(){

                @SuppressWarnings("deprecation")
				@Override
                public void run() {
                    TagAPI.refreshPlayer(Bukkit.getPlayer(name));
                }
            }, 20);
        } else {
            this._plugin.playerKillstreak.put(player.getName(), 0);
            if (!this.pvpList.contains(player.getName())) {
                this.pvpList.add(player.getName());
            }
            this.calPlayerWorth(player, true);
        }
    }

    public void calPlayerWorth(Player player, boolean updateTag) {
        if (this.canBuild(player.getName())) {
            return;
        }
        PlayerInventory i = player.getInventory();
        double worth = 0.0;
        Material type = null;
        if (player.getInventory().getHelmet() != null) {
            type = player.getInventory().getHelmet().getType();
        } else if (player.getInventory().getChestplate() != null) {
            type = player.getInventory().getChestplate().getType();
        } else if (player.getInventory().getLeggings() != null) {
            type = player.getInventory().getLeggings().getType();
        } else if (player.getInventory().getBoots() != null) {
            type = player.getInventory().getBoots().getType();
        }
        if (type != null) {
            worth = type == Material.DIAMOND_HELMET || type == Material.DIAMOND_CHESTPLATE || type == Material.DIAMOND_LEGGINGS || type == Material.DIAMOND_BOOTS ? 15.5 : (type == Material.IRON_HELMET || type == Material.IRON_CHESTPLATE || type == Material.IRON_LEGGINGS || type == Material.IRON_BOOTS ? 10.0 : (type == Material.GOLD_HELMET || type == Material.GOLD_CHESTPLATE || type == Material.GOLD_LEGGINGS || type == Material.GOLD_BOOTS ? 8.2 : (type == Material.CHAINMAIL_HELMET || type == Material.CHAINMAIL_CHESTPLATE || type == Material.CHAINMAIL_LEGGINGS || type == Material.CHAINMAIL_BOOTS ? 8.2 : 7.5)));
            int enchantmentArmor = player.getInventory().getHelmet().getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL);
            if (enchantmentArmor == 1) {
                worth+=1.5;
            } else if (enchantmentArmor == 2) {
                worth+=3.0;
            } else if (enchantmentArmor == 3) {
                worth+=4.8;
            }
            if (i.contains(Material.BOW)) {
                int enchantment;
                worth-=1.0;
                if ((enchantment = i.getItem(0).getEnchantmentLevel(Enchantment.ARROW_DAMAGE)) == 1) {
                    worth+=1.0;
                } else if (enchantment == 2) {
                    worth+=1.8;
                } else if (enchantment == 3) {
                    worth+=2.8;
                } else if (enchantment == 4) {
                    worth+=4.0;
                }
            } else {
                int enchantmentSharpness = i.getItem(0).getEnchantmentLevel(Enchantment.DAMAGE_ALL);
                int enchantmentKnockback = i.getItem(0).getEnchantmentLevel(Enchantment.KNOCKBACK);
                if (enchantmentSharpness == 1) {
                    worth+=1.0;
                } else if (enchantmentSharpness == 2) {
                    worth+=1.8;
                } else if (enchantmentSharpness == 3) {
                    worth+=2.8;
                } else if (enchantmentSharpness == 4) {
                    worth+=4.0;
                }
                if (enchantmentKnockback == 1) {
                    worth+=1.0;
                } else if (enchantmentKnockback == 2) {
                    worth+=2.0;
                } else if (enchantmentKnockback == 3) {
                    worth+=3.2;
                }
            }
            if (player.hasPotionEffect(PotionEffectType.SPEED)) {
                worth+=0.8;
            }
            if (player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
                worth+=2.5;
            }
            if (player.hasPotionEffect(PotionEffectType.REGENERATION)) {
                worth+=2.2;
            }
            if (player.hasPotionEffect(PotionEffectType.SLOW)) {
                worth-=1.6;
            }
            if (player.hasPotionEffect(PotionEffectType.WEAKNESS)) {
                worth-=1.5;
            }
            if (this.hasHungerEnabled(player.getName())) {
                worth-=0.4;
            }
            this._plugin.playerWorth.put(player.getName(), worth+=(double)this._plugin.playerKillstreak.get(player.getName()).intValue() * 0.31);
            if (updateTag) {
                TagAPI.refreshPlayer((Player)player);
            }
        } else {
            this.resetPlayer(player, true);
        }
    }

    public boolean playerTransaction(Player player, double amount) {
        double playerBalance = this._plugin.playerEco.get(player.getName());
        if (playerBalance <= amount) {
            DecimalFormat df = new DecimalFormat("#.00");
            this._plugin.chatManager.sendMessage(player, "&cThis would cost &e" + amount + " credits &cbut you only have &e" + df.format(playerBalance) + " credits&c!");
            return false;
        }
        this._plugin.playerEco.put(player.getName(), playerBalance - amount);
        return true;
    }

    public void playerFinishTransaction(Player player, String purchase, double costs) {
        this._plugin.chatManager.sendMessage(player, "&bYour &e" + purchase + "&b has been delivered, at a cost of &e" + costs + " credits&b!");
    }

    public boolean hasPvPEnabled(String name) {
        return !this.pvpList.contains(name);
    }

    public boolean canBuild(String name) {
        return this.buildList.contains(name);
    }

    public boolean hasHungerEnabled(String name) {
        return this.hungerList.contains(name);
    }

    public void giveKillstreakReward(Player player) {
        Random random = new Random();
        int uid = random.nextInt(4);
        if (uid == 0) {
            PlayerInventory i = player.getInventory();
            i.remove(Material.MILK_BUCKET);
            i.remove(Material.BOWL);
            for (int s = 1; s <= 27; ++s) {
                i.addItem(new ItemStack[]{new ItemStack(Material.MILK_BUCKET, 1)});
            }
            this._plugin.chatManager.sendMessage(player, "&aYour inventory have been refilled with milk!");
        } else if (uid == 1) {
            PlayerInventory i = player.getInventory();
            i.addItem(new ItemStack[]{new ItemStack(Material.TNT, 1)});
            i.addItem(new ItemStack[]{new ItemStack(Material.TNT, 1)});
            i.addItem(new ItemStack[]{new ItemStack(Material.TNT, 1)});
            this._plugin.chatManager.sendMessage(player, "&aYou got 3 TNT Blocks for your awesome killstreak!");
        } else if (uid == 2) {
            this._plugin.vanish.vanishPlayer(player);
            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 100, 1));
            this._plugin.chatManager.sendMessage(player, "&aYou have vanished for 5 seconds!");
            final String name = player.getName();
            Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin)this._plugin, (Runnable)new Runnable(){

                @SuppressWarnings("deprecation")
				@Override
                public void run() {
                    Controller.access$000((Controller)Controller.this).vanish.showPlayer(Bukkit.getPlayer(name));
                }
            }, 100);
        } else if (uid == 3) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 2));
            this._plugin.chatManager.sendMessage(player, "&aYou just got extra regen! How lucky are you!?");
        }
    }

    @SuppressWarnings("null")
	public void manageDuelRewards(Player winner, Player losser) {
        if (losser != null) {
            int elo = this._plugin.playerDuelScore.get(losser.getName());
            int add = 1;
            int sub = 0;
            if (elo <= -25) {
                sub = 0;
            } else if (elo <= 0) {
                sub = 1;
            } else if (elo <= 75) {
                sub = 2;
            } else if (elo <= 150) {
                sub = 3;
            } else if (elo <= 300) {
                sub = 4;
            } else if (elo <= 500) {
                sub = 5;
            } else if (elo <= 600) {
                sub = 10;
            } else if (elo <= 700) {
                sub = 20;
            } else if (elo <= 800) {
                sub = 30;
            } else if (elo <= 900) {
                sub = 40;
            } else if (elo <= 1000) {
                sub = 50;
            }
            this._plugin.playerDuelScore.put(winner.getName(), this._plugin.playerDuelScore.get(winner.getName()) + add);
            this._plugin.chatManager.sendMessage(winner, "&3You got &b" + add + " &3duel score for winning the duel!");
            this._plugin.playerDuelScore.put(losser.getName(), this._plugin.playerDuelScore.get(losser.getName()) - sub);
            this._plugin.chatManager.sendMessage(losser, "&3You lost &b" + sub + " &3duel score for lossing the duel!");
        } else {
            int add = 1;
            int sub = 5;
            this._plugin.playerDuelScore.put(winner.getName(), this._plugin.playerDuelScore.get(winner.getName()) + add);
            this._plugin.chatManager.sendMessage(winner, "&3You got &b" + add + " &3duel score for winning the duel!");
            YAMLManager config = new YAMLManager((JavaPlugin)this._plugin, losser.getName() + ".yml");
            config.getConfig().set("dualscore", (Object)(config.getConfig().getInt("dualscore") - sub));
            config.saveConfig();
        }
    }

    static Kits access$000(Controller x0) {
        return x0._plugin;
    }

}

