package cz.vsb.fei.rhythmgame.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"song", "scores"})
public class BeatMapEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String difficulty;
    private String filePath;
    private int laneCount;
    private double bpmMultiplier;
    private double hitZoneHeight;

    @ManyToOne
    @JoinColumn(name = "song_id")
    private SongEntity song;

    @OneToMany(mappedBy = "beatMap")
    private List<ScoreEntity> scores = new ArrayList<>();

    public BeatMapEntity(String difficulty, String filePath, int laneCount, double bpmMultiplier, double hitZoneHeight, SongEntity song) {
        this.difficulty = difficulty;
        this.filePath = filePath;
        this.laneCount = laneCount;
        this.bpmMultiplier = bpmMultiplier;
        this.hitZoneHeight = hitZoneHeight;
        this.song = song;
    }
}
