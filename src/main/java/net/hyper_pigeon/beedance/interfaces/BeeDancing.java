package net.hyper_pigeon.beedance.interfaces;

import net.minecraft.entity.passive.BeeEntity;

import java.util.List;

public interface BeeDancing {
    void setDancing(boolean dancing);

    boolean isDancing();

    boolean isLearning();

    void setLearning(boolean learning);

//    void setStudents(List<BeeEntity> students);
//
//    List<BeeEntity> getStudents();
}
