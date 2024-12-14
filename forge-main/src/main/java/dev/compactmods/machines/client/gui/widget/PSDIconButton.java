package dev.compactmods.machines.client.gui.widget;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.compactmods.machines.core.CompactMachinesNet;
import dev.compactmods.machines.room.client.MachineRoomScreen;
import dev.compactmods.machines.room.network.PlayerRequestedTeleportPacket;
import dev.compactmods.machines.shrinking.Shrinking;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraftforge.client.gui.widget.ExtendedButton;
import org.jetbrains.annotations.NotNull;

public class PSDIconButton extends ExtendedButton {

    private final GlobalPos machine;
    private final ChunkPos targetRoom;

    public PSDIconButton(GlobalPos machine, ChunkPos targetRoom, int xPos, int yPos) {
        super(xPos, yPos, 20, 22, Component.empty(), PSDIconButton::onClicked);
        this.machine = machine;
        this.targetRoom = targetRoom;
        this.active = false;
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.render(graphics, mouseX, mouseY, partialTicks);

        graphics.renderItem(new ItemStack(Shrinking.PERSONAL_SHRINKING_DEVICE.get()),
                getX() + 2, getY() + 2, 40);
    }

    private static void onClicked(Button button) {
        if (button.active && button instanceof PSDIconButton psd) {
            CompactMachinesNet.CHANNEL.sendToServer(new PlayerRequestedTeleportPacket(psd.machine, psd.targetRoom));
        }
    }

    public void setEnabled(boolean has) {
        this.active = has;
    }
}
