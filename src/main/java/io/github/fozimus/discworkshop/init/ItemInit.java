
package io.github.fozimus.discworkshop.init;

import io.github.fozimus.discworkshop.DiscWorkshop;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Rarity;

public class ItemInit {
    public static final Item MUSIC_DISC = register("music_disc", new Item(new Item.Settings()
                                                                          .jukeboxPlayable(SoundEventInit.SOUND_KEY)
                                                                          .component(CompontentTypesInit.DISC_URL, "")
                                                                          .rarity(Rarity.RARE)
                                                                          .maxCount(1)));
    
    
    public static final Item BLANK_DISC = register("blank_disc", new Item(new Item.Settings()));

    public static <T extends Item> T register(String name, T item) {
        return Registry.register(Registries.ITEM, DiscWorkshop.id(name), item);
        
    }

    public static void init() {}
}
