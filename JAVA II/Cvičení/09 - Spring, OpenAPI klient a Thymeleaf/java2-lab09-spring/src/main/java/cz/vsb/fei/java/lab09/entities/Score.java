package cz.vsb.fei.java.lab09.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Setter
@Getter
public class Score {
    @Setter
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String nickName;
    private int score;
    @Enumerated(EnumType.STRING)
    private Level level;

    @ManyToOne
    @com.fasterxml.jackson.annotation.JsonBackReference
    private Player player;

    public Score(String nickName, int score, Level level) {
        this.nickName = nickName;
        this.score = score;
        this.level = level;
    }
}
