package cz.vsb.fei.rhythmgame.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"beatMap", "player"})
public class ScoreEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int points;
    private String rank;
    private double accuracy;
    private int maxCombo;
    private LocalDateTime playedAt = LocalDateTime.now();

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "beat_map_id")
    private BeatMapEntity beatMap;

    @ManyToOne
    @JoinColumn(name = "player_id")
    private PlayerEntity player;

    public ScoreEntity(int points, String rank, double accuracy, int maxCombo, BeatMapEntity beatMap, PlayerEntity player) {
        this.points = points;
        this.rank = rank;
        this.accuracy = accuracy;
        this.maxCombo = maxCombo;
        this.beatMap = beatMap;
        this.player = player;
    }
}
