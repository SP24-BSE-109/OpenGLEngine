package Core;

import Game.ScoreManager;
import Launch.Launcher;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;

public class EngineManager {
    public static final long NANOSECONDS = 1000000000;
    public static final long FRAMERATE = 1000;

    private static int fps;
    private static float frameTime = 1.0f / FRAMERATE;

    private boolean isRunning;

    private WindowManager window;
    private  MouseInput mouseInput;

    private GLFWErrorCallback errorCallback;

    private ILogic gameLogic;

    private void init() throws Exception {
        GLFW.glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));
        window = Launcher.getWindow();
        gameLogic = Launcher.getGame();
        mouseInput = new MouseInput();
        window.init();
        gameLogic.init();
        mouseInput.init();
    }
    public void start() throws Exception {
        init();
        if (isRunning) {
            return;
        }
        run();
    }

    public void run() {
        isRunning = true;
        int frames = 0;
        long frameCounter = 0;
        long lastTime = System.nanoTime();
        double unprocessedTime = 0;

        while (isRunning) {
            boolean render = false;
            long startTime = System.nanoTime();
            long passedTime = startTime - lastTime;
            lastTime = startTime;

            unprocessedTime += passedTime / (double)NANOSECONDS;
            frameCounter += passedTime;

            input();

            while (unprocessedTime >= frameTime) {
                render = true;
                unprocessedTime -= frameTime;
                if (window.windowShouldClose()){
                    stop();
                }
                if(frameCounter >= NANOSECONDS){
                    setFps(frames);
                    frames = 0;
                    frameCounter = 0;
                }
            }
            if (render) {
                update(frameCounter);
                render();
                frames++;
            }
        }
        cleanup();

    }
    private void stop(){
        if (!isRunning) {
            return;
        }
        isRunning = false;
    }
    private void update(float interval) {
        gameLogic.update(interval,mouseInput);
    }
    private void render(){
        gameLogic.render();
        window.update();
    }
    private void input(){
        gameLogic.input();
        mouseInput.input();
    }
    private void cleanup(){
        window.cleanup();
        gameLogic.cleanup();
        errorCallback.free();
        GLFW.glfwTerminate();
    }

    public static int getFps() {
        return fps;
    }

    public static void setFps(int fps) {
        EngineManager.fps = fps;
    }
    public void closeEngine(){
        cleanup();
    }

}
