package cz.vsb.fei.rhythmgame.controller;

import cz.vsb.fei.rhythmgame.entity.ScoreEntity;
import cz.vsb.fei.rhythmgame.repository.ScoreRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/scores")
public class ScoreController {

    @Autowired
    private ScoreRepository scoreRepository;

    @GetMapping
    public List<ScoreEntity> getAllScores() {
        return scoreRepository.findAllByOrderByPlayedAtDesc();
    }
}
