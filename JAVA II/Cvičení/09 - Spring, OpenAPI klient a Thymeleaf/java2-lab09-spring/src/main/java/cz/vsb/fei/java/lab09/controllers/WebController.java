package cz.vsb.fei.java.lab09.controllers;

import cz.vsb.fei.java.lab09.repositories.ScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @Autowired
    private ScoreRepository scoreRepository;

    @GetMapping("/scores")
    public String showScores(Model model) {
        model.addAttribute("scores", scoreRepository.findAllByOrderByScoreDesc());
        return "scores";
    }
}
