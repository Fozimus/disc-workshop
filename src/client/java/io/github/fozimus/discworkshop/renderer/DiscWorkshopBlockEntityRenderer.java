package io.github.fozimus.discworkshop.renderer;

import java.util.Optional;

import org.joml.Quaternionfc;

import io.github.fozimus.discworkshop.block.DiscWorkshopBEBlock;
import io.github.fozimus.discworkshop.block.entity.DiscWorkshopBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Direction.Axis;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LightType;
import net.minecraft.world.World;

public class DiscWorkshopBlockEntityRenderer implements BlockEntityRenderer<DiscWorkshopBlockEntity> {

    public DiscWorkshopBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
        
    }

    private int getLigthLevel(World world, BlockPos pos) {
        int bLigth = world.getLightLevel(LightType.BLOCK, pos);
        int sLigth = world.getLightLevel(LightType.SKY, pos);
        return LightmapTextureManager.pack(bLigth, sLigth);
    }
    
	@Override
    public void render(DiscWorkshopBlockEntity entity, float tickDelta, MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers, int light, int overlay, Vec3d cameraPos) {
        ItemStack stack = entity.getCraftingResult();
        if (stack.isEmpty()) return;

        BlockState state = entity.getWorld().getBlockState(entity.getPos());
        Optional<Direction> dirOpt = state.getOrEmpty(DiscWorkshopBEBlock.FACING);

        if (dirOpt.isPresent()) {
            ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();            

            matrices.push();
            Direction dir = dirOpt.get().getOpposite();
            matrices.translate(0.5f, DiscWorkshopBEBlock.SHAPE.getMax(Axis.Y) + 1.f / 32.f, 0.5f);
            matrices.multiply(dir.getRotationQuaternion());
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180));
            

            matrices.translate(1.f/32.f, 0, 0);
            itemRenderer.renderItem(stack, ItemDisplayContext.GUI, getLigthLevel(entity.getWorld(), entity.getPos()),
                                    OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, entity.getWorld(), 1);
            
            matrices.pop();
        }
	}
}
