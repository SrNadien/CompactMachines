package dev.compactmods.machines.compat.theoneprobe.elements;

import com.mojang.blaze3d.vertex.PoseStack;
import mcjty.theoneprobe.api.IElement;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class MachineTunnelElement implements IElement {
    @Override
    public void render(GuiGraphics graphics, int i, int i1) {

    }

    @Override
    public int getWidth() {
        return 0;
    }

    @Override
    public int getHeight() {
        return 0;
    }

    @Override
    public void toBytes(FriendlyByteBuf friendlyByteBuf) {

    }

    @Override
    public ResourceLocation getID() {
        return null;
    }
}
