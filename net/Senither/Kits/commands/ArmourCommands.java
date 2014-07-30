package net.Senither.Kits.commands;

import net.Senither.Kits.Kits;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ArmourCommands
implements CommandExecutor {
    private final Kits _plugin;

    public ArmourCommands(Kits plugin) {
        this._plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLable, String[] args) {
        Player player = null;
        if (!(sender instanceof Player)) {
            this._plugin.chatManager.LogInfo("You can't use that command from the console!");
            return false;
        }
        player = (Player)sender;
        if (commandLable.equalsIgnoreCase("armor")) {
            this.armourMenu(player);
        }
        if (this._plugin.duel.isPlayerInBattle(player.getName())) {
            this._plugin.chatManager.sendMessage(player, "&cYou can't use this command while in a duel!");
            return true;
        }
        if (commandLable.equalsIgnoreCase("diamond")) {
            this.diamond(player);
        } else if (commandLable.equalsIgnoreCase("iron")) {
            this.iron(player);
        } else if (commandLable.equalsIgnoreCase("gold")) {
            this.gold(player);
        } else if (commandLable.equalsIgnoreCase("leather")) {
            this.leather(player);
        }
        return false;
    }

    private void armourMenu(Player player) {
        this._plugin.chatManager.sendMessage(player, "&e -------- &6Armor Menu&e --------");
        this._plugin.chatManager.sendMessage(player, "&6/diamond&f: Price: 750 Credits");
        this._plugin.chatManager.sendMessage(player, "&6/iron&f: Price: 10 Credits");
        this._plugin.chatManager.sendMessage(player, "&6/gold&f: Price: Free");
        this._plugin.chatManager.sendMessage(player, "&6/leather&f: Price: Free");
        this._plugin.chatManager.sendMessage(player, "");
        this._plugin.chatManager.sendMessage(player, "&3Strong armor will give you less money per kill!");
        this._plugin.chatManager.sendMessage(player, "&3Weak armor will give you more money per kill!");
    }

    public void diamond(Player player) {
        if (!this._plugin.controller.playerTransaction(player, 750.0)) {
            return;
        }
        player.getInventory().setHelmet(new ItemStack(Material.DIAMOND_HELMET, 1));
        player.getInventory().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE, 1));
        player.getInventory().setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS, 1));
        player.getInventory().setBoots(new ItemStack(Material.DIAMOND_BOOTS, 1));
        this._plugin.controller.playerFinishTransaction(player, "Diamond Armor", 750.0);
        this._plugin.controller.calPlayerWorth(player, true);
    }

    public void iron(Player player) {
        if (!this._plugin.controller.playerTransaction(player, 10.0)) {
            return;
        }
        player.getInventory().setHelmet(new ItemStack(Material.IRON_HELMET, 1));
        player.getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE, 1));
        player.getInventory().setLeggings(new ItemStack(Material.IRON_LEGGINGS, 1));
        player.getInventory().setBoots(new ItemStack(Material.IRON_BOOTS, 1));
        this._plugin.controller.playerFinishTransaction(player, "Iron Armor", 10.0);
        this._plugin.controller.calPlayerWorth(player, true);
    }

    public void gold(Player player) {
        player.getInventory().setHelmet(new ItemStack(Material.GOLD_HELMET, 1));
        player.getInventory().setChestplate(new ItemStack(Material.GOLD_CHESTPLATE, 1));
        player.getInventory().setLeggings(new ItemStack(Material.GOLD_LEGGINGS, 1));
        player.getInventory().setBoots(new ItemStack(Material.GOLD_BOOTS, 1));
        this._plugin.chatManager.sendMessage(player, "&bYour &eGold Armor&b has been delivered");
        this._plugin.controller.calPlayerWorth(player, true);
    }

    public void leather(Player player) {
        player.getInventory().setHelmet(new ItemStack(Material.LEATHER_HELMET, 1));
        player.getInventory().setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE, 1));
        player.getInventory().setLeggings(new ItemStack(Material.LEATHER_LEGGINGS, 1));
        player.getInventory().setBoots(new ItemStack(Material.LEATHER_BOOTS, 1));
        this._plugin.chatManager.sendMessage(player, "&bYour &eLeather Armor&b has been delivered");
        this._plugin.controller.calPlayerWorth(player, true);
    }
}

