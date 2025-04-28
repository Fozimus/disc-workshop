package io.github.fozimus.discworkshop;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.codec.digest.DigestUtils;

import io.github.fozimus.discworkshop.audio.AudioDownloader;
import io.github.fozimus.discworkshop.audio.ClientAudioHandler;
import io.github.fozimus.discworkshop.init.CompontentTypesInit;
import io.github.fozimus.discworkshop.init.ItemInit;
import io.github.fozimus.discworkshop.init.ScreenHandlerTypeInit;
import io.github.fozimus.discworkshop.network.PlaySoundPayload;
import io.github.fozimus.discworkshop.screen.DiscWorkshopScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.item.ItemColorProvider;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.item.BannerItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;

public class DiscWorkshopClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
        HandledScreens.register(ScreenHandlerTypeInit.DISC_WORKSHOP, DiscWorkshopScreen::new);

        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
                return 0xFF0000;
            }, ItemInit.MUSIC_DISC);

        try {
            AudioDownloader.checkExecutables();
        }
        catch (IOException e){
            DiscWorkshop.LOGGER.error("Error while downloading executables for {}: {}", DiscWorkshop.MOD_ID, e.getMessage());
        }

        ItemTooltipCallback.EVENT.register((ItemStack stack, Item.TooltipContext context, TooltipType type, List<Text> lines) -> {
                if (stack.getItem().equals(ItemInit.MUSIC_DISC)) {
                    String url = stack.get(CompontentTypesInit.DISC_URL);
                    String filename = DigestUtils.sha256Hex(url) + ".ogg";

                    if (url.isEmpty()) return;

                    if (!ClientAudioHandler.descriptions.containsKey(filename)) {
                        ClientAudioHandler.fetchDescription(AudioDownloader.DOWNLOAD_FOLDER.resolve(filename));
                    }

                    ListIterator<Text> linesIterator = lines.listIterator();

                    while (linesIterator.hasNext()) {
                        Text text = linesIterator.next();
                        if (text.getContent() instanceof TranslatableTextContent translatableTextContent) {
                            if (translatableTextContent.getKey().equals("item." + DiscWorkshop.MOD_ID + ".music_disc.desc")) {
                                linesIterator.remove();
                                linesIterator.add(Text.literal(ClientAudioHandler.descriptions.getOrDefault(filename, "Play in jukebox to show description")));
                            }
                        }

                    }
                }
            });
        
        ClientPlayNetworking.registerGlobalReceiver(PlaySoundPayload.ID, (payload, context) -> {
                MinecraftClient client = context.client();
                Vec3d position = payload.position().toCenterPos();
                String url = payload.url();
                String fileName = DigestUtils.sha256Hex(url);
                Path filePath = AudioDownloader.DOWNLOAD_FOLDER.resolve(fileName + ".ogg");
                Boolean loop = payload.loop();

                if (client.player == null) return;

                ClientAudioHandler.stopSoundAtPos(position, client);
                client.inGameHud.setOverlayMessage(Text.literal(""), false);

                if (!url.isBlank()) {
                    if (!filePath.toFile().exists()) {
                        try {
                            AudioDownloader.downloadAudio(client, url, fileName)
                                .thenAccept((success) -> {
                                        if (success) {
                                            client.player.sendMessage(Text.literal("Download complete!").formatted(Formatting.GREEN), true);
                                            ClientAudioHandler.fetchDescription(filePath);
                                            ClientAudioHandler.playSound(client, fileName, position, loop);
                                        } else {
                                            client.player.sendMessage(Text.literal("Download failed!").formatted(Formatting.RED), true);
                                        }
                                    });
                        } catch (IOException e) {
                            DiscWorkshop.LOGGER.error("Error while downloading \"{}\": {}", url, e.getMessage());
                            return;
                        }
                    }
                    else {
                        ClientAudioHandler.fetchDescription(filePath);
                        ClientAudioHandler.playSound(client, fileName, position, loop);
                    }
                }
            });
	}
}
