package io.github.fozimus.discworkshop;

import java.io.IOException;

import io.github.fozimus.discworkshop.audio.AudioDownloader;
import io.github.fozimus.discworkshop.command.AudioCacheCommand;
import io.github.fozimus.discworkshop.config.ClientConfig;
import io.github.fozimus.discworkshop.init.BlockEntityTypeInit;
import io.github.fozimus.discworkshop.init.ScreenHandlerTypeInit;
import io.github.fozimus.discworkshop.network.ClientPlaySoundPayloadReciver;
import io.github.fozimus.discworkshop.network.ClientUrlPayloadReciver;
import io.github.fozimus.discworkshop.network.PlaySoundPayload;
import io.github.fozimus.discworkshop.network.UrlPayload;
import io.github.fozimus.discworkshop.renderer.DiscWorkshopBlockEntityRenderer;
import io.github.fozimus.discworkshop.screen.DiscWorkshopScreen;
import io.github.fozimus.discworkshop.tooltip.MusicDiscTooltip;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

public class DiscWorkshopClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {        
        BlockEntityRendererFactories.register(BlockEntityTypeInit.DISC_WORKSHOP_BLOCK_ENTITY, DiscWorkshopBlockEntityRenderer::new);

        HandledScreens.register(ScreenHandlerTypeInit.DISC_WORKSHOP, DiscWorkshopScreen::new);

        ClientCommandRegistrationCallback.EVENT.register(AudioCacheCommand::register);

        ClientPlayNetworking.registerGlobalReceiver(UrlPayload.ID, ClientUrlPayloadReciver::register);
        ClientPlayNetworking.registerGlobalReceiver(PlaySoundPayload.ID, ClientPlaySoundPayloadReciver::register);

        ItemTooltipCallback.EVENT.register(MusicDiscTooltip::register);

        ClientConfig.init();
        
        try {
            AudioDownloader.checkExecutables();
        }
        catch (IOException e){
            DiscWorkshop.LOGGER.error("Error while downloading executables for {}: {}", DiscWorkshop.MOD_ID, e.getMessage());
        }
	}
}
