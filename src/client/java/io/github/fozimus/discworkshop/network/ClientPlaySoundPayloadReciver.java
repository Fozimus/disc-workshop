package io.github.fozimus.discworkshop.network;

import java.io.IOException;
import java.nio.file.Path;

import org.apache.commons.codec.digest.DigestUtils;

import io.github.fozimus.discworkshop.DiscWorkshop;
import io.github.fozimus.discworkshop.audio.AudioDownloader;
import io.github.fozimus.discworkshop.audio.ClientAudioHandler;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;

public class ClientPlaySoundPayloadReciver {
    public static void register(PlaySoundPayload payload, ClientPlayNetworking.Context context) {
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
                    AudioDownloader.downloadAudio(client, url, fileName, (success) -> {
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
    }
}
