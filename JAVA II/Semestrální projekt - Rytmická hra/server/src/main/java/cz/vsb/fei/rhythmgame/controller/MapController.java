package cz.vsb.fei.rhythmgame.controller;

import cz.vsb.fei.rhythmgame.entity.BeatMapEntity;
import cz.vsb.fei.rhythmgame.entity.PlayerEntity;
import cz.vsb.fei.rhythmgame.entity.ScoreEntity;
import cz.vsb.fei.rhythmgame.repository.BeatMapRepository;
import cz.vsb.fei.rhythmgame.repository.PlayerRepository;
import cz.vsb.fei.rhythmgame.repository.ScoreRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/maps")
public class MapController {

    @Autowired
    private BeatMapRepository beatMapRepository;

    @Autowired
    private ScoreRepository scoreRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @GetMapping
    public List<BeatMapEntity> getAllMaps() {
        return beatMapRepository.findAllOrdered();
    }

    @GetMapping("/{id}")
    public BeatMapEntity getMap(@PathVariable Long id) {
        return findBeatMap(id);
    }

    @GetMapping("/{id}/scores")
    public List<ScoreEntity> getScores(@PathVariable Long id) {
        return scoreRepository.findByBeatMapIdOrderByPlayedAtDesc(id);
    }

    @PostMapping("/{id}/scores")
    public ScoreEntity saveScore(@PathVariable Long id, @RequestBody ScoreEntity score) {
        BeatMapEntity beatMap = findBeatMap(id);
        PlayerEntity player = findOrCreatePlayer(score);
        score.setBeatMap(beatMap);
        score.setPlayer(player);
        return scoreRepository.save(score);
    }

    private PlayerEntity findOrCreatePlayer(ScoreEntity score) {
        String playerName = "Anonymous";
        if (score.getPlayer() != null && score.getPlayer().getNickName() != null && !score.getPlayer().getNickName().isBlank()) {
            playerName = score.getPlayer().getNickName();
        }

        PlayerEntity existingPlayer = playerRepository.findByNickName(playerName);
        if (existingPlayer != null) {
            return existingPlayer;
        }

        PlayerEntity newPlayer = new PlayerEntity(playerName);
        return playerRepository.save(newPlayer);
    }

    private BeatMapEntity findBeatMap(Long id) {
        BeatMapEntity beatMap = beatMapRepository.findBeatMapById(id);
        if (beatMap == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Mapa nebyla nalezena.");
        }
        return beatMap;
    }
}
