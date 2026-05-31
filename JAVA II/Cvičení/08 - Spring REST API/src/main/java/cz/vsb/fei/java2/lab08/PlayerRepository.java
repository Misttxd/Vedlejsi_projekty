package cz.vsb.fei.java2.lab08;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PlayerRepository extends JpaRepository<Player, UUID> {

    @Query("SELECT p FROM Player p WHERE p.dayOfBirth = (SELECT max(p2.dayOfBirth) FROM Player p2)")
    List<Player> findYoungestPlayers();
}