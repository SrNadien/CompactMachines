package dev.compactmods.machines.client.level;

import dev.compactmods.machines.CompactMachines;
import dev.compactmods.machines.advancement.GenericAdvancementTriggerListener;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.TickingBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.EmptyLevelChunk;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.ticks.TickContainerAccess;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Taken from Immersive Engineering's manual code.
 * Source: https://github.com/BluSunrize/ImmersiveEngineering/blob/1.18.2/src/main/java/blusunrize/immersiveengineering/common/util/fakeworld/TemplateChunk.java
 */
public class TemplateChunk extends EmptyLevelChunk {
    private final Map<BlockPos, BlockState> blocksInChunk;
    private final Map<BlockPos, BlockEntity> tiles;
    private final Predicate<BlockPos> shouldShow;
    private final Map<BlockPos, BlockEntityTicker<BlockEntity>> tickers;

    public TemplateChunk(RenderingLevel worldIn, ChunkPos chunkPos, Map<BlockPos, StructureTemplate.StructureBlockInfo> blocksInChunk, Predicate<BlockPos> shouldShow) {
        super(worldIn, chunkPos, worldIn.getUncachedNoiseBiome(0, 0, 0));
        this.shouldShow = shouldShow;
        this.blocksInChunk = new HashMap<>();

        tiles = new HashMap<>();
        tickers = new HashMap<>();

        for (var pos : blocksInChunk.keySet()) {
            final var blockInfo = blocksInChunk.get(pos);
            this.blocksInChunk.put(pos, blockInfo.state());

            if(blockInfo.nbt() != null) {
                BlockEntity tile = BlockEntity.loadStatic(blockInfo.pos(), blockInfo.state(), blockInfo.nbt());
                if (tile != null) {
                    tile.setLevel(worldIn);
                    tile.setBlockState(blockInfo.state());
                    tiles.put(blockInfo.pos(), tile);
                    tile.onLoad();

                    if(blockInfo.state().getBlock() instanceof EntityBlock eb) {
                        final BlockEntityTicker<?> ticker = eb.getTicker(worldIn, blockInfo.state(), tile.getType());
                        if(ticker != null)
                            this.tickers.put(pos, (BlockEntityTicker<BlockEntity>) ticker);
                    }
                }
            }
        }
    }

    @Override
    public int getHeight() {
        return super.getHeight();
    }

    public void tick() {
        this.tickers.forEach((pos, ticker) -> ticker.tick(this.getLevel(), pos, getBlockState(pos), tiles.get(pos)));
    }

    @Nonnull
    @Override
    public BlockState getBlockState(@Nonnull BlockPos pos) {
        if (shouldShow.test(pos)) {
            var state = blocksInChunk.get(pos);
            if (state != null)
                return state;
        }

        return Blocks.VOID_AIR.defaultBlockState();
    }

    @Nonnull
    @Override
    public FluidState getFluidState(@Nonnull BlockPos pos) {
        return getBlockState(pos).getFluidState();
    }


    @Nullable
    @Override
    public BlockEntity getBlockEntity(@Nonnull BlockPos pos, @Nonnull EntityCreationType creationMode) {
        if (!shouldShow.test(pos))
            return null;
        return tiles.get(pos);
    }

}