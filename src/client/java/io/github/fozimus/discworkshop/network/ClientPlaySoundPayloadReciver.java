package io.github.fozimus.discworkshop.network;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.codec.digest.DigestUtils;

import io.github.fozimus.discworkshop.DiscWorkshop;
import io.github.fozimus.discworkshop.audio.AudioDownloader;
import io.github.fozimus.discworkshop.audio.ClientAudioHandler;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.block.entity.JukeboxBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;

public class ClientPlaySoundPayloadReciver {
    public static ConcurrentMap<Vec3d, String> toPlay = new ConcurrentHashMap<>();
    
    public static void register(PlaySoundPayload payload, ClientPlayNetworking.Context context) {
        MinecraftClient client = context.client();
        Vec3d position = payload.position().toCenterPos();
        String url = payload.url();
        String fileName = DigestUtils.sha256Hex(url);
        Path filePath = AudioDownloader.DOWNLOAD_FOLDER.resolve(fileName + ".ogg");
        Boolean loop = payload.loop();

        if (client.player == null) return;

        ClientAudioHandler.stopSoundAtPos(position);
        toPlay.remove(position);
        client.inGameHud.setOverlayMessage(Text.literal(""), false);

        if (!url.isBlank()) {
            if (!filePath.toFile().exists()) {
                toPlay.put(position, url);
                try {
                    if (!AudioDownloader.downloadCallbacks.containsKey(url)) {
                        MinecraftClient.getInstance().player.sendMessage(Text.literal("Downloading..."), true);                        
                    }
                    AudioDownloader.downloadAudio(url, fileName, (success) -> {
                            if (success) {
                                client.player.sendMessage(Text.literal("Download complete!").formatted(Formatting.GREEN), true);
                                ClientAudioHandler.fetchDescription(filePath);                                
                                if (client.world.getBlockEntity(payload.position()) instanceof JukeboxBlockEntity &&
                                    toPlay.getOrDefault(position, "").equals(url)) {
                                    ClientAudioHandler.playSound(fileName, position, loop);
                                }
                            } else {
                                client.player.sendMessage(Text.literal("Download failed!").formatted(Formatting.RED), true);
                            }
                            toPlay.remove(position);
                        });
                } catch (IOException e) {
                    DiscWorkshop.LOGGER.error("Error while downloading \"{}\": {}", url, e.getMessage());
                    return;
                }
            }
            else {
                ClientAudioHandler.fetchDescription(filePath);
                ClientAudioHandler.playSound(fileName, position, loop);
            }
        }
    }
}
