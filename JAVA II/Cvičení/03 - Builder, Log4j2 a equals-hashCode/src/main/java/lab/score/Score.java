package lab.score;

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

    @Override
    public int hashCode() {
        return java.util.Objects.hash(nickName, score);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (obj == this) {
            return true;
        } else if (obj instanceof Score) {
            Score s = (Score) obj;
            return this.nickName.equals(s.nickName) && this.score == s.score;
        }

        return false;

    }
}
