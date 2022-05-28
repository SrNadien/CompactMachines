package dev.compactmods.machines.client.shader;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import dev.compactmods.machines.CompactMachines;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.io.IOException;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = CompactMachines.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CM4Shaders
{
    private static ShaderInstance blockFullbrightShader;
    private static ShaderInstance wallShader;

    @SubscribeEvent
    public static void registerShaders(final RegisterShadersEvent ev) throws IOException
    {
        ev.registerShader(
                new ShaderInstance(ev.getResourceManager(), new ResourceLocation(CompactMachines.MOD_ID, "block_fullbright"), DefaultVertexFormat.BLOCK),
                shader -> blockFullbrightShader = shader
        );

        ev.registerShader(
                new ShaderInstance(ev.getResourceManager(), new ResourceLocation(CompactMachines.MOD_ID, "wall"), DefaultVertexFormat.BLOCK),
                shader -> wallShader = shader
        );
    }

    public static ShaderInstance wall() { return wallShader; }
    public static ShaderInstance fullbright()
    {
        return blockFullbrightShader;
    }
}
