package io.github.fozimus.discworkshop.data.provider;

import java.util.List;

import io.github.fozimus.discworkshop.data.model.MusicDiscItemModel;
import io.github.fozimus.discworkshop.init.ItemInit;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.minecraft.client.data.BlockStateModelGenerator;
import net.minecraft.client.data.ItemModelGenerator;
import net.minecraft.client.data.Models;
import net.minecraft.client.render.item.model.BasicItemModel;

public class DiscWorkshopModelProvider extends FabricModelProvider {       
    public DiscWorkshopModelProvider(FabricDataOutput output) {
        super(output);
    }

	@Override
	public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {

	}
    
	@Override
	public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ItemInit.BLANK_DISC, Models.GENERATED);
        itemModelGenerator.output.accept(
            ItemInit.MUSIC_DISC,
            new MusicDiscItemModel.Unbaked(itemModelGenerator.upload(ItemInit.MUSIC_DISC, Models.GENERATED))
        );
	}

    @Override
    public String getName() {
        return "DiscWorkshop Model Provider";
    }
}
