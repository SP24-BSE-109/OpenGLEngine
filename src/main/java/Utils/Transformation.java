package Utils;

import Core.Camera;
import Core.Entity.Entity;
import Core.Entity.Terrain.Terrain;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Transformation {

    public static Matrix4f createTransformationMatrix(Entity entity) {
        Matrix4f matrix = new Matrix4f();
        matrix.identity().translate(entity.getPosition()).
                rotateX((float) Math.toRadians(entity.getRotation().x)).
                rotateY((float) Math.toRadians(entity.getRotation().y)).
                rotateZ((float) Math.toRadians(entity.getRotation().z)).
                scale(entity.getScale());
        return matrix;
    }
    public static Matrix4f createTransformationMatrix(Terrain terrain) {
        Matrix4f matrix = new Matrix4f();
        matrix.identity().translate(terrain.getPosition()).scale(1);
        return matrix;
    }
    public static Matrix4f getViewMatrix(Camera camera) {
        Vector3f position = camera.getPosition();
        Vector3f rotation = camera.getRotation();
        Matrix4f matrix = new Matrix4f();
        matrix.identity();
        matrix.rotate((float) Math.toRadians(rotation.x), new Vector3f(1f, 0f, 0f) ).
                rotate((float) Math.toRadians(rotation.y), new Vector3f(0f,1f,0f)).
                rotate((float) Math.toRadians(rotation.z), new Vector3f(0f,0f,1f));
        matrix.translate(-position.x, -position.y, -position.z);
        return matrix;
    }
}
