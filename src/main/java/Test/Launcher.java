package Test;

import Core.DisplayManager;
import Core.EngineManager;
import Utils.*;

public class Launcher {

    public static DisplayManager display;
    public static EngineManager engine;
    public static void main(String[] args) {
        display = new DisplayManager(Consts.TITLE, 1280,720,true);

        engine = new EngineManager();
        try{
            engine.start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static DisplayManager getDisplayManager() {
        return display;
    }
}