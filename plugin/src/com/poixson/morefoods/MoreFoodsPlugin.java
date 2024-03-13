package com.poixson.morefoods;

import static com.poixson.utils.BlockUtils.GetCustomModel;

import java.io.File;
import java.io.IOException;
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
import org.bukkit.inventory.ItemStack;

import com.poixson.morefoods.commands.Commands;
import com.poixson.tools.xJavaPlugin;
import com.poixson.tools.xTime;


public class MoreFoodsPlugin extends xJavaPlugin {
	@Override public int getSpigotPluginID() { return 108357; }
	@Override public int getBStatsID() {       return 18356;  }
	public static final String CHAT_PREFIX = ChatColor.AQUA+"[Foods] "+ChatColor.WHITE;

	public static final String PERSISTENT_AGE_KEY     = "food_age";
	public static final String DEFAULT_AGING_INTERVAL = "5m";
	public static final double DEFAULT_AGING_CHANCE   = 0.164; // about one stage every 2.5 hours
	public static final int    DEFAULT_AGING_FACTOR   = 5;

	// listeners
	protected final AtomicReference<FoodAgeHandler>  ageHandler  = new AtomicReference<FoodAgeHandler>(null);
	protected final AtomicReference<FoodEatListener> eatListener = new AtomicReference<FoodEatListener>(null);

	protected final AtomicReference<Commands> commands = new AtomicReference<Commands>(null);

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
			final FoodAgeHandler handler = new FoodAgeHandler(this, interval, chance);
			final FoodAgeHandler previous = this.ageHandler.getAndSet(handler);
			if (previous != null)
				previous.stop();
			handler.start();
		}
		// eat listener
		{
			final FoodEatListener listener = new FoodEatListener(this);
			final FoodEatListener previous = this.eatListener.getAndSet(listener);
			if (previous != null)
				previous.unregister();
			listener.register(this);
		}
		// commands
		{
			final Commands commands = new Commands(this);
			final Commands previous = this.commands.getAndSet(commands);
			if (previous != null)
				previous.unregister();
			commands.register(this);
		}
		this.saveConfigs();
	}

	@Override
	public void onDisable() {
		super.onDisable();
		// commands
		{
			final Commands commands = this.commands.getAndSet(null);
			if (commands != null)
				commands.unregister();
		}
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
				final Material type = Material.matchMaterial(key);
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
			this.log().info(String.format(
				"Loaded %d foods with %d states",
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
		// apple -> rotten apple
		cfg.set("APPLE", (new FoodBuilder())
			.addStage("Apple",          0,  8, DEFAULT_AGING_FACTOR)
			.addStage("Unfresh Apple",  8,  9, DEFAULT_AGING_FACTOR)
			.addStage("Old Apple",      9, 10, DEFAULT_AGING_FACTOR)
			.addStage("Rotting Apple", 10, 11, DEFAULT_AGING_FACTOR)
			.addStage("Rotten Apple",  11                          )
			.build());
		// sweet berries -> beans
		cfg.set("SWEET_BERRIES", (new FoodBuilder())
			.addStage("Sweet Berries", 0, 15, 2) // 3 hours
			.addStage("Beans", 57              )
			.build());
		// carrot -> corn
		cfg.set("CARROT", (new FoodBuilder())
			.addStage("Carrot", 0, 11, 14) // 7 hours
			.addStage("Corn",  11        )
			.build());
		// bread -> ritz
		cfg.set("BREAD", (new FoodBuilder())
			.addStage("Bread", 0, 11, 8) // 4 hours
			.addStage("Ritz", 11       )
			.build());
		cfg.set("COOKIE", (new FoodBuilder())
			.addStage("Cookie",         0, 88, 4) // 2 hours
			.addStage("Cookie Crumbs", 88       )
			.build());
		// rotten flesh -> poop
		cfg.set("ROTTEN_FLESH", (new FoodBuilder())
			.addStage("Rotten Flesh", 0, 6, 5) // 2 hours
			.addStage("Poop", 6              )
			.build());
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



	// -------------------------------------------------------------------------------



	public FoodAgeHandler getAgeHandler() {
		return this.ageHandler.get();
	}



	public Set<CustomFoodDAO> getFood(final Material type) {
		return this.foods.get(type);
	}

	public CustomFoodDAO getFoodDAO(final ItemStack stack) {
		final Material type = stack.getType();
		final int model = GetCustomModel(stack);
		return this.getFoodDAO(type, model);
	}
	public CustomFoodDAO getFoodDAO(final Material type, final int model) {
		final Set<CustomFoodDAO> set = this.foods.get(type);
		if (set != null) {
			for (final CustomFoodDAO dao : set) {
				if (dao.model == model)
					return dao;
			}
		}
		return null;
	}

	public boolean isSupportedFood(final ItemStack stack) {
		if (stack == null) throw new NullPointerException();
		return this.foods.containsKey(stack.getType());
	}



	public static enum FoodAge {
		FRESH,
		UNFRESH,
		FULLY_AGED,
	}

	public FoodAge getFoodAge(final ItemStack stack) {
		if (stack == null) throw new NullPointerException();
		final int model = GetCustomModel(stack);
		if (model == 0) {
			return (this.isSupportedFood(stack) ? FoodAge.FRESH : null);
		} else {
			final Set<CustomFoodDAO> daos = this.foods.get(stack.getType());
			if (daos == null) return null;
			for (final CustomFoodDAO dao : daos) {
				if (dao.model == model)
					return (dao.next < 0 ? FoodAge.FULLY_AGED : FoodAge.UNFRESH);
			}
		}
		return null;
	}

	public Boolean isFresh(final ItemStack stack) {
		final FoodAge age = this.getFoodAge(stack);
		if (age == null)
			return null;
		return Boolean.valueOf( FoodAge.FRESH.equals(age) );
	}
	public Boolean isUnfresh(final ItemStack stack) {
		final FoodAge age = this.getFoodAge(stack);
		if (age == null)
			return null;
		return Boolean.valueOf( FoodAge.UNFRESH.equals(age) );
	}
	public Boolean isFullyAged(final ItemStack stack) {
		final FoodAge age = this.getFoodAge(stack);
		if (age == null)
			return null;
		return Boolean.valueOf( FoodAge.FULLY_AGED.equals(age) );
	}



}
