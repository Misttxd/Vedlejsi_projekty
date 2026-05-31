package lab.map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class Score {
    private final int points;
    private final String playerName;

    @Override
    public String toString() {
        return playerName + ": " + points;
    }
}
