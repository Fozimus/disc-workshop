// package io.github.fozimus.discworkshop.data.provider;

// import java.util.Collection;
// import java.util.List;
// import java.util.function.Function;

// import io.github.fozimus.discworkshop.DiscWorkshop;
// import net.fabricmc.fabric.api.renderer.v1.Renderer;
// import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
// import net.fabricmc.fabric.api.renderer.v1.mesh.Mesh;
// import net.fabricmc.fabric.api.renderer.v1.mesh.MeshBuilder;
// import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
// import net.minecraft.block.BlockState;
// import net.minecraft.client.render.model.BakedModel;
// import net.minecraft.client.render.model.BakedQuad;
// import net.minecraft.client.render.model.Baker;
// import net.minecraft.client.render.model.ModelBakeSettings;
// import net.minecraft.client.render.model.UnbakedModel;
// import net.minecraft.client.render.model.json.ModelOverrideList;
// import net.minecraft.client.render.model.json.ModelTransformation;
// import net.minecraft.client.texture.Sprite;
// import net.minecraft.client.util.SpriteIdentifier;
// import net.minecraft.screen.PlayerScreenHandler;
// import net.minecraft.util.Identifier;
// import net.minecraft.util.math.Direction;
// import net.minecraft.util.math.random.Random;

// public class MusicDiscModel implements UnbakedModel, BakedModel {

//     private static final SpriteIdentifier[] SPRITE_IDS = new SpriteIdentifier[] {
//         new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, DiscWorkshop.id("item/music_disc"))
//     };
//     private final Sprite[] sprites = new Sprite[SPRITE_IDS.length];

//     private Mesh mesh;
    
// 	@Override
// 	public Collection<Identifier> getModelDependencies() {
// 		return List.of();
// 	}

// 	@Override
// 	public void setParents(Function<Identifier, UnbakedModel> modelLoader) {
		
// 	}

// 	@Override
// 	public BakedModel bake(Baker baker, Function<SpriteIdentifier, Sprite> textureGetter,
// 			ModelBakeSettings rotationContainer) {
// 		for (int i = 0; i < SPRITE_IDS.length; ++i) {
//             sprites[i] = textureGetter.apply(SPRITE_IDS[i]);
//         }

//         Renderer renderer = RendererAccess.INSTANCE.getRenderer();

//         MeshBuilder builder = renderer.meshBuilder();
// 	}

// 	@Override
// 	public List<BakedQuad> getQuads(BlockState state, Direction face, Random random) {
// 		// TODO Auto-generated method stub
// 		throw new UnsupportedOperationException("Unimplemented method 'getQuads'");
// 	}

// 	@Override
// 	public boolean useAmbientOcclusion() {
// 		// TODO Auto-generated method stub
// 		throw new UnsupportedOperationException("Unimplemented method 'useAmbientOcclusion'");
// 	}

// 	@Override
// 	public boolean hasDepth() {
// 		// TODO Auto-generated method stub
// 		throw new UnsupportedOperationException("Unimplemented method 'hasDepth'");
// 	}

// 	@Override
// 	public boolean isSideLit() {
// 		// TODO Auto-generated method stub
// 		throw new UnsupportedOperationException("Unimplemented method 'isSideLit'");
// 	}

// 	@Override
// 	public boolean isBuiltin() {
// 		// TODO Auto-generated method stub
// 		throw new UnsupportedOperationException("Unimplemented method 'isBuiltin'");
// 	}

// 	@Override
// 	public Sprite getParticleSprite() {
// 		// TODO Auto-generated method stub
// 		throw new UnsupportedOperationException("Unimplemented method 'getParticleSprite'");
// 	}

// 	@Override
// 	public ModelTransformation getTransformation() {
// 		// TODO Auto-generated method stub
// 		throw new UnsupportedOperationException("Unimplemented method 'getTransformation'");
// 	}

// 	@Override
// 	public ModelOverrideList getOverrides() {
// 		// TODO Auto-generated method stub
// 		throw new UnsupportedOperationException("Unimplemented method 'getOverrides'");
// 	}

	
// }
