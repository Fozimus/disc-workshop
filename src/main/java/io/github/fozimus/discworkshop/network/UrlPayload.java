package io.github.fozimus.discworkshop.network;

import io.github.fozimus.discworkshop.DiscWorkshop;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public record UrlPayload(String url, BlockPos pos) implements CustomPayload {
    public static final Identifier URL_PAYLOAD_ID = DiscWorkshop.id("url_payload");
    public static final CustomPayload.Id<UrlPayload> ID = new CustomPayload.Id<>(URL_PAYLOAD_ID);
    public static final PacketCodec<RegistryByteBuf, UrlPayload> PACKET_CODEC = PacketCodec.tuple(PacketCodecs.STRING, UrlPayload::url,
                                                                                                  BlockPos.PACKET_CODEC, UrlPayload::pos,
                                                                                                  UrlPayload::new);

	@Override
	public Id<? extends CustomPayload> getId() {
        return ID;
	}
    
}
