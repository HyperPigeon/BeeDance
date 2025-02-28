package net.hyper_pigeon.beedance.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.hyper_pigeon.beedance.interfaces.BeeDancing;
import net.hyper_pigeon.beedance.networking.BeeDancingNetworkingConstants;
import net.hyper_pigeon.beedance.networking.BeeDancingPayload;
import net.minecraft.entity.passive.BeeEntity;

@net.fabricmc.api.Environment(net.fabricmc.api.EnvType.CLIENT)
public class BeeDanceClient implements ClientModInitializer {


    @Override
    public void onInitializeClient() {
        PayloadTypeRegistry.playS2C().register(BeeDancingPayload.PACKET_ID, BeeDancingPayload.PACKET_CODEC);

        ClientPlayNetworking.registerGlobalReceiver(BeeDancingPayload.PACKET_ID, (payload, context) -> {
            int entityId = payload.entityId();
            boolean isDancing = payload.isDancing();
            assert context.player().getWorld() != null;
            BeeEntity beeEntity = (BeeEntity) context.player().getWorld().getEntityById(entityId);
            BeeDancing dancingBee = (BeeDancing) beeEntity;
            if(dancingBee != null)
                dancingBee.setDancing(isDancing);
        });
    }
}
