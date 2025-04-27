package io.github.fozimus.discworkshop.init;

import io.github.fozimus.discworkshop.DiscWorkshop;
import io.github.fozimus.discworkshop.block.DiscWorkshopBEBlock;
import io.github.fozimus.discworkshop.block.entity.DiscWorkshopBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class BlockEntityTypeInit {
    public static final BlockEntityType<DiscWorkshopBlockEntity> DISC_WORKSHOP_BLOCK_ENTITY = register("disc_workshop_block_entity",
                                                                                                       BlockEntityType.Builder
                                                                                                       .create(DiscWorkshopBlockEntity::new, BlockInit.DISC_WORKSHOP_BE_BLOCK)
                                                                                                       .build());

    public static <T extends BlockEntity> BlockEntityType<T> register(String name, BlockEntityType<T> type) {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, DiscWorkshop.id(name), type);
    }

    public static void init() {}
}
