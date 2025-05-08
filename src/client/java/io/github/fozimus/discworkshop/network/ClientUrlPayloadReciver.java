package io.github.fozimus.discworkshop.network;

import io.github.fozimus.discworkshop.block.entity.DiscWorkshopBlockEntity;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.block.entity.BlockEntity;

public class ClientUrlPayloadReciver {
    public static void register(UrlPayload payload, ClientPlayNetworking.Context context) {
        BlockEntity be = context.player().getWorld().getBlockEntity(payload.pos());
        if (be instanceof DiscWorkshopBlockEntity discWorkshopBlockEntity) {
            discWorkshopBlockEntity.setUrl(payload.url());
        }

    }
}
