package io.github.fozimus.discworkshop.tooltip;

import java.util.List;
import java.util.ListIterator;

import org.apache.commons.codec.digest.DigestUtils;

import io.github.fozimus.discworkshop.DiscWorkshop;
import io.github.fozimus.discworkshop.audio.AudioDownloader;
import io.github.fozimus.discworkshop.audio.ClientAudioHandler;
import io.github.fozimus.discworkshop.init.ComponentTypesInit;
import io.github.fozimus.discworkshop.init.ItemInit;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;

public class MusicDiscTooltip {
    public final static void register(ItemStack stack, Item.TooltipContext context, TooltipType type, List<Text> lines) {
        if (stack.getItem().equals(ItemInit.MUSIC_DISC)) {
            String url = stack.get(ComponentTypesInit.DISC_URL);
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
    }
}
