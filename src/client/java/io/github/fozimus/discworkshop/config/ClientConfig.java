package io.github.fozimus.discworkshop.config;

import io.github.fozimus.discworkshop.DiscWorkshop;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;

@Config(name = DiscWorkshop.MOD_ID)
public class ClientConfig implements ConfigData {

    @ConfigEntry.Gui.Excluded
    public static ClientConfig INSTANCE;
        
    public static void init() {
		AutoConfig.register(ClientConfig.class, JanksonConfigSerializer::new);
		INSTANCE = AutoConfig.getConfigHolder(ClientConfig.class).getConfig();
	}

    public enum AudioQuality {
        LOW("48K"),
        MEDIUM("96K"),
        HIGH("128K");

        public final String quality; 

        private AudioQuality (String quality) {
            this.quality = quality;
        }
        
    }

    @ConfigEntry.Gui.Tooltip()
    public AudioQuality quality = AudioQuality.MEDIUM;
}
