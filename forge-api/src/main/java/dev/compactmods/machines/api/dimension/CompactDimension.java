package dev.compactmods.machines.api.dimension;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;

import javax.annotation.Nullable;

import static dev.compactmods.machines.api.core.Constants.MOD_ID;

public abstract class CompactDimension {
    public static final ResourceKey<Level> LEVEL_KEY = ResourceKey
            .create(Registries.DIMENSION, new ResourceLocation(MOD_ID, "compact_world"));

    public static final ResourceKey<DimensionType> DIM_TYPE_KEY = ResourceKey
            .create(Registries.DIMENSION_TYPE, new ResourceLocation(MOD_ID, "compact_world"));

    private CompactDimension() {}

    @Nullable
    public static ServerLevel forServer(MinecraftServer server) {
        return server.getLevel(LEVEL_KEY);
    }

    public static boolean isLevelCompact(Level level) {
        return isLevelCompact(level.dimension());
    }

    public static boolean isLevelCompact(ResourceKey<Level> level) {
        return level.equals(LEVEL_KEY);
    }
}
