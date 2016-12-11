package uk.co.terragaming.TerraWorlds.commands;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.api.Server;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.service.pagination.PaginationService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Text.Builder;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.storage.WorldProperties;

import uk.co.terragaming.TerraCore.Commands.annotations.Alias;
import uk.co.terragaming.TerraCore.Commands.annotations.Command;
import uk.co.terragaming.TerraCore.Commands.annotations.Desc;
import uk.co.terragaming.TerraCore.Commands.annotations.Perm;
import uk.co.terragaming.TerraCore.Util.Context;

import com.google.inject.Inject;


public class ListCommand {
	
	@Inject
	PaginationService paginationService;
	
	@Inject
	Server server;
	
	@Command("world list")
	@Desc("View a list of Worlds.")
	@Perm("tc.world.list")
	@Alias("l")
	public CommandResult onWorld(Context context){
		CommandSource source = context.get(CommandSource.class);
		
		PaginationList.Builder pages = paginationService.builder();
		
		pages.title(Text.of(TextColors.DARK_GRAY, TextColors.AQUA, "Worlds", TextColors.DARK_GRAY));
		pages.padding(Text.of("-"));
		
		List<Text> contents = new ArrayList<>();
		
		for (World world : server.getWorlds()){
			contents.add(getLine(source, true, world.getProperties()));
		}
		
		for (WorldProperties world : server.getUnloadedWorlds()){
			contents.add(getLine(source, false, world));
		}
		
		pages.contents(contents);
		pages.sendTo(source);
		return CommandResult.success();
	}
	
	private Text getLine(CommandSource source, boolean loaded, WorldProperties world){
		Builder builder = Text.builder();
		builder.append(Text
				.builder(world.getWorldName())
				.color(TextColors.DARK_GRAY)
				.build());
		
		builder.append(Text.of(" "));
		
		if (loaded){
			if (source.hasPermission("tc.world.unload"))
				builder.append(Text
					.builder("[Unload]")
					.color(TextColors.GREEN)
					.onHover(TextActions.showText(Text.of(TextColors.GREEN, "Unload the world.")))
					.onClick(TextActions.runCommand("/tc:world unload " + world.getWorldName()))
					.build());
		} else {
			if (source.hasPermission("tc.world.load"))
				builder.append(Text
					.builder("[Load]")
					.color(TextColors.GREEN)
					.onHover(TextActions.showText(Text.of(TextColors.GREEN, "Load the world.")))
					.onClick(TextActions.runCommand("/tc:world load " + world.getWorldName()))
					.build());
		}
		
		if ((source.hasPermission("tc.world.load") && !loaded) || (source.hasPermission("tc.world.unload") && loaded))
		builder.append(Text.of(" "));
		
		
		if (source.hasPermission("tc.world.teleport")){
			if (loaded){
				builder.append(Text
						.builder("[Teleport]")
						.color(TextColors.GREEN)
						.onHover(TextActions.showText(Text.of(TextColors.GREEN, "Teleport to the world.")))
						.onClick(TextActions.runCommand("/tc:world tp " + world.getWorldName()))
						.build());
			} else {
				builder.append(Text
						.builder("[Teleport]")
						.color(TextColors.GRAY)
						.build());
			}
		}
		
		if (source.hasPermission("tc.world.teleport"))
		builder.append(Text.of(" "));
		
		if (source.hasPermission("tc.world.properties")){
			builder.append(Text
					.builder("[Properties]")
					.color(TextColors.GREEN)
					.onHover(TextActions.showText(Text.of(TextColors.GREEN, "View the worlds properties.")))
					.onClick(TextActions.runCommand("/tc:world properties " + world.getWorldName()))
					.build());
		}
		
		if (source.hasPermission("tc.world.properties"))
		builder.append(Text.of(" "));
		
		if (source.hasPermission("tc.world.delete"))
		builder.append(Text
				.builder("[Delete]")
				.color(TextColors.GREEN)
				.onHover(TextActions.showText(Text.of(TextColors.GREEN, "Delete the world.")))
				.onClick(TextActions.runCommand("/tc:world delete " + world.getWorldName()))
				.build());
		
		return builder.build();
	}
}
