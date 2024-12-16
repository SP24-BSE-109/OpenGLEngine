package Game;

import Launch.Launcher;

public class GameManager {

    private static GameStates currentState;

    public static GameStates getCurrentState() {
        return currentState;
    }

    public static void setCurrentState(GameStates currentState) {

        switch (currentState){
            default:
            case Start:
                try{
                    Launcher.getGame().startGame();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case Play:
                break;
            case Pause:
                break;
            case Lose, Win:
                try {
                    Launcher.getGame().stopGame();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

        }

        GameManager.currentState = currentState;
    }
}
