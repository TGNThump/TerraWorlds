package uk.co.terragaming.TerraWorlds.commands;

import org.spongepowered.api.Server;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.storage.WorldProperties;

import uk.co.terragaming.TerraCore.Commands.annotations.Command;
import uk.co.terragaming.TerraCore.Commands.annotations.Desc;
import uk.co.terragaming.TerraCore.Commands.annotations.Perm;
import uk.co.terragaming.TerraCore.Util.Context;

import com.google.inject.Inject;

public class RenameCommand {
	
	@Inject
	Server server;
	
	@Command("world rename")
	@Desc("Rename the world.")
	@Perm("tc.world.rename")
	public CommandResult onWorld(Context context,
		@Desc("The world to rename.") WorldProperties world,
		@Desc("The new name to set.") String name
	){
		CommandSource source = context.get(CommandSource.class);

		String oldName = world.getWorldName();
		
		if (server.getWorld(world.getUniqueId()).isPresent()){
			source.sendMessage(Text.of(TextColors.YELLOW, world.getWorldName(), TextColors.RED, " must be unloaded before you can rename it."));
			return CommandResult.empty();
		}
		
		if (!server.renameWorld(world, name).isPresent()){
			source.sendMessage(Text.of(TextColors.YELLOW, world.getWorldName(), TextColors.RED, " could not be renamed."));
			return CommandResult.empty();
		}
		
		source.sendMessage(Text.of(TextColors.YELLOW, oldName, TextColors.AQUA, " was renamed to ", TextColors.YELLOW, name, TextColors.AQUA, "." ));
		return CommandResult.success();
	}
}
