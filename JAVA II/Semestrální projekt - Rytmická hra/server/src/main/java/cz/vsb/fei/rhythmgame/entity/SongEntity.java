package cz.vsb.fei.rhythmgame.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "beatMaps")
public class SongEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String musicPath;
    private int bpm;
    private int durationSeconds;

    @JsonIgnore
    @OneToMany(mappedBy = "song")
    private List<BeatMapEntity> beatMaps = new ArrayList<>();

    @Builder
    public SongEntity(String title, String musicPath, int bpm, int durationSeconds) {
        this.title = title;
        this.musicPath = musicPath;
        this.bpm = bpm;
        this.durationSeconds = durationSeconds;
    }
}
