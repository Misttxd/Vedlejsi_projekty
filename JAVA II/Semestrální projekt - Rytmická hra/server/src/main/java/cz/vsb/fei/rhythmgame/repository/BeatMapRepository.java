package cz.vsb.fei.rhythmgame.repository;

import cz.vsb.fei.rhythmgame.entity.BeatMapEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BeatMapRepository extends JpaRepository<BeatMapEntity, Long> {

    @Query("select b from BeatMapEntity b order by b.song.title, b.difficulty")
    List<BeatMapEntity> findAllOrdered();

    BeatMapEntity findBeatMapById(Long id);
}
