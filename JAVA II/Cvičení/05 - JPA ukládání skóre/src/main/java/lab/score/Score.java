package lab.score;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Entity
@Table(name = "scores")
@EqualsAndHashCode
@ToString


@AllArgsConstructor
@Builder(toBuilder = true)
public class Score {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter
    @Getter
    private Long id;
    @Getter
    @Setter
    private String nickName;
    @Getter
    @Setter
    private int score;
    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    private Level level;

    public Score(String nickName, int score, Level level) {
        this.nickName = nickName;
        this.score = score;
        this.level = level;
    }
    public Score() {
    }
}
