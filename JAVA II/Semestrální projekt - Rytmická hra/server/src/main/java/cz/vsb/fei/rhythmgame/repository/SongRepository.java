package cz.vsb.fei.rhythmgame.repository;

import cz.vsb.fei.rhythmgame.entity.SongEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SongRepository extends JpaRepository<SongEntity, Long> {
}
