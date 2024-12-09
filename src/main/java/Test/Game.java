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
    private DirectionalLight directionalLight;

    private Entity player;
    private Vector3f moveDir;

    public Game() {
        this.renderer = new RenderManager();
        this.window = Launcher.getWindow();
        loader = new ObjectLoader();
        camera = new Camera();
        camera.setPosition(0,10,0);
        camera.setRotation(45,0,0);
        sceneManager = new SceneManager(-90);
    }

    @Override
    public void init() throws Exception {
        renderer.init();
        // Player Model
        Model model = loader.loadOBJModel("/models/car.obj");
        model.setTexture(new Texture(loader.loadTexture("textures/car.png")), 1f);

        // Fuel model
        Model fuelModel = loader.loadOBJModel("/models/fuel.obj");
        fuelModel.setTexture(new Texture(loader.loadTexture("textures/fuel.png")), 1f);


        Terrain terrain = new Terrain(new Vector3f(-400,-1,-800), loader, new Material(new Texture(loader.loadTexture("textures/stonefloor.png"))));
        sceneManager.addTerrain(terrain);
        Random rnd = new Random();

        float y = 1;
        float z = 0;
        for (int i = 0; i < 200; i++) {
            float x = rnd.nextFloat() * 10 - 5;
            z += 10;
            sceneManager.addEntity(new Entity(fuelModel,
                    new Vector3f(x,y,-z),
                    new Vector3f(0,0,0),
                    1f));
        }
        // Player entity
        player = new Entity(model, new Vector3f(0,-1,-10), new Vector3f(0,180,0), 0.05f);
        sceneManager.addEntity(player);
        float lightIntensity = 0.5f;
        Vector3f lightPosition = new Vector3f(-1,10,0);
        Vector3f lightColour = new Vector3f(1,1,1);
        directionalLight = new DirectionalLight(lightColour, lightPosition, lightIntensity);
        sceneManager.setDirectionalLight(directionalLight);


    }

    @Override
    public void input() {
        moveDir = new Vector3f(0,0,0);
        if (player.getPosition().x >= 10 || player.getPosition().x <= -10) {
            return;
        }

        if (window.isKeyPressed(GLFW.GLFW_KEY_A)){
            moveDir = new Vector3f(-1,0,0);
        }
        if (window.isKeyPressed(GLFW.GLFW_KEY_D)){
            moveDir = new Vector3f(1,0,0);
        }
    }
    @Override
    public void update(float interval, MouseInput mouseInput) {

        // Move Player
        float moveSpeed = 0.1f;
        player.incrementPosition(moveDir.x * moveSpeed, 0,-1 * moveSpeed);
        camera.movePosition(0,0,-1 * moveSpeed);
        directionalLight.setDirection(player.getPosition());



        // camera.movePosition(cameraInc.x * Consts.CAMERA_MOVE_SPEED, cameraInc.y * Consts.CAMERA_MOVE_SPEED, cameraInc.z * Consts.CAMERA_MOVE_SPEED);
//
//        if (mouseInput.isLeftPressed()){
//            Vector2f rotVector = mouseInput.getDisplayVector();
//            camera.moveRotation(rotVector.x * Consts.MOUSE_SENSITIVITY, rotVector.y * Consts.MOUSE_SENSITIVITY, 0);
//        }

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
