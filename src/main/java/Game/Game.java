package Game;

import Core.*;
import Core.Entity.*;
import Core.Entity.Terrain.Terrain;
import Rendering.RenderManager;
import Lighting.DirectionalLight;
import Launch.Launcher;
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



    private Player player;

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
        GameManager.setCurrentState(GameStates.Start);
    }

    public void startGame() throws Exception {
        // Player Model
        Model model = loader.loadOBJModel("/models/car.obj");
        model.setTexture(new Texture(loader.loadTexture("textures/car.png")), 1f);

        // Fuel model
        Model fuelModel = loader.loadOBJModel("/models/chicken.obj");
        fuelModel.setTexture(new Texture(loader.loadTexture("textures/chicken.png")), 1f);
        Terrain terrain = new Terrain(new Vector3f(-400,-1,-800),
                loader,
                new Material(new Texture(loader.loadTexture("textures/floor.png"))));

        sceneManager.addTerrain(terrain);

        Random rnd = new Random();
        float y = 1;
        float z = 0;
        for (int i = 0; i < 200; i++) {
            float x = rnd.nextFloat() * 20 - 10;
            z += 20;
            Collectables fuelEntity = new Collectables(fuelModel, new Vector3f(x,y,-z), new Vector3f(0,0,0), 0.01f);
            fuelEntity.setCollider(new Vector3f(0f,0f,0f), new Vector3f(2,2 ,2));
            sceneManager.addEntity(fuelEntity);
        }
        // Player entity
        player = new Player(model, new Vector3f(0,-1,-10), new Vector3f(0,180,0), 0.05f);
        player.setCollider(new Vector3f(0f,0f,0f), new Vector3f(2,2 ,2));

        sceneManager.addEntity(player);

        float lightIntensity = 0.5f;
        Vector3f lightPosition = new Vector3f(-1,10,0);
        Vector3f lightColour = new Vector3f(1,1,1);
        directionalLight = new DirectionalLight(lightColour, lightPosition, lightIntensity);
        sceneManager.setDirectionalLight(directionalLight);
        GameManager.setCurrentState(GameStates.Play);

        System.out.println("Started Game");
    }

    @Override
    public void input() {
        player.input();

        if (window.isKeyPressed(GLFW.GLFW_KEY_P)) {
            if (GameManager.getCurrentState() == GameStates.Pause) {
                GameManager.setCurrentState(GameStates.Play);
            }else if (GameManager.getCurrentState() == GameStates.Play) {
                GameManager.setCurrentState(GameStates.Pause);
            }
        }

    }

    @Override
    public void update(float interval, MouseInput mouseInput) {
        processEntities();

        if (GameManager.getCurrentState() == GameStates.Pause ||
                GameManager.getCurrentState() == GameStates.Win ||
                GameManager.getCurrentState() == GameStates.Lose) {
            if (GameManager.getCurrentState() == GameStates.Win || GameManager.getCurrentState() == GameStates.Lose) {
                stopGame();
            }
            return;
        }

        updateCalls();


        float moveSpeed = 0.3f;
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

    private void updateCalls() {
        for (Entity entity : sceneManager.getEntities()) {
            entity.update();
        }
        sceneManager.getScoreManager().update();
    }
    public void restartGame() throws Exception {
        stopGame();
        startGame();

    }

    public void stopGame() {
//        // Remove all entities
//        for (Entity entity : sceneManager.getEntities()) {
//            sceneManager.removeEntity(entity);
//        }
//        // Remove all terrains
//        for (Terrain terrain : sceneManager.getTerrains()) {
//            sceneManager.removeTerrain(terrain);
//        }

    }

    private void processEntities() {
        for (Entity entity : sceneManager.getEntities()) {
            renderer.processEntity(entity);
        }
        for (Terrain terrain : sceneManager.getTerrains()) {
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
