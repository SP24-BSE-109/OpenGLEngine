package Core.Entity;

import Utils.Consts;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class Material {

    private Vector4f ambientColour, diffuseColour, specularColour;
    private float reflectivity;
    private Texture texture;

    public Material(){
        this.ambientColour = Consts.DEFAULT_COLOR;
        this.diffuseColour = Consts.DEFAULT_COLOR;
        this.specularColour = Consts.DEFAULT_COLOR;
        this.texture = null;
        this.reflectivity = 0.0f;
    }
    public Material(Vector4f colour, float reflectivity){
        this(colour,colour,colour,reflectivity,null);
    }
    public Material(Texture texture){
        this(Consts.DEFAULT_COLOR,
                Consts.DEFAULT_COLOR,
                Consts.DEFAULT_COLOR,
                0,
                texture);
    }
    public Material(Vector4f colour, float reflectivity, Texture texture){
        this(colour,colour,colour,reflectivity,texture);
    }
    public Material(Vector4f ambientColour,
                    Vector4f diffuseColour,
                    Vector4f specularColour,
                    float reflectivity,
                    Texture texture) {
        this.ambientColour = ambientColour;
        this.diffuseColour = diffuseColour;
        this.specularColour = specularColour;
        this.reflectivity = reflectivity;
        this.texture = texture;
    }

    public Vector4f getAmbientColour() {
        return ambientColour;
    }

    public void setAmbientColour(Vector4f ambientColour) {
        this.ambientColour = ambientColour;
    }

    public Vector4f getDiffuseColour() {
        return diffuseColour;
    }

    public void setDiffuseColour(Vector4f diffuseColour) {
        this.diffuseColour = diffuseColour;
    }

    public Vector4f getSpecularColour() {
        return specularColour;
    }

    public void setSpecularColour(Vector4f specularColour) {
        this.specularColour = specularColour;
    }

    public float getReflectivity() {
        return reflectivity;
    }

    public void setReflectivity(float reflectivity) {
        this.reflectivity = reflectivity;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }
    public boolean hasTexture(){
        return texture != null;
    }
}
