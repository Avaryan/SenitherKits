package net.Senither.Kits.commands;

import net.Senither.Kits.Kits;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ExtraCommands
implements CommandExecutor {
    private final Kits _plugin;

    public ExtraCommands(Kits plugin) {
        this._plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLable, String[] args) {
        Player player = null;
        if (!(sender instanceof Player)) {
            this._plugin.chatManager.LogInfo("You can't use that command from the console!");
            return false;
        }
        player = (Player) sender;
        if (commandLable.equalsIgnoreCase("extra")) {
            this.extraMenu(player);
        } else if (commandLable.equalsIgnoreCase("milk") || commandLable.equalsIgnoreCase("soup")) {
            this.milk(player);
        } else if (commandLable.equalsIgnoreCase("snowball")) {
            this.snowball(player);
        } else if (commandLable.equalsIgnoreCase("tnt")) {
            this.tnt(player);
        } else if (commandLable.equalsIgnoreCase("pay")) {
            this.payPlayer(player, args);
        }
        return false;
    }

    private void extraMenu(Player player) {
        this._plugin.chatManager.sendMessage(player, " -------- &6Extra Menu&e --------");
        this._plugin.chatManager.sendMessage(player, "&6/milk&f: Price: 6 - Gives you more milk");
        this._plugin.chatManager.sendMessage(player, "&6/snowball&f: Price: 150 - 32 Snowballs of Slowness");
        this._plugin.chatManager.sendMessage(player, "&6/tnt&f: Price: Price: 100 - 1 TNT Block");
    }

    private void snowball(Player player) {
        if (!this._plugin.controller.playerTransaction(player, 150.0)) {
            return;
        }
        PlayerInventory i = player.getInventory();
        i.addItem(new ItemStack[]{new ItemStack(Material.SNOW_BALL, 32)});
        this._plugin.controller.playerFinishTransaction(player, "32 Snowballs", 150.0);
    }

    public void milk(Player player) {
        if (!this._plugin.controller.playerTransaction(player, 6.0)) {
            return;
        }
        PlayerInventory i = player.getInventory();
        i.remove(Material.MILK_BUCKET);
        i.remove(Material.BOWL);
        for (int s = 1; s <= 27; ++s) {
            i.addItem(new ItemStack[]{new ItemStack(Material.MILK_BUCKET, 1)});
        }
        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 90, 1));
        this._plugin.controller.playerFinishTransaction(player, "Milk", 6.0);
        this._plugin.chatManager.sendMessage(player, "You got blindness for 4 seconds for using /milk)");
    }

    public void tnt(Player player) {
        if (!this._plugin.controller.playerTransaction(player, 100.0)) {
            return;
        }
        PlayerInventory i = player.getInventory();
        i.addItem(new ItemStack[]{new ItemStack(Material.TNT, 1)});
        this._plugin.controller.playerFinishTransaction(player, "TNT Block", 100.0);
    }

    @SuppressWarnings("deprecation")
	private void payPlayer(Player player, String[] args) {
        Player target;
        if (args.length != 2) {
            this._plugin.chatManager.sendMessage(player, "&cInvalid format!");
            this._plugin.chatManager.sendMessage(player, "&cFormat: /<command> <player> <amount>");
            return;
        }
        if ((target = Bukkit.getPlayer((String)args[0])) == null) {
            this._plugin.chatManager.sendMessage(player, "&c" + args[0] + " is offline!");
            return;
        }
        int payment = 0;
        try {
            payment = Integer.parseInt(args[1]);
        }
        catch (NumberFormatException e) {
            this._plugin.chatManager.sendMessage(player, "&cThe amount has be to a number!");
            this._plugin.chatManager.sendMessage(player, "&cFormat: /<command> <player> <amount>");
            return;
        }
        if (payment <= 0) {
            this._plugin.chatManager.sendMessage(player, "&cInvailed number, atleast let the number be above 0.");
            return;
        }
        if ((double)payment >= this._plugin.playerEco.get(player.getName())) {
            this._plugin.chatManager.sendMessage(player, "&cYou don't have enough credits to send that amount!");
            return;
        }
        this._plugin.playerEco.put(player.getName(), this._plugin.playerEco.get(player.getName()) - (double)payment);
        this._plugin.playerEco.put(target.getName(), this._plugin.playerEco.get(target.getName()) + (double)payment);
        this._plugin.chatManager.sendMessage(player, "&aYou have sent " + payment + " to " + target.getName() + "!");
        this._plugin.chatManager.sendMessage(target, "&a" + player.getName() + " has sent you " + payment + " credits!");
    }
}

