package io.github.fozimus.discworkshop.init;

import io.github.fozimus.discworkshop.DiscWorkshop;
import io.github.fozimus.discworkshop.network.BlockPosPayload;
import io.github.fozimus.discworkshop.screenhandler.DiscWorkshopScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;

public class ScreenHandlerTypeInit {
    public static final ScreenHandlerType<DiscWorkshopScreenHandler> DISC_WORKSHOP =
        register("disc_workshop", DiscWorkshopScreenHandler::new, BlockPosPayload.PACKET_CODEC);

    
    public static <T extends ScreenHandler, D extends CustomPayload>
        ExtendedScreenHandlerType<T, D> register(String name,
                                                 ExtendedScreenHandlerType.ExtendedFactory<T, D> factory,
                                                 PacketCodec<? super RegistryByteBuf, D> codec) {
        return Registry.register(Registries.SCREEN_HANDLER, DiscWorkshop.id(name), new ExtendedScreenHandlerType<>(factory, codec));
    }
    
    public static void init() {}
}
