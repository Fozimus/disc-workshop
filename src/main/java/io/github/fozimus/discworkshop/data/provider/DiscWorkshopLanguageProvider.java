package io.github.fozimus.discworkshop.data.provider;

import java.util.concurrent.CompletableFuture;

import org.jetbrains.annotations.NotNull;

import io.github.fozimus.discworkshop.DiscWorkshop;
import io.github.fozimus.discworkshop.block.entity.DiscWorkshopBlockEntity;
import io.github.fozimus.discworkshop.init.BlockInit;
import io.github.fozimus.discworkshop.init.ItemGroupInit;
import io.github.fozimus.discworkshop.init.ItemInit;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;

public class DiscWorkshopLanguageProvider extends FabricLanguageProvider {

    public DiscWorkshopLanguageProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    private static void addText(@NotNull TranslationBuilder builder, @NotNull Text text, @NotNull String value) {
        if (text.getContent() instanceof TranslatableTextContent translatableTextContent) {
            builder.add(translatableTextContent.getKey(), value);
        }
        else {
            DiscWorkshop.LOGGER.warn("Failed to add translation for text: {}", text.getString());
        }
    }
    
	@Override
	public void generateTranslations(WrapperLookup registryLookup, TranslationBuilder translationBuilder) {
        translationBuilder.add(ItemInit.MUSIC_DISC, "Music disc");
        translationBuilder.add(ItemInit.BLANK_DISC, "Blank disc");
        translationBuilder.add(BlockInit.DISC_WORKSHOP_BE_BLOCK, "Disc workshop");
        addText(translationBuilder, DiscWorkshopBlockEntity.TITLE, "Disc workshop");
        addText(translationBuilder, ItemGroupInit.DISC_WORKSHOP_GROUP_TITLE, "Disc Workshop");

        addText(translationBuilder, Text.translatable("item.discworkshop.music_disc.desc"), "Empty");
        addText(translationBuilder, Text.translatable("text.autoconfig.discworkshop.option.quality"), "Quality");
        addText(translationBuilder, Text.translatable("text.autoconfig.discworkshop.option.quality.@Tooltip"), "Sets the downloaded audio quality. (LOW = 48K, MEDIUM = 96K, HIGH = 128K)");
	}	
}
