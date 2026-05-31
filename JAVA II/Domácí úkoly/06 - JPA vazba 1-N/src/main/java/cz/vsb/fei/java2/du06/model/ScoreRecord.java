package cz.vsb.fei.java2.du06.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "scores")
public class ScoreRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String songName;
    private int points;
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne(optional = false)
    private Player player;

    public ScoreRecord() {
    }

    public ScoreRecord(String songName, int points) {
        this.songName = songName;
        this.points = points;
    }

    public Long getId() {
        return id;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public String toString() {
        String nickName = player == null ? "bez hráče" : player.getNickName();
        return "ScoreRecord{id=%s, songName='%s', points=%d, player='%s'}"
            .formatted(id, songName, points, nickName);
    }
}
