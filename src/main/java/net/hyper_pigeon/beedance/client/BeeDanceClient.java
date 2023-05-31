package net.hyper_pigeon.beedance.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.hyper_pigeon.beedance.interfaces.BeeDancing;
import net.hyper_pigeon.beedance.networking.BeeDancingNetworkingConstants;
import net.minecraft.entity.passive.BeeEntity;

@net.fabricmc.api.Environment(net.fabricmc.api.EnvType.CLIENT)
public class BeeDanceClient implements ClientModInitializer {


    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(BeeDancingNetworkingConstants.BEE_DANCING, (client, handler, buf, responseSender) -> {
            int entityId = buf.readInt();
            boolean isDancing = buf.readBoolean();
            assert client.world != null;
            BeeEntity beeEntity = (BeeEntity) client.world.getEntityById(entityId);
            BeeDancing dancingBee = (BeeDancing) beeEntity;
            assert dancingBee != null;
            dancingBee.setDancing(isDancing);
        });
    }
}
