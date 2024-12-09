package Test;

import Core.WindowManager;
import Core.EngineManager;
import Utils.*;

public class Launcher {

    public static WindowManager display;
    public static EngineManager engine;
    private static Game game;

    public static void main(String[] args) {
        display = new WindowManager(Consts.TITLE, 1280,720,true);
        game = new Game();
        engine = new EngineManager();
        try{
            engine.start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static WindowManager getWindow() {
        return display;
    }
    public static Game getGame() {
        return game;
    }
}