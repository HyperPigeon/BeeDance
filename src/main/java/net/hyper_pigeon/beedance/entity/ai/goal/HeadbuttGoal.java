package net.hyper_pigeon.beedance.entity.ai.goal;

import net.hyper_pigeon.beedance.interfaces.BeeDancing;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.util.math.Box;

import java.util.List;
import java.util.Objects;

public class HeadbuttGoal extends BeeNotAngryGoal{

    private BeeEntity target;


    public HeadbuttGoal(BeeEntity beeEntity) {
        super(beeEntity);
    }

    @Override
    public boolean canBeeStart() {
        if(this.getBeeEntity().hasFlower()){
            List<BeeEntity> dancingBees = this.getBeeEntity().getEntityWorld().getEntitiesByClass(BeeEntity.class, this.getSearchBox(5), beeEntity ->
                    beeEntity != null &&
                    this.getBeeEntity().canSee(beeEntity) &&
                    isBeeDancing(beeEntity) &&
                    !Objects.equals(beeEntity.getFlowerPos(), this.getBeeEntity().getFlowerPos()) &&
                    (Objects.requireNonNull(beeEntity.getFlowerPos()).getSquaredDistance(beeEntity.getPos()) -
                            Objects.requireNonNull(this.getBeeEntity().getFlowerPos()).
                                    getSquaredDistance(this.getBeeEntity().getPos()) > 10));
            if(dancingBees != null && !dancingBees.isEmpty()) {
                target = dancingBees.get(0);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean canBeeContinue() {
        if(target != null && target.isAlive() && isBeeDancing(target) && this.getBeeEntity().distanceTo(target) <= 5) {
            return true;
        }
        return false;
    }

    @Override
    public void start() {
        setHeadbutting(true);
    }

    public void tick(){
        this.getBeeEntity().getNavigation().startMovingTo(this.target,1.5F);
        if(this.getBeeEntity().getBoundingBox().intersects(this.target.getBoundingBox().expand(0.2))){
            setHeadbutted(true,this.target);
        }
    }

    @Override
    public void stop() {
        setHeadbutting(false);
    }

    public void setHeadbutting(boolean headbutting){
        BeeDancing beeEntity = (BeeDancing) this.getBeeEntity();
        beeEntity.setHeadbutting(headbutting);
    }

    public boolean isHeadbutting(){
        BeeDancing beeEntity = (BeeDancing) this.getBeeEntity();
        return beeEntity.isHeadbutting();
    }

    public void setHeadbutted(boolean headbutted, BeeEntity bee){
        BeeDancing beeEntity = (BeeDancing) bee;
        beeEntity.setHeadbutted(headbutted);
    }

    public boolean isBeeDancing(BeeEntity bee){
        BeeDancing beeEntity = (BeeDancing) bee;
        return beeEntity.isDancing();
    }


    protected Box getSearchBox(double distance) {
        return this.getBeeEntity().getBoundingBox().expand(distance, distance, distance);
    }
}
