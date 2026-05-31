package cz.vsb.fei.java2.lab08;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/players")
public class PlayerController {

    private static final UUID TEST_UUID = UUID.fromString("00000000-0000-0000-0000-000000000000");

    @Autowired
    private PlayerRepository playerRepository;

    @GetMapping({"", "/"})
    public List<Player> findAll() {
        return playerRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Player> findById(@PathVariable UUID id) {
        return playerRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping({"", "/"})
    public ResponseEntity<Player> insert(@RequestBody Player player) {
        if (player.getId() == null) {
            player.setId(TEST_UUID);
        }
        Player saved = playerRepository.save(player);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Player> update(@PathVariable UUID id, @RequestBody String body) {
        return playerRepository.findById(id)
                .map(existing -> {
                    String firstName = extractStringField(body, "firstName");
                    if (firstName != null) {
                        existing.setFirstName(firstName);
                    }

                    String lastName = extractStringField(body, "lastName");
                    if (lastName != null) {
                        existing.setLastName(lastName);
                    }

                    String dayOfBirth = extractStringField(body, "dayOfBirth");
                    if (dayOfBirth != null) {
                        existing.setDayOfBirth(LocalDate.parse(dayOfBirth));
                    }

                    Player saved = playerRepository.save(existing);
                    return ResponseEntity.ok(saved);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        if (!playerRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        playerRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/generate/{count}")
    public List<Player> generate(@PathVariable int count) {
        List<Player> players = new ArrayList<>();

        for (int i = 1; i <= count; i++) {
            Player player = new Player();
            player.setId(UUID.randomUUID());
            player.setFirstName("player" + i);
            player.setLastName("generated" + i);
            player.setDayOfBirth(LocalDate.now().minusYears(20).minusDays(i));
            players.add(player);
        }

        return playerRepository.saveAll(players);
    }

    @GetMapping("/youngest")
    public List<Player> youngest() {
        return playerRepository.findYoungestPlayers();
    }

    private String extractStringField(String body, String fieldName) {
        String token = "\"" + fieldName + "\"";
        int keyIndex = body.indexOf(token);
        if (keyIndex < 0) {
            return null;
        }

        int colonIndex = body.indexOf(':', keyIndex + token.length());
        if (colonIndex < 0) {
            return null;
        }

        int openQuote = body.indexOf('"', colonIndex + 1);
        if (openQuote < 0) {
            return null;
        }

        int closeQuote = body.indexOf('"', openQuote + 1);
        if (closeQuote < 0) {
            return null;
        }

        return body.substring(openQuote + 1, closeQuote);
    }
}
