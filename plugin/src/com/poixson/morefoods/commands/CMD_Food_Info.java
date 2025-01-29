package com.poixson.morefoods.commands;

import static com.poixson.morefoods.MoreFoodsDefines.CMD_LABELS_FOOD_INFO;
import static com.poixson.morefoods.MoreFoodsDefines.PERM_CMD_FOOD_INFO;
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


// /food info
public interface CMD_Food_Info extends PluginCommand {



	default ArgumentBuilder<CommandSourceStack, ?> register_Food_Info(final MoreFoodsPlugin plugin) {
		return Commands.literal(CMD_LABELS_FOOD_INFO.NODE)
			// /food info
			.executes(context -> this.onCommand_Food_Info(context, plugin));
	}



//TODO: more info - percent aged
	default int onCommand_Food_Info(final CommandContext<CommandSourceStack> context, final MoreFoodsPlugin plugin) {
		final CommandSourceStack source = context.getSource();
		final CommandSender sender = source.getSender();
		final Player self = PlayerOnlyNoConsole(sender);
		// permission
		if (!HasPermissionUseCMD(sender, PERM_CMD_FOOD_INFO.NODE))
			return FAILURE;
		final PlayerInventory inventory = self.getInventory();
		final ItemStack stack = inventory.getItemInMainHand();
		if (IsEmptyOrAir(stack))
			return FAILURE;
		final Set<CustomFoodDAO> states = plugin.getFood(stack.getType());
		if (IsEmpty(states)) {
			sender.sendMessage(CHAT_PREFIX.append(Component.text(
				"Invalid item").color(NamedTextColor.RED)));
			return FAILURE;
		}
		final int model = GetCustomModel(stack);
		// find state
		for (final CustomFoodDAO dao : states) {
			if (dao.model == model) {
				sender.sendMessage(CHAT_PREFIX
					.append(Component.text("Model: ").color(NamedTextColor.AQUA))
					.append(Component.text(model    ).color(NamedTextColor.GOLD))
					.append(Component.text(' '))
					.append(Component.text(dao.name))
				);
				return SUCCESS;
			}
		} // end state
		sender.sendMessage(CHAT_PREFIX
			.append(Component.text("Invalid state: "+Integer.toString(model)).color(NamedTextColor.RED))
		);
		return FAILURE;
	}



}
