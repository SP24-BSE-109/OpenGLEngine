package Game;

public class ScoreManager {
    private int score;
    public ScoreManager() {
        score = 0;
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
    public void update() {
        score -= 1;
    }
}
