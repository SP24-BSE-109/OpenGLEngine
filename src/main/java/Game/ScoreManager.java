package Game;

public class ScoreManager {
    private int score;

    private float previousTime;

    public ScoreManager() {
        score = 0;
    }

    public void update(){
        float currentTime = System.nanoTime();
        float scoreDecrementDelay = 1e9f;
        if (currentTime - previousTime >= scoreDecrementDelay) {
            if (getScore() <= 0) {
                // Handle game-over logic if needed
            } else if (getScore() >= 100) {
                setScore(100);
                GameManager.currentState = GameStates.Win;
            } else {
               decrementScore(1);
            }
            previousTime = currentTime;
        }
    }

    public int getScore() {
        return score;
    }
    public void setScore(int score) {
        this.score = score;
    }
    public void incrementScore() {
        score += 1;
    }
    public void decrementScore() {
        score -= 1;
    }
    public void incrementScore(int amount) {
        score += amount;
    }
    public void decrementScore(int amount) {
        score -= amount;
    }
}
