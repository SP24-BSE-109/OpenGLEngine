package Core;

import Test.Launcher;
import org.joml.*;
import org.lwjgl.glfw.GLFW;


public class MouseInput {

    private final Vector2d previousPosition, currentPosition;
    private final Vector2f displayVector;

    private boolean inWindow = false, leftPressed = false, rightPressed = false;

    public MouseInput(Vector2d previousPosition, Vector2d currentPosition, Vector2f displayVector) {
        this.previousPosition = previousPosition;
        this.currentPosition = currentPosition;
        this.displayVector = displayVector;
    }

    public MouseInput() {
        previousPosition = new Vector2d(-1, -1);
        currentPosition = new Vector2d(0, 0);
        displayVector = new Vector2f();
    }
    public void init(){
        GLFW.glfwSetCursorPosCallback(Launcher.getWindow().getWindowHandle() , (window, xpos, ypos) -> {
            currentPosition.x = xpos;
            currentPosition.y = ypos;
        });
        GLFW.glfwSetCursorEnterCallback(Launcher.getWindow().getWindowHandle() , (window, entered) -> {
            inWindow = entered;
        });
        GLFW.glfwSetMouseButtonCallback(Launcher.getWindow().getWindowHandle() , (window, button, action, mods) -> {
            leftPressed = button == GLFW.GLFW_MOUSE_BUTTON_1 && action == GLFW.GLFW_RELEASE;
            rightPressed = button == GLFW.GLFW_MOUSE_BUTTON_2 && action == GLFW.GLFW_RELEASE;
        });
    }
    public void input(){
        displayVector.x = 0;
        displayVector.y = 0;
        if(previousPosition.x>0 && previousPosition.y>0){
            double x = currentPosition.x - previousPosition.x;
            double y = currentPosition.y - previousPosition.y;
            boolean rotateX = x != 0;
            boolean rotateY = y != 0;
            if (rotateX) {
                displayVector.y = (float)x;
            }
            if (rotateY) {
                displayVector.x = (float)y;
            }
        }
        previousPosition.x = currentPosition.x;
        previousPosition.y = currentPosition.y;
    }

    public boolean isRightPressed() {
        return rightPressed;
    }

    public boolean isLeftPressed() {
        return leftPressed;
    }

    public Vector2f getDisplayVector() {
        return displayVector;
    }
}
