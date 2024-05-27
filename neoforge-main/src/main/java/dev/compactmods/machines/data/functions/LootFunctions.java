package dev.compactmods.machines.data.functions;

import dev.compactmods.machines.CMRegistries;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.neoforged.neoforge.registries.DeferredHolder;

public class LootFunctions {

    public static DeferredHolder<LootItemFunctionType<?>, LootItemFunctionType<?>> COPY_ROOM_BINDING = CMRegistries.LOOT_FUNCTIONS
            .register("copy_room_binding", () -> new LootItemFunctionType<>(CopyRoomBindingFunction.CODEC));

    public static void prepare() {

    }
}
