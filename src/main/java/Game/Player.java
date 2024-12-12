package Game;

import Core.Entity.Entity;
import Core.Entity.Model;
import Launch.Launcher;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;


public class Player extends Entity {

    private Vector3f moveDir;

    public Player(Model model, Vector3f position, Vector3f rotation, float scale) {
        super(model, position, rotation, scale);
        moveDir = new Vector3f(0, 0, 0);
    }

    public Player(Model model, Vector3f position, Vector3f rotation, float scale, Vector3f collisionBoxMax, Vector3f collisionBoxMin) {
        super(model, position, rotation, scale, collisionBoxMax, collisionBoxMin);
        moveDir = new Vector3f(0, 0, 0);
    }
    @Override
    public void input(){
        moveDir = new Vector3f(0,0,0);
        if (getPosition().x <= 10) {
            if (Launcher.getWindow().isKeyPressed(GLFW.GLFW_KEY_D) || Launcher.getWindow().isKeyPressed(GLFW.GLFW_KEY_RIGHT)){
                moveDir = new Vector3f(1,0,0);
            }
        }
        if(getPosition().x >= -10){
            if (Launcher.getWindow().isKeyPressed(GLFW.GLFW_KEY_A)|| Launcher.getWindow().isKeyPressed(GLFW.GLFW_KEY_LEFT)){
                moveDir = new Vector3f(-1,0,0);
            }
        }
    }

    @Override
    public void update() {
        float moveSpeed = 0.3f;
        incrementPosition(moveDir.x * moveSpeed, 0, -1 * moveSpeed);
    }

}
