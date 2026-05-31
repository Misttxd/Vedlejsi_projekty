package lab;

public interface GameEventListener {
    void onScoreChanged(int newScore);
    void onOverdriveChanged(double newProgress);
    void onGameFinished(int finalScore);
}