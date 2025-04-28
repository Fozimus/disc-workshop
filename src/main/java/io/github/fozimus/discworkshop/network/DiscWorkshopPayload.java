package io.github.fozimus.discworkshop.network;

import io.github.fozimus.discworkshop.DiscWorkshop;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.math.BlockPos;

public record DiscWorkshopPayload(BlockPos pos, String url) implements CustomPayload {
    public static final Id<DiscWorkshopPayload> ID = new Id<>(DiscWorkshop.id("disc_workshop_payload"));
    public static final PacketCodec<RegistryByteBuf, DiscWorkshopPayload> PACKET_CODEC =
        PacketCodec.tuple(BlockPos.PACKET_CODEC, DiscWorkshopPayload::pos,
                          PacketCodecs.STRING, DiscWorkshopPayload::url,
                          DiscWorkshopPayload::new);
    
	@Override
        public Id<? extends CustomPayload> getId() {
        return ID;
	}
    
}
