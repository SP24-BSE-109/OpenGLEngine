package Rendering;

import Core.Camera;
import Core.Entity.Model;
import Lighting.DirectionalLight;

public interface IRenderer<T> {
    public void init() throws Exception;
    public void render(Camera camera, DirectionalLight directionalLight);

    abstract void bind(Model model);
    public void unbind();
    public void prepare(T t, Camera camera);
    public void cleanup();
}
