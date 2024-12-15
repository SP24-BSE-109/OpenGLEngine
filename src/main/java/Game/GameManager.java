package Game;

public class GameManager {

    public static GameStates currentState;

    public static GameStates getCurrentState() {
        return currentState;
    }

    public static void setCurrentState(GameStates currentState) {

        switch (currentState){
            
        }

        GameManager.currentState = currentState;
    }
}
