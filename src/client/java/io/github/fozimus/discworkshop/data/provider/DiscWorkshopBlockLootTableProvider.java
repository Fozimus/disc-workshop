package io.github.fozimus.discworkshop.data.provider;

import java.util.concurrent.CompletableFuture;

import io.github.fozimus.discworkshop.init.BlockInit;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.registry.RegistryWrapper;

public class DiscWorkshopBlockLootTableProvider extends FabricBlockLootTableProvider {

    public DiscWorkshopBlockLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }
    
	@Override
	public void generate() {
        addDrop(BlockInit.DISC_WORKSHOP_BE_BLOCK);
	}

	
}
