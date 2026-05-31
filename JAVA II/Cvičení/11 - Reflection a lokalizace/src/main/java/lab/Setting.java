package lab;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class Setting {

    @Getter
    private static Setting instance;

    @lombok.Builder.Default
    private final double gravity = 9.81;
    @lombok.Builder.Default
    private final int numberOfUfos = 3;
    @lombok.Builder.Default
    private final double ufoMinPercentageHeight = 0.3;
    @lombok.Builder.Default
    private final double ufoMinSpeed = 70;
    @lombok.Builder.Default
    private final double ufoMaxSpeed = 150;
    @lombok.Builder.Default
    private final double bulletMinSpeed = 30;
    @lombok.Builder.Default
    private final double bulletMaxSpeed = 300;


    public static void configure(Setting setting) {
        instance = setting;
    }

    public static Setting getInstanceForHardcoreGame() {
        return builder().numberOfUfos(50).ufoMinPercentageHeight(0.9).ufoMinSpeed(200).ufoMaxSpeed(500).build();
    }

}
