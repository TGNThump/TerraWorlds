package uk.co.terragaming.TerraWorlds.commands;

import java.util.Optional;

import javax.inject.Inject;

import org.spongepowered.api.Server;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.DimensionType;
import org.spongepowered.api.world.DimensionTypes;
import org.spongepowered.api.world.GeneratorType;
import org.spongepowered.api.world.GeneratorTypes;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.WorldCreationSettings;
import org.spongepowered.api.world.WorldCreationSettings.Builder;
import org.spongepowered.api.world.gen.WorldGeneratorModifier;
import org.spongepowered.api.world.storage.WorldProperties;

import uk.co.terragaming.TerraCore.Commands.Flag;
import uk.co.terragaming.TerraCore.Commands.annotations.Alias;
import uk.co.terragaming.TerraCore.Commands.annotations.Command;
import uk.co.terragaming.TerraCore.Commands.annotations.Desc;
import uk.co.terragaming.TerraCore.Commands.annotations.Perm;
import uk.co.terragaming.TerraCore.Util.Context;


public class WorldCreateCommand {
	
	@Inject
	Server server;
		
	@Command("world create")
	@Desc("Create a new World.")
	@Perm("tc.world.create")
	@Alias("c")
	public CommandResult onCreate(Context context,
		@Desc("The name of the new world.") String name,
		@Desc("The dimension type of the new world.") Optional<DimensionType> dimension,
		@Desc("The generator for the new world.") Optional<GeneratorType> generator,
		@Desc("The seed for the new world.") @Alias("s") Flag<String> seed,
		@Desc("The modifier for the new world.") WorldGeneratorModifier... modifiers
		
	){
		CommandSource sender = context.get(CommandSource.class);
		
		if (name.contains(":")){
			sender.sendMessage(Text.of(TextColors.YELLOW, name, TextColors.RED, " is not a valid world name."));
			return CommandResult.empty();
		}
		
		if (server.getWorld(name).isPresent()){
			sender.sendMessage(Text.of(TextColors.RED, "The world ", TextColors.YELLOW, name, TextColors.RED, " allready exists."));
			return CommandResult.empty();
		}
		
		sender.sendMessage(Text.of(TextColors.AQUA, "Creating the world ", TextColors.YELLOW, name, TextColors.AQUA, "..."));
		
		Builder builder = WorldCreationSettings.builder()
			.enabled(true)
			.keepsSpawnLoaded(true)
			.loadsOnStartup(true)
			.dimension(dimension.orElse(DimensionTypes.OVERWORLD))
			.generator(generator.orElse(GeneratorTypes.OVERWORLD))
			.generatorModifiers(modifiers)
			.name(name);
		
		
		if (seed.isPresent()){
			builder.seed(Long.parseLong(seed.get()));
		}
		
		
		final Optional<WorldProperties> properties = server.createWorldProperties(builder.build());
		
		if (!properties.isPresent()){
			sender.sendMessage(Text.of(TextColors.RED, "Could not create the new world."));
			return CommandResult.empty();
		}
		
		Optional<World> world = server.loadWorld(properties.get());
		
		if (!world.isPresent()){
			sender.sendMessage(Text.of(TextColors.RED, "Could not create the new world."));
			return CommandResult.empty();
		}
		
		sender.sendMessage(Text.of(TextColors.AQUA, "World ", TextColors.YELLOW, name, TextColors.AQUA, " created successfully."));
		return CommandResult.success();
	}
	
}
