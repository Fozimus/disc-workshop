package io.github.fozimus.discworkshop.init;

import io.github.fozimus.discworkshop.DiscWorkshop;
import io.github.fozimus.discworkshop.block.entity.DiscWorkshopBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class BlockEntityTypeInit {
    public static final BlockEntityType<DiscWorkshopBlockEntity> DISC_WORKSHOP_BLOCK_ENTITY =
        register("disc_workshop_block_entity", DiscWorkshopBlockEntity::new, BlockInit.DISC_WORKSHOP_BE_BLOCK);
    
    public static <T extends BlockEntity> BlockEntityType<T> register
        (String name,
         FabricBlockEntityTypeBuilder.Factory<? extends T> entityFactory,
         Block blocks) {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, DiscWorkshop.id(name),
                                 FabricBlockEntityTypeBuilder
                                 .<T>create(entityFactory, blocks)
                                 .build());
    }

    public static void init() {
        DiscWorkshop.LOGGER.info("Registering blockentities for {}", DiscWorkshop.MOD_ID);
    }
}
