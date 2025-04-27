package io.github.fozimus.discworkshop.data.provider;

import java.util.concurrent.CompletableFuture;

import io.github.fozimus.discworkshop.DiscWorkshop;
import io.github.fozimus.discworkshop.init.BlockInit;
import io.github.fozimus.discworkshop.init.ItemInit;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;

public class DiscWorkshopRecipeProvider extends FabricRecipeProvider{

    public DiscWorkshopRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }
    
	@Override
	public void generate(RecipeExporter exporter) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ItemInit.BLANK_DISC)
            .input('S', Items.STONE)
            .input('I', Items.IRON_INGOT)
            .pattern(" S ")
            .pattern("SIS")
            .pattern(" S ")
            .criterion(hasItem(Items.STONE), conditionsFromItem(Items.STONE))
            .criterion(hasItem(Items.IRON_INGOT), conditionsFromItem(Items.IRON_INGOT))
            .offerTo(exporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, BlockInit.DISC_WORKSHOP_BE_BLOCK)
            .input('P', ItemTags.PLANKS)
            .input('E', Items.EMERALD)
            .pattern("PPP")
            .pattern("PEP")
            .pattern("PPP")
            .criterion(hasItem(Items.CRAFTING_TABLE), conditionsFromItem(Items.CRAFTING_TABLE))
            .criterion(hasItem(Items.EMERALD), conditionsFromItem(Items.EMERALD))
            .offerTo(exporter);

	}

}
