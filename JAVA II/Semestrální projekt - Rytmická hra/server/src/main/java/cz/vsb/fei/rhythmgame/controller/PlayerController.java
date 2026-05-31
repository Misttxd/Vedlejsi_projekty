package cz.vsb.fei.rhythmgame.controller;

import cz.vsb.fei.rhythmgame.entity.PlayerEntity;
import cz.vsb.fei.rhythmgame.repository.PlayerRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/players")
public class PlayerController {

    @Autowired
    private PlayerRepository playerRepository;

    @GetMapping
    public List<PlayerEntity> getAll() {
        return playerRepository.findAll();
    }

    @GetMapping("/{id}")
    public PlayerEntity getOne(@PathVariable Long id) {
        return findPlayer(id);
    }

    @PostMapping
    public PlayerEntity create(@RequestBody PlayerEntity player) {
        return playerRepository.save(player);
    }

    @PutMapping("/{id}")
    public PlayerEntity update(@PathVariable Long id, @RequestBody PlayerEntity newPlayer) {
        PlayerEntity player = findPlayer(id);
        player.setNickName(newPlayer.getNickName());
        return playerRepository.save(player);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        playerRepository.delete(findPlayer(id));
    }

    private PlayerEntity findPlayer(Long id) {
        PlayerEntity player = playerRepository.findPlayerById(id);
        if (player == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Hráč nebyl nalezen.");
        }
        return player;
    }
}
