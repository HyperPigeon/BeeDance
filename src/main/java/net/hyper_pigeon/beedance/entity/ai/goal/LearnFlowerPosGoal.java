package net.hyper_pigeon.beedance.entity.ai.goal;

import net.hyper_pigeon.beedance.interfaces.BeeDancing;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.util.math.Box;

import java.util.List;

public class LearnFlowerPosGoal extends BeeNotAngryGoal {

    private BeeEntity teacher;
    private int learningTicks;

    public LearnFlowerPosGoal(BeeEntity beeEntity) {
        super(beeEntity);
    }

    @Override
    public boolean canBeeStart() {
        if(!this.getBeeEntity().hasFlower()) {
            List<BeeEntity> dancingBees = this.getBeeEntity().world.getEntitiesByClass(BeeEntity.class, this.getSearchBox(5), livingEntity -> livingEntity != null && isBeeDancing(livingEntity));
            if(dancingBees != null && !dancingBees.isEmpty()) {
                teacher = dancingBees.get(0);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean canBeeContinue() {
        if(!this.getBeeEntity().hasFlower() && teacher.isAlive() && isBeeDancing(teacher) && learningTicks >= 0){
            return true;
        }
        return false;
    }

    public void start(){
        learningTicks = this.getBeeEntity().world.getRandom().nextInt((300 - 200) + 1) + 200;
        setLearning(true);
    }

    @Override
    public void tick() {
        this.getBeeEntity().getNavigation().stop();
        this.getBeeEntity().getLookControl().lookAt(this.teacher);
        learningTicks--;
    }

    public void stop(){
        if(teacher != null && teacher.isAlive()){
            this.getBeeEntity().setFlowerPos(teacher.getFlowerPos());
        }
        setLearning(false);
    }

    public void setLearning(boolean learning){
        BeeDancing beeEntity = (BeeDancing) this.getBeeEntity();
        beeEntity.setLearning(learning);
    }

    public boolean isBeeDancing(BeeEntity bee){
        BeeDancing beeEntity = (BeeDancing) bee;
        return beeEntity.isDancing();
    }

    protected Box getSearchBox(double distance) {
        return this.getBeeEntity().getBoundingBox().expand(distance, distance, distance);
    }
}
