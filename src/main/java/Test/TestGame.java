package Test;

import Core.*;
import Core.Entity.Entity;
import Core.Entity.Material;
import Core.Entity.Model;
import Core.Entity.Terrain.Terrain;
import Core.Entity.Texture;
import Rendering.RenderManager;
import Lighting.DirectionalLight;
import Utils.Consts;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TestGame implements ILogic {

    private static final float CAMERA_MOVE_SPEED = 0.1f;
    private final RenderManager renderer;
    private final WindowManager window;
    private final ObjectLoader loader;

    private float lightAngle;
    private DirectionalLight directionalLight;

    private List<Entity> entities;
    private List<Terrain> terrains;

    private Camera camera;
    private Vector3f cameraInc;
    public TestGame() {
        this.renderer = new RenderManager();
        this.window = Launcher.getWindow();
        loader = new ObjectLoader();
        camera = new Camera();
        cameraInc = new Vector3f(0f,0f,0f);
        lightAngle = -90;
    }

    @Override
    public void init() throws Exception {
        renderer.init();

        Model model = loader.loadOBJModel("/models/chicken.obj");
        model.setTexture(new Texture(loader.loadTexture("textures/chicken.png")), 1f);

        terrains = new ArrayList<>();
        Terrain terrain = new Terrain(new Vector3f(0,-1,-800), loader, new Material(new Texture(loader.loadTexture("textures/floor.png"))));
        terrains.add(terrain);
        entities = new ArrayList<>();
        Random rnd = new Random();
        for (int i = 0; i < 2; i++) {
            float x = rnd.nextFloat() * 100 - 50;
            float y = rnd.nextFloat() * 100 - 50;
            float z = rnd.nextFloat() * -300;
            entities.add(new Entity(model,
                    new Vector3f(x,y,z),
                    new Vector3f(rnd.nextFloat() * 180,rnd.nextFloat() * 180,rnd.nextFloat() * 180),
                    1));
        }
        entities.add(new Entity(model, new Vector3f(0,0,-5), new Vector3f(0,0,0), 1));
        float lightIntensity = 0.0f;
        Vector3f lightPosition = new Vector3f(-1,10,0);
        Vector3f lightColour = new Vector3f(1,1,1);
        directionalLight = new DirectionalLight(lightPosition, lightColour, lightIntensity);
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

        lightAngle += 0.5f;
        if (lightAngle > 90) {
            directionalLight.setIntensity(0);
            if (lightAngle > 360){
                lightAngle = -90;
            }
        } else if (lightAngle <= -80 || lightAngle >= 80) {
            float factor = 1 - (Math.abs(lightAngle) - 80) / 10.0f;
            directionalLight.setIntensity(factor);
            directionalLight.getColour().y = Math.max(factor,0.9f);
            directionalLight.getColour().z = Math.max(factor,0.5f);
        }else {
            directionalLight.setIntensity(1);
            directionalLight.getColour().x = 1f;
            directionalLight.getColour().y = 1f;
            directionalLight.getColour().z = 1f;
        }
        double angleRad = Math.toRadians(lightAngle);
        directionalLight.getDirection().x = (float) Math.sin(angleRad);
        directionalLight.getDirection().y = (float) Math.cos(angleRad);

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
        renderer.render(camera, directionalLight);
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        loader.cleanup();
    }
}
