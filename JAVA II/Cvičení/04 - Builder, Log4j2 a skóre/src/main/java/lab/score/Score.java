package lab.score;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode
@Getter
@ToString

public class Score {
    @Setter
    private long id;
    @NonNull
    private String nickName;
    private int score;

    public Score(String randomNick, int i) {
    }


//    public Score(String nickName, int score) {
//        this.nickName = nickName;
//        this.score = score;
//    }

}
