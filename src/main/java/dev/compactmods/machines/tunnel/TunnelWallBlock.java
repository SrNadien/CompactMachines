package dev.compactmods.machines.tunnel;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import dev.compactmods.machines.api.core.Messages;
import dev.compactmods.machines.api.tunnels.TunnelDefinition;
import dev.compactmods.machines.api.tunnels.connection.ITunnelConnection;
import dev.compactmods.machines.api.tunnels.lifecycle.ITunnelTeardown;
import dev.compactmods.machines.api.tunnels.lifecycle.TeardownReason;
import dev.compactmods.machines.api.tunnels.redstone.IRedstoneReaderTunnel;
import dev.compactmods.machines.core.Capabilities;
import dev.compactmods.machines.core.Registration;
import dev.compactmods.machines.core.Tunnels;
import dev.compactmods.machines.util.TranslationUtil;
import dev.compactmods.machines.wall.ProtectedWallBlock;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;

@SuppressWarnings("deprecation")
public class TunnelWallBlock extends ProtectedWallBlock implements EntityBlock {
    public static final DirectionProperty TUNNEL_SIDE = DirectionProperty.create("tunnel_side", Direction.values());
    public static final DirectionProperty CONNECTED_SIDE = DirectionProperty.create("connected_side", Direction.values());

    public static final BooleanProperty REDSTONE = BooleanProperty.create("redstone");

    public TunnelWallBlock(Properties props) {
        super(props);
        registerDefaultState(getStateDefinition().any()
                .setValue(CONNECTED_SIDE, Direction.UP)
                .setValue(TUNNEL_SIDE, Direction.UP)
                .setValue(REDSTONE, false)
        );
    }

    public static Optional<TunnelDefinition> getTunnelInfo(BlockGetter world, BlockPos position) {
        TunnelWallEntity tile = (TunnelWallEntity) world.getBlockEntity(position);
        if (tile == null)
            return Optional.empty();

        return Optional.of(tile.getTunnelType());
    }

    @Override
    public boolean canConnectRedstone(BlockState state, BlockGetter world, BlockPos position, @Nullable Direction side) {
        return state.getValue(REDSTONE);
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return state.getValue(REDSTONE);
    }

    @Override
    public int getDirectSignal(BlockState state, BlockGetter world, BlockPos position, Direction side) {
        return 0;
    }

    @Override
    public int getSignal(BlockState state, BlockGetter world, BlockPos position, Direction side) {
        Optional<TunnelDefinition> tunnelInfo = getTunnelInfo(world, position);

        return tunnelInfo.map(definition -> {
            // TODO - Redstone tunnels
            if (definition instanceof IRedstoneReaderTunnel redstone) {
                return 0;
            }

            return 0;
        }).orElse(0);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(TUNNEL_SIDE).add(CONNECTED_SIDE).add(REDSTONE);
        super.createBlockStateDefinition(builder);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TunnelWallEntity(pos, state);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!(level instanceof ServerLevel serverLevel))
            return InteractionResult.SUCCESS;

        if (!(level.getBlockEntity(pos) instanceof TunnelWallEntity tunnel))
            return InteractionResult.FAIL;

        var def = tunnel.getTunnelType();
        final Direction tunnelWallSide = hitResult.getDirection();
        var tunnels = level.getChunkAt(pos).getCapability(Capabilities.ROOM_TUNNELS);

        if (player.isShiftKeyDown()) {
            BlockState solidWall = Registration.BLOCK_SOLID_WALL.get().defaultBlockState();

            level.setBlockAndUpdate(pos, solidWall);

            ItemStack stack = new ItemStack(Tunnels.ITEM_TUNNEL.get(), 1);
            CompoundTag defTag = stack.getOrCreateTagElement("definition");
            defTag.putString("id", def.getRegistryName().toString());

            ItemEntity ie = new ItemEntity(level, player.getX(), player.getY(), player.getZ(), stack);
            level.addFreshEntity(ie);

            if (def instanceof ITunnelTeardown teardown) {
                teardown.teardown(new TunnelPosition(serverLevel, pos, tunnelWallSide), tunnel.getTunnel(), TeardownReason.REMOVED);
            }

            tunnels.ifPresent(t -> t.unregister(pos));
        } else {
            // Rotate tunnel
            Direction dir = state.getValue(CONNECTED_SIDE);

            var next = tunnels.map(t -> {
                final Set<BlockPos> existing = t.stream(def).collect(Collectors.toSet());
                final Set<Direction> existingDirs = existing.stream()
                        .map(t::locatedAt)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .map(ITunnelConnection::side)
                        .collect(Collectors.toSet());

                final Optional<Direction> firstNotFound = TunnelHelper.getOrderedSides()
                        .filter(side -> !existingDirs.contains(side))
                        .findFirst();

                return firstNotFound.orElse(dir);
            }).orElse(dir);

            if (next == dir) {
                // WARN PLAYER - NO OTHER SIDES REMAIN
                player.displayClientMessage(
                        TranslationUtil.message(Messages.NO_TUNNEL_SIDE).withStyle(ChatFormatting.DARK_RED), true);

                return InteractionResult.FAIL;
            }

            level.setBlockAndUpdate(pos, state.setValue(CONNECTED_SIDE, next));

            if (def instanceof ITunnelTeardown teardown) {
                teardown.teardown(new TunnelPosition(serverLevel, pos, tunnelWallSide), tunnel.getTunnel(), TeardownReason.ROTATED);
            }

            var newTunn = def.newInstance(pos, tunnelWallSide);
            tunnel.setTunnel(newTunn);
        }

        return InteractionResult.SUCCESS;
    }
}
