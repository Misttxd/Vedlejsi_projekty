package lab;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Setting {

	@Getter
    private static Setting instance;

    @Builder.Default private final double gravity = 9.81;
    @Builder.Default private final int numberOfUfos = 3;
    @Builder.Default private final double ufoMinPercentageHeight = 0.3;
    @Builder.Default private final double ufoMinSpeed = 70;
    @Builder.Default private final double ufoMaxSpeed = 150;
    @Builder.Default private final double bulletMinSpeed = 30;
    @Builder.Default private final double bulletMaxSpeed = 300;


    public static void configure(Setting setting) {
		instance = setting;
	}

    private Setting(double gravity, int numberOfUfos, double ufoMinPercentageHeight, double ufoMinSpeed,
                   double ufoMaxSpeed, double bulletMinSpeed, double bulletMaxSpeed) {
        this.gravity = gravity;
        this.numberOfUfos = numberOfUfos;
        this.ufoMinPercentageHeight = ufoMinPercentageHeight;
        this.ufoMinSpeed = ufoMinSpeed;
        this.ufoMaxSpeed = ufoMaxSpeed;
        this.bulletMinSpeed = bulletMinSpeed;
        this.bulletMaxSpeed = bulletMaxSpeed;
    }

//    public static Builder builder() {
//        return new Builder();
//    }

    public static Setting getInstanceForHardcoreGame() {
        return builder().numberOfUfos(50).ufoMinPercentageHeight(0.9).ufoMinSpeed(200).ufoMaxSpeed(500).build();
    }

//    public static class Builder {
//        private double gravity = 9.81;
//        private int numberOfUfos = 3;
//        private double ufoMinPercentageHeight = 0.3;
//        private double ufoMinSpeed = 70;
//        private double ufoMaxSpeed = 150;
//        private double bulletMinSpeed = 30;
//        private double bulletMaxSpeed = 300;
//
//        public Builder bulletMaxSpeed(double bulletMaxSpeed) {
//            this.bulletMaxSpeed = bulletMaxSpeed;
//            return this;
//        }
//
//        public Builder gravity(double gravity) {
//            this.gravity = gravity;
//            return this;
//        }
//
//        public Builder numberOfUfos(int numberOfUfos) {
//            this.numberOfUfos = numberOfUfos;
//            return this;
//        }
//
//        public Builder ufoMinPercentageHeight(double ufoMinPercentageHeight) {
//            this.ufoMinPercentageHeight = ufoMinPercentageHeight;
//            return this;
//        }
//
//        public Builder ufoMinSpeed(double ufoMinSpeed) {
//            this.ufoMinSpeed = ufoMinSpeed;
//            return this;
//        }
//
//        public Builder ufoMaxSpeed(double ufoMaxSpeed) {
//            this.ufoMaxSpeed = ufoMaxSpeed;
//            return this;
//        }
//
//        public Builder bulletMinSpeed(double bulletMinSpeed) {
//            this.bulletMinSpeed = bulletMinSpeed;
//            return this;
//        }
//
//        public Setting build() {
//            return new Setting(gravity, numberOfUfos, ufoMinPercentageHeight,
//                ufoMinSpeed, ufoMaxSpeed, bulletMinSpeed, bulletMaxSpeed);
//        }
//    }
}
