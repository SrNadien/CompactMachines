package dev.compactmods.machines.tunnel.client;

import java.util.Optional;
import dev.compactmods.machines.api.tunnels.TunnelDefinition;
import dev.compactmods.machines.tunnel.TunnelItem;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.item.ItemStack;

public class TunnelItemColor implements ItemColor {
    @Override
    public int getColor(ItemStack stack, int tintIndex) {
        Optional<TunnelDefinition> definition = TunnelItem.getDefinition(stack);
        if(!definition.isPresent())
            return 0;

        TunnelDefinition actualDef = definition.get();
        if (tintIndex == 0) {
            return actualDef.ringColor();
        }

        return actualDef.indicatorColor();
    }
}
