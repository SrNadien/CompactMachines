package dev.compactmods.machines.room.network;

import dev.compactmods.machines.room.network.client.ClientRoomNetworkHandler;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record OpenMachinePreviewScreenPacket(ChunkPos roomChunk, StructureTemplate internalBlocks) {
    public void toNetwork(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeChunkPos(roomChunk);

        final var tag = internalBlocks.save(new CompoundTag());
        friendlyByteBuf.writeNbt(tag);
    }

    public static OpenMachinePreviewScreenPacket fromNetwork(FriendlyByteBuf friendlyByteBuf) {
        final var chunkPos = friendlyByteBuf.readChunkPos();
        final var blocks = new StructureTemplate();
        blocks.load(BuiltInRegistries.BLOCK.asLookup(), friendlyByteBuf.readNbt(NbtAccounter.UNLIMITED));

        return new OpenMachinePreviewScreenPacket(chunkPos, blocks);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> ClientRoomNetworkHandler.openRoomPreviewScreen(this));
        context.get().setPacketHandled(true);
    }
}
