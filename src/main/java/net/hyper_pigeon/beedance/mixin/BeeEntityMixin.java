package net.hyper_pigeon.beedance.mixin;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.hyper_pigeon.beedance.entity.ai.goal.LearnFlowerPosGoal;
import net.hyper_pigeon.beedance.entity.ai.goal.TellFlowerPosGoal;
import net.hyper_pigeon.beedance.interfaces.BeeDancing;
import net.hyper_pigeon.beedance.networking.BeeDancingNetworkingConstants;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
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
    //private List<BeeEntity> students;

    protected BeeEntityMixin(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initGoals", at = @At("TAIL"))
    public void addGoals(CallbackInfo ci){
        this.goalSelector.add(6, new LearnFlowerPosGoal((BeeEntity)(Object)this));
        this.goalSelector.add(6, new TellFlowerPosGoal((BeeEntity)(Object)this));
    }

//    @Inject(method = "initDataTracker", at = @At("TAIL"))
//    public void addTrackedData(CallbackInfo ci){
//        this.dataTracker.startTracking(DANCING, false);
//    }

    @Override
    public void setDancing(boolean dancing) {
        this.dancing = dancing;

        if(!world.isClient()) {
            PacketByteBuf packetByteBuf = PacketByteBufs.create();
            packetByteBuf.writeInt(this.getId());
            packetByteBuf.writeBoolean(dancing);
            for (ServerPlayerEntity player : PlayerLookup.tracking((BeeEntityMixin)(Object)(this))) {
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

//    @Override
//    public void setStudents(List<BeeEntity> students){
//        this.students = students;
//    }
//
//    @Override
//    public List<BeeEntity> getStudents(){
//        return this.students;
//    }

//    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
//    public void writeAdditionalCustomDataToNbt(NbtCompound nbt, CallbackInfo ci){
//        nbt.putBoolean("isDancing", dancing);
//        nbt.putBoolean("isLearning", learning);
//    }
//
//    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
//    public void readAdditionalCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci){
//        setDancing(false);
//        setLearning(false);
//    }

//    @Inject(method = "tick", at = @At("TAIL"))
//    public void checkStudents(CallbackInfo ci){
//        if((students == null || students.isEmpty()) && isDancing()){
//            setDancing(false);
//        }
//    }
}
