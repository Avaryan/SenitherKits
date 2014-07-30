package net.Senither.Kits.commands;

import net.Senither.Kits.Kits;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

public class KitsCommands
implements CommandExecutor {
    private final Kits _plugin;

    public KitsCommands(Kits plugin) {
        this._plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLable, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You can't execute that command from the console!");
            return true;
        }
        Player player = (Player)sender;
        if (commandLable.equalsIgnoreCase("kits") || commandLable.equalsIgnoreCase("kit")) {
            this._plugin.chatManager.sendMessage(player, "&e -------- &6Weapon Menu&e --------");
            this._plugin.chatManager.sendMessage(player, "&6/Knight&f: Price: 10 Credits");
            this._plugin.chatManager.sendMessage(player, "&6/Archer&f: Price: 10 Credits");
            this._plugin.chatManager.sendMessage(player, "&6/Medic&f: Price: 100 Credits");
            this._plugin.chatManager.sendMessage(player, "&6/Mage&f: Price: 200 Credits");
            this._plugin.chatManager.sendMessage(player, "&6/Ninja&f: Price: 300 Credits");
            return true;
        }
        if (this._plugin.duel.isPlayerInBattle(player.getName())) {
            this._plugin.chatManager.sendMessage(player, "&cYou can't use this command while in a duel!");
            return true;
        }
        if (commandLable.equalsIgnoreCase("Knight")) {
            this.knight(player);
        } else if (commandLable.equalsIgnoreCase("Archer")) {
            this.archer(player);
        } else if (commandLable.equalsIgnoreCase("Medic")) {
            this.medic(player);
        } else if (commandLable.equalsIgnoreCase("Mage")) {
            this.mage(player);
        } else if (commandLable.equalsIgnoreCase("Ninja")) {
            this.ninja(player);
        }
        return true;
    }
    

    private void knight(Player player) {
        if (!this._plugin.controller.playerTransaction(player, 10.0)) {
            return;
        }
        player.getInventory().clear();
        player.getInventory().setHelmet(new ItemStack(Material.IRON_HELMET, 1));
        player.getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE, 1));
        player.getInventory().setLeggings(new ItemStack(Material.IRON_LEGGINGS, 1));
        player.getInventory().setBoots(new ItemStack(Material.IRON_BOOTS, 1));
        PlayerInventory i = player.getInventory();
        i.setItem(0, new ItemStack(Material.DIAMOND_SWORD, 1));
        for (int s = 1; s <= 27; ++s) {
            i.addItem(new ItemStack[]{new ItemStack(Material.MILK_BUCKET, 1)});
        }
        this._plugin.controller.playerFinishTransaction(player, "Knight Kit", 10.0);
        this._plugin.controller.calPlayerWorth(player, true);
    }

    private void archer(Player player) {
        if (!this._plugin.controller.playerTransaction(player, 10.0)) {
            return;
        }
        player.getInventory().clear();
        ItemStack healmet = new ItemStack(Material.CHAINMAIL_HELMET, 1);
        ItemStack chestplate = new ItemStack(Material.CHAINMAIL_CHESTPLATE, 1);
        ItemStack leggins = new ItemStack(Material.CHAINMAIL_LEGGINGS, 1);
        ItemStack boots = new ItemStack(Material.CHAINMAIL_BOOTS, 1);
        healmet.addUnsafeEnchantment(Enchantment.THORNS, 1);
        chestplate.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        chestplate.addUnsafeEnchantment(Enchantment.PROTECTION_PROJECTILE, 1);
        leggins.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        leggins.addUnsafeEnchantment(Enchantment.PROTECTION_PROJECTILE, 1);
        boots.addUnsafeEnchantment(Enchantment.THORNS, 1);
        player.getInventory().setHelmet(healmet);
        player.getInventory().setChestplate(chestplate);
        player.getInventory().setLeggings(leggins);
        player.getInventory().setBoots(boots);
        PlayerInventory i = player.getInventory();
        ItemStack weapon = new ItemStack(Material.BOW, 1);
        weapon.addEnchantment(Enchantment.ARROW_INFINITE, 1);
        i.addItem(new ItemStack[]{weapon});
        for (int s = 1; s <= 27; ++s) {
            i.addItem(new ItemStack[]{new ItemStack(Material.MILK_BUCKET, 1)});
        }
        i.addItem(new ItemStack[]{new ItemStack(Material.ARROW, 1)});
        this._plugin.controller.playerFinishTransaction(player, "Archer Kit", 10.0);
        this._plugin.controller.calPlayerWorth(player, true);
    }

    private void medic(Player player) {
        if (!this._plugin.controller.playerTransaction(player, 100.0)) {
            return;
        }
        if (!this._plugin.playerUsingKits.contains(player.getName())) {
            this._plugin.playerUsingKits.add(player.getName());
        }
        player.getInventory().clear();
        player.getInventory().setHelmet(new ItemStack(Material.GOLD_HELMET, 1));
        player.getInventory().setChestplate(new ItemStack(Material.GOLD_CHESTPLATE, 1));
        player.getInventory().setLeggings(new ItemStack(Material.GOLD_LEGGINGS, 1));
        player.getInventory().setBoots(new ItemStack(Material.GOLD_BOOTS, 1));
        PlayerInventory i = player.getInventory();
        ItemStack weapon = new ItemStack(Material.GOLD_SWORD, 1);
        weapon.addEnchantment(Enchantment.KNOCKBACK, 1);
        
        i.addItem(new ItemStack[]{weapon});
        i.addItem(new ItemStack[]{new Potion(PotionType.INSTANT_HEAL,1).splash().toItemStack(6)});
        i.addItem(new ItemStack[]{new Potion(PotionType.REGEN,1).splash().toItemStack(6)});
        i.addItem(new ItemStack[]{new Potion(PotionType.INSTANT_HEAL,2).splash().toItemStack(3)});
        i.addItem(new ItemStack[]{new Potion(PotionType.REGEN,1).splash().toItemStack(3)});
 
        for (int s = 1; s <= 27; ++s) {
            i.addItem(new ItemStack[]{new ItemStack(Material.MILK_BUCKET, 1)});
        }
        this._plugin.controller.playerFinishTransaction(player, "Medic Kit", 100.0);
        this._plugin.controller.calPlayerWorth(player, true);
    }

    private void mage(Player player) {
        if (!this._plugin.controller.playerTransaction(player, 200.0)) {
            return;
        }
        if (!this._plugin.playerUsingKits.contains(player.getName())) {
            this._plugin.playerUsingKits.add(player.getName());
        }
        player.getInventory().clear();
        ItemStack healmet = new ItemStack(Material.LEATHER_HELMET, 1);
        ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
        ItemStack leggins = new ItemStack(Material.LEATHER_LEGGINGS, 1);
        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS, 1);
        healmet.addUnsafeEnchantment(Enchantment.OXYGEN, 1);
        chestplate.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        chestplate.addUnsafeEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 2);
        chestplate.addUnsafeEnchantment(Enchantment.PROTECTION_FIRE, 2);
        leggins.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        leggins.addUnsafeEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 2);
        leggins.addUnsafeEnchantment(Enchantment.PROTECTION_FIRE, 2);
        boots.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, 1);
        player.getInventory().setHelmet(healmet);
        player.getInventory().setChestplate(chestplate);
        player.getInventory().setLeggings(leggins);
        player.getInventory().setBoots(boots);
        PlayerInventory i = player.getInventory();
        ItemStack weapon = new ItemStack(Material.GOLD_SWORD, 1);
        weapon.addEnchantment(Enchantment.KNOCKBACK, 1);
        weapon.addEnchantment(Enchantment.FIRE_ASPECT, 1);
        weapon.addEnchantment(Enchantment.DURABILITY, 2);
        i.addItem(new ItemStack[]{weapon});
        i.addItem(new ItemStack[]{new Potion(PotionType.REGEN,2).toItemStack(4)});
        i.addItem(new ItemStack[]{new Potion(PotionType.INSTANT_HEAL,1).toItemStack(4)});
        i.addItem(new ItemStack[]{new Potion(PotionType.SPEED,2).toItemStack(2)});
        i.addItem(new ItemStack[]{new Potion(PotionType.FIRE_RESISTANCE,1).toItemStack(2)});
        i.addItem(new ItemStack[]{new Potion(PotionType.POISON,1).splash().toItemStack(2)});
        i.addItem(new ItemStack[]{new Potion(PotionType.SLOWNESS,1).splash().toItemStack(2)});
        i.addItem(new ItemStack[]{new Potion(PotionType.INVISIBILITY,1).toItemStack(1)});
        for (int s = 1; s <= 27; ++s) {
            i.addItem(new ItemStack[]{new ItemStack(Material.MILK_BUCKET, 1)});
        }
        this._plugin.controller.playerFinishTransaction(player, "Mage Kit", 200.0);
        this._plugin.controller.calPlayerWorth(player, true);
    }

    private void ninja(Player player) {
        if (!this._plugin.controller.playerTransaction(player, 300.0)) {
            return;
        }
        if (!this._plugin.playerUsingKits.contains(player.getName())) {
            this._plugin.playerUsingKits.add(player.getName());
        }
        player.getInventory().clear();
        ItemStack healmet = new ItemStack(Material.LEATHER_HELMET, 1);
        ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
        ItemStack leggins = new ItemStack(Material.LEATHER_LEGGINGS, 1);
        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS, 1);
        healmet.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        healmet.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, 1);
        chestplate.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        chestplate.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, 1);
        leggins.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        leggins.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, 1);
        boots.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        boots.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, 1);
        LeatherArmorMeta healmetMeta = (LeatherArmorMeta)healmet.getItemMeta();
        LeatherArmorMeta chestplateMeta = (LeatherArmorMeta)chestplate.getItemMeta();
        LeatherArmorMeta legginsMeta = (LeatherArmorMeta)leggins.getItemMeta();
        LeatherArmorMeta bootsMeta = (LeatherArmorMeta)boots.getItemMeta();
        healmetMeta.setColor(Color.BLACK);
        chestplateMeta.setColor(Color.BLACK);
        legginsMeta.setColor(Color.BLACK);
        bootsMeta.setColor(Color.BLACK);
        healmet.setItemMeta((ItemMeta)healmetMeta);
        chestplate.setItemMeta((ItemMeta)chestplateMeta);
        leggins.setItemMeta((ItemMeta)legginsMeta);
        boots.setItemMeta((ItemMeta)bootsMeta);
        player.getInventory().setHelmet(healmet);
        player.getInventory().setChestplate(chestplate);
        player.getInventory().setLeggings(leggins);
        player.getInventory().setBoots(boots);
        PlayerInventory i = player.getInventory();
        ItemStack weapon = new ItemStack(Material.DIAMOND_SWORD, 1);
        weapon.addEnchantment(Enchantment.DAMAGE_ALL, 2);
        i.addItem(new ItemStack[]{weapon});
        for (int s = 1; s <= 27; ++s) {
            i.addItem(new ItemStack[]{new ItemStack(Material.MILK_BUCKET, 1)});
        }
        this._plugin.controller.playerFinishTransaction(player, "Ninja Kit", 300.0);
        this._plugin.controller.calPlayerWorth(player, true);
    }
}

