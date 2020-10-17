package com.robotgryphon.compactmachines.core;

import com.robotgryphon.compactmachines.CompactMachines;
import com.robotgryphon.compactmachines.block.BlockCompactMachine;
import com.robotgryphon.compactmachines.block.tiles.CompactMachineTile;
import com.robotgryphon.compactmachines.block.walls.BreakableWallBlock;
import com.robotgryphon.compactmachines.block.walls.SolidWallBlock;
import com.robotgryphon.compactmachines.block.walls.TunnelWallBlock;
import com.robotgryphon.compactmachines.item.ItemBlockMachine;
import com.robotgryphon.compactmachines.item.ItemBlockWall;
import com.robotgryphon.compactmachines.item.ItemPersonalShrinkingDevice;
import com.robotgryphon.compactmachines.reference.EnumMachineSize;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

import static com.robotgryphon.compactmachines.CompactMachines.MODID;

public class Registrations {
    // ================================================================================================================
    //   REGISTRIES
    // ================================================================================================================
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    private static final DeferredRegister<TileEntityType<?>> TILES_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, MODID);
    // private static final DeferredRegister<Dimension> DIMENSIONS = DeferredRegister.create(ForgeRegistries.WORLD_CARVERS, MODID);;

    // ================================================================================================================
    //   PROPERTIES
    // ================================================================================================================
    private static Block.Properties MACHINE_BLOCK_PROPS = Block.Properties
            .create(Material.IRON)
            .hardnessAndResistance(8.0F, 20.0F)
            .setLightLevel((state) -> 3)
            .harvestLevel(1)
            .harvestTool(ToolType.PICKAXE)
            .setRequiresTool();

    private static Supplier<Item.Properties> BASIC_ITEM_PROPS = () -> new Item.Properties()
            .group(CompactMachines.COMPACT_MACHINES_ITEMS);

    // ================================================================================================================
    //   COMPACT MACHINE BLOCKS
    // ================================================================================================================
    public static final RegistryObject<Block> MACHINE_BLOCK_TINY = BLOCKS.register("machine_tiny", () ->
            new BlockCompactMachine(EnumMachineSize.TINY, MACHINE_BLOCK_PROPS));

    public static final RegistryObject<Block> MACHINE_BLOCK_SMALL = BLOCKS.register("machine_small", () ->
            new BlockCompactMachine(EnumMachineSize.SMALL, MACHINE_BLOCK_PROPS));

    public static final RegistryObject<Block> MACHINE_BLOCK_NORMAL = BLOCKS.register("machine_normal", () ->
            new BlockCompactMachine(EnumMachineSize.NORMAL, MACHINE_BLOCK_PROPS));

    public static final RegistryObject<Block> MACHINE_BLOCK_LARGE = BLOCKS.register("machine_large", () ->
            new BlockCompactMachine(EnumMachineSize.LARGE, MACHINE_BLOCK_PROPS));

    public static final RegistryObject<Block> MACHINE_BLOCK_GIANT = BLOCKS.register("machine_giant", () ->
            new BlockCompactMachine(EnumMachineSize.GIANT, MACHINE_BLOCK_PROPS));

    public static final RegistryObject<Block> MACHINE_BLOCK_MAXIMUM = BLOCKS.register("machine_maximum", () ->
            new BlockCompactMachine(EnumMachineSize.MAXIMUM, MACHINE_BLOCK_PROPS));

    public static final RegistryObject<Item> MACHINE_BLOCK_ITEM_TINY = ITEMS.register("machine_tiny",
            () -> new ItemBlockMachine(MACHINE_BLOCK_TINY.get(), EnumMachineSize.TINY, BASIC_ITEM_PROPS.get()));

    public static final RegistryObject<Item> MACHINE_BLOCK_ITEM_SMALL = ITEMS.register("machine_small",
            () -> new ItemBlockMachine(MACHINE_BLOCK_SMALL.get(), EnumMachineSize.SMALL, BASIC_ITEM_PROPS.get()));

    public static final RegistryObject<Item> MACHINE_BLOCK_ITEM_NORMAL = ITEMS.register("machine_normal",
            () -> new ItemBlockMachine(MACHINE_BLOCK_NORMAL.get(), EnumMachineSize.NORMAL, BASIC_ITEM_PROPS.get()));

    public static final RegistryObject<Item> MACHINE_BLOCK_ITEM_LARGE = ITEMS.register("machine_large",
            () -> new ItemBlockMachine(MACHINE_BLOCK_LARGE.get(), EnumMachineSize.LARGE, BASIC_ITEM_PROPS.get()));

    public static final RegistryObject<Item> MACHINE_BLOCK_ITEM_GIANT = ITEMS.register("machine_giant",
            () -> new ItemBlockMachine(MACHINE_BLOCK_GIANT.get(), EnumMachineSize.GIANT, BASIC_ITEM_PROPS.get()));

    public static final RegistryObject<Item> MACHINE_BLOCK_ITEM_MAXIMUM = ITEMS.register("machine_maximum",
            () -> new ItemBlockMachine(MACHINE_BLOCK_MAXIMUM.get(), EnumMachineSize.MAXIMUM, BASIC_ITEM_PROPS.get()));

    public static final RegistryObject<TileEntityType<CompactMachineTile>> MACHINE_TILE_ENTITY = TILES_ENTITIES.register("compact_machine", () ->
            TileEntityType.Builder.create(CompactMachineTile::new,
                    MACHINE_BLOCK_TINY.get(), MACHINE_BLOCK_SMALL.get(), MACHINE_BLOCK_NORMAL.get(),
                    MACHINE_BLOCK_NORMAL.get(), MACHINE_BLOCK_GIANT.get(), MACHINE_BLOCK_MAXIMUM.get())
                    .build(null));

    // ================================================================================================================
    //   WALLS
    // ================================================================================================================
    public static final RegistryObject<Block> BLOCK_TUNNEL_WALL = BLOCKS.register("tunnel_wall", () ->
            new TunnelWallBlock(AbstractBlock.Properties
                    .create(Material.IRON, MaterialColor.CLAY)
                    .hardnessAndResistance(-1.0F, 3600000.8F)
                    .sound(SoundType.METAL)
                    .setLightLevel((state) -> 15)
                    .noDrops()));

    public static final RegistryObject<Block> BLOCK_SOLID_WALL = BLOCKS.register("solid_wall", () ->
            new SolidWallBlock(AbstractBlock.Properties
                    .create(Material.IRON, MaterialColor.CLAY)
                    .hardnessAndResistance(-1.0F, 3600000.8F)
                    .sound(SoundType.METAL)
                    .setLightLevel((state) -> 15)
                    .noDrops()));

    public static final RegistryObject<Block> BLOCK_BREAKABLE_WALL = BLOCKS.register("wall", () ->
            new BreakableWallBlock(Block.Properties
                    .create(Material.IRON)
                    .hardnessAndResistance(3.0f, 128.0f)
                    .setRequiresTool()
                    .harvestTool(ToolType.PICKAXE)
                    .harvestLevel(1)));

    public static final RegistryObject<ItemPersonalShrinkingDevice> PERSONAL_SHRINKING_DEVICE = ITEMS.register("personal_shrinking_device",
            () -> new ItemPersonalShrinkingDevice(BASIC_ITEM_PROPS.get()
                    .maxStackSize(1)));

    public static final RegistryObject<Item> ITEM_SOLID_WALL = ITEMS.register("solid_wall", () ->
            new ItemBlockWall(BLOCK_SOLID_WALL.get(), BASIC_ITEM_PROPS.get()));

    public static final RegistryObject<Item> ITEM_BREAKABLE_WALL = ITEMS.register("wall", () ->
            new ItemBlockWall(BLOCK_BREAKABLE_WALL.get(), BASIC_ITEM_PROPS.get()));

    // ================================================================================================================
    //   DIMENSION
    // ================================================================================================================
    public static final RegistryKey<World> COMPACT_DIMENSION = RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation("compactmachines:compact_world"));

    // ================================================================================================================
    //   INITIALIZATION
    // ================================================================================================================
    public static void init() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        BLOCKS.register(eventBus);
        ITEMS.register(eventBus);
        TILES_ENTITIES.register(eventBus);
    }
}
