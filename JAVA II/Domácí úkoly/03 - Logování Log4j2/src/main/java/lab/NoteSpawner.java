package lab;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.media.MediaPlayer;
import lab.map.MapInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NoteSpawner implements DrawableSimulable {

    private static final Random RANDOM = new Random();
    private final Level level;
    private final MapInfo mapInfo;
    private final MediaPlayer mediaPlayer;
    
    private final double beatInterval;  // Čas mezi beaty v sekundách
    private final double fallTime;      // Čas pádu noty od spawnu po hit zónu
    private int nextBeat = -1;          // Další beat na který spawneme (-1 = neinicializováno)
    private double lastNoteLength = 0;
    private int beatsToSkip = 0;        // Kolik beatů přeskočit (kvůli dlouhým notám)

    public NoteSpawner(Level level, MapInfo mapInfo, MediaPlayer mediaPlayer) {
        this.level = level;
        this.mapInfo = mapInfo;
        this.mediaPlayer = mediaPlayer;
        
        // Výpočet intervalu mezi beaty
        Difficulty difficulty = level.getDifficulty();
        double effectiveBpm = mapInfo.getBpm() * difficulty.getBpmMultiplier();
        this.beatInterval = 60.0 / effectiveBpm;
        
        // Výpočet času pádu noty (vzdálenost / rychlost)
        double fallDistance = level.getHeight() - 100;
        double noteSpeed = 200;
        this.fallTime = fallDistance / noteSpeed;
    }

    @Override
    public void draw(GraphicsContext gc) {
    }

    @Override
    public void simulate(double deltaT) {
        if (mediaPlayer == null) return;
        
        double currentTime = mediaPlayer.getCurrentTime().toSeconds();
        double targetBeatTime = currentTime + fallTime;
        int targetBeat = (int) (targetBeatTime / beatInterval);
        
        // Inicializace při prvním spuštění -> začni od aktuálního beatu
        if (nextBeat == -1) {
            nextBeat = targetBeat;
        }
        
        // Spawnuje pouze JEDEN beat za volání
        if (nextBeat <= targetBeat) {
            if (beatsToSkip > 0) {
                beatsToSkip--;
            } else {
                spawnNotesForBeat();
            }
            nextBeat++;
        }
    }
    
    private void spawnNotesForBeat() {
        lastNoteLength = 0;
        
        // Seznam volných drah - mozna udelat jako field? ale funguje to
        List<Integer> availableLanes = new ArrayList<>();
        for (int i = 0; i < level.getLanecount(); i++) {
            availableLanes.add(i);
        }
        
        
        int firstLaneIndex = RANDOM.nextInt(availableLanes.size()); // Vždy se spawne alespoň jedna nota
        int firstLane = availableLanes.remove(firstLaneIndex);
        spawnNote(firstLane);
        
        
        if (!availableLanes.isEmpty() && RANDOM.nextDouble() < 0.08) { // 8% šance na druhou notu
            int secondLaneIndex = RANDOM.nextInt(availableLanes.size());
            int secondLane = availableLanes.remove(secondLaneIndex);
            spawnNote(secondLane);
            
            
            while (!availableLanes.isEmpty() && RANDOM.nextDouble() < 0.10) { // 10% šance na třetí a každou další
                int nextLaneIndex = RANDOM.nextInt(availableLanes.size());
                int nextLane = availableLanes.remove(nextLaneIndex);
                spawnNote(nextLane);
            }
        }
        
        if (lastNoteLength > 0) { // Přeskočit beaty pokud byla dlouhá nota nebo release nota
            beatsToSkip = (int) Math.ceil(lastNoteLength / 200.0 / beatInterval);
        }
    }
    
    private void spawnNote(int lane) {
        if (RANDOM.nextDouble() < 0.15) {
            level.add(new Note(level, lane, true)); // release note
            lastNoteLength = 60; // magicky cislo lol
        } else {
            if (RANDOM.nextDouble() < 0.2) {
                double noteLength = RANDOM.nextDouble(150, 400);
                if (noteLength > lastNoteLength) {
                    lastNoteLength = noteLength;
                }
                level.add(new Note(level, lane, noteLength));
            } else {
                level.add(new Note(level, lane));
            }
        }
    }
}
