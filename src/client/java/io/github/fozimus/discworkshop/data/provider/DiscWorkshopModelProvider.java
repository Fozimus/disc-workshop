package io.github.fozimus.discworkshop.data.provider;

import java.util.List;

import io.github.fozimus.discworkshop.DiscWorkshop;
import io.github.fozimus.discworkshop.data.model.MusicDiscItemModel;
import io.github.fozimus.discworkshop.init.ItemInit;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.minecraft.client.data.BlockStateModelGenerator;
import net.minecraft.client.data.ItemModelGenerator;
import net.minecraft.client.data.Models;
import net.minecraft.client.data.TextureMap;
import net.minecraft.client.render.item.model.BasicItemModel;
import net.minecraft.item.Item;

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
        Item item = ItemInit.MUSIC_DISC;
        itemModelGenerator.output.accept(
            item,
            new MusicDiscItemModel.Unbaked(Models.GENERATED_TWO_LAYERS.upload(
                item,
                TextureMap.layered(DiscWorkshop.id("item/music_disc"), DiscWorkshop.id("item/music_disc_pixel")),
                itemModelGenerator.modelCollector                
            ))
        );
	}

    @Override
    public String getName() {
        return "DiscWorkshop Model Provider";
    }
}
