package io.github.fozimus.discworkshop.sound;

import io.github.fozimus.discworkshop.init.ComponentTypesInit;
import io.github.fozimus.discworkshop.network.PlaySoundPayload;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;


public class DiscSound {

    private static int JUKEBOX_RANGE = 64;
    
    public static void stop(ServerWorld world, BlockPos pos) {
        for (ServerPlayerEntity player : PlayerLookup.world((ServerWorld)world)) {
            ServerPlayNetworking.send(player, new PlaySoundPayload(pos, "", false));
        }
    }

    public static void play(ServerWorld world, ItemStack stack, BlockPos pos) {
        String url = stack.get(ComponentTypesInit.DISC_URL);

        if (url == null) return;
        
        for (ServerPlayerEntity player : PlayerLookup.world((ServerWorld)world)) {
            if (player.getPos().distanceTo(pos.toCenterPos()) < JUKEBOX_RANGE) {
                ServerPlayNetworking.send(player, new PlaySoundPayload(pos, url, false));                
            }
        }
    }
    
}
