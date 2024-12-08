package Core;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

public class WindowManager {

    public static final float FOV = (float) Math.toRadians(60.0f);
    public static final float Z_NEAR = 0.01f;
    public static final float Z_FAR = 1000f;

    private String title;
    private int height, width;
    private long window;

    private boolean resize, vSync;

    private final Matrix4f projectionMatrix;

    public WindowManager(String title, int width, int height, boolean vSync) {
        this.title = title;
        this.width = width;
        this.height = height;
        this.vSync = vSync;
        this.projectionMatrix = new Matrix4f();
    }

    public void init() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!GLFW.glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }


        GLFW.glfwDefaultWindowHints();
        // Set screen to visible
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);

        // Window Resizable
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);

        // GLFW Version
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 2);

        // Use OpenGL rendering
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE,GLFW.GLFW_OPENGL_CORE_PROFILE);

        // Any functions that are deprecated in the current version of OpenGL will also be deprecated in the code
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GL11.GL_TRUE);

        boolean maximized = false;

        // Maximize window
        if (width == 0 || height == 0 ) {
            width = 1280;
            height = 720;

            GLFW.glfwWindowHint(GLFW.GLFW_MAXIMIZED, GLFW.GLFW_TRUE);
            maximized = true;
        }
        window = GLFW.glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL);
        if (window == MemoryUtil.NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        GLFW.glfwSetFramebufferSizeCallback(window, (window, width, height) -> {
           this.width = width;
           this.height = height;
           this.setResize(true);
        });

        GLFW.glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            // Close the window when escape key pressed
            if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE) {
                GLFW.glfwSetWindowShouldClose(window, true);
            }
        });

        // Maximize else center the window in the screen
        if (maximized) {
            GLFW.glfwMaximizeWindow(window);
        }
        else {
            // Center the window
            GLFWVidMode vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
            GLFW.glfwSetWindowPos(window, (vidmode.width() - width) / 2, vidmode.height() - height);
        }
        GLFW.glfwMakeContextCurrent(window);

        if (isvSync()) {
            GLFW.glfwSwapInterval(1);
        }

        // Show the window
        GLFW.glfwShowWindow(window);

        GL.createCapabilities();
        // Black window colour
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_STENCIL_TEST);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
    }
    // Update the graphics on the window
    public void update() {
        GLFW.glfwSwapBuffers(window);
        // Start displaying the windows currently in queue
        GLFW.glfwPollEvents();
    }
    // Destroy window
    public void cleanup() {
        GLFW.glfwDestroyWindow(window);
    }
    public void setClearColour(float red, float green, float blue, float alpha) {
        GL11.glClearColor(red, green, blue, alpha);
    }
    public boolean isKeyPressed(int keycode) {
        return GLFW.glfwGetKey(window, keycode) == GLFW.GLFW_PRESS;
    }
    public boolean windowShouldClose() {
        return GLFW.glfwWindowShouldClose(window);
    }

    public void setTitle(String title) {
        GLFW.glfwSetWindowTitle(window, title);
        this.title = title;
    }

    public boolean isvSync() {
        return vSync;
    }

    public void setvSync(boolean vSync) {
        this.vSync = vSync;
    }

    public boolean isResize() {
        return resize;
    }

    public void setResize(boolean resize) {
        this.resize = resize;
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }
    public Matrix4f updateProjectionMatrix() {
        float aspectRatio = (float) width / (float) height;
        return projectionMatrix.setPerspective(FOV, aspectRatio, Z_NEAR, Z_FAR);
    }
    public Matrix4f updateProjectionMatrix(Matrix4f matrix, int width, int height) {
        float aspectRatio = (float) width / (float) height;
        return projectionMatrix.setPerspective(FOV, aspectRatio, Z_NEAR, Z_FAR);
    }

    public long getWindowHandle() {
        return window;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
