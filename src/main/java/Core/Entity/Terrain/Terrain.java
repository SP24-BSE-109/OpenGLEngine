package Core.Entity.Terrain;

import Core.Entity.Material;
import Core.Entity.Model;
import Core.Entity.Texture;
import Core.ObjectLoader;
import org.joml.Vector3f;

public class Terrain {

    private static final float SIZE = 800;
    private static final int VERTEX_COUNT = 128;

    private Vector3f position;
    private Model model;

    public Terrain(Vector3f position, ObjectLoader loader, Material material) {
        this.position = position;
        this.model = generateTerrain(loader);
        this.model.setMaterial(material);
    }

    private Model generateTerrain(ObjectLoader loader) {
        int count = VERTEX_COUNT * VERTEX_COUNT;
        float[] vertices = new float[count * 3];
        float[] normals = new float[count * 3];
        float[] texCoords = new float[count * 2];

        int[] indices = new int[6 * (VERTEX_COUNT - 1) * (VERTEX_COUNT - 1)];

        int vertexIndex = 0;
        for (int i = 0; i < VERTEX_COUNT; i++) {
            for (int j = 0; j < VERTEX_COUNT; j++) {
                vertices[vertexIndex * 3] = j / (VERTEX_COUNT - 1.0f) * SIZE;
                vertices[vertexIndex * 3 + 1] = 0;
                vertices[vertexIndex * 3 + 2] = i / (VERTEX_COUNT - 1.0f) * SIZE;
                normals[vertexIndex * 3] = 0;
                normals[vertexIndex * 3 + 1] = 1;
                normals[vertexIndex * 3 + 2] = 0;
                texCoords[vertexIndex * 2] = j / (VERTEX_COUNT - 1.0f) * SIZE;
                texCoords[vertexIndex * 2 + 1] = i / (VERTEX_COUNT - 1.0f) * SIZE;
                vertexIndex++;
            }
        }
        int pointer = 0;
        for (int z = 0; z < VERTEX_COUNT - 1; z++) {
            for (int x = 0; x < VERTEX_COUNT - 1; x++) {
                int topLeft = (z * VERTEX_COUNT) + x;
                int topRight = topLeft + 1;
                int bottomLeft = ((z + 1) * VERTEX_COUNT) + x;
                int bottomRight = bottomLeft + 1;

                indices[pointer++] = topLeft;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = topRight;
                indices[pointer++] = topRight;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = bottomRight;

            }
        }
        return loader.loadModel(vertices, indices, texCoords, normals);

    }

    public Vector3f getPosition() {
        return position;
    }

    public Model getModel() {
        return model;
    }
    public Material getMaterial() {
        return model.getMaterial();
    }
    public Texture getTexture() {
        return model.getMaterial().getTexture();
    }
}
