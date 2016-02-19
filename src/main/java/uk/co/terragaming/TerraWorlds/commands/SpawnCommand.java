package uk.co.terragaming.TerraWorlds.commands;

import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.source.LocatedSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import uk.co.terragaming.TerraCore.Commands.Flag;
import uk.co.terragaming.TerraCore.Commands.annotations.Alias;
import uk.co.terragaming.TerraCore.Commands.annotations.Command;
import uk.co.terragaming.TerraCore.Commands.annotations.Desc;
import uk.co.terragaming.TerraCore.Commands.annotations.Perm;
import uk.co.terragaming.TerraCore.Commands.exceptions.CommandException;
import uk.co.terragaming.TerraCore.Util.Context;


public class SpawnCommand {
	
	@Command("world spawn")
	@Desc("Teleport to the world's spawn.")
	@Perm("tc.world.spawn")
	public CommandResult onSpawn(Context context,
		@Desc("Force the teleport if unsafe.") @Perm("tc.world.spawn.unsafe") @Alias("-f") Flag<Boolean> force	
	) throws CommandException{
		CommandSource source = context.get(CommandSource.class);
		
		if (source instanceof Player){
			Player player = (Player) source;
			
			if (force.isPresent()){
				player.setLocation(player.getLocation().getExtent().getSpawnLocation());
				source.sendMessage(Text.of(TextColors.AQUA, "Teleported you to the world's spawn."));
			} else {
				if (player.setLocationSafely(player.getLocation().getExtent().getSpawnLocation())){
					source.sendMessage(Text.of(TextColors.AQUA, "Teleported you to the world's spawn."));
				} else {
					source.sendMessage(Text.of(TextColors.RED, "Could not safely teleport you to spawn."));
					if (source.hasPermission("tc.world.spawn.unsafe"))
						source.sendMessage(Text.of(TextColors.RED, "Use the ", TextColors.YELLOW, "-force", TextColors.RED, " flag to continue anyway."));
				}
			}

			return CommandResult.success();
		} else {
			source.sendMessage(Text.of(TextColors.RED, "This command can only be run as a Player."));
			return CommandResult.empty();
		}
	}
	
	@Command("world setspawn")
	@Desc("Set the world's spawn point.")
	@Perm("tc.world.spawn.set")
	@Alias("spawn set")
	public CommandResult onSpawnSet(Context context){
		CommandSource source = context.get(CommandSource.class);
		
		if (source instanceof LocatedSource){
			((LocatedSource) source).getWorld().getProperties().setSpawnPosition(((LocatedSource) source).getLocation().getBlockPosition());
			source.sendMessage(Text.of(TextColors.AQUA, "The world's spawn point has been set to your location."));
			return CommandResult.success();
		} else {
			source.sendMessage(Text.of(TextColors.RED, "This command can only be run as a Player."));
			return CommandResult.empty();
		}
	}
}
