package fr.minemobs.puffertweaks.object.blocks;

import fr.minemobs.puffertweaks.init.SoundList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.stats.Stats;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import java.util.stream.Stream;

public class BathroomSink extends HorizontalBlock {

    public static final IntegerProperty LEVEL = IntegerProperty.create("level", 0, 1);
    private static final VoxelShape INSIDE = Block.makeCuboidShape(4, 2, 4, 12, 5, 12);

    private static final VoxelShape SHAPE_N = Stream.of(
            Block.makeCuboidShape(4, 0, 3, 12, 5, 4),
            Block.makeCuboidShape(4, 0, 4, 12, 2, 12),
            Block.makeCuboidShape(7, 5, 13, 9, 8, 14),
            Block.makeCuboidShape(7, 8, 10, 9, 9, 14),
            Block.makeCuboidShape(7, 7.699999999999999, 10, 9, 8, 11),
            Block.makeCuboidShape(10, 5, 12, 11, 6, 13),
            Block.makeCuboidShape(5, 5, 12, 6, 6, 13),
            Block.makeCuboidShape(4, 0, 12, 12, 5, 14),
            Block.makeCuboidShape(3, 0, 3, 4, 5, 14),
            Block.makeCuboidShape(12, 0, 3, 13, 5, 14)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR)).get();

    private static final VoxelShape SHAPE_W = Stream.of(
            Block.makeCuboidShape(4, 0, 4, 12, 2, 12),
            Block.makeCuboidShape(12, 5, 10, 13, 6, 11),
            Block.makeCuboidShape(3, 2, 12, 14, 5, 13),
            Block.makeCuboidShape(3, 2, 3, 14, 5, 4),
            Block.makeCuboidShape(3, 2, 4, 4, 5, 12),
            Block.makeCuboidShape(12, 2, 4, 14, 5, 12),
            Block.makeCuboidShape(13, 5, 7, 14, 8, 9),
            Block.makeCuboidShape(10, 8, 7, 14, 9, 9),
            Block.makeCuboidShape(10, 7.699999999999999, 7, 11, 8, 9),
            Block.makeCuboidShape(12, 5, 5, 13, 6, 6),
            Block.makeCuboidShape(12, 1, 5, 13, 2, 11)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR)).get();

    private static final VoxelShape SHAPE_S = Stream.of(
            Block.makeCuboidShape(4, 0, 4, 12, 2, 12),
            Block.makeCuboidShape(10, 5, 3, 11, 6, 4),
            Block.makeCuboidShape(12, 2, 2, 13, 5, 13),
            Block.makeCuboidShape(3, 2, 2, 4, 5, 13),
            Block.makeCuboidShape(4, 2, 12, 12, 5, 13),
            Block.makeCuboidShape(4, 2, 2, 12, 5, 4),
            Block.makeCuboidShape(7, 5, 2, 9, 8, 3),
            Block.makeCuboidShape(7, 8, 2, 9, 9, 6),
            Block.makeCuboidShape(7, 7.699999999999999, 5, 9, 8, 6),
            Block.makeCuboidShape(5, 5, 3, 6, 6, 4),
            Block.makeCuboidShape(5, 1, 3, 11, 2, 4)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR)).get();

    private static final VoxelShape SHAPE_E = Stream.of(
            Block.makeCuboidShape(4, 0, 4, 12, 2, 12),
            Block.makeCuboidShape(3, 5, 5, 4, 6, 6),
            Block.makeCuboidShape(2, 2, 3, 13, 5, 4),
            Block.makeCuboidShape(2, 2, 12, 13, 5, 13),
            Block.makeCuboidShape(12, 2, 4, 13, 5, 12),
            Block.makeCuboidShape(2, 2, 4, 4, 5, 12),
            Block.makeCuboidShape(2, 5, 7, 3, 8, 9),
            Block.makeCuboidShape(2, 8, 7, 6, 9, 9),
            Block.makeCuboidShape(5, 7.699999999999999, 7, 6, 8, 9),
            Block.makeCuboidShape(3, 5, 10, 4, 6, 11),
            Block.makeCuboidShape(3, 1, 5, 4, 2, 11)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR)).get();

    private static final VoxelShape SHAPE_2 =
            VoxelShapes.combineAndSimplify(VoxelShapes.fullCube(), VoxelShapes.or(SHAPE_N, INSIDE), IBooleanFunction.ONLY_FIRST);

    public BathroomSink(Properties properties) {
        super(properties);
        this.setDefaultState(this.stateContainer.getBaseState()
                .with(LEVEL, Integer.valueOf(0))
                .with(HORIZONTAL_FACING, Direction.NORTH)
        );
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getRaytraceShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return INSIDE;
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        switch (state.get(HORIZONTAL_FACING)) {
            case NORTH:
                return SHAPE_N;
            case SOUTH:
                return SHAPE_S;
            case EAST:
                return SHAPE_E;
            case WEST:
                return SHAPE_W;
            default:
                return SHAPE_N;
        }
    }

    public void setWaterLevel(World worldIn, BlockPos pos, BlockState state, int level) {
        worldIn.setBlockState(pos, state.with(LEVEL, Integer.valueOf(MathHelper.clamp(level, 0, 3))), 2);
        worldIn.updateComparatorOutputLevel(pos, this);
    }

    @SuppressWarnings("deprecation")
    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        ItemStack itemstack = player.getHeldItem(handIn);
        if (itemstack.isEmpty()) {
            return ActionResultType.PASS;
        } else {
            int i = state.get(LEVEL);
            Item item = itemstack.getItem();
            if (item == Items.WATER_BUCKET) {
                if (i < 1 && !worldIn.isRemote) {
                    if (!player.abilities.isCreativeMode) {
                        player.setHeldItem(handIn, new ItemStack(Items.BUCKET));
                    }

                    player.addStat(Stats.FILL_CAULDRON);
                    this.setWaterLevel(worldIn, pos, state, 1);
                    worldIn.playSound((PlayerEntity) null, pos, SoundList.OPEN_FAUCET_SOUND.get(), SoundCategory.BLOCKS, 1.0F, 1.0F);
                }

                return ActionResultType.func_233537_a_(worldIn.isRemote);
            } else if (item == Items.BUCKET) {
                if (i == 1 && !worldIn.isRemote) {
                    if (!player.abilities.isCreativeMode) {
                        itemstack.shrink(1);
                        if (itemstack.isEmpty()) {
                            player.setHeldItem(handIn, new ItemStack(Items.WATER_BUCKET));
                        } else if (!player.inventory.addItemStackToInventory(new ItemStack(Items.WATER_BUCKET))) {
                            player.dropItem(new ItemStack(Items.WATER_BUCKET), false);
                        }
                    }

                    player.addStat(Stats.USE_CAULDRON);
                    this.setWaterLevel(worldIn, pos, state, 0);
                    worldIn.playSound((PlayerEntity) null, pos, SoundList.CLOSE_FAUCET_SOUND.get(), SoundCategory.BLOCKS, 1.0F, 1.0F);
                }
                return ActionResultType.func_233537_a_(worldIn.isRemote);
            }
        }
        return ActionResultType.PASS;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean hasComparatorInputOverride(BlockState state) {
        return true;
    }

    @SuppressWarnings("deprecation")
    @Override
    public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos) {
        return blockState.get(LEVEL);
    }

    @Override
    public void fillWithRain(World worldIn, BlockPos pos) {
        if ((worldIn.rand.nextInt(20) % 2) == 0) {
            float f = worldIn.getBiome(pos).getTemperature(pos);
            if (!(f < 0.15F)) {
                BlockState blockstate = worldIn.getBlockState(pos);
                if (blockstate.get(LEVEL) < 1) {
                    worldIn.setBlockState(pos, blockstate.func_235896_a_(LEVEL), 1);
                }

            }
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(HORIZONTAL_FACING, context.getPlacementHorizontalFacing().getOpposite());
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(LEVEL);
        builder.add(HORIZONTAL_FACING);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.with(HORIZONTAL_FACING, rot.rotate(state.get(HORIZONTAL_FACING)));
    }

    @SuppressWarnings("deprecation")
    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.toRotation(state.get(HORIZONTAL_FACING)));
    }
}
