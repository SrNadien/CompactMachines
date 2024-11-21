package dev.compactmods.machines.api.advancement;

import dev.compactmods.machines.api.CompactMachines;
import net.minecraft.resources.ResourceLocation;

/**
 * Holds references to advancements and grantables.
 */
public interface Advancements {
    /**
     * Granted when the player is teleported out by leaving room boundaries, or
     * when in an invalid state (such as trying to leave a machine room with no entry history)
     */
    ResourceLocation HOW_DID_YOU_GET_HERE = CompactMachines.modRL("how_did_you_get_here");

    /**
     * Root advancement. Required for vanilla's tree design.
     */
    ResourceLocation ROOT = CompactMachines.modRL("root");

    /**
     * Granted when a player first crafts machine items.
     */
    ResourceLocation FOUNDATIONS = CompactMachines.modRL("foundations");

    /**
     * Granted on first pickup of a PSD item.
     */
    ResourceLocation GOT_SHRINKING_DEVICE = CompactMachines.modRL("got_shrinking_device");

    /**
     * Granted if a player tries to enter a machine room they're currently in.
     */
    ResourceLocation RECURSIVE_ROOMS = CompactMachines.modRL("recursion");
}
