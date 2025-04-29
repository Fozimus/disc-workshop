package io.github.fozimus.discworkshop.mixin.client;


import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.github.fozimus.discworkshop.DiscWorkshop;
import io.github.fozimus.discworkshop.init.ComponentTypesInit;
import io.github.fozimus.discworkshop.init.ItemInit;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper.Argb;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;

//TODO: use a proper Custom model instead of this hack
// https://wiki.fabricmc.net/tutorial:custom_model
@Mixin(ItemRenderer.class)
public class ItemRendererMixin {

    private void renderQuadColor(VertexConsumer vertices, MatrixStack.Entry entry, BakedQuad quad, int color, int light, int overlay) {
        float r = Argb.getRed(color) / 255.0F;
        float g = Argb.getGreen(color) / 255.0F;
        float b = Argb.getBlue(color) / 255.0F;
        float a = Argb.getAlpha(color) / 255.0F;
        vertices.quad(entry, quad, r, g, b, a, light, overlay);
    }
    
	private void renderBakedItemQuads_(MatrixStack matrices, VertexConsumer vertices, List<BakedQuad> quads, ItemStack stack, int light, int overlay) {
        List<Integer> pattern = stack.get(ComponentTypesInit.DISC_PATTERN);

		for (BakedQuad bakedQuad : quads) {

            Identifier texture = bakedQuad.getSprite().getContents().getId();

            if (texture.equals(DiscWorkshop.id("item/music_disc_pixel"))) {

                int i = 0;
                for (int row = 0; row < 3; ++row) {
                    for (int col = 0; col < 5; ++col) {
                        if ((row == 0 || row == 2) && (col == 0 || col == 4)) continue;
                        if (row == 1 && col == 2) continue;
                        matrices.push();
                        matrices.translate(col * 1.f / 16.f, -row * 1.f / 16.f, 0);
                        matrices.scale(1, 1, 0.999f);

                        int color = Colors.WHITE;
                        if (pattern.size() > i) {
                            color = pattern.get(i);
                        }
                        MatrixStack.Entry entry = matrices.peek();
                
                        renderQuadColor(vertices, entry, bakedQuad, color, light, overlay);
                        matrices.pop();
                        i += 1;
                    }
                }
                
                
            } else {
                MatrixStack.Entry entry = matrices.peek();
                int color = Colors.WHITE;
                renderQuadColor(vertices, entry, bakedQuad, color, light, overlay);
            }            
            
		}
	}
    
    private void renderBakedItemModel_(BakedModel model, ItemStack stack, int light, int overlay, MatrixStack matrices, VertexConsumer vertices) {
		Random random = Random.create();
		long l = 42L;

		for (Direction direction : Direction.values()) {
			random.setSeed(42L);
			this.renderBakedItemQuads_(matrices, vertices, model.getQuads(null, direction, random), stack, light, overlay);
		}

		random.setSeed(42L);
		this.renderBakedItemQuads_(matrices, vertices, model.getQuads(null, null, random), stack, light, overlay);
	}


    @Inject(at = @At("HEAD"), method = "renderItem", cancellable = true)
    public void renderItem(
                           ItemStack stack,
                           ModelTransformationMode renderMode,
                           boolean leftHanded,
                           MatrixStack matrices,
                           VertexConsumerProvider vertexConsumers,
                           int light,
                           int overlay,
                           BakedModel model,
                           CallbackInfo ci) {
        if (stack.getItem().equals(ItemInit.MUSIC_DISC)) {
            matrices.push();
            boolean bl = renderMode == ModelTransformationMode.GUI || renderMode == ModelTransformationMode.GROUND || renderMode == ModelTransformationMode.FIXED;

            model.getTransformation().getTransformation(renderMode).apply(leftHanded, matrices);
            matrices.translate(-0.5F, -0.5F, -0.5F);

            RenderLayer renderLayer = RenderLayers.getItemLayer(stack, true);
            VertexConsumer vertexConsumer;

            vertexConsumer = ItemRenderer.getDirectItemGlintConsumer(vertexConsumers, renderLayer, true, stack.hasGlint());

            this.renderBakedItemModel_(model, stack, light, overlay, matrices, vertexConsumer);

            matrices.pop();
            ci.cancel();
        }
    }
}
