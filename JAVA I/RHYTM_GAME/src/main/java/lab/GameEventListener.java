package lab;

public interface GameEventListener {
    void onScoreChanged(int newScore);
    void onComboChanged(int newCombo, int multiplier, boolean overdriveActive);
    void onOverdriveChanged(double newProgress, boolean overdriveActive);
    void onGameFinished(GameStats stats);
}