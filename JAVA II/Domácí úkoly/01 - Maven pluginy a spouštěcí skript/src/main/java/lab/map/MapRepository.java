package lab.map;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapRepository {

    private static final String FILE_NAME = "maps.csv";

    private MapRepository() {}

    public static void save(List<MapInfo> maps) throws MapException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (MapInfo map : maps) {
                Score score = map.getHighScore();
                String line = String.join(";",
                        map.getMapName(),
                        map.getDifficulty(),
                        map.getFilePath(),
                        String.valueOf(map.getNoteCount()), // Přidáno
                        String.valueOf(score.getPoints()),
                        score.getPlayerName()
                );
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            throw new MapException("Chyba při ukládání map do souboru: " + FILE_NAME, e);
        }
    }

    public static List<MapInfo> load() throws MapException {
        List<MapInfo> result = new ArrayList<>();
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            return createDefaultMaps();
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";", -1);
                if (parts.length != 6) { // Očekáváme 6 částí
                    throw new MapException("Chybný formát řádku v souboru: " + line);
                }

                int noteCount = Integer.parseInt(parts[3]);
                MapInfo mapInfo = new MapInfo(parts[0], parts[1], parts[2], noteCount);

                int points = Integer.parseInt(parts[4]);
                Score score = new Score(points, parts[5]);
                mapInfo.setHighScore(score);
                result.add(mapInfo);
            }
        } catch (IOException e) {
            throw new MapException("Chyba při načítání map ze souboru: " + FILE_NAME, e);
        } catch (NumberFormatException e) {
            throw new MapException("Chyba při parsování čísla (počet not nebo skóre).", e);
        }
        return result;
    }

    private static List<MapInfo> createDefaultMaps() {
        List<MapInfo> defaultMaps = new ArrayList<>();
        defaultMaps.add(new MapInfo("test song 1", "Lehká", "maps/song1.txt", 50));
        defaultMaps.add(new MapInfo("test song 2", "Střední", "maps/song2.txt", 75));
        defaultMaps.add(new MapInfo("test song 3", "Těžká", "maps/song3.txt", 100));
        return defaultMaps;
    }
}
