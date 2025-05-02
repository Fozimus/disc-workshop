package io.github.fozimus.discworkshop.audio;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.jcraft.jorbis.JOrbisException;
import com.jcraft.jorbis.VorbisFile;

import io.github.fozimus.discworkshop.DiscWorkshop;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

public class ClientAudioHandler {
    public static HashMap<Vec3d, FileSound> playing = new HashMap<>();
    public static ConcurrentHashMap<String, String> descriptions = new ConcurrentHashMap<>();
    
    public static InputStream getAudioInputStream(String fileName) {
        try {
            return new FileInputStream(AudioDownloader.DOWNLOAD_FOLDER.resolve(fileName).toFile());
        }
        catch (IOException e) {
            DiscWorkshop.LOGGER.error("{}", e.getMessage());
            return null;
        }
    }

    public static void stopSoundAtPos(Vec3d position, MinecraftClient client) {
        FileSound currentSound = playing.get(position);

        if (currentSound != null) {
            client.getSoundManager().stop(currentSound);
            playing.remove(position);
        }

    }

    private static String getOggAttribute(Path file, String attribute) throws JOrbisException {
        VorbisFile vorbisFile = null;
        try {
            vorbisFile = new VorbisFile(file.toString());

            String metadata = vorbisFile.getComment(0).toString();

            String filter = "Comment: " + attribute + "=";
            return Arrays.stream(metadata.split("\n"))
                .filter(line -> line.startsWith(filter))
                .map(line -> line.substring(filter.length()))
                .findFirst()
                .orElse("N/A");

        } 
        finally {
            if (vorbisFile != null) {
                try {
					vorbisFile.close();
				} catch (IOException e) {
					DiscWorkshop.LOGGER.error("Error while closing vorbis file {}", e.getMessage());
				}
            }
        }
    }

    public static void fetchDescription(Path file) {
        if (descriptions.containsKey(file.getFileName().toString())) return;
        if (!file.toFile().exists()) return;
        try {
                
            descriptions.put(file.getFileName().toString(),
                             String.format("%s - %s",
                                           getOggAttribute(file, "artist"),
                                           getOggAttribute(file, "title")));
        }
        catch (JOrbisException e) {
            DiscWorkshop.LOGGER.warn("Failed to get ogg attributes: {}", e.getMessage());
        }
    }
    
    public static void playSound(MinecraftClient client, String fileName, Vec3d position, Boolean loop) {
        ClientAudioHandler.stopSoundAtPos(position, client);
        FileSound sound = new FileSound(fileName, position, loop);
        playing.put(position, sound);
        client.getSoundManager().play(sound);
        for (Entry<String, String> set : descriptions.entrySet()) {
            client.inGameHud.setRecordPlayingOverlay(Text.literal(descriptions.getOrDefault(fileName + ".ogg", "[No description]")));
        }
    }
}
