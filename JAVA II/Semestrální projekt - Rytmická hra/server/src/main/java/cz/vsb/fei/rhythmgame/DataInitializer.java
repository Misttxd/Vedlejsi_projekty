package cz.vsb.fei.rhythmgame;

import cz.vsb.fei.rhythmgame.entity.BeatMapEntity;
import cz.vsb.fei.rhythmgame.entity.SongEntity;
import cz.vsb.fei.rhythmgame.repository.BeatMapRepository;
import cz.vsb.fei.rhythmgame.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private BeatMapRepository beatMapRepository;

    @Override
    public void run(String... args) {
        if (songRepository.count() > 0) {
            return;
        }

        createSong("Big City Life", "/musicFiles/Big City Life - Luude, Mattafix.mp3", 87, 147);
        createSong("silicon wings", "/musicFiles/silicon wings - Yung Lean.mp3", 160, 136);
    }

    private void createSong(String title, String musicPath, int bpm, int durationSeconds) {
        SongEntity song = SongEntity.builder()
            .title(title)
            .musicPath(musicPath)
            .bpm(bpm)
            .durationSeconds(durationSeconds)
            .build();
        songRepository.save(song);

        createMap(song, "Lehká", 4, 0.25, 60);
        createMap(song, "Střední", 4, 0.5, 50);
        createMap(song, "Těžká", 4, 1.0, 40);
        createMap(song, "Expert", 5, 2.0, 30);
    }

    private void createMap(SongEntity song, String difficulty, int laneCount, double bpmMultiplier, double hitZoneHeight) {
        BeatMapEntity beatMap = new BeatMapEntity(difficulty, "maps/gen.txt", laneCount, bpmMultiplier, hitZoneHeight, song);
        beatMapRepository.save(beatMap);
    }
}
