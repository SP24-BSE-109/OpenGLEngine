package Game;

import Core.*;
import Core.Entity.*;
import Core.Entity.Terrain.Terrain;
import Rendering.RenderManager;
import Lighting.DirectionalLight;
import Test.Launcher;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game implements ILogic {

    private final RenderManager renderer;
    private final WindowManager window;
    private final ObjectLoader loader;

    private final SceneManager sceneManager;

    private final Camera camera;
    private DirectionalLight directionalLight;

    private float previousTime;


    private Player player;
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
        GameManager.currentState = GameStates.Start;

        renderer.init();
        // Player Model
        Model model = loader.loadOBJModel("/models/car.obj");
        model.setTexture(new Texture(loader.loadTexture("textures/car.png")), 1f);

        // Fuel model
        Model fuelModel = loader.loadOBJModel("/models/fuel.obj");
        fuelModel.setTexture(new Texture(loader.loadTexture("textures/fuel.png")), 1f);


        Terrain terrain = new Terrain(new Vector3f(-400,-1,-800), loader, new Material(new Texture(loader.loadTexture("textures/floor.png"))));
        sceneManager.addTerrain(terrain);
        Random rnd = new Random();

        float y = 1;
        float z = 0;
        for (int i = 0; i < 200; i++) {
            float x = rnd.nextFloat() * 20 - 10;
            z += 20;
            Collectables fuelEntity = new Collectables(fuelModel, new Vector3f(x,y,-z), new Vector3f(0,0,0), 1f);
            fuelEntity.setName("Fuel");
            fuelEntity.setCollider(new Vector3f(0f,0f,0f), new Vector3f(2,2 ,2));
            sceneManager.addEntity(fuelEntity);
        }
        // Player entity
        player = new Player(model, new Vector3f(0,-1,-10), new Vector3f(0,180,0), 0.05f);
        player.setName("Player");
        player.setCollider(new Vector3f(0f,0f,0f), new Vector3f(2,2 ,2));
        sceneManager.addEntity(player);

        float lightIntensity = 0.5f;
        Vector3f lightPosition = new Vector3f(-1,10,0);
        Vector3f lightColour = new Vector3f(1,1,1);
        directionalLight = new DirectionalLight(lightColour, lightPosition, lightIntensity);
        sceneManager.setDirectionalLight(directionalLight);

        GameManager.currentState = GameStates.Play;
    }

    @Override
    public void input() {
        moveDir = new Vector3f(0,0,0);
        if (player.getPosition().x <= 10) {
            if (window.isKeyPressed(GLFW.GLFW_KEY_D) || window.isKeyPressed(GLFW.GLFW_KEY_RIGHT)){
                moveDir = new Vector3f(1,0,0);
            }
        }
        if(player.getPosition().x >= -10){
            if (window.isKeyPressed(GLFW.GLFW_KEY_A)|| window.isKeyPressed(GLFW.GLFW_KEY_LEFT)){
                moveDir = new Vector3f(-1,0,0);
            }
        }
        if (window.isKeyPressed(GLFW.GLFW_KEY_P)) {
            if (GameManager.currentState == GameStates.Pause){
                GameManager.currentState = GameStates.Play;
            }else if (GameManager.currentState == GameStates.Play){
                GameManager.currentState = GameStates.Pause;
            }
        }

    }

    @Override
    public void update(float interval, MouseInput mouseInput) {
        for (Entity entity : sceneManager.getEntities()) {
            renderer.processEntity(entity);
        }
        for (Terrain terrain : sceneManager.getTerrains()) {
            renderer.processTerrain(terrain);
        }

        if (GameManager.currentState == GameStates.Pause || GameManager.currentState == GameStates.Win || GameManager.currentState == GameStates.Lose) {
            return;
        }

        float currentTime = System.nanoTime();
        float scoreIncrementDelay = 1f;
        if (currentTime > previousTime + scoreIncrementDelay) {
            if (sceneManager.getScoreManager().getScore() <= 0) {
            } else if (sceneManager.getScoreManager().getScore() >= 100) {
                sceneManager.getScoreManager().setScore(100);
                GameManager.currentState = GameStates.Win;
                // Launcher.getEngine().closeEngine();
            } else {
                sceneManager.getScoreManager().decrementScore(1);
            }
        }
        previousTime = currentTime;

        float moveSpeed = 0.3f;
        player.incrementPosition(moveDir.x * moveSpeed, 0, -1 * moveSpeed);
        camera.movePosition(0, 0, -1 * moveSpeed);
        directionalLight.setDirection(player.getPosition());

        List<Entity> toRemove = new ArrayList<>();
        for (Entity entity : sceneManager.getEntities()) {
            if (entity instanceof Collectables && player.isColliding(entity)) {
                sceneManager.getScoreManager().incrementScore(10);
                toRemove.add(entity);
            }
        }
        for (Entity entity : toRemove) {
            sceneManager.removeEntity(entity);
        }

        window.setTitle("Score: " + sceneManager.getScoreManager().getScore());
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
