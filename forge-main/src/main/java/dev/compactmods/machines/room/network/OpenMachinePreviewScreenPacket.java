package dev.compactmods.machines.room.network;

import dev.compactmods.machines.CompactMachines;
import dev.compactmods.machines.room.network.client.ClientRoomNetworkHandler;
import java.util.function.Supplier;

import net.minecraft.core.GlobalPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraftforge.network.NetworkEvent;

public record OpenMachinePreviewScreenPacket(GlobalPos machinePos, ChunkPos roomChunk, StructureTemplate internalBlocks) {
    public void toNetwork(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeGlobalPos(machinePos);
        friendlyByteBuf.writeChunkPos(roomChunk);

        final var tag = internalBlocks.save(new CompoundTag());
        friendlyByteBuf.writeNbt(tag);
    }

    public static OpenMachinePreviewScreenPacket fromNetwork(FriendlyByteBuf friendlyByteBuf) {
        final var machinePos = friendlyByteBuf.readGlobalPos();
        final var chunkPos = friendlyByteBuf.readChunkPos();
        final var blocks = new StructureTemplate();
        blocks.load(BuiltInRegistries.BLOCK.asLookup(), friendlyByteBuf.readNbt(NbtAccounter.UNLIMITED));

        return new OpenMachinePreviewScreenPacket(machinePos, chunkPos, blocks);
    }

    public static boolean handle(OpenMachinePreviewScreenPacket pkt, Supplier<NetworkEvent.Context> context) {
        //context.get().enqueueWork(() -> {
        try {
            CompactMachines.LOGGER.debug("Opening machine preview screen: {}", pkt.roomChunk);
            ClientRoomNetworkHandler.openRoomPreviewScreen(pkt);
        } catch (Throwable t) {
            t.printStackTrace();
        }
        // });

        return true;
    }
}
