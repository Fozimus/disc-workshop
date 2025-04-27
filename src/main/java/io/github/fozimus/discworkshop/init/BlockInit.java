package io.github.fozimus.discworkshop.init;

import io.github.fozimus.discworkshop.DiscWorkshop;
import io.github.fozimus.discworkshop.block.DiscWorkshopBEBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;

public class BlockInit {
    public static final DiscWorkshopBEBlock DISC_WORKSHOP_BE_BLOCK =
        registerWithItem("disc_workshop", new DiscWorkshopBEBlock(AbstractBlock.Settings.create()
                                                                  .strength(2.0f, 6.0f)
                                                                  .mapColor(MapColor.DIRT_BROWN)
                                                                  .sounds(BlockSoundGroup.WOOD)));

    public static <T extends Block> T registerWithItem(String name, T block) {
        T registered = register(name, block);
        ItemInit.register(name, new BlockItem(registered, new Item.Settings()));
        return registered;
    }
    
    public static <T extends Block> T register(String name, T block) {
        return Registry.register(Registries.BLOCK, DiscWorkshop.id(name), block);
    }

    public static void init() {}
}
