package Core.Entity;

public class Model {
    private int id;
    private Material material;
    private int vertexCount;

    public Model(int id, int vertexCount) {
        this.id = id;
        this.vertexCount = vertexCount;
        this.material = new Material();
    }

    public Model(int id, Texture texture, int vertexCount) {
        this.id = id;
        this.material = new Material(texture);
        this.vertexCount = vertexCount;
    }

    public Model(Model model, Texture texture) {
        this.id = model.id;
        this.vertexCount = model.vertexCount;
        this.material = model.getMaterial();
        this.material.setTexture(texture);
    }

    public int getId() {
        return id;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }
    public Texture getTexture() {
        return material.getTexture();
    }
    public void setTexture(Texture texture) {
        this.material.setTexture(texture);
    }
    public void setTexture(Texture texture, float reflectivity) {
        this.material.setTexture(texture);
        this.material.setReflectivity(reflectivity);
    }
}
