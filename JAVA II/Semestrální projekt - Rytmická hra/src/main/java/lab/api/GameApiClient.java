package lab.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import lab.GameStats;
import lab.map.MapInfo;
import lab.map.Score;

public class GameApiClient {

    private static final String BASE_URL = "http://localhost:8080";

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<MapInfo> loadMaps() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/api/maps"))
            .GET()
            .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        checkResponse(response);

        JsonNode maps = objectMapper.readTree(response.body());
        List<MapInfo> result = new java.util.ArrayList<>();
        for (JsonNode map : maps) {
            result.add(toMapInfo(map));
        }
        return result;
    }

    public void saveScore(long mapId, String playerName, int points, GameStats stats) throws IOException, InterruptedException {
        ObjectNode player = objectMapper.createObjectNode();
        player.put("nickName", playerName);

        ObjectNode score = objectMapper.createObjectNode();
        score.put("points", points);
        score.put("rank", stats.getRank());
        score.put("accuracy", stats.getAccuracy());
        score.put("maxCombo", stats.getMaxCombo());
        score.set("player", player);

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/api/maps/" + mapId + "/scores"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(score), StandardCharsets.UTF_8))
            .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        checkResponse(response);
    }

    private MapInfo toMapInfo(JsonNode mapNode) {
        JsonNode song = mapNode.get("song");
        MapInfo map = new MapInfo(
            mapNode.get("id").asLong(),
            song.get("title").asText(),
            mapNode.get("difficulty").asText(),
            mapNode.get("filePath").asText(),
            song.get("musicPath").asText(),
            song.get("bpm").asInt(),
            song.get("durationSeconds").asInt()
        );

        map.setHighScore(findHighScore(mapNode.get("scores")));
        return map;
    }

    private Score findHighScore(JsonNode scores) {
        int bestPoints = 0;
        String bestPlayer = "---";
        if (scores != null && scores.isArray()) {
            for (JsonNode score : scores) {
                int points = score.get("points").asInt();
                if (points > bestPoints) {
                    bestPoints = points;
                    JsonNode player = score.get("player");
                    if (player != null && player.get("nickName") != null) {
                        bestPlayer = player.get("nickName").asText();
                    }
                }
            }
        }
        return new Score(bestPoints, bestPlayer);
    }

    private void checkResponse(HttpResponse<String> response) throws IOException {
        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new IOException("Server returned HTTP " + response.statusCode() + ": " + response.body());
        }
    }
}
