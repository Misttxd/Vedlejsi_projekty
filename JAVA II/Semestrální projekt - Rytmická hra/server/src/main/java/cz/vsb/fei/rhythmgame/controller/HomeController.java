package cz.vsb.fei.rhythmgame.controller;

import cz.vsb.fei.rhythmgame.entity.BeatMapEntity;
import cz.vsb.fei.rhythmgame.entity.ScoreEntity;
import cz.vsb.fei.rhythmgame.repository.BeatMapRepository;
import cz.vsb.fei.rhythmgame.repository.ScoreRepository;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private ScoreRepository scoreRepository;

    @Autowired
    private BeatMapRepository beatMapRepository;

    @GetMapping("/")
    public String index(Model model) {
        List<BeatMapEntity> maps = beatMapRepository.findAllOrdered();
        Map<Long, List<ScoreEntity>> scoresByMap = new HashMap<>();
        Map<String, List<BeatMapEntity>> mapsBySong = new LinkedHashMap<>();
        int totalScoreCount = 0;

        for (BeatMapEntity beatMap : maps) {
            List<ScoreEntity> mapScores = scoreRepository.findByBeatMapIdOrderByPlayedAtDesc(beatMap.getId());
            scoresByMap.put(beatMap.getId(), mapScores);
            totalScoreCount += mapScores.size();

            String songTitle = beatMap.getSong().getTitle();
            List<BeatMapEntity> songMaps = mapsBySong.get(songTitle);
            if (songMaps == null) {
                songMaps = new ArrayList<>();
                mapsBySong.put(songTitle, songMaps);
            }
            songMaps.add(beatMap);
        }

        model.addAttribute("maps", maps);
        model.addAttribute("mapsBySong", mapsBySong);
        model.addAttribute("scoresByMap", scoresByMap);
        model.addAttribute("totalScoreCount", totalScoreCount);
        return "index";
    }
}
