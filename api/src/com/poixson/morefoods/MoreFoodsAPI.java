package com.poixson.morefoods;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicesManager;


public class MoreFoodsAPI {
	protected static final Logger LOG = Logger.getLogger("Minecraft");

	protected static final String NAME  = "Foods";
	protected static final String CLASS = "com.poixson.morefoods.MoreFoodsPlugin";

	protected final MoreFoodsPlugin plugin;



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
		} catch (ClassNotFoundException e) {
			LOG.severe("Plugin not found: "+NAME);
			return null;
		}
		{
			final PluginManager manager = Bukkit.getPluginManager();
			final Plugin plugin = manager.getPlugin(NAME);
			return new MoreFoodsAPI(plugin);
		}
	}

	protected MoreFoodsAPI(final Plugin p) {
		this.plugin = (MoreFoodsPlugin) p;
	}



	public boolean isFresh(final ItemStack stack) {
return false;
//		return this.plugin.isFresh(stack);
	}
	public boolean isUnfresh(final ItemStack stack) {
return false;
//		return this.plugin.isUnfresh(stack);
	}
	public boolean isRotting(final ItemStack stack) {
return false;
//		return this.plugin.isRotting(stack);
	}
	public boolean isFullyRotten(final ItemStack stack) {
return false;
//		return this.plugin.isFullyRotten(stack);
	}



}
