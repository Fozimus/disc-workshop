
package io.github.fozimus.discworkshop.init;

import java.util.function.Function;
import java.util.stream.Stream;

import io.github.fozimus.discworkshop.DiscWorkshop;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

public class ItemInit {
    public static final Identifier MUSIC_DISC_ID = DiscWorkshop.id("music_disc");
    
    public static final Item MUSIC_DISC =
        register(MUSIC_DISC_ID, Item::new, new Item.Settings()
                                        .jukeboxPlayable(SoundEventInit.SOUND_KEY)
                                        .component(ComponentTypesInit.DISC_URL, "")
                                        .component(ComponentTypesInit.DISC_PATTERN,
                                                   Stream
                                                   .generate(DyeColor.WHITE::getEntityColor)
                                                   .limit(10)
                                                   .toList())
                                        .rarity(Rarity.RARE)
                                        .maxCount(1));
    
    
    public static final Identifier BLANK_DISC_ID = DiscWorkshop.id("blank_disc");
    public static final Item BLANK_DISC = register(BLANK_DISC_ID, Item::new, new Item.Settings());

    public static <T extends Item> T register
        (Identifier id,
         Function<T.Settings, T> itemFactory,
         T.Settings settings) {
        
        RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, id);
        T item = itemFactory.apply(settings.registryKey(itemKey));
        return Registry.register(Registries.ITEM, itemKey, item);
        
    }

    public static void init() {
        DiscWorkshop.LOGGER.info("Registering items for {}", DiscWorkshop.MOD_ID);
    }
}
