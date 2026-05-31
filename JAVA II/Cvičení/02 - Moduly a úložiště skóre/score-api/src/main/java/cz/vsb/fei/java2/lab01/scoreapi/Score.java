package cz.vsb.fei.java2.lab01.scoreapi;

public class Score {
    private long id;
    private String nickName;
    private int score;

    public Score(long id, String nickName, int score) {
        this.id = id;
        this.nickName = nickName;
        this.score = score;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Score(String nickName, int score) {
        this.nickName = nickName;
        this.score = score;
    }

    public String getNickName() {
        return nickName;
    }

    public int getScore() {
        return score;
    }
}
