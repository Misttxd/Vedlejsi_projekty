package lab;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Setting {

    private static final Logger LOGGER = LogManager.getLogger(Setting.class);

    private static Setting instance;

    private final double gravity;
    private final int numberOfUfos;
    private final double ufoMinPercentageHeight;
    private final double ufoMinSpeed;
    private final double ufoMaxSpeed;
    private final double bulletMinSpeed;
    private final double bulletMaxSpeed;

    public static void configure(Setting setting) {
        instance = setting;
    }

    public static Setting getInstance() {
        return instance;
    }

    public static Builder builder() {
        return new Builder();
    }

    public double getGravity() {
        return gravity;
    }

    public int getNumberOfUfos() {
        return numberOfUfos;
    }

    public double getUfoMinPercentageHeight() {
        return ufoMinPercentageHeight;
    }

    public double getUfoMinSpeed() {
        return ufoMinSpeed;
    }

    public double getUfoMaxSpeed() {
        return ufoMaxSpeed;
    }

    public double getBulletMinSpeed() {
        return bulletMinSpeed;
    }

    public double getBulletMaxSpeed() {
        return bulletMaxSpeed;
    }

    private Setting(Builder builder) {
        LOGGER.trace("Creating new Setting from Builder");
        this.gravity = builder.gravity;
        this.numberOfUfos = builder.numberOfUfos;
        this.ufoMaxSpeed = builder.ufoMaxSpeed;
        this.ufoMinSpeed = builder.ufoMinSpeed;
        this.bulletMinSpeed = builder.bulletMinSpeed;
        this.bulletMaxSpeed = builder.bulletMaxSpeed;
        this.ufoMinPercentageHeight = builder.ufoMinPercentageHeight;
    }

    public static class Builder {
        private double gravity;
        private int numberOfUfos;
        private double ufoMinPercentageHeight;
        private double ufoMinSpeed;
        private double ufoMaxSpeed;
        private double bulletMinSpeed;
        private double bulletMaxSpeed;

        public Builder() {
            this.gravity = 9.81;
            this.numberOfUfos = 3;
            this.ufoMinPercentageHeight = 0.3;
            this.ufoMinSpeed = 70;
            this.ufoMaxSpeed = 150;
            this.bulletMinSpeed = 30;
            this.bulletMaxSpeed = 300;
        }

        public Builder gravity(double gravity) {
            this.gravity = gravity;
            return this;
        }

        public Builder numberOfUfos(int numberOfUfos) {
            this.numberOfUfos = numberOfUfos;
            return this;
        }

        public Builder ufoMinPercentageHeight(double ufoMinPercentageHeight) {

            this.ufoMinPercentageHeight = ufoMinPercentageHeight;
            return this;
        }

        public Builder ufoMinSpeed(double ufoMinSpeed) {
            this.ufoMinSpeed = ufoMinSpeed;
            return this;
        }

        public Builder ufoMaxSpeed(double ufoMaxSpeed) {
            this.ufoMaxSpeed = ufoMaxSpeed;
            return this;
        }

        public Builder bulletMinSpeed(double bulletMinSpeed) {
            this.bulletMinSpeed = bulletMinSpeed;
            return this;
        }

        public Builder bulletMaxSpeed(double bulletMaxSpeed) {
            this.bulletMaxSpeed = bulletMaxSpeed;
            return this;
        }

        public Setting build() {
            return new Setting(this);
        }

        public static Setting buildWithMediumDifficulty() {
            return new Builder().numberOfUfos(10).ufoMinSpeed(100).ufoMaxSpeed(1010).bulletMinSpeed(50)
                    .bulletMaxSpeed(100).build();
        }
    }

}
