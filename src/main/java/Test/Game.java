package Test;

import Core.*;
import Core.Entity.*;
import Core.Entity.Terrain.Terrain;
import Rendering.RenderManager;
import Lighting.DirectionalLight;
import Utils.Consts;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import java.util.List;
import java.util.Random;

public class Game implements ILogic {

    private final RenderManager renderer;
    private final WindowManager window;
    private final ObjectLoader loader;

    private SceneManager sceneManager;

    private Camera camera;
    private Vector3f cameraInc;

    private Entity player;

    public Game() {
        this.renderer = new RenderManager();
        this.window = Launcher.getWindow();
        loader = new ObjectLoader();
        camera = new Camera();
        cameraInc = new Vector3f(0f,0f,0f);
        sceneManager = new SceneManager(-90);
    }

    @Override
    public void init() throws Exception {
        renderer.init();
        // Player Model
        Model model = loader.loadOBJModel("/models/car.obj");
        model.setTexture(new Texture(loader.loadTexture("textures/car.png")), 1f);

        Terrain terrain = new Terrain(new Vector3f(0,-1,-800), loader, new Material(new Texture(loader.loadTexture("textures/stonefloor.png"))));
        sceneManager.addTerrain(terrain);
        Random rnd = new Random();
        for (int i = 0; i < 2; i++) {
            float x = rnd.nextFloat() * 100 - 50;
            float y = rnd.nextFloat() * 100 - 50;
            float z = rnd.nextFloat() * -300;
            sceneManager.addEntity(new Entity(model,
                    new Vector3f(x,y,z),
                    new Vector3f(rnd.nextFloat() * 180,rnd.nextFloat() * 180,rnd.nextFloat() * 180),
                    0.05f));
        }
        // Player entity
        player = new Entity(model, new Vector3f(0,-1,-10), new Vector3f(0,180,0), 0.05f);
        sceneManager.addEntity(player);
        float lightIntensity = 1.0f;
        Vector3f lightPosition = new Vector3f(-1,10,0);
        Vector3f lightColour = new Vector3f(1,1,1);
        DirectionalLight directionalLight = new DirectionalLight(lightColour, lightPosition, lightIntensity);
        sceneManager.setDirectionalLight(directionalLight);

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

        // Move Player
        player.incrementPosition(0,0,-0.1f);

        camera.movePosition(cameraInc.x * Consts.CAMERA_MOVE_SPEED, cameraInc.y * Consts.CAMERA_MOVE_SPEED, cameraInc.z * Consts.CAMERA_MOVE_SPEED);

        if (mouseInput.isLeftPressed()){
            Vector2f rotVector = mouseInput.getDisplayVector();
            camera.moveRotation(rotVector.x * Consts.MOUSE_SENSITIVITY, rotVector.y * Consts.MOUSE_SENSITIVITY, 0);
        }

        List<Entity> entities = sceneManager.getEntities();
        List<Terrain> terrains = sceneManager.getTerrains();
        for (Entity entity : entities) {
            renderer.processEntity(entity);
        }
        for (Terrain terrain : terrains) {
            renderer.processTerrain(terrain);
        }

    }


    @Override
    public void render() {
        if (window.isResize()){
            GL11.glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResize(true);
        }
        renderer.render(camera, sceneManager.getDirectionalLight());
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        loader.cleanup();
    }
}
