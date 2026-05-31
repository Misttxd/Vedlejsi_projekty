package cz.vsb.fei.java.lab09.repositories;

import cz.vsb.fei.java.lab09.entities.Player;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

    List<Player> findPlayerByFirstName(String firstName);

    @Query("select p from Player p where p.dayOfBirth < :date")
    List<Player> playersBornBefor(LocalDate date);

}
