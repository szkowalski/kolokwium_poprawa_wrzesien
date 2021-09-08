package edu.iis.mto.oven;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class BakingProgram implements Iterable<ProgramStage> {

    private final int initialTemp;
    private final List<ProgramStage> stages;
    private final boolean coolAtFinish;

    private BakingProgram(Builder builder) {
        this.initialTemp = builder.initialTemp;
        this.stages = builder.stages;
        this.coolAtFinish = builder.coolAtFinish;
    }

    @Override
    public Iterator<ProgramStage> iterator() {
        return List.copyOf(stages)
                   .iterator();
    }

    public int getInitialTemp() {
        return initialTemp;
    }

    public boolean isCoolAtFinish() {
        return coolAtFinish;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private int initialTemp;
        private List<ProgramStage> stages = Collections.emptyList();
        private boolean coolAtFinish;

        private Builder() {}

        public Builder withInitialTemp(int initialTemp) {
            this.initialTemp = initialTemp;
            return this;
        }

        public Builder withStages(List<ProgramStage> stages) {
            this.stages = stages;
            return this;
        }

        public Builder withCoolAtFinish(boolean coolAtFinish) {
            this.coolAtFinish = coolAtFinish;
            return this;
        }

        public BakingProgram build() {
            return new BakingProgram(this);
        }
    }

}
