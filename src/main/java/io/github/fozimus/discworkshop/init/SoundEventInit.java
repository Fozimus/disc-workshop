package io.github.fozimus.discworkshop.init;

import io.github.fozimus.discworkshop.DiscWorkshop;
import net.minecraft.block.jukebox.JukeboxSong;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class SoundEventInit {
    public static final SoundEvent SOUND = register("sound");
    public static final RegistryKey<JukeboxSong> SOUND_KEY = RegistryKey.of(RegistryKeys.JUKEBOX_SONG, SOUND.getId());
    
    public static SoundEvent register(String name) {
        Identifier id = DiscWorkshop.id(name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void init() {
        DiscWorkshop.LOGGER.info("Registering sounds for {}", DiscWorkshop.MOD_ID);
    }
}
