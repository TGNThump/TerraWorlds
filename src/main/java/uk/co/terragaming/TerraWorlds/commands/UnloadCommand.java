package uk.co.terragaming.TerraWorlds.commands;

import java.util.Collection;

import javax.inject.Named;

import org.spongepowered.api.Server;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import uk.co.terragaming.TerraCore.Commands.annotations.Command;
import uk.co.terragaming.TerraCore.Commands.annotations.Desc;
import uk.co.terragaming.TerraCore.Commands.annotations.Perm;
import uk.co.terragaming.TerraCore.Commands.exceptions.CommandException;
import uk.co.terragaming.TerraCore.Config.Config;
import uk.co.terragaming.TerraCore.Util.Context;
import uk.co.terragaming.TerraEssentials.config.EssentialsData;

import com.google.inject.Inject;

public class UnloadCommand {
	
	@Inject
	Server server;
	
	@Inject(optional = true)
	@Named("EssentialsData")
	Config data;
	
	@Command("world unload")
	@Desc("Unload a World.")
	@Perm("tc.world.unload")
	public CommandResult onUnload(Context context,
		@Desc("The world to unload.") World world
	) throws CommandException {
		CommandSource sender = context.get(CommandSource.class);
		String name = world.getName();
		
		if (server.getDefaultWorld().get().getWorldName().equals(name)){
			sender.sendMessage(Text.of(TextColors.RED, "You can't unload the default world."));
			return CommandResult.empty();
		}
		
		sender.sendMessage(Text.of(TextColors.AQUA, "Unloading the world ", TextColors.YELLOW, name, TextColors.AQUA, "..."));
				
		Collection<Entity> entities = world.getEntities(e -> e instanceof Player);
		for (Entity e : entities) {
			Player player = (Player) e;
			player.setLocation(getSpawn(world));
			player.sendMessage(Text.of(TextColors.YELLOW, world.getName(), TextColors.AQUA, " is being unloaded."));
		}
		
		if (server.unloadWorld(world)) {
			sender.sendMessage(Text.of(TextColors.AQUA, "World ", TextColors.YELLOW, name, TextColors.AQUA, " unloaded."));
			return CommandResult.success();
		} else {
			sender.sendMessage(Text.of(TextColors.RED, "Could not unload the world ", TextColors.YELLOW, name, TextColors.AQUA, "."));
			return CommandResult.empty();
		}
	}
	
	private Location<World> getSpawn(World w) throws CommandException {
		if (data != null) {
			Location<World> spawn = ((EssentialsData) data).spawn.get();
			if (!spawn.getExtent().getUniqueId().equals(w.getUniqueId())) {
				return spawn;
			}
		}
		
		return server.getWorld(server.getDefaultWorld().orElseThrow(() -> {
			return new CommandException("Could not unload the world.");
		}).getUniqueId()).orElseThrow(() -> {
			return new CommandException("Could not unload the world.");
		}).getSpawnLocation();
	}
}
