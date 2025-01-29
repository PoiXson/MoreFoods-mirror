package com.poixson.morefoods.commands;

import static com.poixson.morefoods.MoreFoodsDefines.CMD_LABELS_FOOD_ROT;
import static com.poixson.morefoods.MoreFoodsDefines.PERM_CMD_FOOD_ROT;
import static com.poixson.morefoods.MoreFoodsDefines.PERM_FOOD_DETAILED;
import static com.poixson.morefoods.MoreFoodsPlugin.CHAT_PREFIX;
import static com.poixson.tools.commands.PluginCommand.HasPermissionUseCMD;
import static com.poixson.tools.commands.PluginCommand.PlayerOnlyNoConsole;
import static com.poixson.utils.BlockUtils.GetCustomModel;
import static com.poixson.utils.BlockUtils.IsEmptyOrAir;
import static com.poixson.utils.Utils.IsEmpty;

import java.util.Set;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.poixson.morefoods.CustomFoodDAO;
import com.poixson.morefoods.MoreFoodsPlugin;
import com.poixson.tools.commands.PluginCommand;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;


// /food rot
public interface CMD_Food_Rot extends PluginCommand {



	default ArgumentBuilder<CommandSourceStack, ?> register_Food_Rot(final MoreFoodsPlugin plugin) {
		return Commands.literal(CMD_LABELS_FOOD_ROT.NODE)
			// /food rot
			.executes(context -> this.onCommand_Food_Rot(context, plugin));
	}



	default int onCommand_Food_Rot(final CommandContext<CommandSourceStack> context, final MoreFoodsPlugin plugin) {
		final CommandSourceStack source = context.getSource();
		final CommandSender sender = source.getSender();
		final Player self = PlayerOnlyNoConsole(sender);
		// permission
		if (!HasPermissionUseCMD(sender, PERM_CMD_FOOD_ROT.NODE))
			return FAILURE;
		final PlayerInventory inventory = self.getInventory();
		final ItemStack stack = inventory.getItemInMainHand();
		if (IsEmptyOrAir(stack))
			return FAILURE;
		final Set<CustomFoodDAO> states = plugin.getFood(stack.getType());
		if (IsEmpty(states))
			return FAILURE;
		final int model = GetCustomModel(stack);
		//LOOP_STATES:
		for (final CustomFoodDAO dao : states) {
			if (dao.model == model) {
				int model_new = model;
				LOOP_I:
				for (int i=0; i<100; i++) {
					if (!plugin.getAgeHandler().ageItem(stack)) {
						sender.sendMessage(CHAT_PREFIX.append(Component.text(
							"The item in your main hand cannot be aged").color(NamedTextColor.AQUA)));
						return SUCCESS;
					}
					model_new = GetCustomModel(stack);
					if (model != model_new)
						break LOOP_I;
				} // end LOOP_I
				inventory.setItemInMainHand(stack);
				if (sender.hasPermission(PERM_FOOD_DETAILED.NODE)) {
					sender.sendMessage(CHAT_PREFIX
						.append(Component.text("The food spoils in your hand, from model ").color(NamedTextColor.AQUA))
						.append(Component.text(model                                      ).color(NamedTextColor.GOLD))
						.append(Component.text(" to "                                     ).color(NamedTextColor.AQUA))
						.append(Component.text(model_new                                  ).color(NamedTextColor.GOLD))
					);
				} else {
					sender.sendMessage(CHAT_PREFIX.append(Component.text(
						"The food spoils in your hand").color(NamedTextColor.AQUA)));
				}
				return SUCCESS;
			}
		} // end LOOP_STATES
		return SUCCESS;
	}



}
