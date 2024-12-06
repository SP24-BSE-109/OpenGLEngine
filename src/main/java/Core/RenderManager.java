package Core;

import Test.Launcher;
import org.lwjgl.opengl.GL11;

public class RenderManager {

    private final DisplayManager display;

    public RenderManager() {
        this.display = Launcher.getDisplayManager();
    }

    public void init() throws Exception {

    }
    public void render() {

    }
    public void clear() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }
    public void cleanup(){

    }
}
