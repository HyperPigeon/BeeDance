package net.hyper_pigeon.beedance.networking;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public record BeeDancingPayload(int entityId, boolean isDancing) implements CustomPayload {
    public static final CustomPayload.Id<BeeDancingPayload> PACKET_ID = new CustomPayload.Id<>(BeeDancingNetworkingConstants.BEE_DANCING);
    public static final PacketCodec<RegistryByteBuf, BeeDancingPayload> PACKET_CODEC = CustomPayload.codecOf(BeeDancingPayload::write,BeeDancingPayload::new);


    private BeeDancingPayload(PacketByteBuf buf) {
        this(buf.readInt(), buf.readBoolean());
    }

    public BeeDancingPayload(int entityId, boolean isDancing){
        this.entityId = entityId;
        this.isDancing = isDancing;
    }

    private void write(RegistryByteBuf registryByteBuf) {
        registryByteBuf.writeInt(entityId);
        registryByteBuf.writeBoolean(isDancing);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }
}
