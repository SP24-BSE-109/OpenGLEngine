package Core.Entity;

import org.joml.Vector3f;

public abstract class Entity {

    private String name;
    private Model model;
    private Vector3f position;
    private Vector3f rotation;
    private float scale;

    private Vector3f collisionBoxMax;
    private Vector3f collisionBoxMin;


    public Entity(Model model, Vector3f position, Vector3f rotation, float scale) {
        this.model = model;
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
        this.collisionBoxMax = new Vector3f(0,0,0);
        this.collisionBoxMin = new Vector3f(0,0,0);

    }
    public Entity(Model model, Vector3f position, Vector3f rotation, float scale, Vector3f collisionBoxMax , Vector3f collisionBoxMin) {
        this.model = model;
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
        this.collisionBoxMax = collisionBoxMax;
        this.collisionBoxMin = collisionBoxMin;
    }


    public void incrementPosition(float x, float y, float z) {
        this.position.x += x;
        this.position.y += y;
        this.position.z += z;
    }
    public void setPosition(float x, float y, float z) {
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
    }
    public void incrementRotation(float x, float y, float z) {
        this.rotation.x += x;
        this.rotation.y += y;
        this.rotation.z += z;
    }
    public void setRotation(float x, float y, float z) {
        this.rotation.x = x;
        this.rotation.y = y;
        this.rotation.z = z;
    }
    public boolean isColliding(Entity e) {
        Vector3f thisMin = new Vector3f(this.collisionBoxMin).add(this.position);
        Vector3f thisMax = new Vector3f(this.collisionBoxMax).add(this.position);
        Vector3f otherMin = new Vector3f(e.getCollisionBoxMin()).add(e.getPosition());
        Vector3f otherMax = new Vector3f(e.getCollisionBoxMax()).add(e.getPosition());

        return (thisMin.x <= otherMax.x && thisMax.x >= otherMin.x) &&
                (thisMin.y <= otherMax.y && thisMax.y >= otherMin.y) &&
                (thisMin.z <= otherMax.z && thisMax.z >= otherMin.z);
    }
    public abstract void input();
    public abstract void update();

    public Model getModel() {
        return model;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public float getScale() {
        return scale;
    }
    public Vector3f getCollisionBoxMax() {
        return collisionBoxMax;
    }
    public Vector3f getCollisionBoxMin() {
        return collisionBoxMin;
    }

    public void setCollisionBoxMax(Vector3f collisionBoxMax) {
        this.collisionBoxMax = collisionBoxMax;
    }

    public void setCollisionBoxMin(Vector3f collisionBoxMin) {
        this.collisionBoxMin = collisionBoxMin;
    }
    public void setCollider(Vector3f collisionBoxMin, Vector3f collisionBoxMax) {
        this.collisionBoxMin = collisionBoxMin;
        this.collisionBoxMax = collisionBoxMax;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void destroy(){
        this.model = null;
        this.position = null;
        this.rotation = null;
        this.scale = 0f;
        this.collisionBoxMax = null;
        this.collisionBoxMin = null;
    }

}
