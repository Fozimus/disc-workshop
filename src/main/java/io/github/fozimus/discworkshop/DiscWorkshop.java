package io.github.fozimus.discworkshop;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.fozimus.discworkshop.init.BlockEntityTypeInit;
import io.github.fozimus.discworkshop.init.BlockInit;
import io.github.fozimus.discworkshop.init.ItemGroupInit;
import io.github.fozimus.discworkshop.init.ItemInit;
import io.github.fozimus.discworkshop.init.ScreenHandlerTypeInit;

public class DiscWorkshop implements ModInitializer {
	public static final String MOD_ID = "discworkshop";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
        ItemInit.init();
        BlockInit.init();
        ItemGroupInit.init();
        BlockEntityTypeInit.init();
        ScreenHandlerTypeInit.init();
	}

    public static Identifier id(String path) {
        return Identifier.of(MOD_ID, path);
    }
}
