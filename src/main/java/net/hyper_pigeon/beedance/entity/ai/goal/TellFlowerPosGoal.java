package net.hyper_pigeon.beedance.entity.ai.goal;

import net.hyper_pigeon.beedance.interfaces.BeeDancing;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.util.math.Box;

import java.util.List;

public class TellFlowerPosGoal extends BeeNotAngryGoal{

    private List<BeeEntity> students;

    public TellFlowerPosGoal(BeeEntity beeEntity) {
        super(beeEntity);
    }

    @Override
    public boolean canBeeStart() {
        if(this.getBeeEntity().hasFlower() && !getHeadbutted() && !isHeadbutting() && !this.getBeeEntity().isLeashed()) {
            List<BeeEntity> flowerlessBees = this.getBeeEntity().getEntityWorld().getEntitiesByClass(BeeEntity.class, this.getSearchBox(4), beeEntity -> beeEntity != null && !beeEntity.hasFlower() && this.getBeeEntity().canSee(beeEntity) && !isLearning(beeEntity));
            if (flowerlessBees != null && !flowerlessBees.isEmpty()) {
                students = flowerlessBees;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean canBeeContinue() {
        if(this.getBeeEntity() != null && this.getBeeEntity().isAlive() && this.getBeeEntity().hasFlower() && students != null && !students.isEmpty() && isDancing() && !getHeadbutted() && !isHeadbutting() && !this.getBeeEntity().isLeashed()) {
            return true;
        }
        return false;
    }

    public void start(){
        if(this.getBeeEntity().hasFlower()) {
            this.getBeeEntity().getLookControl().lookAt(this.getBeeEntity().getFlowerPos().getX(), this.getBeeEntity().getFlowerPos().getY(), this.getBeeEntity().getFlowerPos().getZ());
            setDancing(true);
        }
    }

    public void tick(){
        if(this.getBeeEntity().hasFlower()) {
            this.getBeeEntity().getNavigation().stop();
            this.getBeeEntity().getLookControl().lookAt(this.getBeeEntity().getFlowerPos().getX(),this.getBeeEntity().getFlowerPos().getY(),this.getBeeEntity().getFlowerPos().getZ());
            students.removeIf(student -> !student.isAlive() || this.getBeeEntity().distanceTo(student) > 4 || student.hasFlower());
        }


        setDancing(true);
    }


    public void stop(){
        setDancing(false);
    }

    public boolean isLearning(BeeEntity potentialStudent){
        BeeDancing beeEntity = (BeeDancing) potentialStudent;
        return beeEntity.isLearning();
    }

    public void setDancing(boolean dancing){
        BeeDancing beeEntity = (BeeDancing) this.getBeeEntity();
        beeEntity.setDancing(dancing);
    }

    public boolean isDancing(){
        BeeDancing beeEntity = (BeeDancing) this.getBeeEntity();
        return beeEntity.isDancing();
    }

    public boolean getHeadbutted(){
        BeeDancing beeEntity = (BeeDancing) this.getBeeEntity();
        return beeEntity.getHeadbutted();
    }

    public boolean isHeadbutting(){
        BeeDancing beeEntity = (BeeDancing) this.getBeeEntity();
        return beeEntity.isHeadbutting();
    }

    protected Box getSearchBox(double distance) {
        return this.getBeeEntity().getBoundingBox().expand(distance, distance, distance);
    }
}
