package net.hyper_pigeon.beedance.networking.packets;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.EntityS2CPacket;

public class BeeDancingS2CPacket extends EntityS2CPacket {

    protected final boolean isDancing;

    protected BeeDancingS2CPacket(int entityId, short deltaX, short deltaY, short deltaZ, byte yaw, byte pitch, boolean onGround, boolean rotate, boolean positionChanged, boolean isDancing) {
        super(entityId, deltaX, deltaY, deltaZ, yaw, pitch, onGround, rotate, positionChanged);
        this.isDancing = isDancing;
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeVarInt(this.id);
        buf.writeShort(this.deltaX);
        buf.writeShort(this.deltaY);
        buf.writeShort(this.deltaZ);
        buf.writeBoolean(this.onGround);
        buf.writeBoolean(isDancing);
    }

}
