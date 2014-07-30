package net.Senither.Kits.commands;

import net.Senither.Kits.Kits;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class WeaponCommands
implements CommandExecutor {
    private final Kits _plugin;

    public WeaponCommands(Kits plugin) {
        this._plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLable, String[] args) {
        Player player = null;
        if (!(sender instanceof Player)) {
            this._plugin.chatManager.LogInfo("You can't use that command from the console!");
            return false;
        }
        player = (Player)sender;
        if (commandLable.equalsIgnoreCase("weapons")) {
            this.weaponMenu(player);
            return true;
        }
        if (this._plugin.playerUsingKits.contains(player.getName())) {
            this._plugin.chatManager.sendMessage(player, "&cYou can't use this command while using a kit!");
            return true;
        }
        if (commandLable.equalsIgnoreCase("sword")) {
            this.sword(player);
        } else if (commandLable.equalsIgnoreCase("bow")) {
            this.bow(player);
        } else if (commandLable.equalsIgnoreCase("axe")) {
            this.axe(player);
        }
        return false;
    }

    private void weaponMenu(Player player) {
        this._plugin.chatManager.sendMessage(player, "&e -------- &6Weapon Menu&e --------");
        this._plugin.chatManager.sendMessage(player, "&6/sword&f: Price: Free");
        this._plugin.chatManager.sendMessage(player, "&6/bow&f: Price: Free");
        this._plugin.chatManager.sendMessage(player, "&6/axe&f: Price: Free");
        this._plugin.chatManager.sendMessage(player, "");
        this._plugin.chatManager.sendMessage(player, "&3When you change you will lose ALL enchants on your weapon!");
    }

    public void sword(Player player) {
        PlayerInventory i = player.getInventory();
        this.removeWeapons((Inventory)i);
        i.addItem(new ItemStack[]{new ItemStack(Material.DIAMOND_SWORD, 1)});
        this._plugin.chatManager.sendMessage(player, "&bYou change your weapon of choice to a &eSword&b!");
        this._plugin.controller.calPlayerWorth(player, true);
    }

    public void bow(Player player) {
        PlayerInventory i = player.getInventory();
        this.removeWeapons((Inventory)i);
        ItemStack bow = new ItemStack(Material.BOW, 1);
        bow.addEnchantment(Enchantment.ARROW_INFINITE, 1);
        i.addItem(new ItemStack[]{bow});
        i.addItem(new ItemStack[]{new ItemStack(Material.ARROW, 1)});
        this._plugin.chatManager.sendMessage(player, "&bYou change your weapon of choice to a &eBow&b!");
        this._plugin.controller.calPlayerWorth(player, true);
    }

    public void axe(Player player) {
        PlayerInventory i = player.getInventory();
        this.removeWeapons((Inventory)i);
        ItemStack axe = new ItemStack(Material.DIAMOND_AXE, 1);
        i.addItem(new ItemStack[]{axe});
        this._plugin.chatManager.sendMessage(player, "&bYou change your weapon of choice to a &eAxe&b!");
        this._plugin.controller.calPlayerWorth(player, true);
    }

    private void removeWeapons(Inventory i) {
        i.remove(Material.BOW);
        i.remove(Material.ARROW);
        i.remove(Material.DIAMOND_SWORD);
        i.remove(Material.DIAMOND_AXE);
    }
}

