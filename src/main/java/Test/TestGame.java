package Test;

import Core.*;
import Core.Entity.Entity;
import Core.Entity.Model;
import Core.Entity.Texture;
import Core.RenderManager;
import Utils.Consts;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

public class TestGame implements ILogic {

    private static final float CAMERA_MOVE_SPEED = 0.1f;
    private final RenderManager renderer;
    private final WindowManager window;
    private final ObjectLoader loader;

    private Entity entity;
    private Camera camera;
    private Vector3f cameraInc;
    public TestGame() {
        this.renderer = new RenderManager();
        this.window = Launcher.getWindow();
        loader = new ObjectLoader();
        camera = new Camera();
        cameraInc = new Vector3f(0f,0f,0f);
    }

    @Override
    public void init() throws Exception {
        renderer.init();


        Model model = loader.loadOBJModel("/models/chicken.obj");

        model.setTexture(new Texture(loader.loadTexture("textures/chicken.png")));
        entity = new Entity(model, new Vector3f(0,0,-100),new Vector3f(0,0,0), 1);
    }

    @Override
    public void input() {
        cameraInc.set(0, 0, 0);
        if (window.isKeyPressed(GLFW.GLFW_KEY_W)) {
            cameraInc.z = -1;
        }
        if (window.isKeyPressed(GLFW.GLFW_KEY_S)) {
            cameraInc.z = 1;
        }
        if (window.isKeyPressed(GLFW.GLFW_KEY_A)) {
            cameraInc.x = -1;
        }
        if (window.isKeyPressed(GLFW.GLFW_KEY_D)) {
            cameraInc.x = 1;
        }
        if (window.isKeyPressed(GLFW.GLFW_KEY_Z)) {
            cameraInc.y = -1;
        }
        if (window.isKeyPressed(GLFW.GLFW_KEY_X)) {
            cameraInc.y = 1;
        }
    }
    @Override
    public void update(float interval, MouseInput mouseInput) {
        camera.movePosition(cameraInc.x * CAMERA_MOVE_SPEED, cameraInc.y * CAMERA_MOVE_SPEED, cameraInc.z * CAMERA_MOVE_SPEED);
        // entity.incrementRotation(0.5f,0.5f,0.5f);

        if (mouseInput.isLeftPressed()){
            Vector2f rotVector = mouseInput.getDisplayVector();
            camera.moveRotation(rotVector.x * Consts.MOUSE_SENSITIVITY, rotVector.y * Consts.MOUSE_SENSITIVITY, 0);
        }

    }


    @Override
    public void render() {
        if (window.isResize()){
            GL11.glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResize(true);
        }
        window.setClearColour(0f,0f,0f,0.0f);
        renderer.render(entity,camera);
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        loader.cleanup();
    }
}
