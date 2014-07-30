package net.Senither.Kits.commands;

import net.Senither.Kits.Kits;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class BuffCommands
implements CommandExecutor {
    private final Kits _plugin;

    public BuffCommands(Kits plugin) {
        this._plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLable, String[] args) {
        Player player = null;
        if (!(sender instanceof Player)) {
            this._plugin.chatManager.LogInfo("You can't use that command from the console!");
            return false;
        }
        player = (Player)sender;
        if (commandLable.equalsIgnoreCase("buffs")) {
            this.buffMenu(player);
            return true;
        }
        if (this._plugin.playerUsingKits.contains(player.getName())) {
            this._plugin.chatManager.sendMessage(player, "&cYou can't use this command while wearing a special kit!");
            return true;
        }
        if (this._plugin.duel.isPlayerInBattle(player.getName())) {
            this._plugin.chatManager.sendMessage(player, "&cYou can't use this command while in a duel!");
            return true;
        }
        if (commandLable.equalsIgnoreCase("debuffs")) {
            this.debuffMenu(player);
        } else if (commandLable.equalsIgnoreCase("speed")) {
            this.speed(player);
        } else if (commandLable.equalsIgnoreCase("strength")) {
            this.strength(player);
        } else if (commandLable.equalsIgnoreCase("regen")) {
            this.regen(player);
        } else if (commandLable.equalsIgnoreCase("hunger")) {
            this.hunger(player);
        } else if (commandLable.equalsIgnoreCase("slow")) {
            this.slowness(player);
        } else if (commandLable.equalsIgnoreCase("weak")) {
            this.weakness(player);
        }
        return false;
    }

    private void buffMenu(Player player) {
        this._plugin.chatManager.sendMessage(player, "&e -------- &6Buff Menu &e--------");
        this._plugin.chatManager.sendMessage(player, "&6/speed&f: Price: 150 Credits - Last for: 8 minutes");
        this._plugin.chatManager.sendMessage(player, "&6/strength&f: Price: 200 Credits - Last for: 5 minutes");
        this._plugin.chatManager.sendMessage(player, "&6/regen&f: Price: 250 Credits - Last for: 5 minutes");
        this._plugin.chatManager.sendMessage(player, "");
        this._plugin.chatManager.sendMessage(player, "&3Make yourself powerful but get less money for your kills!");
    }

    private void debuffMenu(Player player) {
        this._plugin.chatManager.sendMessage(player, "&e -------- &6Buff Menu &e--------");
        this._plugin.chatManager.sendMessage(player, "&6/hunger&f: Price: Free");
        this._plugin.chatManager.sendMessage(player, "&6/slow&f: Price: Free");
        this._plugin.chatManager.sendMessage(player, "&6/weak&f: Price: Free");
        this._plugin.chatManager.sendMessage(player, "");
        this._plugin.chatManager.sendMessage(player, "&3Add a litte more challange with de-buffs!");
        this._plugin.chatManager.sendMessage(player, "&3And get more money for your kills!");
    }

    private void speed(Player player) {
        if (!this._plugin.controller.playerTransaction(player, 150.0)) {
            return;
        }
        if (player.hasPotionEffect(PotionEffectType.SPEED)) {
            player.removePotionEffect(PotionEffectType.SPEED);
        }
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 9600, 0));
        this._plugin.controller.playerFinishTransaction(player, "Speed Boost", 150.0);
        this._plugin.controller.calPlayerWorth(player, true);
    }

    public void strength(Player player) {
        if (!this._plugin.controller.playerTransaction(player, 200.0)) {
            return;
        }
        if (player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
            player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
        }
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 6000, 0));
        this._plugin.controller.playerFinishTransaction(player, "Damage Boost", 200.0);
        this._plugin.controller.calPlayerWorth(player, true);
    }

    public void regen(Player player) {
        if (!this._plugin.controller.playerTransaction(player, 250.0)) {
            return;
        }
        if (player.hasPotionEffect(PotionEffectType.REGENERATION)) {
            player.removePotionEffect(PotionEffectType.REGENERATION);
        }
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 6000, 0));
        this._plugin.controller.playerFinishTransaction(player, "Regen Boost", 250.0);
        this._plugin.controller.calPlayerWorth(player, true);
    }

    public void hunger(Player player) {
        if (this._plugin.controller.hasHungerEnabled(player.getName())) {
            this._plugin.controller.hungerList.remove(player.getName());
            this._plugin.chatManager.sendMessage(player, "Hunger has been disabled");
        } else {
            this._plugin.controller.hungerList.add(player.getName());
            this._plugin.chatManager.sendMessage(player, "Hunger has been enabled");
            this._plugin.chatManager.sendMessage(player, "Use /hunger again to disable it");
        }
        this._plugin.controller.calPlayerWorth(player, true);
    }

    public void slowness(Player player) {
        if (this._plugin.slowList.contains(player.getName())) {
            this._plugin.slowList.remove(player.getName());
            this._plugin.chatManager.sendMessage(player, "Slowness has been disabled");
            this._plugin.chatManager.sendMessage(player, "Please wait 6 seconds for it to disappear");
            player.removePotionEffect(PotionEffectType.SLOW);
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 130, 1));
        } else {
            if (player.hasPotionEffect(PotionEffectType.SLOW)) {
                this._plugin.chatManager.sendMessage(player, "&cPlease wait for the slowness to run out before enabling it again");
                return;
            }
            this._plugin.slowList.add(player.getName());
            this._plugin.chatManager.sendMessage(player, "Slowness has been enabled");
            this._plugin.chatManager.sendMessage(player, "Use /slow again to disable it");
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 10000000, 1));
        }
        this._plugin.controller.calPlayerWorth(player, true);
    }

    public void weakness(Player player) {
        if (this._plugin.weakList.contains(player.getName())) {
            this._plugin.weakList.remove(player.getName());
            this._plugin.chatManager.sendMessage(player, "Weakness has been disabled");
            this._plugin.chatManager.sendMessage(player, "Please wait 6 seconds for it to disappear");
            player.removePotionEffect(PotionEffectType.WEAKNESS);
            player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 130, 1));
        } else {
            if (player.hasPotionEffect(PotionEffectType.WEAKNESS)) {
                this._plugin.chatManager.sendMessage(player, "&cPlease wait for the Weakness to run out before enabling it again");
                return;
            }
            this._plugin.weakList.add(player.getName());
            this._plugin.chatManager.sendMessage(player, "Weakness has been enabled");
            this._plugin.chatManager.sendMessage(player, "Use /weak again to disable it");
            player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 10000000, 1));
        }
        this._plugin.controller.calPlayerWorth(player, true);
    }
}

