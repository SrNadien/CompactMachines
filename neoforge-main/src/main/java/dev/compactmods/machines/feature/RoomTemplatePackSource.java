package dev.compactmods.machines.feature;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.repository.PackSource;
import org.jetbrains.annotations.NotNull;

public record RoomTemplatePackSource(String id) implements PackSource {
    @Override
    public @NotNull Component decorate(@NotNull Component component) {
        return Component.translatable("pack.nameAndSource", component, Component.translatable("pack.source." + id))
                .withStyle(ChatFormatting.GRAY);
    }

    @Override
    public boolean shouldAddAutomatically() {
        return false;
    }
}
