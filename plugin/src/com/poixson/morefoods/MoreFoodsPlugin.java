package com.poixson.morefoods;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.poixson.commonmc.tools.plugin.xJavaPlugin;
import com.poixson.tools.xTime;


public class MoreFoodsPlugin extends xJavaPlugin {
	@Override public int getSpigotPluginID() { return 108357; }
	@Override public int getBStatsID() {       return 17853;  }
	public static final String LOG_PREFIX  = "[Foods] ";
	public static final String CHAT_PREFIX = ChatColor.AQUA + "[Foods] " + ChatColor.WHITE;

	public static final String DEFAULT_AGING_INTERVAL = "5m";
	public static final double DEFAULT_AGING_CHANCE   = 0.164; // about one stage every 2.5 hours
	public static final int    DEFAULT_AGING_FACTOR   = 5;

	// listeners
	protected final AtomicReference<FoodAgeHandler>  ageHandler  = new AtomicReference<FoodAgeHandler>(null);
	protected final AtomicReference<FoodEatListener> eatListener = new AtomicReference<FoodEatListener>(null);

	protected final Map<Material, Set<CustomFoodDAO>> foods = new HashMap<Material, Set<CustomFoodDAO>>();



	public MoreFoodsPlugin() {
		super(MoreFoodsPlugin.class);
	}



	@Override
	public void onEnable() {
		super.onEnable();
		// age handler
		{
			final long interval = this.getAgingInterval();
			final double chance = this.getAgingChance();
			final FoodAgeHandler listener = new FoodAgeHandler(this, interval, chance, this.foods);
			final FoodAgeHandler previous = this.ageHandler.getAndSet(listener);
			if (previous != null)
				previous.stop();
			listener.start();
		}
		// eat listener
		{
			final FoodEatListener listener = new FoodEatListener(this);
			final FoodEatListener previous = this.eatListener.getAndSet(listener);
			if (previous != null)
				previous.unregister();
			listener.register();
		}
	}

	@Override
	public void onDisable() {
		super.onDisable();
		// age handler
		{
			final FoodAgeHandler listener = this.ageHandler.getAndSet(null);
			if (listener != null)
				listener.stop();
		}
		// eat listener
		{
			final FoodEatListener listener = this.eatListener.getAndSet(null);
			if (listener != null)
				listener.unregister();
		}
	}



	// -------------------------------------------------------------------------------
	// configs



	@Override
	protected void loadConfigs() {
		this.mkPluginDir();
		// config.yml
		{
			final FileConfiguration cfg = this.getConfig();
			this.config.set(cfg);
			this.configDefaults(cfg);
			cfg.options().copyDefaults(true);
			super.saveConfig();
		}
		// foods.yml
		{
			final File file = new File(this.getDataFolder(), "foods.yml");
			final FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
			// default foods
			if (!file.isFile()) {
				this.foodsDefaults(cfg);
				try {
					cfg.save(file);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			// load food states
			int count_states = 0;
			for (final String key : cfg.getKeys(false)) {
				final Material type = Material.getMaterial(key);
				final HashSet<CustomFoodDAO> states = new HashSet<CustomFoodDAO>();
				final List<Map<String, Object>> list = Safe_GetStatesList(cfg, key);
				for (final Map<String, Object> map : list) {
					final String name    = (String) map.get("name");
					final int model = ((Integer)map.get("model")).intValue();
					final int next  = (map.containsKey("next" ) ? ((Integer)map.get("next" )).intValue() :-1);
					final int delay = (map.containsKey("delay") ? ((Integer)map.get("delay")).intValue() : 1);
					final CustomFoodDAO dao = new CustomFoodDAO(type, name, model, next, delay);
					states.add(dao);
					count_states++;
				}
				this.foods.put(type, states);
			}
			LOG.info(String.format(
				"%sLoaded %d foods with %d states",
				LOG_PREFIX,
				Integer.valueOf(this.foods.size()),
				Integer.valueOf(count_states)
			));
		}
	}

	@Override
	protected void saveConfigs() {
		// config.yml
		super.saveConfig();
	}

	@Override
	protected void configDefaults(final FileConfiguration cfg) {
		cfg.addDefault("Interval", DEFAULT_AGING_INTERVAL);
		cfg.addDefault("Chance", Double.valueOf(DEFAULT_AGING_CHANCE));
	}
	protected void foodsDefaults(final FileConfiguration cfg) {
		// apple
		{
			List<Map<String, Object>> states = new ArrayList<Map<String, Object>>();
			states.add(this.foodDefault("Apple",          0,  8, DEFAULT_AGING_FACTOR));
			states.add(this.foodDefault("Unfresh Apple",  8,  9, DEFAULT_AGING_FACTOR));
			states.add(this.foodDefault("Old Apple",      9, 10, DEFAULT_AGING_FACTOR));
			states.add(this.foodDefault("Rotting Apple", 10, 11, DEFAULT_AGING_FACTOR));
			states.add(this.foodDefault("Rotten Apple",  11                          ));
			cfg.set("APPLE", states);
		}
		// corn
		{
			List<Map<String, Object>> states = new ArrayList<Map<String, Object>>();
			states.add(this.foodDefault("Carrot",  0, 11, 22));
			states.add(this.foodDefault("Corn",   11        ));
			cfg.set("CARROT", states);
		}
		// ritz
		{
			List<Map<String, Object>> states = new ArrayList<Map<String, Object>>();
			states.add(this.foodDefault("Bread",  0, 11, 22));
			states.add(this.foodDefault("Ritz",   11       ));
			cfg.set("BREAD", states);
		}
	}
	protected Map<String, Object> foodDefault(final String name, final int model) {
		return this.foodDefault(name, model, -1, -1);
	}
	protected Map<String, Object> foodDefault(final String name, final int model, final int next, final int factor) {
		final Map<String, Object> map = new HashMap<String, Object>();
		map.put("name",  name);
		map.put("model", Integer.valueOf(model));
		if (next   > 0) map.put("next",  Integer.valueOf(next  ));
		if (factor > 0) map.put("delay", Integer.valueOf(factor));
		return map;
	}
	@SuppressWarnings("unchecked")
	protected static List<Map<String, Object>> Safe_GetStatesList(final FileConfiguration cfg, final String key) {
		return (List<Map<String, Object>>) cfg.getList(key);
	}



	public long getAgingInterval() {
		return xTime.Parse( this.config.get().getString("Interval") ).ticks(50L);
	}
	public double getAgingChance() {
		return this.config.get().getDouble("Chance");
	}



}
