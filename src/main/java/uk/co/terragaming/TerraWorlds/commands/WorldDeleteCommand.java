package uk.co.terragaming.TerraWorlds.commands;

import java.util.Collection;
import java.util.Optional;

import javax.inject.Named;

import org.spongepowered.api.Server;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Scheduler;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.storage.WorldProperties;

import uk.co.terragaming.TerraCore.Commands.annotations.Alias;
import uk.co.terragaming.TerraCore.Commands.annotations.Command;
import uk.co.terragaming.TerraCore.Commands.annotations.Desc;
import uk.co.terragaming.TerraCore.Commands.annotations.Perm;
import uk.co.terragaming.TerraCore.Commands.exceptions.CommandException;
import uk.co.terragaming.TerraCore.Config.Config;
import uk.co.terragaming.TerraCore.Util.Context;
import uk.co.terragaming.TerraEssentials.config.EssentialsData;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.inject.Inject;



public class WorldDeleteCommand {
	
	@Inject
	Server server;
	
	@Inject
	Scheduler scheduler;
	
	@Inject(optional=true)
	@Named("EssentialsData")
	Config data;
		
	@Command("world delete")
	@Desc("Delete a World.")
	@Perm("tc.world.delete")
	@Alias("d")
	public CommandResult onDelete(Context context,
		@Desc("The world to delete.") WorldProperties world
	) throws CommandException{
		CommandSource sender = context.get(CommandSource.class);
		String name = world.getWorldName();
		
		sender.sendMessage(Text.of(TextColors.AQUA, "Deleting the world ", TextColors.YELLOW, name, TextColors.AQUA, "..."));
		
		Optional<World> optWorld = server.getWorld(world.getUniqueId());
		if (optWorld.isPresent() && optWorld.get().isLoaded()){
			
			World w = optWorld.get();
			
			Collection<Entity> entities = w.getEntities(e -> e instanceof Player);
			for (Entity e : entities){
				Player player = (Player) e;
				player.setLocation(getSpawn(w));
				player.sendMessage(Text.of(TextColors.AQUA, "You were teleported out of the world ", TextColors.YELLOW, w.getName(), TextColors.AQUA, " because it is being deleted."));
			}
			
			if (server.unloadWorld(optWorld.get())){
				sender.sendMessage(Text.of(TextColors.AQUA, "World ", TextColors.YELLOW, name, TextColors.AQUA, " unloaded."));
			} else {
				sender.sendMessage(Text.of(TextColors.RED, "Could not unload the world ", TextColors.YELLOW, name, TextColors.AQUA, "."));
				return CommandResult.empty();
			}
		}
		
		ListenableFuture<Boolean> future = server.deleteWorld(world);
		
		Futures.addCallback(future, new FutureCallback<Boolean>(){
			
			@Override
			public void onSuccess(Boolean result) {
				sender.sendMessage(Text.of(TextColors.AQUA, "World ", TextColors.YELLOW, name, TextColors.AQUA, " deleted successfully."));
			}

			@Override
			public void onFailure(Throwable t) {
				sender.sendMessage(Text.of(TextColors.RED, "Could not delete the world ", TextColors.YELLOW, name, TextColors.AQUA, "."));
			}
		});
		return CommandResult.empty();
	}
	
	private Location<World> getSpawn(World w) throws CommandException{
		if (data != null){
			Location<World> spawn = ((EssentialsData)data).spawn.get();
			if (!spawn.getExtent().getUniqueId().equals(w.getUniqueId())){
				return spawn;
			}
		}

		return server.getWorld(server.getDefaultWorld()
			.orElseThrow(()->{return new CommandException("Could not unload the world.");})
			.getUniqueId())
			.orElseThrow(()->{return new CommandException("Could not unload the world.");})
			.getSpawnLocation();
	}
	
}
