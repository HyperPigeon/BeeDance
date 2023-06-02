package net.hyper_pigeon.beedance.mixin;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.hyper_pigeon.beedance.entity.ai.goal.HeadbuttGoal;
import net.hyper_pigeon.beedance.entity.ai.goal.LearnFlowerPosGoal;
import net.hyper_pigeon.beedance.entity.ai.goal.TellFlowerPosGoal;
import net.hyper_pigeon.beedance.interfaces.BeeDancing;
import net.hyper_pigeon.beedance.networking.BeeDancingNetworkingConstants;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BeeEntity.class)
public abstract class BeeEntityMixin extends AnimalEntity implements BeeDancing {

    //private static final TrackedData<Boolean> DANCING = DataTracker.registerData(BeeEntity.class, TrackedDataHandlerRegistry.BOOLEAN);


    private boolean dancing = false;

    private boolean learning = false;

    private boolean headbutting = false;
    private int headbuttingParticleTicks = 0;

    private boolean headbutted = false;
    private int headbuttedTicks = 0;


    protected BeeEntityMixin(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initGoals", at = @At("TAIL"))
    public void addGoals(CallbackInfo ci){
        this.goalSelector.add(6, new LearnFlowerPosGoal((BeeEntity)(Object)this));
        this.goalSelector.add(6, new TellFlowerPosGoal((BeeEntity)(Object)this));
        this.goalSelector.add(7,new HeadbuttGoal((BeeEntity) (Object)(this)));
    }

    @Override
    public void setDancing(boolean dancing) {
        this.dancing = dancing;

        if(!world.isClient()) {
            PacketByteBuf packetByteBuf = PacketByteBufs.create();
            packetByteBuf.writeInt(this.getId());
            packetByteBuf.writeBoolean(dancing);
            for (ServerPlayerEntity player : PlayerLookup.tracking((BeeEntity)(Object)(this))) {
                ServerPlayNetworking.send(player, BeeDancingNetworkingConstants.BEE_DANCING, packetByteBuf);
            }
        }
        //this.dataTracker.set(DANCING,dancing);
    }

    @Override
    public boolean isDancing() {
        return dancing;
        //return this.dataTracker.get(DANCING);
    }

    @Override
    public void setLearning(boolean learning){this.learning = learning;}

    @Override
    public boolean isLearning(){return learning;}

    @Override
    public void setHeadbutting(boolean headbutting){
        if(headbutting){
            this.headbuttingParticleTicks = 20;
        }
        else {
            this.headbuttingParticleTicks = 0;
        }
        this.headbutting = headbutting;
    }

    @Override
    public boolean isHeadbutting(){
        return headbutting;
    }

    @Override
    public void setHeadbutted(boolean headbutted){
        if(headbutted) {
            this.playSound(SoundEvents.ENTITY_BEE_HURT, 0.4F, 1.0F);
            this.headbuttedTicks = 200;
        }
        else {
            this.headbuttedTicks = 0;
        }
        this.headbutted = headbutted;
    }

    @Override
    public boolean getHeadbutted(){
        return headbutted;
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void tickHeadbutt(CallbackInfo ci){
        if(headbuttingParticleTicks > 0){
            headbuttingParticleTicks--;

            BeeEntity beeEntity = (BeeEntity)(Object)(this);
            double d = beeEntity.getRandom().nextGaussian() * 0.02;
            double e = beeEntity.getRandom().nextGaussian() * 0.02;
            double f = beeEntity.getRandom().nextGaussian() * 0.02;

            if(!beeEntity.getEntityWorld().isClient()) {
                ServerWorld serverWorld = (ServerWorld)(this.getEntityWorld());
                serverWorld.spawnParticles(ParticleTypes.ANGRY_VILLAGER,
                        beeEntity.getX(), beeEntity.getRandomBodyY() + 0.5, beeEntity.getZ(),3, d, e, f,0.0);
            }
        }

        if(headbuttedTicks > 0){
            headbuttedTicks--;
        }
        else {
            setHeadbutted(false);
        }
    }


}
