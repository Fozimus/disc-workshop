package io.github.fozimus.discworkshop.data.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import org.joml.Vector3f;

import com.google.common.base.Suppliers;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import io.github.fozimus.discworkshop.DiscWorkshop;
import io.github.fozimus.discworkshop.init.ComponentTypesInit;
import net.minecraft.client.item.ItemModelManager;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.render.item.model.BasicItemModel;
import net.minecraft.client.render.item.model.ItemModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.BakedQuadFactory;
import net.minecraft.client.render.model.BakedSimpleModel;
import net.minecraft.client.render.model.Baker;
import net.minecraft.client.render.model.ModelRotation;
import net.minecraft.client.render.model.ModelSettings;
import net.minecraft.client.render.model.ModelTextures;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteContents;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class MusicDiscItemModel implements ItemModel {
    public class MusicDiscSprite extends Sprite {
        private final List<Integer> m_pattern;
        
		protected MusicDiscSprite(Identifier atlasId, SpriteContents contents, int atlasWidth, int atlasHeight, int x,
				int y) {
			super(atlasId, contents, atlasWidth, atlasHeight, x, y);
            m_pattern = null;
		}
 
        public MusicDiscSprite(Sprite sprite, List<Integer> pattern) {
			super(
                  sprite.getAtlasId(),
                  sprite.getContents(),
                  (int)(sprite.getX() / sprite.getMinU()),
                  (int)(sprite.getY() / sprite.getMinV()),
                  sprite.getX(),
                  sprite.getY()
                  );
            m_pattern = pattern;
		}

        public List<Integer> getPattern() {
            return m_pattern;
        }
    }
        
    private final List<BakedQuad> m_quads;
    private final Supplier<Vector3f[]> m_vector;
    private final ModelSettings m_settings;

    public MusicDiscItemModel(List<BakedQuad> quads, ModelSettings settings) {
        m_quads = quads;
        m_vector = Suppliers.memoize(() -> bakeQuads(quads));
        m_settings = settings;
    }
        
    @Override
    public void update(ItemRenderState state, ItemStack stack, ItemModelManager resolver,
                       ItemDisplayContext displayContext, ClientWorld world, LivingEntity user, int seed) {
        ItemRenderState.LayerRenderState layerRenderState = state.newLayer();
        if (stack.hasGlint()) {
            ItemRenderState.Glint glint = ItemRenderState.Glint.STANDARD;
            layerRenderState.setGlint(glint);
            state.markAnimated();
            state.addModelKey(glint);
        }

        List<Integer> pattern = stack.get(ComponentTypesInit.DISC_PATTERN);

        state.addModelKey(pattern);
        
        List<BakedQuad> quads = m_quads
            .stream()
            .map(quad -> 
                 new BakedQuad(
                               quad.vertexData(),
                               quad.tintIndex(),
                               quad.face(),
                               new MusicDiscSprite(quad.sprite(), pattern),
                               quad.shade(),
                               quad.lightEmission()
                               )
                 )
            .toList();

        layerRenderState.setVertices(m_vector);
        layerRenderState.setRenderLayer(RenderLayers.getItemLayer(stack));
        m_settings.addSettings(layerRenderState, displayContext);
        layerRenderState.getQuads().addAll(quads);
    }

    public static Vector3f[] bakeQuads(List<BakedQuad> quads) {
        Set<Vector3f> set = new HashSet<>();
            
        for (BakedQuad bakedQuad : quads) {
            BakedQuadFactory.calculatePosition(bakedQuad.vertexData(), set::add);
        }

        return (Vector3f[])set.toArray(Vector3f[]::new);
    }
    
    public record Unbaked(Identifier model) implements ItemModel.Unbaked {
        public static final MapCodec<MusicDiscItemModel.Unbaked> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                Identifier.CODEC.fieldOf("model").forGetter(MusicDiscItemModel.Unbaked::model)
            ).apply(instance, MusicDiscItemModel.Unbaked::new)
        );
        @Override            
        public void resolve(Resolver resolver) {
            resolver.markDependency(model);            
        }

        @Override
        public MapCodec<MusicDiscItemModel.Unbaked> getCodec() {
            return CODEC;
        }

        @Override
        public MusicDiscItemModel bake(BakeContext context) {
            Baker baker = context.blockModelBaker();
            BakedSimpleModel bakedSimpleModel = baker.getModel(model);
            ModelTextures modelTextures = bakedSimpleModel.getTextures();
            List<BakedQuad> list = bakedSimpleModel.bakeGeometry(modelTextures, baker, ModelRotation.X0_Y0).getAllQuads();
            ModelSettings modelSettings = ModelSettings.resolveSettings(baker, bakedSimpleModel, modelTextures);
            return new MusicDiscItemModel(list, modelSettings);
        }
    }
}
