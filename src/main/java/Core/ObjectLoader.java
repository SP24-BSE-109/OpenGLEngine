package Core;

import Core.Entity.Model;
import Utils.Utils;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class ObjectLoader {

    ArrayList<Integer> vaos = new ArrayList<Integer>();
    ArrayList<Integer> vbos = new ArrayList<Integer>();
    ArrayList<Integer> textures = new ArrayList<Integer>();

    public Model loadModel(float[] vertices, int[] indices, float[] textureCoords, float[] normals) {
        int id = createVao();
        storeIndicesBuffer(indices);
        storeDataInAttributeList(0,3, vertices);

        storeDataInAttributeList(1,2, textureCoords);
        storeDataInAttributeList(2,3, normals);
        unbind();
        return new Model(id,indices.length);
    }
    public Model loadOBJModel(String filename) {
        List<String> lines = Utils.readAllLines(filename);

        List<Vector3f> vertices = new ArrayList<>();
        List<Vector3f> normals = new ArrayList<>();
        List<Vector2f> textures = new ArrayList<>();

        List<Vector3i> faces = new ArrayList<>();

        for (String line : lines) {
            String[] tokens = line.split("\\s+");
            switch (tokens[0]) {
                case"v":
                    // Vertices
                    Vector3f verticesVector = new Vector3f(
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2]),
                            Float.parseFloat(tokens[3])
                    );
                    vertices.add(verticesVector);
                    break;
                case "vt":
                    // Vertex Textures
                    Vector2f texturesVector = new Vector2f(
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2])
                    );
                    textures.add(texturesVector);
                    break;
                case "vn":
                    // Vertex Normals
                    Vector3f normalsVector = new Vector3f(
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2]),
                            Float.parseFloat(tokens[3])
                    );
                    normals.add(normalsVector);
                    break;
                case "f":
                    // Faces
                    if (tokens.length == 5) {
                        processFace(tokens[1], faces);
                        processFace(tokens[2], faces);
                        processFace(tokens[3], faces);

                        processFace(tokens[1], faces);
                        processFace(tokens[3], faces);
                        processFace(tokens[4], faces);
                    } else {
                        processFace(tokens[1], faces);
                        processFace(tokens[2], faces);
                        processFace(tokens[3], faces);
                    }
                    break;
                default:
                    break;
            }
        }
        List<Integer> indices = new ArrayList<>();
        float[] verticesArray = new float[vertices.size() * 3];
        int i = 0;
        for (Vector3f pos : vertices) {
            verticesArray[i * 3] = pos.x;
            verticesArray[i * 3 + 1] = pos.y;
            verticesArray[i * 3 + 2] = pos.z;
            i++;
        }
        float[] textureCoordsArray = new float[textures.size() * 2];
        float[] normalsArray = new float[normals.size() * 3];

        for (Vector3i face : faces) {
            processVertex(face.x,face.y,face.z, textures, normals, indices,textureCoordsArray,normalsArray);
        }
        int[] indicesArray = indices.stream().mapToInt((Integer v) -> (v)).toArray();
        return loadModel(verticesArray,indicesArray, textureCoordsArray, normalsArray);
    }
    private static void processVertex(int pos,
                                      int textCoords,
                                      int normal,
                                      List<Vector2f> textCoordsList,
                                      List<Vector3f> normalsList,
                                      List<Integer> indicesList,
                                      float[] textCoordsArray,
                                      float[] normalArray) {

        indicesList.add(pos);
        if (textCoords >= 0) {
            Vector2f textCoordsVector = textCoordsList.get(textCoords);
            textCoordsArray[pos * 2] = textCoordsVector.x;
            textCoordsArray[pos * 2 + 1] = 1 - textCoordsVector.y;
        }
        if (normal >= 0){
            Vector3f normalVector = normalsList.get(normal);
            normalArray[pos * 3] = normalVector.x;
            normalArray[pos * 3 + 1] = normalVector.y;
            normalArray[pos * 3 + 2] = normalVector.z;
        }
    }
    private static void processFace(String token, List<Vector3i> faces) {
        String[] lineTokens = token.split("/");
        int length = lineTokens.length;
        int pos = -1;
        int coords = -1;
        int normals = -1;
        pos = Integer.parseInt(lineTokens[0])-1;
        if (length > 1){
            String texCoords = lineTokens[1];
            coords = texCoords.length() > 0 ? Integer.parseInt(texCoords) - 1 : -1;
            if (length > 2){
                normals = Integer.parseInt(lineTokens[2]) - 1;
            }
        }
        Vector3i facesVector = new Vector3i(pos,coords,normals);
        faces.add(facesVector);
    }
    public int loadTexture(String filename) throws Exception {
        int width, height;
        ByteBuffer buffer;
        try(MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer c = stack.mallocInt(1);

            buffer = STBImage.stbi_load(filename, w, h, c, 4);
            if (buffer == null) {
                throw new Exception("Image File: " + filename + " could not be loaded " + STBImage.stbi_failure_reason());
            }
            width = w.get();
            height = h.get();
        }
        int id = GL11.glGenTextures();
        textures.add(id);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA,width,height,0,GL11.GL_RGBA,GL11.GL_UNSIGNED_BYTE,buffer);
        GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
        STBImage.stbi_image_free(buffer);
        return id;
    }
    private int createVao() {
        int id = GL30.glGenVertexArrays();
        vaos.add(id);
        GL30.glBindVertexArray(id);
        return id;
    }
    private void storeIndicesBuffer(int[] indices) {
        int vbo = GL15.glGenBuffers();
        vbos.add(vbo);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vbo);
        IntBuffer buffer = Utils.storeDataInIntBuffer(indices);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
    }
    private void storeDataInAttributeList(int attributeNumber, int vertexCount, float[] data) {
        int vbo = GL15.glGenBuffers();
        vbos.add(vbo);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        FloatBuffer buffer = Utils.storeDataInFloatBuffer(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(attributeNumber, vertexCount, GL11.GL_FLOAT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

    }
    private void unbind(){
        GL30.glBindVertexArray(0);
    }
    public void cleanup(){
        for (int vao : vaos) {
            GL30.glDeleteVertexArrays(vao);
        }
        for (int vbo : vbos) {
            GL30.glDeleteVertexArrays(vbo);
        }
        for (int texture : textures) {
            GL30.glDeleteTextures(texture);
        }
    }
}
