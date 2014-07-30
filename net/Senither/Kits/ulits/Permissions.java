package net.Senither.Kits.ulits;

import net.Senither.Kits.Kits;

public class Permissions {
    @SuppressWarnings("unused")
	private final Kits _plugin;
    private String prefix = "kits.";
    public final String MANAGE_BUILD = this.prefix + "build";
    public final String MANAGE_ADDCREDITS = this.prefix + "admin.addcredits";
    public final String MANAGE_MODIFY = this.prefix + "admin.modify";
    public final String MANAGE_MODIFY_KILLS = this.prefix + "admin.modify.kills";
    public final String MANAGE_MODIFY_DEATHS = this.prefix + "admin.modify.deaths";
    public final String MANAGE_MODIFY_KILLSTREAK = this.prefix + "admin.modify.killstreak";
    public final String MANAGE_MODIFY_DUELSCORE = this.prefix + "admin.modify.duelscore";
    public final String MANAGE_MAP = this.prefix + "admin.map";

    public Permissions(Kits plugin) {
        this._plugin = plugin;
    }
}

