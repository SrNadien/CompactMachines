package dev.compactmods.machines.room.upgrade.example;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.compactmods.machines.api.room.upgrade.RoomUpgrade;
import dev.compactmods.machines.api.room.upgrade.RoomUpgradeType;
import dev.compactmods.machines.feature.CMFeatureFlags;
import net.minecraft.network.chat.Component;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.function.Consumer;

public class RoomItemBufferUpgrade implements RoomUpgrade {

    private final ItemStackHandler inventory;

    public static final MapCodec<RoomItemBufferUpgrade> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
        ItemStackHandlerCodec.codec(9).fieldOf("inventory").forGetter(x -> x.inventory)
    ).apply(i, RoomItemBufferUpgrade::new));

    public RoomItemBufferUpgrade() {
        this.inventory = new ItemStackHandler(9);
    }

    public RoomItemBufferUpgrade(ItemStackHandler inventory) {
        this.inventory = inventory;
    }

    @Override
    public RoomUpgradeType<?> getType() {
        return null;
    }

    @Override
    public void addToTooltip(Item.TooltipContext tooltipContext, Consumer<Component> consumer, TooltipFlag tooltipFlag) {

    }
}
