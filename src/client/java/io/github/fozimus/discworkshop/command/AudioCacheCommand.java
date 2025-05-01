package io.github.fozimus.discworkshop.command;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import io.github.fozimus.discworkshop.DiscWorkshop;
import io.github.fozimus.discworkshop.audio.AudioDownloader;
import io.github.fozimus.discworkshop.audio.ClientAudioHandler;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.text.Text;
import oshi.util.FileUtil;

public class AudioCacheCommand {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
        dispatcher.register(ClientCommandManager.literal("audiocache")
                            .then(ClientCommandManager.literal("size")
                                  .executes(AudioCacheCommand::cacheSize))
                            .then(ClientCommandManager.literal("clear")
                                  .executes(AudioCacheCommand::clearCache))
                            .then(ClientCommandManager.literal("list")
                                  .executes(AudioCacheCommand::listCache)));
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

    
    public static int clearCache(CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {
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

        public static int listCache(CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {
        try {
            List<Path> paths = Files.walk(AudioDownloader.DOWNLOAD_FOLDER).toList();

            context.getSource().sendFeedback(Text.literal("Audio cache list:"));
            
            for (Path path : paths) {
                if (path.toFile().isDirectory()) continue;
                
                ClientAudioHandler.fetchDescription(path);

                String desc = ClientAudioHandler.descriptions.getOrDefault(path.getFileName().toString(), "N/A");
                String size = FileUtils.byteCountToDisplaySize(path.toFile().length());
                
                context.getSource().sendFeedback(Text.literal(String.format("%s: %s", desc, size)));
            }
                                   
            if (paths.size() == 0) {
                context.getSource().sendFeedback(Text.literal(String.format("Cache folder is empty")));
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
