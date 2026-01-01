package lab;

public interface GameEventListener {
    void onScoreChanged(int newScore);
    void onComboChanged(int newCombo);
    void onOverdriveChanged(double newProgress);
    void onGameFinished(GameStats stats);
}