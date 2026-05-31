package cz.vsb.fei.rhythmgame.repository;

import cz.vsb.fei.rhythmgame.entity.ScoreEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScoreRepository extends JpaRepository<ScoreEntity, Long> {

    List<ScoreEntity> findByBeatMapIdOrderByPlayedAtDesc(Long beatMapId);

    List<ScoreEntity> findAllByOrderByPlayedAtDesc();
}
