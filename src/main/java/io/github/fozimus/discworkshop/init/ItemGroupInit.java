package io.github.fozimus.discworkshop.init;

import java.util.Optional;

import io.github.fozimus.discworkshop.DiscWorkshop;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;

public class ItemGroupInit {
    public static final Text DISC_WORKSHOP_GROUP_TITLE = Text.translatable("itemgroup." + DiscWorkshop.MOD_ID + ".disc_workshop");
    public static final ItemGroup DISC_WORKSHOP_GROUP = register("disc_workshop", FabricItemGroup.builder()
                                                                 .displayName(DISC_WORKSHOP_GROUP_TITLE)
                                                                 .icon(ItemInit.MUSIC_DISC::getDefaultStack)
                                                                 .entries((displayContext, entries) ->
                                                                          Registries.ITEM.getIds()
                                                                          .stream()
                                                                          .filter(key -> key.getNamespace().equals(DiscWorkshop.MOD_ID))
                                                                          .map(Registries.ITEM::getOrEmpty)
                                                                          .map(Optional::orElseThrow)
                                                                          .forEach(entries::add))
                                                                 .build());

    public static <T extends ItemGroup> T register(String name, T itemGroup) {
        return Registry.register(Registries.ITEM_GROUP, DiscWorkshop.id(name), itemGroup);
    }

    public static void init() {}
}
