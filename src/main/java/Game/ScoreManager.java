package Game;

import Launch.Launcher;

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
            } else if (getScore() >= 100) {
                setScore(100);
                try{
                    System.out.println("Restarting Game");
                    Launcher.getGame().restartGame();
                }catch (Exception e){
                    System.out.println(e);
                }

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
