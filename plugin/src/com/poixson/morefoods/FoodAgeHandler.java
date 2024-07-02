package com.poixson.morefoods;

import static com.poixson.morefoods.MoreFoodsPlugin.PERSISTENT_AGE_KEY;
import static com.poixson.utils.BukkitUtils.SafeCancel;

import java.util.concurrent.atomic.AtomicInteger;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import com.poixson.tools.xRand;
import com.poixson.tools.abstractions.xStartStop;


public class FoodAgeHandler extends BukkitRunnable implements xStartStop {

	protected static final long START_DELAY_TICKS = 200L; // 10 seconds

	protected final MoreFoodsPlugin plugin;

	protected final long interval;
	protected final int  chance;

	protected final xRand random = (new xRand()).seed_time();
	protected final AtomicInteger rndLast = new AtomicInteger(-1);



	public FoodAgeHandler(final MoreFoodsPlugin plugin,
			final long interval, final double chance) {
		this.plugin   = plugin;
		this.interval = interval;
		this.chance   = (int) (1.0 / chance);
	}



	@Override
	public void start() {
		this.runTaskTimer(this.plugin, START_DELAY_TICKS, this.interval);
	}
	@Override
	public void stop() {
		SafeCancel(this);
	}



	@Override
	public void run() {
		for (final Player player : Bukkit.getOnlinePlayers()) {
			if (player.hasPermission("morefoods.aging")) {
				final PlayerInventory inventory = player.getInventory();
				boolean changed = false;
				for (final ItemStack stack : inventory.getStorageContents()) {
					if (stack == null) continue;
					if (this.ageItemRandom(stack))
						changed = true;
				}
				if (changed)
					player.playSound(player, Sound.BLOCK_HONEY_BLOCK_SLIDE, 0.17f, 1.49f);
			}
		}
	}



	public boolean ageItemRandom(final ItemStack stack) {
		// random ticks
		if (this.chance > 1) {
			final int rnd = this.random.nextInt(0, this.chance * 1000) % this.chance;
			this.rndLast.set(rnd);
			if (rnd != 0) return false;
		}
		return this.ageItem(stack);
	}
	public boolean ageItem(final ItemStack stack) {
		// age the food
		final CustomFoodDAO dao = this.plugin.getFoodDAO(stack);
		if (dao != null) {
			// last stage
			if (dao.next < 0) return false;
			final ItemMeta meta = stack.getItemMeta();
			final PersistentDataContainer persistent = meta.getPersistentDataContainer();
			final NamespacedKey key = new NamespacedKey(this.plugin, PERSISTENT_AGE_KEY);
			int current_factor = 0;
			if (persistent.has(key, PersistentDataType.INTEGER)) {
				final Integer value = persistent.get(key, PersistentDataType.INTEGER);
				if (value != null)
					current_factor = value.intValue() + 1;
			}
			if (current_factor >= dao.factor) {
				current_factor = 0;
				final CustomFoodDAO dao_new = this.plugin.getFoodDAO(stack.getType(), dao.next);
				if (dao_new == null) {
					this.plugin.log().warning("Next food age id not found: "+Integer.toString(dao.next));
					return false;
				}
				meta.setDisplayName(dao_new.name);
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
