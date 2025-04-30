package io.github.fozimus.discworkshop.network;

import io.github.fozimus.discworkshop.block.entity.DiscWorkshopBlockEntity;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.entity.BlockEntity;

public class UrlPayloadReciver {
    public static void register(UrlPayload payload, ServerPlayNetworking.Context context) {
        BlockEntity be = context.player().getWorld().getBlockEntity(payload.pos());
        if (be instanceof DiscWorkshopBlockEntity discWorkshopBlockEntity) {
            discWorkshopBlockEntity.setUrlFromClient(payload.url());
        }
    }
}
