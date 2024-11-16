package dev.compactmods.machines.feature;

import dev.compactmods.machines.api.CompactMachines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.neoforged.neoforge.event.AddPackFindersEvent;

public class CMFeaturePacks {

    public static void addFeaturePacks(final AddPackFindersEvent event) {
        addOptionalPack(event, CompactMachines.modRL("room_upgrades"), Component.literal("Compact Machines: Room Upgrades"));
    }

    private static void addOptionalPack(AddPackFindersEvent event, ResourceLocation packName, Component displayName) {
        event.addPackFinders(
                CompactMachines.modRL("data/" + packName.getNamespace() + "/datapacks/" + packName.getPath()),
                PackType.SERVER_DATA,
                displayName,
                PackSource.FEATURE,
                false,
                Pack.Position.TOP
        );
    }
}
