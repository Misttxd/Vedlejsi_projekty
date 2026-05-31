package cz.vsb.fei.java.lab09.controllers;

import cz.vsb.fei.java.lab09.entities.Score;
import cz.vsb.fei.java.lab09.repositories.ScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/scores")
public class ScoreController {

    @Autowired
    private ScoreRepository scoreRepository;

    @GetMapping({"/", ""})
    public List<Score> getAll(){
        return scoreRepository.findAll();
    }

    @PostMapping({"/", ""})
    public Score save(@RequestBody Score score){
        return scoreRepository.save(score);
    }

}
