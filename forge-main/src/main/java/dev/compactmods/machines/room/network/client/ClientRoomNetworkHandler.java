package dev.compactmods.machines.room.network.client;

import dev.compactmods.gander.level.VirtualLevel;
import dev.compactmods.gander.render.baked.LevelBakery;
import dev.compactmods.machines.room.client.MachineRoomScreen;
import dev.compactmods.machines.room.network.OpenMachinePreviewScreenPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import org.joml.Vector3f;

public class ClientRoomNetworkHandler {
    public static void openRoomPreviewScreen(OpenMachinePreviewScreenPacket pkt) {
        final var mc = Minecraft.getInstance();
        mc.setScreen(new MachineRoomScreen(Component.empty(), pkt.machinePos(), pkt.roomChunk()));
        if(mc.screen instanceof MachineRoomScreen mrs) {
            var virtualLevel = new VirtualLevel(Minecraft.getInstance().level.registryAccess());
            var bounds = pkt.internalBlocks().getBoundingBox(new StructurePlaceSettings(), BlockPos.ZERO);
            virtualLevel.setBounds(bounds);
            pkt.internalBlocks().placeInWorld(virtualLevel, BlockPos.ZERO, BlockPos.ZERO, new StructurePlaceSettings().setKnownShape(true), RandomSource.create(), Block.UPDATE_CLIENTS);

            var bakedLevel = LevelBakery.bakeVertices(virtualLevel, bounds, new Vector3f());
            mrs.setScene(bakedLevel);
        }
    }
}
