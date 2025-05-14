package net.henrycmoss;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallbackI;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;


public class Chopped {

    private long window;

    private float blue = 0f;

    private final Map<List<Integer>, GLFWKeyCallbackI> inputMap = new HashMap<>();

    public void run() {

        System.out.println("LWJGL VERS.: " + Version.getVersion());

        init();
        loop();

        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init() {

        GLFWErrorCallback.createPrint(System.err).set();

        if(!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, 0);
        glfwWindowHint(GLFW_RESIZABLE, 1);

        window = glfwCreateWindow(300, 300, "Chopped", NULL, NULL);

        if(window == NULL) {
            throw new RuntimeException("Failed to create GLFW Window");
        }



        try (MemoryStack stack = stackPush()) {
            IntBuffer width = stack.mallocInt(1);
            IntBuffer height = stack.mallocInt(1);

            glfwGetWindowSize(window, width, height);

            GLFWVidMode videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            glfwSetWindowPos(window,
                    (videoMode.width() - width.get(0)) / 2,
                    (videoMode.height() - height.get(0)) / 2
            );

            glfwMakeContextCurrent(window);

            glfwSwapInterval(1);

            glfwShowWindow(window);
        }
    }

    private void loop() {
        GL.createCapabilities();

        glClearColor(1f, 0f, 0f, 0f);

        while(!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            glfwSwapBuffers(window);

            glfwPollEvents();

            handleInput();
        }
    }

    private void handleInput() {
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if(key == GLFW_KEY_ESCAPE && action == 0 /*Release*/ ) {
                glfwSetWindowShouldClose(window, true);
            }
            if(key == GLFW_KEY_A && action == 0) {
                blue += 0.1f;
                glClearColor(1f, 0f, blue, 0f);
            }
            else if(key == GLFW_KEY_D && action == 1) {
                blue -= 0.1f;
                glClearColor(1f, 0f, blue, 0f);
            }
            if(key == GLFW_KEY_W && action == 0) {
                green += 0.1f;
                glClearColor(1f, green, blue, 0f);
            }
            else if(key == GLFW_KEY_S && action == 1) {
                green -= 0.1f;
                glClearColor(1f, green, blue, 0f);
            }
        });
    }

    private void registerInput() {
        glfwSetKeyCallback()
    }

    public static void main(String[] args) {

        new Chopped().run();
    }
}