package Game;

import Core.Entity.Entity;
import Core.Entity.Model;
import org.joml.Vector3f;

public class Player extends Entity {
    public Player(Model model, Vector3f position, Vector3f rotation, float scale) {
        super(model, position, rotation, scale);
    }

    public Player(Model model, Vector3f position, Vector3f rotation, float scale, Vector3f collisionBoxMax, Vector3f collisionBoxMin) {
        super(model, position, rotation, scale, collisionBoxMax, collisionBoxMin);
    }
}
