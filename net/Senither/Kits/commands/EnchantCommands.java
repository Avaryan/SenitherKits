package net.Senither.Kits.commands;

import net.Senither.Kits.Kits;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class EnchantCommands
implements CommandExecutor {
    private final Kits _plugin;

    public EnchantCommands(Kits plugin) {
        this._plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLable, String[] args) {
        Player player = null;
        if (!(sender instanceof Player)) {
            this._plugin.chatManager.LogInfo("You can't use that command from the console!");
            return false;
        }
        player = (Player)sender;
        if (commandLable.equalsIgnoreCase("enchants")) {
            this.enchantMenu(player);
            return true;
        }
        if (this._plugin.playerUsingKits.contains(player.getName())) {
            this._plugin.chatManager.sendMessage(player, "&cYou can't use this command while wearing a special kit a kit!");
            return true;
        }
        if (this._plugin.duel.isPlayerInBattle(player.getName())) {
            this._plugin.chatManager.sendMessage(player, "&cYou can't use this command while in a duel!");
            return true;
        }
        if (commandLable.equalsIgnoreCase("sharp")) {
            this.sharpness(player);
        } else if (commandLable.equalsIgnoreCase("power")) {
            this.power(player);
        } else if (commandLable.equalsIgnoreCase("knockback")) {
            this.knockback(player);
        } else if (commandLable.equalsIgnoreCase("protect")) {
            this.protection(player);
        }
        return false;
    }

    private void enchantMenu(Player player) {
        this._plugin.chatManager.sendMessage(player, "&e -------- &6Enchant Menu&e --------");
        this._plugin.chatManager.sendMessage(player, "&6/sharp&f: Price: 5, 40, 100, 255");
        this._plugin.chatManager.sendMessage(player, "&6/power&f: Price: 5, 40, 100, 255");
        this._plugin.chatManager.sendMessage(player, "&6/knockback&f: Price: 60, 140, 250");
        this._plugin.chatManager.sendMessage(player, "&6/protect&f: Price: 100(255 for diamond), 250, 400");
        this._plugin.chatManager.sendMessage(player, "");
        this._plugin.chatManager.sendMessage(player, "&3Make yourself powerful but get less money for your kills!");
        this._plugin.chatManager.sendMessage(player, "&3Use a command twice to get level two, three and four of the enchant for a higher price every upgrade");
    }

    private void sharpness(Player player) {
        ItemStack weapon = player.getInventory().getItemInHand();
        if (player.getInventory().getItemInHand() != null && weapon.getType() != Material.DIAMOND_SWORD && weapon.getType() != Material.DIAMOND_AXE) {
            this._plugin.chatManager.sendMessage(player, "&cPlease hold a Sword or Axe in your hand to use this command");
            return;
        }
        String weaponType = weapon.getType() == Material.DIAMOND_SWORD ? "sword" : "axe";
        double cost = 0.0;
        int enchantment = weapon.getEnchantmentLevel(Enchantment.DAMAGE_ALL);
        if (enchantment == 0) {
            cost = 5.0;
        } else if (enchantment == 1) {
            cost = 40.0;
        } else if (enchantment == 2) {
            cost = 100.0;
        } else if (enchantment == 3) {
            cost = 255.0;
        } else if (enchantment == 4) {
            this._plugin.chatManager.sendMessage(player, "&bYour &e" + weaponType + "&b is already fully enchanted!");
            return;
        }
        if (!this._plugin.controller.playerTransaction(player, cost)) {
            return;
        }
        double endCost = 0.0;
        if (enchantment == 0) {
            weapon.addEnchantment(Enchantment.DAMAGE_ALL, 1);
            endCost = 5.0;
        } else if (enchantment == 1) {
            weapon.addEnchantment(Enchantment.DAMAGE_ALL, 2);
            endCost = 40.0;
        } else if (enchantment == 2) {
            weapon.addEnchantment(Enchantment.DAMAGE_ALL, 3);
            endCost = 100.0;
        } else if (enchantment == 3) {
            weapon.addEnchantment(Enchantment.DAMAGE_ALL, 4);
            endCost = 255.0;
        }
        player.getInventory().setItemInHand(weapon);
        this._plugin.controller.playerFinishTransaction(player, "Sharpness " + (enchantment + 1), endCost);
        this._plugin.controller.calPlayerWorth(player, true);
    }

    @SuppressWarnings("unused")
	private void power(Player player) {
        int enchantment;
        ItemStack weapon = player.getInventory().getItemInHand();
        if (player.getInventory().getItemInHand() != null && weapon.getType() != Material.BOW) {
            this._plugin.chatManager.sendMessage(player, "&cPlease hold a Bow in your hand to use this command");
            return;
        }
        double cost = 0.0;
        if ((enchantment = weapon.getEnchantmentLevel(Enchantment.ARROW_DAMAGE)) == 0) {
            cost = 5.0;
        } else if (enchantment == 1) {
            cost = 40.0;
        } else if (enchantment == 2) {
            cost = 100.0;
        } else if (enchantment == 3) {
            cost = 255.0;
        } else if (enchantment == 4) {
            this._plugin.chatManager.sendMessage(player, "&bYour &ebow&b is already fully enchanted!");
            return;
        }
        if (!this._plugin.controller.playerTransaction(player, cost)) {
            return;
        }
        double endCost = 0.0;
        if (enchantment == 0) {
            weapon.addEnchantment(Enchantment.ARROW_DAMAGE, 1);
            endCost = 5.0;
        } else if (enchantment == 1) {
            weapon.addEnchantment(Enchantment.ARROW_DAMAGE, 2);
            endCost = 40.0;
        } else if (enchantment == 2) {
            weapon.addEnchantment(Enchantment.ARROW_DAMAGE, 3);
            endCost = 100.0;
        } else if (enchantment == 3) {
            weapon.addEnchantment(Enchantment.ARROW_DAMAGE, 4);
            endCost = 255.0;
        }
        player.getInventory().setItemInHand(weapon);
        this._plugin.controller.playerFinishTransaction(player, "Power " + (enchantment + 1), cost);
        this._plugin.controller.calPlayerWorth(player, true);
    }

    @SuppressWarnings("unused")
	private void knockback(Player player) {
        ItemStack weapon = player.getInventory().getItemInHand();
        if (player.getInventory().getItemInHand() != null && weapon.getType() != Material.DIAMOND_SWORD && weapon.getType() != Material.DIAMOND_AXE) {
            this._plugin.chatManager.sendMessage(player, "&cPlease hold a Sword or Axe in your hand to use this command");
            return;
        }
        String weaponType = weapon.getType() == Material.DIAMOND_SWORD ? "sword" : "axe";
        double cost = 0.0;
        int enchantment = weapon.getEnchantmentLevel(Enchantment.KNOCKBACK);
        if (enchantment == 0) {
            cost = 60.0;
        } else if (enchantment == 1) {
            cost = 140.0;
        } else if (enchantment == 2) {
            cost = 250.0;
        } else if (enchantment == 3) {
            this._plugin.chatManager.sendMessage(player, "&bYour &e" + weaponType + "&b is already fully enchanted!");
            return;
        }
        if (!this._plugin.controller.playerTransaction(player, cost)) {
            return;
        }
        double endCost = 0.0;
        if (enchantment == 0) {
            weapon.addEnchantment(Enchantment.KNOCKBACK, 1);
            endCost = 60.0;
        } else if (enchantment == 1) {
            weapon.addEnchantment(Enchantment.KNOCKBACK, 2);
            endCost = 140.0;
        } else if (enchantment == 2) {
            weapon.addUnsafeEnchantment(Enchantment.KNOCKBACK, 3);
            endCost = 250.0;
        }
        player.getInventory().setItemInHand(weapon);
        this._plugin.controller.playerFinishTransaction(player, "Knockback " + (enchantment + 1), cost);
        this._plugin.controller.calPlayerWorth(player, true);
    }

    @SuppressWarnings("unused")
	private void protection(Player player) {
        Material type = null;
        ItemStack item = null;
        if (player.getInventory().getHelmet() != null) {
            type = player.getInventory().getHelmet().getType();
            item = player.getInventory().getHelmet();
        } else if (player.getInventory().getChestplate() != null) {
            type = player.getInventory().getChestplate().getType();
            item = player.getInventory().getChestplate();
        } else if (player.getInventory().getLeggings() != null) {
            type = player.getInventory().getLeggings().getType();
            item = player.getInventory().getLeggings();
        } else if (player.getInventory().getBoots() != null) {
            type = player.getInventory().getBoots().getType();
            item = player.getInventory().getBoots();
        }
        if (type == null) {
            this._plugin.chatManager.sendMessage(player, "&bYou don't have any armour on.. Here is some iron armor.");
            type = Material.IRON_HELMET;
            player.getInventory().setHelmet(new ItemStack(Material.IRON_HELMET, 1));
            player.getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE, 1));
            player.getInventory().setLeggings(new ItemStack(Material.IRON_LEGGINGS, 1));
            player.getInventory().setBoots(new ItemStack(Material.IRON_BOOTS, 1));
        }
        if (type == Material.DIAMOND_HELMET || type == Material.DIAMOND_CHESTPLATE || type == Material.DIAMOND_LEGGINGS || type == Material.DIAMOND_BOOTS) {
            if (item.getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL) == 1) {
                this._plugin.chatManager.sendMessage(player, "&bYour &earmor&b is already fully enchanted");
                return;
            }
            if (!this._plugin.controller.playerTransaction(player, 250.0)) {
                return;
            }
            player.getInventory().getHelmet().addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
            player.getInventory().getChestplate().addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
            player.getInventory().getLeggings().addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
            player.getInventory().getBoots().addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
            this._plugin.controller.playerFinishTransaction(player, "Protection 1", 250.0);
        } else {
            int enchantment;
            int endCost = 0;
            double cost = 0.0;
            if ((enchantment = item.getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL)) == 0) {
                cost = 100.0;
            } else if (enchantment == 1) {
                cost = 250.0;
            } else if (enchantment == 2) {
                cost = 400.0;
            } else if (enchantment == 3) {
                this._plugin.chatManager.sendMessage(player, "&bYour &earmor&b is already fully enchanted");
                return;
            }
            if (!this._plugin.controller.playerTransaction(player, cost)) {
                return;
            }
            if (enchantment == 0) {
                player.getInventory().getHelmet().addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
                player.getInventory().getChestplate().addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
                player.getInventory().getLeggings().addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
                player.getInventory().getBoots().addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
                endCost = 100;
            } else if (enchantment == 1) {
                player.getInventory().getHelmet().addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
                player.getInventory().getChestplate().addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
                player.getInventory().getLeggings().addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
                player.getInventory().getBoots().addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
                endCost = 250;
            } else if (enchantment == 2) {
                player.getInventory().getHelmet().addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
                player.getInventory().getChestplate().addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
                player.getInventory().getLeggings().addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
                player.getInventory().getBoots().addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
                endCost = 400;
            }
            this._plugin.controller.playerFinishTransaction(player, "Protection " + (enchantment + 1), cost);
        }
        this._plugin.controller.calPlayerWorth(player, true);
    }
}

