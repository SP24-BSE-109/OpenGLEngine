package Core.GUI;

import org.joml.Vector2f;

public abstract class GUIElement {

    protected Vector2f position;
    protected Vector2f size;
    protected boolean visible = true;

    public abstract void render();
    public abstract void update(float deltaTime);

}
