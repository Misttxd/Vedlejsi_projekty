package lab.score;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Entity
@Table(name = "rg_scores")
@Getter
@Setter
@ToString
@AllArgsConstructor
public class Score {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private long songId;
    private int points;
    private String playerName;

    @jakarta.persistence.Enumerated(jakarta.persistence.EnumType.STRING)
    private Level level;

    public Score(long songId, int points, String playerName, Level level) {
        this.songId = songId;
        this.points = points;
        this.playerName = playerName;
        this.level = level;
    }

    public Score(int points, String playerName, Level level) {
        this.points = points;
        this.playerName = playerName;
        this.level = level;
    }

    public Score() {
    }
}
