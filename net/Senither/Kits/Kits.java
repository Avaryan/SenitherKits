package net.Senither.Kits;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.Senither.Kits.Events.BlockListener;
import net.Senither.Kits.Events.BoosterBlockListener;
import net.Senither.Kits.Events.ChatListener;
import net.Senither.Kits.Events.PlayerListener;
import net.Senither.Kits.Events.ServerListener;
import net.Senither.Kits.commands.ArmourCommands;
import net.Senither.Kits.commands.BuffCommands;
import net.Senither.Kits.commands.EnchantCommands;
import net.Senither.Kits.commands.ExtraCommands;
import net.Senither.Kits.commands.KitsCommands;
import net.Senither.Kits.commands.ManagerCommands;
import net.Senither.Kits.commands.WeaponCommands;
import net.Senither.Kits.engine.Controller;
import net.Senither.Kits.engine.DuelHandler;
import net.Senither.Kits.engine.MapHandler;
import net.Senither.Kits.engine.ScoreboardManager;
import net.Senither.Kits.ulits.ChatManager;
import net.Senither.Kits.ulits.Permissions;
import net.Senither.Kits.ulits.YAMLManager;
import net.Senither.Kits.vanish.VanishHandler;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Kits
extends JavaPlugin {
    public String v;
    public ChatManager chatManager;
    public Controller controller;
    public MapHandler mapHandler;
    public ScoreboardManager scoreboard;
    public Permissions permissions;
    public VanishHandler vanish;
    public DuelHandler duel;
    public HashMap<String, YAMLManager> playerConfig = new HashMap<String, YAMLManager>();
    public HashMap<String, Double> playerEco = new HashMap<String, Double>();
    public HashMap<String, Double> playerWorth = new HashMap<String, Double>();
    public HashMap<String, Integer> playerKillstreak = new HashMap<String, Integer>();
    public HashMap<String, Integer> playerKills = new HashMap<String, Integer>();
    public HashMap<String, Integer> playerDeaths = new HashMap<String, Integer>();
    public HashMap<String, Integer> playerLog = new HashMap<String, Integer>();
    public HashMap<String, Integer> playerDuelScore = new HashMap<String, Integer>();
    public List<String> slowList = new ArrayList<String>();
    public List<String> weakList = new ArrayList<String>();
    public List<String> playerUsingKits = new ArrayList<String>();

    public void onEnable() {
        this.saveDefaultConfig();
        File playerRoot = new File("plugins" + File.separator + "Kits" + File.separator + "playerData");
        playerRoot.mkdir();
        this.chatManager = new ChatManager(this);
        this.permissions = new Permissions(this);
        this.controller = new Controller(this);
        this.scoreboard = new ScoreboardManager(this);
        this.mapHandler = new MapHandler(this);
        this.vanish = new VanishHandler(this);
        this.duel = new DuelHandler(this);
        this.getCommand("map").setExecutor(this.mapHandler);
        ArmourCommands ac = new ArmourCommands(this);
        this.getCommand("armor").setExecutor(ac);
        this.getCommand("diamond").setExecutor(ac);
        this.getCommand("iron").setExecutor(ac);
        this.getCommand("gold").setExecutor(ac);
        this.getCommand("leather").setExecutor(ac);
        BuffCommands bc = new BuffCommands(this);
        this.getCommand("buffs").setExecutor(bc);
        this.getCommand("debuffs").setExecutor(bc);
        this.getCommand("speed").setExecutor(bc);
        this.getCommand("strength").setExecutor(bc);
        this.getCommand("regen").setExecutor(bc);
        this.getCommand("hunger").setExecutor(bc);
        this.getCommand("slow").setExecutor(bc);
        this.getCommand("weak").setExecutor(bc);
        EnchantCommands ec = new EnchantCommands(this);
        this.getCommand("enchants").setExecutor(ec);
        this.getCommand("sharp").setExecutor(ec);
        this.getCommand("power").setExecutor(ec);
        this.getCommand("knockback").setExecutor(ec);
        this.getCommand("protect").setExecutor(ec);
        ExtraCommands exc = new ExtraCommands(this);
        this.getCommand("extra").setExecutor(exc);
        this.getCommand("milk").setExecutor(exc);
        this.getCommand("soup").setExecutor(exc);
        this.getCommand("snowball").setExecutor(exc);
        this.getCommand("tnt").setExecutor(exc);
        this.getCommand("pay").setExecutor(exc);
        ManagerCommands mc = new ManagerCommands(this);
        this.getCommand("build").setExecutor(mc);
        this.getCommand("help").setExecutor(mc);
        this.getCommand("guide").setExecutor(mc);
        this.getCommand("spectate").setExecutor(mc);
        this.getCommand("duel").setExecutor(mc);
        this.getCommand("stats").setExecutor(mc);
        this.getCommand("addcredits").setExecutor(mc);
        this.getCommand("modify").setExecutor(mc);
        WeaponCommands wc = new WeaponCommands(this);
        this.getCommand("weapons").setExecutor(wc);
        this.getCommand("sword").setExecutor(wc);
        this.getCommand("bow").setExecutor(wc);
        this.getCommand("axe").setExecutor(wc);
        KitsCommands kc = new KitsCommands(this);
        this.getCommand("kits").setExecutor(kc);
        this.getCommand("knight").setExecutor(kc);
        this.getCommand("archer").setExecutor(kc);
        this.getCommand("medic").setExecutor(kc);
        this.getCommand("mage").setExecutor(kc);
        this.getCommand("ninja").setExecutor(kc);
        this.getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        this.getServer().getPluginManager().registerEvents(new BlockListener(this), this);
        this.getServer().getPluginManager().registerEvents(new ServerListener(this), this);
        this.getServer().getPluginManager().registerEvents(new ChatListener(this), this);
        this.getServer().getPluginManager().registerEvents(new BoosterBlockListener(this), this);
    }

    public void onDisable() {
        if (Bukkit.getOnlinePlayers().length != 0) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                this.controller.unloadPLayer(player);
                this.controller.removePlayer(player);
            }
        }
    }
}

