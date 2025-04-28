package io.github.fozimus.discworkshop.mixin.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.github.fozimus.discworkshop.DiscWorkshop;
import io.github.fozimus.discworkshop.audio.ClientAudioHandler;
import io.github.fozimus.discworkshop.audio.FileSound;
import net.minecraft.client.sound.AudioStream;
import net.minecraft.client.sound.OggAudioStream;
import net.minecraft.client.sound.RepeatingAudioStream;
import net.minecraft.client.sound.SoundLoader;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

@Mixin(SoundLoader.class)
public class SoundLoaderMixin {

    private static final String SOUND_PREFIX = "sounds/" + FileSound.ID_PREFIX;
    
    @Inject(at = @At("HEAD"), method = "loadStreamed", cancellable = true)
    public void loadStreamed(Identifier id, boolean repeatInstantly, CallbackInfoReturnable<CompletableFuture<AudioStream>> cir) {
        

        if (!id.getNamespace().equals(DiscWorkshop.MOD_ID) || !id.getPath().startsWith(SOUND_PREFIX)) return;

        
        cir.setReturnValue(CompletableFuture.supplyAsync(() -> {
                    try {
                        InputStream inputStream =
                            ClientAudioHandler.getAudioInputStream(id
                                                                   .getPath()
                                                                   .substring(SOUND_PREFIX.length()));
                        return (AudioStream)(repeatInstantly
                                             ? new RepeatingAudioStream(OggAudioStream::new, inputStream)
                                             : new OggAudioStream(inputStream));
                        
                    } catch (IOException e) {
                        DiscWorkshop.LOGGER.error("{}", e.getMessage());
                        throw new CompletionException(e);
                    }
                }, Util.getDownloadWorkerExecutor()));        
        cir.cancel();
    }
}
