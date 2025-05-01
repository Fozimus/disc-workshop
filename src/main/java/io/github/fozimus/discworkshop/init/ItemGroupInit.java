package io.github.fozimus.discworkshop.init;

import io.github.fozimus.discworkshop.DiscWorkshop;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;

public class ItemGroupInit {
    public static final Text DISC_WORKSHOP_GROUP_TITLE = Text.translatable("itemgroup." + DiscWorkshop.MOD_ID + ".disc_workshop");
    public static final ItemGroup DISC_WORKSHOP_GROUP = register("disc_workshop", FabricItemGroup.builder()
                                                                 .displayName(DISC_WORKSHOP_GROUP_TITLE)
                                                                 .icon(() -> new ItemStack(BlockInit.DISC_WORKSHOP_BE_BLOCK))
                                                                 .entries(ItemGroupInit::collectEntries)
                                                                 .build());

    public static <T extends ItemGroup> T register(String name, T itemGroup) {
        return Registry.register(Registries.ITEM_GROUP, DiscWorkshop.id(name), itemGroup);
    }

    private static void collectEntries(ItemGroup.DisplayContext displayContext, ItemGroup.Entries entries) {
        entries.add(new ItemStack(BlockInit.DISC_WORKSHOP_BE_BLOCK));
        entries.add(ItemInit.BLANK_DISC);
    }
        
    public static void init() {
        DiscWorkshop.LOGGER.info("Registering item groups for {}", DiscWorkshop.MOD_ID); 
    }
}
