/*
 * Decompiled with CFR 0_82.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.HandlerList
 */
package net.Senither.Kits.api.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerKilledByPlayerEvent
extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Player killer;
    private final Player target;
    private final int killerKillstreak;
    private final int targetKillstreak;
    private final int killerCredits;
    private final int targetCredits;

    public PlayerKilledByPlayerEvent(Player killer, int killerKillstreak, int killerCredits, Player target, int targetKillstreak, int targetCredits) {
        this.killer = killer;
        this.killerKillstreak = killerKillstreak;
        this.killerCredits = killerCredits;
        this.target = target;
        this.targetKillstreak = targetKillstreak;
        this.targetCredits = targetCredits;
    }

    public Player getPlayer() {
        return this.killer;
    }

    public Player getTarget() {
        return this.target;
    }

    public int getPlayerKillstreak() {
        return this.killerKillstreak;
    }

    public int getTargetKillstreak() {
        return this.targetKillstreak;
    }

    public int getKillerCredits() {
        return this.killerCredits;
    }

    public int getTargetCredits() {
        return this.targetCredits;
    }

    public HandlerList getHandlers() {
        return PlayerKilledByPlayerEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return PlayerKilledByPlayerEvent.handlers;
    }
}

