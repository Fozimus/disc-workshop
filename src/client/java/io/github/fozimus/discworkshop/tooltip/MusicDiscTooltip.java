package io.github.fozimus.discworkshop.tooltip;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import org.apache.commons.codec.digest.DigestUtils;

import io.github.fozimus.discworkshop.DiscWorkshop;
import io.github.fozimus.discworkshop.audio.AudioDownloader;
import io.github.fozimus.discworkshop.audio.ClientAudioHandler;
import io.github.fozimus.discworkshop.init.ComponentTypesInit;
import io.github.fozimus.discworkshop.init.ItemInit;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.Formatting;

public class MusicDiscTooltip {
    private static HashSet<String> failedDownloads = new HashSet<>();
    
    public final static void register(ItemStack stack, Item.TooltipContext context, TooltipType type, List<Text> lines) {
        if (stack.getItem().equals(ItemInit.MUSIC_DISC)) {
            String url = stack.get(ComponentTypesInit.DISC_URL);
            String fileName = DigestUtils.sha256Hex(url);
            Path filePath = AudioDownloader.DOWNLOAD_FOLDER.resolve(fileName + ".ogg");

            if (url.isEmpty()) return;

            if (!ClientAudioHandler.descriptions.containsKey(fileName)) {
                ClientAudioHandler.fetchDescription(AudioDownloader.DOWNLOAD_FOLDER.resolve(fileName));
            }

            ListIterator<Text> linesIterator = lines.listIterator();

            while (linesIterator.hasNext()) {
                Text text = linesIterator.next();
                if (text.getContent() instanceof TranslatableTextContent translatableTextContent) {
                    if (translatableTextContent.getKey().equals("item." + DiscWorkshop.MOD_ID + ".music_disc.desc")) {
                        linesIterator.remove();
                        
                        if (!filePath.toFile().exists()) {
                            if (failedDownloads.contains(url)) {
                                linesIterator.add(Text.literal("Download failed").formatted(Formatting.RED));
                            }
                            else if (!AudioDownloader.downloadCallbacks.containsKey(url)) {
                                try {
                                    AudioDownloader.downloadAudio(url, fileName, success -> {
                                            if (!success) {
                                                failedDownloads.add(url);
                                            }
                                        });                                    
                                }
                                catch (IOException e) {
                                    DiscWorkshop.LOGGER.error("Error while downloading \"{}\": {}", url, e.getMessage());
                                }
                            }
                            else {
                                linesIterator.add(Text.literal("Downloading"));                                
                            }
                            return;
                        }                        
                        else if (!AudioDownloader.downloadCallbacks.containsKey(url)) {
                            ClientAudioHandler.fetchDescription(filePath);                            
                        }
                        linesIterator.add(Text.literal(ClientAudioHandler.descriptions.getOrDefault(fileName + ".ogg", "")));
                    }
                }

            }
        }
    }
}
