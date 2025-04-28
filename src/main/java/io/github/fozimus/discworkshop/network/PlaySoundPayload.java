package io.github.fozimus.discworkshop.network;

import io.github.fozimus.discworkshop.DiscWorkshop;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.math.BlockPos;

public record PlaySoundPayload(BlockPos position, String url, boolean loop) implements CustomPayload {
    public static final Id<PlaySoundPayload> ID = new Id<>(DiscWorkshop.id("play_sound_payload"));
    public static final PacketCodec<RegistryByteBuf, PlaySoundPayload> PACKET_CODEC =
        PacketCodec.tuple(BlockPos.PACKET_CODEC, PlaySoundPayload::position,
                          PacketCodecs.STRING, PlaySoundPayload::url,
                          PacketCodecs.BOOL, PlaySoundPayload::loop,
                          PlaySoundPayload::new);
    
	@Override
	public Id<? extends CustomPayload> getId() {
        return ID;
	}    
}
