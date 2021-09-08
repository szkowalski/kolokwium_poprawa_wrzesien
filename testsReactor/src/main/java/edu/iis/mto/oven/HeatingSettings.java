package edu.iis.mto.oven;

import java.util.Objects;

public class HeatingSettings {

    private final int targetTemp;
    private final int timeInMinutes;

    private HeatingSettings(Builder builder) {
        this.targetTemp = builder.targetTemp;
        this.timeInMinutes = builder.timeInMinutes;
    }

    public int getTargetTemp() {
        return targetTemp;
    }

    public int getTimeInMinutes() {
        return timeInMinutes;
    }

    @Override
    public int hashCode() {
        return Objects.hash(targetTemp, timeInMinutes);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        HeatingSettings other = (HeatingSettings) obj;
        return targetTemp == other.targetTemp && timeInMinutes == other.timeInMinutes;
    }

    @Override
    public String toString() {
        return "HeatingSettings [targetTemp=" + targetTemp + ", timeMinutes=" + timeInMinutes + "]";
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private int targetTemp;
        private int timeInMinutes;

        private Builder() {}

        public Builder withTargetTemp(int targetTemp) {
            this.targetTemp = targetTemp;
            return this;
        }

        public Builder withTimeInMinutes(int timeInMinutes) {
            this.timeInMinutes = timeInMinutes;
            return this;
        }

        public HeatingSettings build() {
            return new HeatingSettings(this);
        }
    }
}
