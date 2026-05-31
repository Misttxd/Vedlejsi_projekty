package cz.vsb.fei.java.lab09.controllers;

import cz.vsb.fei.java.lab09.entities.Player;
import cz.vsb.fei.java.lab09.repositories.PlayerRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/players")
public class PlayerController {

    @Autowired
    private PlayerRepository playerRepository;

    @GetMapping({"/", ""})
    public List<Player> getAll(){
        return playerRepository.findAll();
    }

    @GetMapping("/filter")
    public List<Player> getByName2(@RequestParam String name){
        return playerRepository.findPlayerByFirstName(name);
    }

    @GetMapping("/name/{name}")
    public List<Player> getByName(@PathVariable String name){
        return playerRepository.findPlayerByFirstName(name);
    }
    @GetMapping("/before")
    public List<Player> getBefore(){
        return playerRepository.playersBornBefor(LocalDate.of(2025, 12, 24));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id){
        Optional<Player> result = playerRepository.findById(id);
        if(result.isPresent()){
            return ResponseEntity.ok(result.get());
        } else {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Player with id " + id + " not found");
        }
    }

    @PostMapping({"/", ""})
    public Player save(@RequestBody Player player){
        return playerRepository.save(player);
    }
}
