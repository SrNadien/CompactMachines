package dev.compactmods.machines;

import dev.compactmods.machines.api.core.Constants;
import dev.compactmods.machines.client.CreativeTabs;
import dev.compactmods.machines.command.Commands;
import dev.compactmods.machines.config.CommonConfig;
import dev.compactmods.machines.config.EnableVanillaRecipesConfigCondition;
import dev.compactmods.machines.config.ServerConfig;
import dev.compactmods.machines.core.Registries;
import dev.compactmods.machines.dimension.Dimension;
import dev.compactmods.machines.dimension.WorldBorderFixer;
import dev.compactmods.machines.graph.Graph;
import dev.compactmods.machines.machine.Machines;
import dev.compactmods.machines.room.data.LootFunctions;
import dev.compactmods.machines.shrinking.Shrinking;
import dev.compactmods.machines.tunnel.Tunnels;
import dev.compactmods.machines.upgrade.MachineRoomUpgrades;
import dev.compactmods.machines.wall.Walls;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

@Mod(Constants.MOD_ID)
public class CompactMachines {

    public static final Logger LOGGER = LogManager.getLogger();
    public static final Marker CONN_MARKER = MarkerManager.getMarker("cm_connections");

    public CompactMachines() {
        var modBus = FMLJavaModLoadingContext.get().getModEventBus();
        initConfigs();
        preparePackages();
        registerEvents(modBus);

        Registries.setup(modBus);

        // Configuration
        CraftingHelper.register(EnableVanillaRecipesConfigCondition.Serializer.INSTANCE);
    }

    private static void initConfigs() {
        ModLoadingContext mlCtx = ModLoadingContext.get();
        mlCtx.registerConfig(ModConfig.Type.COMMON, CommonConfig.CONFIG);
        mlCtx.registerConfig(ModConfig.Type.SERVER, ServerConfig.CONFIG);
    }

    private static void preparePackages() {
        // Package initialization here, this kickstarts the rest of the DR code (classloading)
        Machines.prepare();
        Walls.prepare();
        Tunnels.prepare();
        Shrinking.prepare();
        CreativeTabs.prepare();

        Dimension.prepare();
        MachineRoomUpgrades.prepare();
        Graph.prepare();
        Commands.prepare();
        LootFunctions.prepare();
    }

    private static void registerEvents(IEventBus modBus) {
        WorldBorderFixer.registerEvents();
    }
}
