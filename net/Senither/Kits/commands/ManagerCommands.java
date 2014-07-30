package net.Senither.Kits.commands;

import java.util.ArrayList;

import net.Senither.Kits.Kits;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;

public class ManagerCommands
implements CommandExecutor {
    private final Kits _plugin;

    public ManagerCommands(Kits plugin) {
        this._plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLable, String[] args) {
        Player player = null;
        if (commandLable.equalsIgnoreCase("addcredits")) {
            if (sender instanceof Player) {
                if ((player = (Player)sender).hasPermission(this._plugin.permissions.MANAGE_ADDCREDITS)) {
                    this.addCreditsToPlayer(sender, args);
                }
            } else {
                this.addCreditsToPlayer(sender, args);
            }
            return false;
        }
        if (!(sender instanceof Player)) {
            this._plugin.chatManager.LogInfo("You can't use that command from the console!");
            return false;
        }
        player = (Player)sender;
        if (commandLable.equalsIgnoreCase("build")) {
            this.buildToggle(player);
        } else if (commandLable.equalsIgnoreCase("help")) {
            this.helpMenu(player);
        } else if (commandLable.equalsIgnoreCase("guide")) {
            this.guideMenu(player, args);
        } else if (commandLable.equalsIgnoreCase("spectate")) {
            this.spectateToggle(player);
        } else if (commandLable.equalsIgnoreCase("duel")) {
            this.duelPlayer(player, args);
        } else if (commandLable.equalsIgnoreCase("stats")) {
            this.stats(player, args);
        } else if (commandLable.equalsIgnoreCase("modify")) {
            this.modify(player, args);
        }
        return false;
    }

    @SuppressWarnings("deprecation")
	private void addCreditsToPlayer(CommandSender player, String[] args) {
        if (args.length == 2) {
            Player target;
            if ((target = Bukkit.getPlayer((String)args[0])) == null) {
                player.sendMessage(args[0] + " is offline!");
                player.sendMessage("Players need to be online for you to be able to add tokens to them!");
                return;
            }
            double add = 0.0;
            try {
                add = Double.parseDouble(args[1]);
            }
            catch (NumberFormatException e) {
                player.sendMessage("Please enter a number next time..");
                return;
            }
            if (add != 0.0) {
                this._plugin.playerEco.put(target.getName(), this._plugin.playerEco.get(target.getName()) + add);
                player.sendMessage("Added " + add + " credits to " + target.getName() + "'s account!");
                return;
            }
            player.sendMessage("You can't add nothing at all..");
            return;
        }
        player.sendMessage("Invalid format!");
        player.sendMessage("/addcredits <player> <credits>");
    }

    private void buildToggle(Player player) {
        if (player.hasPermission(this._plugin.permissions.MANAGE_BUILD)) {
            String name;
            if (this._plugin.controller.canBuild(name = player.getName())) {
                this._plugin.controller.buildList.remove(name);
                this._plugin.chatManager.sendMessage(player, "Building mode have been turned off");
                this._plugin.chatManager.sendMessage(player, "You can no longer destory blocks");
            } else {
                this._plugin.controller.buildList.add(name);
                this._plugin.chatManager.sendMessage(player, "Building mode have been turned on");
                this._plugin.chatManager.sendMessage(player, "You can now destory blocks");
            }
        } else {
            this._plugin.chatManager.missingPermission(player, this._plugin.permissions.MANAGE_BUILD);
        }
    }

    private void helpMenu(Player player) {
        this._plugin.chatManager.sendMessage(player, "&e -------- &6Help Menu&e --------");
        this._plugin.chatManager.sendMessage(player, "&6/enchants&f: List of Enchants");
        this._plugin.chatManager.sendMessage(player, "&6/buffs&f: List of Buffs");
        this._plugin.chatManager.sendMessage(player, "&6/debuffs&f: List of Debuffs");
        this._plugin.chatManager.sendMessage(player, "&6/armor&f: List of Armor types");
        this._plugin.chatManager.sendMessage(player, "&6/weapons&f: List of Weapons");
        this._plugin.chatManager.sendMessage(player, "&6/Kits&f: List of Kits");
        this._plugin.chatManager.sendMessage(player, "&6/extra&f: List of Extra Suff");
        this._plugin.chatManager.sendMessage(player, "&6/duel <name>&f: Duel another player");
        this._plugin.chatManager.sendMessage(player, "&6/spectate&f: Goes into spectate mode");
    }

    private void guideMenu(Player p, String[] args) {
        if (args.length == 0) {
            p.sendMessage((Object)ChatColor.YELLOW + " ---------- " + (Object)ChatColor.GOLD + "Guide" + (Object)ChatColor.YELLOW + " ----------");
            p.sendMessage((Object)ChatColor.WHITE + "Use /guide [id] for more information");
            p.sendMessage((Object)ChatColor.GOLD + "1" + (Object)ChatColor.WHITE + ": How do I enchant my items?");
            p.sendMessage((Object)ChatColor.GOLD + "2" + (Object)ChatColor.WHITE + ": How can I change my weapon?");
            p.sendMessage((Object)ChatColor.GOLD + "3" + (Object)ChatColor.WHITE + ": Why use debuffs?");
            p.sendMessage((Object)ChatColor.GOLD + "4" + (Object)ChatColor.WHITE + ": How do I get more milk?");
            p.sendMessage((Object)ChatColor.GOLD + "5" + (Object)ChatColor.WHITE + ": When does the map change and why?");
            p.sendMessage((Object)ChatColor.GOLD + "6" + (Object)ChatColor.WHITE + ": Why can't I hit people?");
            p.sendMessage((Object)ChatColor.GOLD + "7" + (Object)ChatColor.WHITE + ": What does the colors above people's head mean?");
            p.sendMessage((Object)ChatColor.GOLD + "8" + (Object)ChatColor.WHITE + ": What is booster blocks?");
        } else if (args[0].equalsIgnoreCase("1")) {
            p.sendMessage((Object)ChatColor.YELLOW + " ---------------------------------------------------- ");
            p.sendMessage((Object)ChatColor.GRAY + " You can use the command " + ChatColor.GOLD + "/enchants" + (Object)ChatColor.GRAY + " to see the list of enchants you can get, some enchants you can upgrade more then once, however it will cost more per upgrade. All enchants will be lost upon death so watch out!");
            p.sendMessage((Object)ChatColor.YELLOW + " ---------------------------------------------------- ");
        } else if (args[0].equalsIgnoreCase("2")) {
            p.sendMessage((Object)ChatColor.YELLOW + " ---------------------------------------------------- ");
            p.sendMessage((Object)ChatColor.GRAY + " You can use the command " + ChatColor.GOLD + "/weapons" + (Object)ChatColor.GRAY + " to see the list of weapons you can get, for quick access use " + (Object)ChatColor.GOLD + "/sword" + (Object)ChatColor.GRAY + " to get a sword or use " + (Object)ChatColor.GOLD + "/bow" + (Object)ChatColor.GRAY + " to get a bow.");
            p.sendMessage((Object)ChatColor.YELLOW + " ---------------------------------------------------- ");
        } else if (args[0].equalsIgnoreCase("3")) {
            p.sendMessage((Object)ChatColor.YELLOW + " ---------------------------------------------------- ");
            p.sendMessage((Object)ChatColor.GRAY + " Debuffs gives you a bigger challenge when pvping others while it make you worth less to everyone else it also allows you to get more credits per kill.");
            p.sendMessage((Object)ChatColor.YELLOW + " ---------------------------------------------------- ");
        } else if (args[0].equalsIgnoreCase("4")) {
            p.sendMessage((Object)ChatColor.YELLOW + " ---------------------------------------------------- ");
            p.sendMessage((Object)ChatColor.GRAY + " You can use " + (Object)ChatColor.GOLD + "/milk" + ChatColor.GRAY + " to get a ton of milk, however watch out as it cost six credits and will blind you for four seconds!");
            p.sendMessage((Object)ChatColor.YELLOW + " ---------------------------------------------------- ");
        } else if (args[0].equalsIgnoreCase("5")) {
            p.sendMessage((Object)ChatColor.YELLOW + " ---------------------------------------------------- ");
            p.sendMessage((Object)ChatColor.GRAY + " The map changes from time to time to keep everything intersting and new. You can check how long thats left before the map changes again by using the command " + (Object)ChatColor.GOLD + "/map");
            p.sendMessage((Object)ChatColor.YELLOW + " ---------------------------------------------------- ");
        } else if (args[0].equalsIgnoreCase("6")) {
            p.sendMessage((Object)ChatColor.YELLOW + " ---------------------------------------------------- ");
            p.sendMessage((Object)ChatColor.GRAY + " You can't hit people with " + (Object)ChatColor.WHITE + "white" + (Object)ChatColor.GRAY + " name plates as they have pvp disabled. You must wait for them to enable it by either hitting someone with pvp enabled or cross the pvp line near spawn.");
            p.sendMessage((Object)ChatColor.YELLOW + " ---------------------------------------------------- ");
        } else if (args[0].equalsIgnoreCase("7")) {
            p.sendMessage((Object)ChatColor.YELLOW + " ---------------------------------------------------- ");
            p.sendMessage((Object)ChatColor.GRAY + " The colors indicate if a player has pvp enabled and how much they're worth");
            p.sendMessage("");
            p.sendMessage((Object)ChatColor.WHITE + " White " + (Object)ChatColor.GRAY + ": PvP is Disabled, you can't hit people with pvp disabled.");
            p.sendMessage((Object)ChatColor.YELLOW + " Yellow " + (Object)ChatColor.GRAY + ": PvP is Enabled and worth between 0 - 7 credits.");
            p.sendMessage((Object)ChatColor.AQUA + " Blue " + (Object)ChatColor.GRAY + ": PvP is Enabled and worth between 7 - 22 credits.");
            p.sendMessage((Object)ChatColor.RED + " Red " + (Object)ChatColor.GRAY + ": PvP is Enabled and worth between 22 - ?? credits.");
            p.sendMessage((Object)ChatColor.YELLOW + " ---------------------------------------------------- ");
        } else if (args[0].equalsIgnoreCase("8")) {
            p.sendMessage((Object)ChatColor.YELLOW + " ---------------------------------------------------- ");
            p.sendMessage((Object)ChatColor.GRAY + " Booster blocks is blocks that give you some kind of effect when you run over them, the colors of the blocks indicate what type of effect you would get");
            p.sendMessage("");
            p.sendMessage((Object)ChatColor.RED + " Red " + (Object)ChatColor.GRAY + ": Enables pvp when you touch it");
            p.sendMessage((Object)ChatColor.YELLOW + " Yellow " + (Object)ChatColor.GRAY + ": Give you a jump boost. 30 seconds cooldown");
            p.sendMessage((Object)ChatColor.GREEN + " Green " + (Object)ChatColor.GRAY + ": Give you a speed boost. 1.5 minute cooldown");
            p.sendMessage((Object)ChatColor.BLACK + " Black " + (Object)ChatColor.GRAY + ": Traps you so you can't move. 2.5 minutes cooldown");
            p.sendMessage((Object)ChatColor.DARK_GRAY + " Gray " + (Object)ChatColor.GRAY + ": Give you a wither effect. 1.5 minute cooldown");
            p.sendMessage((Object)ChatColor.LIGHT_PURPLE + " Pink " + (Object)ChatColor.GRAY + ": Sets you on fire. 30 seconds cooldown");
            p.sendMessage((Object)ChatColor.DARK_PURPLE + " Magenta " + (Object)ChatColor.GRAY + ": Give you a nausea effect. 2 minutes cooldown");
            p.sendMessage((Object)ChatColor.DARK_GREEN + " Dark Green " + (Object)ChatColor.GRAY + ": Makes you go BOOM. 30 seconds cooldown");
            p.sendMessage((Object)ChatColor.BLUE + " Blue " + (Object)ChatColor.GRAY + ": TP's you up and down. No cooldown.");
            p.sendMessage((Object)ChatColor.YELLOW + " ---------------------------------------------------- ");
        } else {
            p.sendMessage((Object)ChatColor.RED + "Couldn't find the guide you were looking for");
            p.sendMessage((Object)ChatColor.RED + "Please use /guide for a list of guides you can use");
        }
    }

    private void spectateToggle(Player player) {
        this._plugin.chatManager.sendMessage(player, " &eThis feature has not yet been implemented!");
    }

    @SuppressWarnings("deprecation")
	private void duelPlayer(Player player, String[] args) {
        Player target;
        if (args.length != 1) {
            this._plugin.chatManager.sendMessage(player, "&cInvailed format!");
            this._plugin.chatManager.sendMessage(player, "&c/<command> <player>");
            return;
        }
        if ((target = Bukkit.getPlayer((String)args[0])) == null) {
            this._plugin.chatManager.sendMessage(player, "&c" + args[0] + " is not online!");
            return;
        }
        if (player.getName().equals(target.getName())) {
            this._plugin.chatManager.sendMessage(player, "&cYou can't duel yourself!");
            return;
        }
        if (this._plugin.duel.isPlayerInBattle(player.getName())) {
            this._plugin.chatManager.sendMessage(player, "&cYou can't challange people to a duel when you're in a battle!");
            return;
        }
        if (this._plugin.duel.isPlayerInQueue(player.getName())) {
            this._plugin.chatManager.sendMessage(player, "&cYou're alrady in a duel queue!");
            return;
        }
        this._plugin.duel.challangePlayer(player, target);
    }

    @SuppressWarnings("deprecation")
	private void stats(Player player, String[] args) {
        if (args.length == 0) {
            this._plugin.chatManager.sendMessage(player, "&bOi mate.. Just look at your scoreboard -->");
            this._plugin.chatManager.sendMessage(player, "&bWant to see player stats? use /stats <player>");
        } else {
            Player target;
            if ((target = Bukkit.getPlayer((String)args[0])) == null) {
                this._plugin.chatManager.sendMessage(player, "&c" + args[0] + " is not online!");
                return;
            }
            this._plugin.chatManager.sendMessage(player, "&3Loading player stats for &b" + target.getName() + "&3!");
            Inventory inv = Bukkit.createInventory((InventoryHolder)null, (int)54, (String)"Player Stats");
            ItemStack hotbarInv = new ItemStack(Material.ITEM_FRAME, 1);
            ItemMeta hotbarInvMeta = hotbarInv.getItemMeta();
            hotbarInvMeta.setDisplayName((Object)ChatColor.RED + target.getName() + "'s hotbar [v]");
            hotbarInv.setItemMeta(hotbarInvMeta);
            ItemStack hotbarArm = new ItemStack(Material.ITEM_FRAME, 1);
            ItemMeta hotbarArmMeta = hotbarArm.getItemMeta();
            hotbarArmMeta.setDisplayName((Object)ChatColor.RED + target.getName() + "'s armour [>]");
            hotbarArm.setItemMeta(hotbarArmMeta);
            hotbarArmMeta.setDisplayName((Object)ChatColor.RED + target.getName() + "'s armour [<]");
            for (int i = 0; i < 9; ++i) {
                if (target.getInventory().getItem(i) == null) continue;
                inv.setItem(i + 45, target.getInventory().getItem(i));
            }
            int cal = 38;
            for (ItemStack item : target.getInventory().getArmorContents()) {
                if (item != null) {
                    inv.setItem(cal, item);
                }
                if (cal == 39) {
                    ++cal;
                }
                ++cal;
            }
            inv.setItem(36, hotbarInv);
            inv.setItem(37, hotbarArm);
            hotbarArm.setItemMeta(hotbarArmMeta);
            inv.setItem(43, hotbarArm);
            inv.setItem(44, hotbarInv);
            ItemStack head = new ItemStack(Material.SKULL_ITEM, 1);
            ItemMeta headMeta = head.getItemMeta();
            headMeta.setDisplayName((Object)ChatColor.GOLD + target.getName() + "'s stats!");
            head.setItemMeta(headMeta);
            head.setDurability((short) 3);
            inv.setItem(22, head);
            ItemStack buffs = new ItemStack(Material.POTION, 1);
            ItemMeta buffsMeta = buffs.getItemMeta();
            ArrayList<String> buffsList = new ArrayList<String>();
            ItemStack debuffs = new ItemStack(Material.POTION, 1);
            ItemMeta debuffsMeta = debuffs.getItemMeta();
            ArrayList<String> debuffsList = new ArrayList<String>();
            if (!target.isDead()) {
                ItemStack health = new ItemStack(Material.APPLE, (int) target.getHealthScale());
                ItemMeta healthMeta = health.getItemMeta();
                healthMeta.setDisplayName((Object)ChatColor.GOLD + "Health Level");
                health.setItemMeta(healthMeta);
                ItemStack food = new ItemStack(Material.PORK, target.getFoodLevel());
                ItemMeta foodMeta = food.getItemMeta();
                foodMeta.setDisplayName((Object)ChatColor.GOLD + "Food Level");
                food.setItemMeta(foodMeta);
                inv.setItem(7, health);
                inv.setItem(8, food);
                buffsList.add((Object)ChatColor.GRAY + "Speed : " + (target.hasPotionEffect(PotionEffectType.SPEED) ? new StringBuilder().append((Object)ChatColor.GREEN).append("on").toString() : new StringBuilder().append((Object)ChatColor.RED).append("off").toString()));
                buffsList.add((Object)ChatColor.GRAY + "Strength : " + (target.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE) ? new StringBuilder().append((Object)ChatColor.GREEN).append("on").toString() : new StringBuilder().append((Object)ChatColor.RED).append("off").toString()));
                buffsList.add((Object)ChatColor.GRAY + "Regen : " + (target.hasPotionEffect(PotionEffectType.REGENERATION) ? new StringBuilder().append((Object)ChatColor.GREEN).append("on").toString() : new StringBuilder().append((Object)ChatColor.RED).append("off").toString()));
                debuffsList.add((Object)ChatColor.GRAY + "Hunger : " + (this._plugin.controller.hungerList.contains(target.getName()) ? new StringBuilder().append((Object)ChatColor.GREEN).append("on").toString() : new StringBuilder().append((Object)ChatColor.RED).append("off").toString()));
                debuffsList.add((Object)ChatColor.GRAY + "Slowness : " + (target.hasPotionEffect(PotionEffectType.SLOW) ? new StringBuilder().append((Object)ChatColor.GREEN).append("on").toString() : new StringBuilder().append((Object)ChatColor.RED).append("off").toString()));
                debuffsList.add((Object)ChatColor.GRAY + "Weakness : " + (target.hasPotionEffect(PotionEffectType.WEAKNESS) ? new StringBuilder().append((Object)ChatColor.GREEN).append("on").toString() : new StringBuilder().append((Object)ChatColor.RED).append("off").toString()));
            } else {
                buffsList.add((Object)ChatColor.GRAY + "Speed : " + (Object)ChatColor.RED + "off");
                buffsList.add((Object)ChatColor.GRAY + "Strength : " + (Object)ChatColor.RED + "off");
                buffsList.add((Object)ChatColor.GRAY + "Regen : " + (Object)ChatColor.RED + "off");
                debuffsList.add((Object)ChatColor.GRAY + "Hunger : " + (Object)ChatColor.RED + "off");
                debuffsList.add((Object)ChatColor.GRAY + "Slowness : " + (Object)ChatColor.RED + "off");
                debuffsList.add((Object)ChatColor.GRAY + "Weakness : " + (Object)ChatColor.RED + "off");
            }
            buffsMeta.setLore(buffsList);
            buffsMeta.setDisplayName((Object)ChatColor.RED + "Buffs");
            buffs.setItemMeta(buffsMeta);
            debuffsMeta.setLore(debuffsList);
            debuffsMeta.setDisplayName((Object)ChatColor.RED + "Debuffs");
            debuffs.setItemMeta(debuffsMeta);
            inv.setItem(2, buffs);
            inv.setItem(3, debuffs);
            ItemStack credits = new ItemStack(Material.GOLD_INGOT, 1);
            ItemMeta creditsMeta = credits.getItemMeta();
            creditsMeta.setDisplayName((Object)ChatColor.GOLD + "Credits");
            ArrayList<String> creditsList = new ArrayList<String>();
            creditsList.add((Object)ChatColor.GRAY + "Credits : " + (Object)ChatColor.RED + this._plugin.playerEco.get(target.getName()));
            creditsMeta.setLore(creditsList);
            credits.setItemMeta(creditsMeta);
            inv.setItem(5, credits);
            ItemStack stats = new ItemStack(Material.DIAMOND_SWORD, 1);
            ItemMeta statsMeta = stats.getItemMeta();
            ArrayList<String> statsLore = new ArrayList<String>();
            statsLore.add((Object)ChatColor.GRAY + "PvP is " + (this._plugin.controller.hasPvPEnabled(target.getName()) ? new StringBuilder().append((Object)ChatColor.GREEN).append("Enabled").toString() : new StringBuilder().append((Object)ChatColor.RED).append("Disabled").toString()));
            statsLore.add("");
            int kills = this._plugin.playerKills.get(target.getName());
            int deaths = this._plugin.playerDeaths.get(target.getName());
            int kdRatio = kills;
            if (kills != 0 && deaths != 0) {
                kdRatio = (int)Math.ceil(kills / deaths);
            }
            statsLore.add((Object)ChatColor.GRAY + "Kills : " + (Object)ChatColor.RED + kills);
            statsLore.add((Object)ChatColor.GRAY + "Deaths : " + (Object)ChatColor.RED + deaths);
            statsLore.add((Object)ChatColor.GRAY + "K/D Ratio : " + (Object)ChatColor.RED + kdRatio);
            statsLore.add((Object)ChatColor.GRAY + "PvP Score : " + (Object)ChatColor.RED + (kills - deaths));
            statsLore.add((Object)ChatColor.GRAY + "Killstreak : " + (Object)ChatColor.RED + this._plugin.playerKillstreak.get(target.getName()));
            statsLore.add("");
            statsLore.add((Object)ChatColor.GRAY + "Player Worth : " + (Object)ChatColor.RED + this._plugin.playerWorth.get(target.getName()));
            statsLore.add("");
            statsLore.add((Object)ChatColor.GRAY + "Duel Score : " + (Object)ChatColor.RED + this._plugin.playerDuelScore.get(target.getName()));
            statsMeta.setLore(statsLore);
            statsMeta.setDisplayName((Object)ChatColor.RED + "Player Stats");
            stats.setItemMeta(statsMeta);
            inv.setItem(0, stats);
            player.openInventory(inv);
        }
    }

    @SuppressWarnings("deprecation")
	private void modify(Player player, String[] args) {
        Player target;
        if (!player.hasPermission(this._plugin.permissions.MANAGE_MODIFY)) {
            this._plugin.chatManager.missingPermission(player, this._plugin.permissions.MANAGE_MODIFY);
            return;
        }
        if (args.length != 3) {
            this._plugin.chatManager.sendMessage(player, "Virables you can edit.");
            this._plugin.chatManager.sendMessage(player, "kills&f: A players kills");
            this._plugin.chatManager.sendMessage(player, "deaths&f: A players kills");
            this._plugin.chatManager.sendMessage(player, "killstreak&f: A players kills");
            this._plugin.chatManager.sendMessage(player, "duel&f: A players duel score");
            this._plugin.chatManager.sendMessage(player, "Format:&f /modify <player> <virable> <value>");
            return;
        }
        if ((target = Bukkit.getPlayer((String)args[0])) == null) {
            this._plugin.chatManager.sendMessage(player, "&c" + args[0] + " is offline!");
            return;
        }
        int value = 0;
        try {
            value = Integer.parseInt(args[2]);
        }
        catch (NumberFormatException e) {
            this._plugin.chatManager.sendMessage(player, "&cInvalid value.");
            return;
        }
        if (value == 0) {
            this._plugin.chatManager.sendMessage(player, "&cPlease enter a value other than 0");
            return;
        }
        if (args[1].equalsIgnoreCase("kills")) {
            this._plugin.playerKills.put(target.getName(), value);
        } else if (args[1].equalsIgnoreCase("deaths")) {
            this._plugin.playerDeaths.put(target.getName(), value);
        } else if (args[1].equalsIgnoreCase("killstreak")) {
            this._plugin.playerKillstreak.put(target.getName(), value);
            this._plugin.controller.calPlayerWorth(player, true);
        } else if (args[1].equalsIgnoreCase("duel")) {
            this._plugin.playerDuelScore.put(target.getName(), value);
        } else {
            this._plugin.chatManager.sendMessage(player, "&cInvalid format!");
            this._plugin.chatManager.sendMessage(player, "Format:&f /modify <player> <virable> <value>");
            return;
        }
        this._plugin.chatManager.sendMessage(player, "&aUpdated " + target.getName() + "'s " + args[1] + " to " + value);
    }
}

