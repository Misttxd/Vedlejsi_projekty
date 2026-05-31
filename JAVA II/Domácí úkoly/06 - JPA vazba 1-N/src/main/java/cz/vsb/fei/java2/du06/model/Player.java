package cz.vsb.fei.java2.du06.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "players")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nickName;

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ScoreRecord> scores = new ArrayList<>();

    public Player() {
    }

    public Player(String nickName) {
        this.nickName = nickName;
    }

    public Long getId() {
        return id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public List<ScoreRecord> getScores() {
        return Collections.unmodifiableList(scores);
    }

    public void addScore(ScoreRecord score) {
        scores.add(score);
        score.setPlayer(this);
    }

    public void removeScore(ScoreRecord score) {
        scores.remove(score);
        score.setPlayer(null);
    }

    @Override
    public String toString() {
        return "Player{id=%s, nickName='%s', scoreCount=%d}".formatted(id, nickName, scores.size());
    }
}
