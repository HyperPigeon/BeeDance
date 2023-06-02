package net.hyper_pigeon.beedance.mixin;

import net.hyper_pigeon.beedance.interfaces.BeeDancing;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BeeEntityModel;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BeeEntityModel.class)
public abstract class BeeEntityModelMixin {
    @Shadow
    private ModelPart bone;
    @Shadow
    private ModelPart rightWing;
    @Shadow
    private ModelPart leftWing;
    @Shadow
    private ModelPart frontLegs;
    @Shadow
    private ModelPart middleLegs;
    @Shadow
    private ModelPart backLegs;
    @Shadow
    private ModelPart leftAntenna;
    @Shadow
    private ModelPart rightAntenna;

    private double amp = Math.PI/4;


    @Inject(method = "setAngles(Lnet/minecraft/entity/passive/BeeEntity;FFFFF)V", at = @At("HEAD"), cancellable = true)
    public void setDancingAngles(BeeEntity beeEntity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch, CallbackInfo ci){
        BeeDancing bee = (BeeDancing) beeEntity;
        if(bee.isDancing()) {
            this.rightWing.pitch = 0.0F;
            this.leftAntenna.pitch = 0.0F;
            this.rightAntenna.pitch = 0.0F;
            //this.bone.pitch = 0.0F;

            amp =  amp < Math.PI/6 ? Math.PI/4 : MathHelper.lerp(0.005,amp, Math.PI/6);
            this.bone.yaw = (float) amp * MathHelper.cos(animationProgress * 0.9F);
            this.bone.pitch = (float) Math.PI/12 * MathHelper.cos(animationProgress * 0.35F);

            boolean bl = beeEntity.isOnGround() && beeEntity.getVelocity().lengthSquared() < 1.0E-7;
            if (bl) {
                this.rightWing.yaw = -0.2618F;
                this.rightWing.roll = 0.0F;
                this.leftWing.pitch = 0.0F;
                this.leftWing.yaw = 0.2618F;
                this.leftWing.roll = 0.0F;
                this.frontLegs.pitch = 0.0F;
                this.middleLegs.pitch = 0.0F;
                this.backLegs.pitch = 0.0F;
            } else {
                float k = animationProgress * 120.32113F * ((float) (Math.PI / 180.0));
                this.rightWing.yaw = 0.0F;
                this.rightWing.roll = MathHelper.cos(k) * (float) Math.PI * 0.15F;
                this.leftWing.pitch = this.rightWing.pitch;
                this.leftWing.yaw = this.rightWing.yaw;
                this.leftWing.roll = -this.rightWing.roll;
                this.frontLegs.pitch = (float) (Math.PI / 4);
                this.middleLegs.pitch = (float) (Math.PI / 4);
                this.backLegs.pitch = (float) (Math.PI / 4);
                //this.bone.pitch = 0.0F;
                this.bone.roll = 0.0F;
            }

            if (!beeEntity.hasAngerTime()) {
                //this.bone.pitch = 0.0F;
                this.bone.roll = 0.0F;
                if (!bl) {
                    float k = MathHelper.cos(animationProgress * 0.18F);
                    //this.bone.pitch = 0.1F + k * (float) Math.PI * 0.025F;
                    this.leftAntenna.pitch = k * (float) Math.PI * 0.03F;
                    this.rightAntenna.pitch = k * (float) Math.PI * 0.03F;
                    this.frontLegs.pitch = -k * (float) Math.PI * 0.1F + ((float) (Math.PI / 8));
                    this.backLegs.pitch = -k * (float) Math.PI * 0.05F + (float) (Math.PI / 4);
                    this.bone.pivotY = 19.0F - MathHelper.cos(animationProgress * 0.18F) * 0.9F;
                }
            }

            ci.cancel();

        }
    }


}
