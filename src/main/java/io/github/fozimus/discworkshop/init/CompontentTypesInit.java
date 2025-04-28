package io.github.fozimus.discworkshop.init;

import java.util.function.UnaryOperator;

import com.mojang.serialization.Codec;

import io.github.fozimus.discworkshop.DiscWorkshop;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Style.Codecs;
import net.minecraft.util.math.BlockPos;

public class CompontentTypesInit {

    public final static ComponentType<String> DISC_URL = register("disc_url", builder -> builder.codec(Codec.STRING));
    
    private static <T> ComponentType<T> register(String name, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, DiscWorkshop.id(name), builderOperator.apply(ComponentType.builder()).build());
    }

    //TODO: add Logger messages
    public static void init() {}
}
