package com.nijikokun.bukkit.Reserves;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Logger;

import org.bukkit.Server;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Original Concept by Nour.
 * Remodified by Nijikokun
 *
 * Reserves.
 * Allows players who are special to get on the server if it is full.
 *
 * @author Nour
 * @author Nijikokun
 */
public class Reserves extends JavaPlugin {
    // Listener
    private Listener Listener = new Listener(this);
    
    // Logger
    public static final Logger log = Logger.getLogger("Minecraft");

    // Server Size & Reserve list
    public int limit;
    public String message = "Server is currently full. You are not on the reserve list.";
    public ArrayList<String> reserved = new ArrayList<String>();

    // Data
    PluginDescriptionFile pluginData = this.getDescription();

    public Reserves(PluginLoader pluginLoader, Server instance, PluginDescriptionFile desc, File folder, File plugin, ClassLoader cLoader) {
	super(pluginLoader, instance, desc, folder, plugin, cLoader);

	registerEvents();

	// Enabled
	log.info("[" + pluginData.getName() + "] version [" + pluginData.getVersion() + "] (" + pluginData.getAuthors() + ") loaded");
    }

    public void onDisable() {
	log.info("[" + pluginData.getName() + "] version [" + pluginData.getVersion() + "] (" + pluginData.getAuthors() + ") un-loaded");
    }

    public void onEnable() {
	try {
	    Properties props = new Properties();
	    props.load(new FileReader("server.properties"));
	    limit = Integer.parseInt(props.getProperty("max-players").trim());
	    message = props.getProperty("reserve-message", message).trim();
	} catch (IOException ex) {
	    System.out.println("Unable to set max server size");
	}

	load();
    }

    private void registerEvents() {
	this.getServer().getPluginManager().registerEvent(Event.Type.PLAYER_JOIN, Listener, Priority.High, this);
	this.getServer().getPluginManager().registerEvent(Event.Type.PLAYER_QUIT, Listener, Priority.High, this);
    }

    public void load() {
	try {
	    File list = new File("reserved.txt");

	    if (!list.exists()) {
		list.createNewFile();
	    }

	    BufferedReader br = new BufferedReader(new FileReader(list));
	    String in;
	    reserved.clear();

	    while ((in = br.readLine()) != null) {
		reserved.add(in);
	    }

	    br.close();
	    System.out.println("Loaded " + reserved.size() + " reserved players");
	} catch (Exception e) {
	    System.out.println("Unable to load reserved list.");
	}
    }
}
