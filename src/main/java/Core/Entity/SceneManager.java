package Core.Entity;

import Core.Entity.Terrain.Terrain;
import Lighting.DirectionalLight;
import Utils.Consts;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class SceneManager {

    private List<Entity> entities;
    private List<Terrain> terrains;

    private Vector3f ambientLight;
    private DirectionalLight directionalLight;

    private float lightAngle;

    public SceneManager(float lightAngle) {
        entities = new ArrayList<Entity>();
        terrains = new ArrayList<Terrain>();
        this.lightAngle = lightAngle;
        ambientLight = Consts.AMBIENT_LIGHT;
    }
    public void addTerrain(Terrain terrain) {
        terrains.add(terrain);
    }
    public void addEntity(Entity entity) {
        entities.add(entity);
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public void setEntities(List<Entity> entities) {
        this.entities = entities;
    }

    public List<Terrain> getTerrains() {
        return terrains;
    }

    public void setTerrains(List<Terrain> terrains) {
        this.terrains = terrains;
    }

    public Vector3f getAmbientLight() {
        return ambientLight;
    }

    public void setAmbientLight(float x, float y, float z){
        ambientLight = new Vector3f(x,y,z);
    }
    public void setAmbientLight(Vector3f ambientLight) {
        this.ambientLight = ambientLight;
    }

    public DirectionalLight getDirectionalLight() {
        return directionalLight;
    }

    public void setDirectionalLight(DirectionalLight directionalLight) {
        this.directionalLight = directionalLight;
    }

    public float getLightAngle() {
        return lightAngle;
    }

    public void setLightAngle(float lightAngle) {
        this.lightAngle = lightAngle;
    }
}
