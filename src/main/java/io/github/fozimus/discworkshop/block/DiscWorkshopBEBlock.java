package io.github.fozimus.discworkshop.block;

import com.mojang.serialization.MapCodec;

import io.github.fozimus.discworkshop.block.entity.DiscWorkshopBlockEntity;
import io.github.fozimus.discworkshop.init.BlockEntityTypeInit;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class DiscWorkshopBEBlock extends BlockWithEntity {
    public static final VoxelShape SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 11.0, 16.0);

	public static final DirectionProperty FACING = DirectionProperty.of("facing");
    public static final MapCodec<DiscWorkshopBEBlock> CODEC = DiscWorkshopBEBlock.createCodec(DiscWorkshopBEBlock::new);
    
    public DiscWorkshopBEBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity playerEntity, BlockHitResult hit) {
        if (!world.isClient) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            
            if (blockEntity instanceof DiscWorkshopBlockEntity discWorkshopBlockEntity && playerEntity != null) {
                playerEntity.openHandledScreen(discWorkshopBlockEntity);
            }
        }
        return ActionResult.success(world.isClient);
    }

	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return BlockEntityTypeInit.DISC_WORKSHOP_BLOCK_ENTITY.instantiate(pos, state);
	}

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        world.setBlockState(pos, state.with(FACING, Direction.fromRotation(placer.headYaw).getOpposite()));
        super.onPlaced(world, pos, state, placer, itemStack);
    }
    
    @Override
    protected void appendProperties(Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

	@Override
	protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
	}

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    protected void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof DiscWorkshopBlockEntity discWorkshopBlockEntity) {
                ItemScatterer.spawn(world, pos, discWorkshopBlockEntity);
                world.updateComparators(pos, this);
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }
}
