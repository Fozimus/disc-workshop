package io.github.fozimus.discworkshop.init;

import java.util.function.Function;

import io.github.fozimus.discworkshop.DiscWorkshop;
import io.github.fozimus.discworkshop.block.DiscWorkshopBEBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockKeys;
import net.minecraft.block.MapColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class BlockInit {
    public static final DiscWorkshopBEBlock DISC_WORKSHOP_BE_BLOCK =
        registerWithItem("disc_workshop", DiscWorkshopBEBlock::new,
                         AbstractBlock.Settings.create()
                         .strength(2.0f, 6.0f)
                         .mapColor(MapColor.DIRT_BROWN)
                         .nonOpaque()
                         .sounds(BlockSoundGroup.WOOD));

    public static <T extends Block> T registerWithItem
        (String name,
         Function<T.Settings, T> blockFactory,
         T.Settings settings)
    {
        T registered = register(name, blockFactory, settings);
        
        Identifier id = DiscWorkshop.id(name);
        RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, id);

        BlockItem blockItem = new BlockItem(registered, new Item.Settings()
                                            .registryKey(itemKey));

        Registry.register(Registries.ITEM, itemKey, blockItem);
        
        return registered;
    }
    
    public static <T extends Block> T register
        (String name,
         Function<T.Settings, T> blockFactory,
         T.Settings settings)
    {
        Identifier id = DiscWorkshop.id(name);
        RegistryKey<Block> blockKey = RegistryKey.of(RegistryKeys.BLOCK, id);
        T block = blockFactory.apply(settings.registryKey(blockKey));
        return Registry.register(Registries.BLOCK, blockKey, block);
    }

    public static void init() {
        DiscWorkshop.LOGGER.info("Registering blocks for {}", DiscWorkshop.MOD_ID);
    }
}
