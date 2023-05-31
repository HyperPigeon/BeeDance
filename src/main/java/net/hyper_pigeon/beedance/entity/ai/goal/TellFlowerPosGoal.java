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
        if(this.getBeeEntity().hasFlower()) {
            List<BeeEntity> flowerlessBees = this.getBeeEntity().world.getEntitiesByClass(BeeEntity.class, this.getSearchBox(5), livingEntity -> livingEntity != null && !livingEntity.hasFlower() && !isLearning(livingEntity));
            if (flowerlessBees != null && !flowerlessBees.isEmpty()) {
                students = flowerlessBees;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean canBeeContinue() {
        if(this.getBeeEntity() != null && this.getBeeEntity().isAlive() && students != null && !students.isEmpty() && isDancing()) {
            return true;
        }
        return false;
    }

    public void start(){
        this.getBeeEntity().getLookControl().lookAt(this.getBeeEntity().getFlowerPos().getX(),this.getBeeEntity().getFlowerPos().getY(),this.getBeeEntity().getFlowerPos().getZ());
        setDancing(true);

    }

    public void tick(){
        this.getBeeEntity().getNavigation().stop();
        this.getBeeEntity().getLookControl().lookAt(this.getBeeEntity().getFlowerPos().getX(),this.getBeeEntity().getFlowerPos().getY(),this.getBeeEntity().getFlowerPos().getZ());
        students.removeIf(student -> !student.isAlive() || this.getBeeEntity().distanceTo(student) > 5 || student.hasFlower());
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

    protected Box getSearchBox(double distance) {
        return this.getBeeEntity().getBoundingBox().expand(distance, distance, distance);
    }
}
