package net.hyper_pigeon.beedance.entity.ai.goal;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.passive.BeeEntity;

public abstract class BeeNotAngryGoal extends Goal {

    private final BeeEntity beeEntity;
    public BeeNotAngryGoal(BeeEntity beeEntity) {
        this.beeEntity = beeEntity;
    }

    public abstract boolean canBeeStart();

    public abstract boolean canBeeContinue();

    public boolean canStart() {
        return this.canBeeStart() && !beeEntity.hasAngerTime();
    }

    public boolean shouldContinue() {
        return this.canBeeContinue() && !beeEntity.hasAngerTime();
    }

    public BeeEntity getBeeEntity(){
        return beeEntity;
    }
}
