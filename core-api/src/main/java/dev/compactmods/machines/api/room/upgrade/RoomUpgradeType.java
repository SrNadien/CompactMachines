package dev.compactmods.machines.api.room.upgrade;

import com.mojang.serialization.MapCodec;
import dev.compactmods.machines.api.CompactMachines;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.flag.FeatureElement;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;
import java.util.function.Supplier;

// TODO: Room Upgrade context (level, itemstack, etc)
public record RoomUpgradeType<T extends RoomUpgrade>(Supplier<T> constructor,
                                                     MapCodec<T> codec,
                                                     FeatureFlagSet requiredFeatures,
                                                     Predicate<ItemStack> itemstackFilter
) implements FeatureElement {

    public static final ResourceKey<Registry<RoomUpgradeType<?>>> REGISTRY_KEY = ResourceKey.createRegistryKey(CompactMachines.modRL("room_upgrades"));

    public static <T extends RoomUpgrade> Builder<T> builder(Supplier<T> constructor, MapCodec<T> codec) {
        return new Builder<>(constructor, codec);
    }

    public boolean canApplyTo(ItemStack item) {
        return itemstackFilter == null || itemstackFilter.test(item);
    }

    public static class Builder<T extends RoomUpgrade> {
        private final Supplier<T> constructor;
        private final MapCodec<T> codec;
        private FeatureFlagSet requiredFeatures;
        private Predicate<ItemStack> itemPredicate;

        public Builder(Supplier<T> constructor, MapCodec<T> codec) {
            this.constructor = constructor;
            this.codec = codec;
            this.requiredFeatures = FeatureFlags.DEFAULT_FLAGS;
        }

        public Builder<T> requiredFeatures(FeatureFlagSet featureFlagSet) {
            this.requiredFeatures = featureFlagSet;
            return this;
        }

        public Builder<T> itemPredicate(Predicate<ItemStack> predicate) {
            this.itemPredicate = predicate;
            return this;
        }

        public RoomUpgradeType<T> build() {
            return new RoomUpgradeType<>(constructor, codec, requiredFeatures, itemPredicate);
        }

    }
}