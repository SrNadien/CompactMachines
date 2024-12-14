package dev.compactmods.machines.dimension;

import dev.compactmods.machines.api.dimension.CompactDimension;
import net.minecraft.network.protocol.game.ClientboundInitializeBorderPacket;
import net.minecraft.network.protocol.game.ClientboundSetBorderSizePacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.border.BorderChangeListener;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.LevelEvent;

import java.util.stream.Collectors;

public class WorldBorderFixer {

	public static void registerEvents() {
		MinecraftForge.EVENT_BUS.addListener(WorldBorderFixer::onWorldLoaded);
		MinecraftForge.EVENT_BUS.addListener(WorldBorderFixer::onPlayerLogin);
		MinecraftForge.EVENT_BUS.addListener(WorldBorderFixer::onPlayerDimChange);
	}

	private static void onWorldLoaded(final LevelEvent.Load evt) {
		if (evt.getLevel() instanceof ServerLevel compactDim && compactDim.dimension().equals(CompactDimension.LEVEL_KEY))
			WorldBorderFixer.sendWorldBorderListenerOverrides(compactDim.getServer(), compactDim);
	}

	private static void onPlayerLogin(final PlayerEvent.PlayerLoggedInEvent evt) {
		if (evt.getEntity() instanceof ServerPlayer sp && CompactDimension.isLevelCompact(sp.level()))
			WorldBorderFixer.sendClientWorldBorderFix(sp);
	}

	private static void onPlayerDimChange(final PlayerEvent.PlayerChangedDimensionEvent evt) {
		if (evt.getTo().equals(CompactDimension.LEVEL_KEY) && evt.getEntity() instanceof ServerPlayer sp)
			WorldBorderFixer.sendClientWorldBorderFix(sp);
	}

	private static void sendWorldBorderListenerOverrides(MinecraftServer serv, ServerLevel compactDim) {
		final var owBorder = serv.overworld().getWorldBorder();
		final var cwBorder = compactDim.getWorldBorder();

		// Filter border listeners down to the compact world, then remove them from the OW listener list
		final var listeners = owBorder.listeners.stream()
			.filter(border -> border instanceof BorderChangeListener.DelegateBorderChangeListener)
			.map(BorderChangeListener.DelegateBorderChangeListener.class::cast)
			.filter(list -> list.worldBorder == cwBorder)
			.collect(Collectors.toSet());

		for (var listener : listeners)
			owBorder.removeListener(listener);

		// Fix set compact world border if it was loaded weirdly
		cwBorder.setCenter(0, 0);
		cwBorder.setSize(WorldBorder.MAX_SIZE);

		// Send update to all players
		serv.getPlayerList().broadcastAll(new ClientboundSetBorderSizePacket(cwBorder), compactDim.dimension());
	}

	private static void sendClientWorldBorderFix(ServerPlayer player) {
		// Send a fake world border to the player instead of the "real" one in overworld
		player.connection.send(new ClientboundInitializeBorderPacket(new WorldBorder()));
	}
}
