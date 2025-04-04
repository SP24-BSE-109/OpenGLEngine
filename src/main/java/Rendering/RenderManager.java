package Rendering;

import Core.Camera;
import Core.Entity.Entity;
import Core.Entity.Terrain.Terrain;
import Core.ShaderManager;
import Core.WindowManager;
import Lighting.DirectionalLight;
import Launch.Launcher;
import Utils.*;

import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class RenderManager {

    private final WindowManager window;
    private EntityRenderer entityRenderer;

    private TerrainRenderer terrainRenderer;

    public RenderManager() {
        this.window = Launcher.getWindow();
    }

    public void init() throws Exception {
        entityRenderer = new EntityRenderer();
        terrainRenderer = new TerrainRenderer();
        entityRenderer.init();
        terrainRenderer.init();
    }

    public static void renderLights(DirectionalLight directionalLight, ShaderManager shader) {
        shader.setUniform("ambientLight", Consts.AMBIENT_LIGHT);
        shader.setUniform("specularPower",Consts.SPECULAR_POWER);
        shader.setUniform("directionalLight", directionalLight);
    }
    public void render(Camera camera, DirectionalLight directionalLight) {
        clear();
        entityRenderer.render(camera, directionalLight);
        terrainRenderer.render(camera, directionalLight);
    }
    public void processEntity(Entity entity) {
        List<Entity> entityList = entityRenderer.getEntities().get(entity.getModel());
        if (entityList != null) {
            entityList.add(entity);
        }else {
            List<Entity> newEntityList = new ArrayList<>();
            newEntityList.add(entity);
            entityRenderer.getEntities().put(entity.getModel(), newEntityList);
        }
    }
    public void processTerrain(Terrain terrain) {
        terrainRenderer.getTerrains().add(terrain);
    }

    public void clear() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }
    public void cleanup(){
        entityRenderer.cleanup();
        terrainRenderer.cleanup();
    }
}
