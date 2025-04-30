package io.github.fozimus.discworkshop.command;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;

import org.apache.commons.io.FileUtils;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import io.github.fozimus.discworkshop.DiscWorkshop;
import io.github.fozimus.discworkshop.audio.AudioDownloader;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.text.Text;

public class AudioCacheCommand {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
        dispatcher.register(ClientCommandManager.literal("audiocache")
                            .then(ClientCommandManager.literal("size")
                                  .executes(AudioCacheCommand::cacheSize))
                            .then(ClientCommandManager.literal("clear")
                                  .executes(AudioCacheCommand::clear)));
    }

    public static int cacheSize(CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {
        try {
            long size = Files.walk(AudioDownloader.DOWNLOAD_FOLDER)
                .map(path -> path.toFile().isDirectory() ? 0L : path.toFile().length())
                .reduce(0L, (total, fileSize) -> total + fileSize);

            if (size == 0) {
                context.getSource().sendFeedback(Text.literal(String.format("Cache folder is empty")));            
            }
            else {
                context.getSource().sendFeedback(Text.literal(String.format("Cache size: %s", FileUtils.byteCountToDisplaySize(size))));
            }
            return 1;        
		}
        catch (NoSuchFileException e) {
            context.getSource().sendFeedback(Text.literal(String.format("Cache folder is empty")));
            return 1;
        }
        catch (IOException e) {
            context.getSource().sendError(Text.literal("Error while calculating cache size"));
            DiscWorkshop.LOGGER.error("Error while calculating cache size: {}", e);
            return -1;
		}
    }

    
    public static int clear(CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {
        try {
            long size = Files.walk(AudioDownloader.DOWNLOAD_FOLDER)
                .map(path -> {
                        if (path.toFile().isDirectory()) return 0L;
                    
                        long fileSize = path.toFile().length();
                        if (path.toFile().delete()) {                        
                            return fileSize;
                        }
                    
                        DiscWorkshop.LOGGER.warn("Failed to delete {}", path);
                        return 0L;
                    })
                .reduce(0L, (total, fileSize) -> total + fileSize);

            if (size == 0) {
                context.getSource().sendFeedback(Text.literal(String.format("Cache folder is empty")));
            }
            else {                
                context.getSource().sendFeedback(Text.literal(String.format("Deleted %s of cache", FileUtils.byteCountToDisplaySize(size))));
            }
            return 1;        
		}
        catch (NoSuchFileException e) {
            context.getSource().sendFeedback(Text.literal(String.format("Cache folder is empty")));
            return 1;
        }
        catch (IOException e) {
            context.getSource().sendError(Text.literal("Error while deleting cache"));
            DiscWorkshop.LOGGER.error("Error while deleting cache: {}", e);
            return -1;
		}
    }
}
