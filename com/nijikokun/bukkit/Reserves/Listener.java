package com.nijikokun.bukkit.Reserves;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerLoginEvent;

public class Listener extends PlayerListener {
    private Reserves plugin;

    public Listener(Reserves plugin) {
	this.plugin = plugin;
    }

    public void onPlayerLogin(PlayerLoginEvent event) {
	if (plugin.getServer().getOnlinePlayers().length == plugin.limit) {
	    if (!plugin.reserved.contains(event.getPlayer().getName())) {
		event.disallow(PlayerLoginEvent.Result.KICK_OTHER, plugin.message);
	    }
	}
    }

    public void onPlayerCommand(PlayerChatEvent event) {
	String[] split = event.getMessage().split(" ");
	Player player = event.getPlayer();

	if (!player.isOp()) {
	    return;
	}

	if (split[0].equalsIgnoreCase("/reload")) {
	    plugin.load();
	}
    }
}
