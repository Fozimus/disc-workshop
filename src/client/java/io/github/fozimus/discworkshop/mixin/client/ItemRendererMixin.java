package io.github.fozimus.discworkshop.mixin.client;


import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.github.fozimus.discworkshop.DiscWorkshop;
import io.github.fozimus.discworkshop.data.model.MusicDiscItemModel.MusicDiscSprite;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;

//TODO: use a proper Custom model instead of this hack
// https://wiki.fabricmc.net/tutorial:custom_model
@Mixin(ItemRenderer.class)
abstract public class ItemRendererMixin {           
    @Shadow
    private static int getTint(int[] tints, int index) {
        throw new RuntimeException();		
	}
    
    @Inject(at = @At("HEAD"), method = "renderBakedItemQuads", cancellable = true)
    private static void renderBakedItemQuads(
        MatrixStack matrices,
        VertexConsumer vertexConsumer,
        List<BakedQuad> quads,
        int[] tints,
        int light,
        int overlay,
        CallbackInfo ci
    ) {
        if (!quads.isEmpty() && quads.getFirst().sprite() instanceof MusicDiscSprite musicDisc) {

            List<Integer> pattern = musicDisc.getPattern();
            
            for (BakedQuad bakedQuad : quads) {                
                Identifier texture = bakedQuad.sprite().getContents().getId();                

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

                            float r = ColorHelper.getRed(color) / 255.0F;
                            float g = ColorHelper.getGreen(color) / 255.0F;
                            float b = ColorHelper.getBlue(color) / 255.0F;
                            float a = ColorHelper.getAlpha(color) / 255.0F;
                            
                            vertexConsumer.quad(entry, bakedQuad, r, g, b, a, light, overlay);
                            matrices.pop();
                            i += 1;
                        }
                    }
                }
                else {
                    MatrixStack.Entry entry = matrices.peek();
                    float r = 1.0f;
                    float g = 1.0f;
                    float b = 1.0f;
                    float a = 1.0f;

                    vertexConsumer.quad(entry, bakedQuad, r, g, b, a, light, overlay);
                }                
            }

            ci.cancel();
        }
	}
}
