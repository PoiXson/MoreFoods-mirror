package com.poixson.morefoods;

import static com.poixson.utils.BukkitUtils.Log;

import java.util.concurrent.atomic.AtomicInteger;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicesManager;


public class MoreFoodsAPI {

	protected static final String NAME  = "MoreFoods";
	protected static final String CLASS = "com.poixson.morefoods.MoreFoodsPlugin";

	protected final MoreFoodsPlugin plugin;

	protected static final AtomicInteger errcount_PluginNotFound = new AtomicInteger(0);



	public static MoreFoodsAPI GetAPI() {
		// existing instance
		{
			final ServicesManager services = Bukkit.getServicesManager();
			final MoreFoodsAPI api = services.load(MoreFoodsAPI.class);
			if (api != null)
				return api;
		}
		// load api
		try {
			if (Class.forName(CLASS) == null)
				throw new ClassNotFoundException(CLASS);
			final PluginManager manager = Bukkit.getPluginManager();
			final Plugin plugin = manager.getPlugin(NAME);
			if (plugin == null) throw new RuntimeException(NAME+" plugin not found");
			return new MoreFoodsAPI(plugin);
		} catch (ClassNotFoundException e) {
			if (errcount_PluginNotFound.getAndIncrement() < 10)
				Log().severe("Plugin not found: "+NAME);
			return null;
		}
	}

	protected MoreFoodsAPI(final Plugin p) {
		if (p == null) throw new NullPointerException();
		this.plugin = (MoreFoodsPlugin) p;
	}



	public Boolean isFresh(final ItemStack stack) {
		return this.plugin.isFresh(stack);
	}
	public Boolean isUnfresh(final ItemStack stack) {
		return this.plugin.isUnfresh(stack);
	}
	public Boolean isFullyAged(final ItemStack stack) {
		return this.plugin.isFullyAged(stack);
	}



}
