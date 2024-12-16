package dev.compactmods.machines.room.client;

import com.mojang.blaze3d.platform.InputConstants;
import dev.compactmods.gander.level.VirtualLevel;
import dev.compactmods.gander.render.baked.BakedLevel;
import dev.compactmods.gander.ui.widget.SpatialRenderer;
import dev.compactmods.machines.client.gui.widget.PSDIconButton;
import dev.compactmods.machines.shrinking.Shrinking;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;

public class MachineRoomScreen extends Screen {

    private final GlobalPos machinePos;
    private final ChunkPos room;

    protected double rotateX = 45.0f;
    protected double rotateY = 20.0f;
    private PSDIconButton psdButton;

    private BakedLevel scene;
    private SpatialRenderer renderer;
    private Component sceneSource;

    public MachineRoomScreen(Component title, GlobalPos machinePos, ChunkPos room) {
        super(title);
        this.machinePos = machinePos;
        this.room = room;
    }

    @Override
    protected void init() {
        super.init();

        final var guiWidth = 260;
        final var guiHeight = 240;
        final var left = (width / 2) - (guiWidth / 2);
        final var top = (height / 2) - (guiHeight / 2);

        this.renderer = this.addRenderableWidget(new SpatialRenderer(left, top, guiWidth, guiHeight));
        this.psdButton = addRenderableWidget(new PSDIconButton(machinePos, room, left + 220, top + 210));
        if (hasPsdItem())
            this.psdButton.setEnabled(true);

        updateSceneRenderers();
    }

    private void updateSceneRenderers() {
        if (this.scene != null) {
            renderer.setData(scene);
        }
    }

    private boolean hasPsdItem() {
        return minecraft.player.getInventory().contains(new ItemStack(Shrinking.PERSONAL_SHRINKING_DEVICE.get()));
    }

    @Override
    public void tick() {
        super.tick();

        if (this.scene != null) {
            var level = ((VirtualLevel) scene.originalLevel().get());
            level.tick(minecraft.getPartialTick());
            level.animateTick();
        }

        if(psdButton != null)
            psdButton.setEnabled(minecraft.player.isCreative() || hasPsdItem());
    }

    @Override
    public boolean mouseDragged(double mx, double my, int mButton, double dx, double dy) {
        var s = super.mouseDragged(mx, my, mButton, dx, dy);
        if (!s) return false;

        rotateX += dx;
        rotateY += dy;
        return true;
    }

    @Override
    public boolean mouseScrolled(double p_94686_, double p_94687_, double p_94688_) {
        this.renderer.zoom(p_94688_);
        return super.mouseScrolled(p_94686_, p_94687_, p_94688_);
    }

    @Override
    public boolean keyPressed(int code, int scanCode, int modifiers) {
        final float rotateSpeed = 1 / 12f;

//        if (code == InputConstants.KEY_A) {
//            this.autoRotate = !autoRotate;
//            return true;
//        }

        if (code == InputConstants.KEY_R) {
            renderer.camera().resetLook();
            this.renderer.recalculateTranslucency();
            return true;
        }

        if (code == InputConstants.KEY_UP) {
            renderer.camera().lookUp(rotateSpeed);
            this.renderer.recalculateTranslucency();
            return true;
        }

        if (code == InputConstants.KEY_DOWN) {
            renderer.camera().lookDown(rotateSpeed);
            this.renderer.recalculateTranslucency();
            return true;
        }

        if (code == InputConstants.KEY_LEFT) {
            renderer.camera().lookLeft(rotateSpeed);
            this.renderer.recalculateTranslucency();
            return true;
        }

        if (code == InputConstants.KEY_RIGHT) {
            renderer.camera().lookRight(rotateSpeed);
            this.renderer.recalculateTranslucency();
            return true;
        }

        return super.keyPressed(code, scanCode, modifiers);
    }

    //    @Override
//    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
//        PoseStack pose = graphics.pose();
//        pose.pushPose();
//        pose.translate(0, 0, 500);
//
//        pose.translate(this.imageWidth / 2f, 0, 0);
//
//        var p = Component.literal(menu.getRoomName());
//        graphics.drawCenteredString(font, p, 0, this.titleLabelY, 0xFFFFFFFF);
//
//        var room = menu.getRoom();
//        var rt =  Component.literal("(%s, %s)".formatted(room.x, room.z));
//        pose.scale(0.8f, 0.8f, 0.8f);
//        graphics.drawCenteredString(font, rt, 0,this.titleLabelY + font.lineHeight + 2, 0xFFCCCCCC);
//        pose.popPose();
//    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partial) {
        this.renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, partial);
    }

    @Override
    public void renderBackground(GuiGraphics graphics) {
        super.renderBackground(graphics);
    }

    @Override
    public void removed() {
        super.removed();
        if (this.scene != null) {
            renderer.dispose();
            renderables.remove(renderer);
        }
    }

    public void setSceneSource(Component src) {
        this.sceneSource = src;
    }

    public void setScene(BakedLevel scene) {
        this.scene = scene;
        updateSceneRenderers();
    }
}
