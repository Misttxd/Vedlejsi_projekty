package cz.vsb.fei.rhythmgame.repository;

import cz.vsb.fei.rhythmgame.entity.PlayerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<PlayerEntity, Long> {

    PlayerEntity findByNickName(String nickName);

    PlayerEntity findPlayerById(Long id);
}
