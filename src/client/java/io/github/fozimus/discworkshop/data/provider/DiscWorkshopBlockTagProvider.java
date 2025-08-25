package io.github.fozimus.discworkshop.data.provider;

import java.util.concurrent.CompletableFuture;

import io.github.fozimus.discworkshop.DiscWorkshop;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.registry.tag.BlockTags;

public class DiscWorkshopBlockTagProvider extends FabricTagProvider.BlockTagProvider {

    public DiscWorkshopBlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }
    
	@Override
	protected void configure(WrapperLookup wrapperLookup) {
        getTagBuilder(BlockTags.AXE_MINEABLE)
            .add(DiscWorkshop.id("disc_workshop"));
	}

}
