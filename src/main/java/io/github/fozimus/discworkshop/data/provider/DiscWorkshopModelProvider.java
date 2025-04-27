package io.github.fozimus.discworkshop.data.provider;

import io.github.fozimus.discworkshop.init.ItemInit;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;

public class DiscWorkshopModelProvider extends FabricModelProvider {

    public DiscWorkshopModelProvider(FabricDataOutput output) {
        super(output);
    }

	@Override
	public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        blockStateModelGenerator.register();
	}
    
	@Override
	public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ItemInit.BLANK_DISC, Models.GENERATED);
        itemModelGenerator.register(ItemInit.MUSIC_DISC, Models.GENERATED);
	}
    
}
