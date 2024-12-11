package Game;

import Core.Entity.Entity;
import Core.Entity.Model;
import org.joml.Vector3f;

public class Collectables extends Entity {
    public Collectables(Model model, Vector3f position, Vector3f rotation, float scale) {
        super(model, position, rotation, scale);
    }

    public Collectables(Model model, Vector3f position, Vector3f rotation, float scale, Vector3f collisionBoxMax, Vector3f collisionBoxMin) {
        super(model, position, rotation, scale, collisionBoxMax, collisionBoxMin);
    }

    @Override
    public void input() {

    }

    @Override
    public void update(){
        idleAnimate();
    }

    private void idleAnimate() {
        incrementRotation(0,1,0);
    }
}
