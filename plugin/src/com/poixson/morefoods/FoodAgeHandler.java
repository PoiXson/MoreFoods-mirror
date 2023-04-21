package com.poixson.morefoods;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import com.poixson.commonmc.utils.ItemUtils;
import com.poixson.tools.abstractions.xStartStop;
import com.poixson.utils.RandomUtils;


public class FoodAgeHandler extends BukkitRunnable implements xStartStop {

	protected static final long START_DELAY_TICKS = 200L; // 10 seconds

	protected final MoreFoodsPlugin plugin;

	protected final long interval;
	protected final int  chance;
	protected final AtomicInteger rndLast = new AtomicInteger(-1);

	public final Map<Material, Set<CustomFoodDAO>> foods;



	public FoodAgeHandler(final MoreFoodsPlugin plugin,
			final long interval, final double chance,
			final Map<Material, Set<CustomFoodDAO>> foods) {
		this.plugin   = plugin;
		this.interval = interval;
		this.chance   = (int) (1.0 / chance);
		this.foods    = foods;
	}
	public FoodAgeHandler(final MoreFoodsPlugin plugin,
			final long interval, final double chance) {
		this(plugin, interval, chance, new HashMap<Material, Set<CustomFoodDAO>>());
	}



	@Override
	public void start() {
		this.runTaskTimer(this.plugin, START_DELAY_TICKS, this.interval);
	}
	@Override
	public void stop() {
		try {
			this.cancel();
		} catch (IllegalStateException ignore) {}
	}



	public void addFood(final Material type, final String name,
			final int model_from, final int model_to, final int threshold) {
		this.addFood(new CustomFoodDAO(type, name, model_from, model_to, threshold));
	}
	public void addFood(final CustomFoodDAO dao) {
		Set<CustomFoodDAO> set = this.foods.get(dao.type);
		if (set == null) {
			set = new HashSet<CustomFoodDAO>();
			this.foods.put(dao.type, set);
		}
		set.add(dao);
	}
	public CustomFoodDAO getFoodDAO(final ItemStack stack) {
		final Set<CustomFoodDAO> set = this.foods.get(stack.getType());
		if (set != null) {
			final int model = ItemUtils.GetCustomModel(stack);
			for (final CustomFoodDAO dao : set) {
				if (dao.model == model)
					return dao;
			}
		}
		return null;
	}



	@Override
	public void run() {
		for (final Player player : Bukkit.getOnlinePlayers()) {
			if (player.hasPermission("morefoods.aging")) {
				final PlayerInventory inventory = player.getInventory();
				boolean changed = false;
				for (final ItemStack stack : inventory.getStorageContents()) {
					if (stack == null) continue;
					if (this.ageItem(stack))
						changed = true;
				}
				if (changed)
					player.playSound(player, Sound.BLOCK_HONEY_BLOCK_SLIDE, 0.17f, 1.49f);
			}
		}
	}



	protected boolean ageItem(final ItemStack stack) {
		// random ticks
		if (this.chance > 1) {
			final int rnd = RandomUtils.GetNewRandom(0, this.chance * 1000, this.rndLast.get()) % this.chance;
			this.rndLast.set(rnd);
			if (rnd != 0) return false;
		}
		// age the food
		final CustomFoodDAO dao = this.getFoodDAO(stack);
		if (dao != null) {
			// last stage
			if (dao.next < 0) return false;
			final ItemMeta meta = stack.getItemMeta();
			final PersistentDataContainer persistent = meta.getPersistentDataContainer();
			final NamespacedKey key = new NamespacedKey(this.plugin, "rotting");
			int current_factor = 0;
			if (persistent.has(key, PersistentDataType.INTEGER)) {
				final Integer value = persistent.get(key, PersistentDataType.INTEGER);
				if (value != null)
					current_factor = value.intValue() + 1;
			}
			if (current_factor >= dao.factor) {
				current_factor = 0;
				meta.setDisplayName(dao.name);
				meta.setCustomModelData(Integer.valueOf(dao.next));
			}
//TODO: debug logs
//System.out.println("MODEL: "+dao.model+" AGE: "+age);
			persistent.set(key, PersistentDataType.INTEGER, Integer.valueOf(current_factor));
			stack.setItemMeta(meta);
			return true;
		}
		return false;
	}



}
