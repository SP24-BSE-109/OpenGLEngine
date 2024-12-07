package Core.Entity;

public class Model {
    private int id;
    private Texture texture;
    private int vertexCount;

    public Model(int id, int vertexCount) {
        this.id = id;
        this.vertexCount = vertexCount;
    }

    public Model(int id, Texture texture, int vertexCount) {
        this.id = id;
        this.texture = texture;
        this.vertexCount = vertexCount;
    }

    public Model(Model model, Texture texture) {
        this.id = model.id;
        this.vertexCount = model.vertexCount;
        this.texture = texture;
    }

    public int getId() {
        return id;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }
}
