package dev.compactmods.machines.room.network;

import dev.compactmods.machines.api.core.Constants;
import dev.compactmods.machines.util.VersionUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;

public class RoomNetworkHandler {

    private static final ArtifactVersion ROOM_TRACK_VERSION = new DefaultArtifactVersion("2.0.0");

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(Constants.MOD_ID, "room_tracking"),
            ROOM_TRACK_VERSION::toString,
            clientVer -> VersionUtil.checkMajor(clientVer, ROOM_TRACK_VERSION),
            serverVer -> VersionUtil.checkMajor(serverVer, ROOM_TRACK_VERSION)
    );

    public static void setupMessages() {
        CHANNEL.messageBuilder(OpenMachinePreviewScreenPacket.class, 1, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(OpenMachinePreviewScreenPacket::toNetwork)
                .decoder(OpenMachinePreviewScreenPacket::fromNetwork)
                .consumerMainThread(OpenMachinePreviewScreenPacket::handle)
                .add();
    }
}
