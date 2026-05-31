package lab;

import lombok.Getter;

@Getter
public class GameStats {
    private final int finalScore;
    private final int perfectCount;
    private final int goodCount;
    private final int missCount;
    private final int maxCombo;
    private final int totalNotes;

    public GameStats(int finalScore, int perfectCount, int goodCount, int missCount, int maxCombo) {
        this.finalScore = finalScore;
        this.perfectCount = perfectCount;
        this.goodCount = goodCount;
        this.missCount = missCount;
        this.maxCombo = maxCombo;
        this.totalNotes = perfectCount + goodCount + missCount;
    }

    public double getAccuracy() {
        if (totalNotes == 0) return 0.0;
        double points = perfectCount * 1.0 + goodCount * 0.5;
        return (points / totalNotes) * 100.0;
    }

    public String getRank() {
        double accuracy = getAccuracy();
        if (accuracy >= 95) {            
            return "S";
        }
        if (accuracy >= 90) {            
            return "A";
        }
        if (accuracy >= 80){                
            return "B";
        }
        if (accuracy >= 70){
            return "C";
        }
        if (accuracy >= 60){       
            return "D";
        }
        else{
            return "F";
        }
    }

    @Override
    public String toString() {
        return String.format(
            "Skóre: %,d\n" +
            "Perfect: %d | Good: %d | Miss: %d\n" +
            "Nejvyšší combo: %d\n" +
            "Přesnost: %.1f%%\n" +
            "Hodnocení: %s",
            finalScore, perfectCount, goodCount, missCount, maxCombo, getAccuracy(), getRank()
        );
    }
}
