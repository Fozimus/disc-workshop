package io.github.fozimus.discworkshop;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.Identifier;

import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.fozimus.discworkshop.block.entity.DiscWorkshopBlockEntity;
import io.github.fozimus.discworkshop.init.BlockEntityTypeInit;
import io.github.fozimus.discworkshop.init.BlockInit;
import io.github.fozimus.discworkshop.init.ComponentTypesInit;
import io.github.fozimus.discworkshop.init.ItemGroupInit;
import io.github.fozimus.discworkshop.init.ItemInit;
import io.github.fozimus.discworkshop.init.ScreenHandlerTypeInit;
import io.github.fozimus.discworkshop.init.SoundEventInit;
import io.github.fozimus.discworkshop.network.PlaySoundPayload;
import io.github.fozimus.discworkshop.network.UrlPayload;

public class DiscWorkshop implements ModInitializer {
	public static final String MOD_ID = "discworkshop";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final Path MODPATH = FabricLoader.getInstance().getGameDir().resolve(MOD_ID);

	@Override
	public void onInitialize() {
        ComponentTypesInit.init();
        ItemInit.init();
        BlockInit.init();
        ItemGroupInit.init();
        BlockEntityTypeInit.init();
        ScreenHandlerTypeInit.init();
        SoundEventInit.init();

        PayloadTypeRegistry.playS2C().register(PlaySoundPayload.ID, PlaySoundPayload.PACKET_CODEC);
        PayloadTypeRegistry.playC2S().register(UrlPayload.ID, UrlPayload.PACKET_CODEC);
        
        ServerPlayNetworking.registerGlobalReceiver(UrlPayload.ID, (payload, context) -> {
                BlockEntity be = context.player().getWorld().getBlockEntity(payload.pos());
                if (be instanceof DiscWorkshopBlockEntity discWorkshopBlockEntity) {
                    discWorkshopBlockEntity.setUrl(payload.url());
                }
            });
	}

    public static Identifier id(String path) {
        return Identifier.of(MOD_ID, path);
    }
}
