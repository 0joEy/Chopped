package net.henrycmoss;

import org.lwjgl.Version;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;


public class Chopped {

    private long window;

    private final RGBColor color = new RGBColor(0.0f, 0.0f, 0.0f, 0.0f);

    private final Map<List<Integer>, GLFWKeyCallbackI> inputMap = new HashMap<>();

    private boolean show;

    private Random source = new Random();

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

            registerInput();
        }
    }

    private void loop() {
        GL.createCapabilities();

        while(!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            glfwSwapBuffers(window);

            glfwPollEvents();

            handleInput();

            if(show) {
                colorShowTick(source);
            }
        }
    }

    private void handleInput() {
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if(inputMap.containsKey(List.of(key, action))) {
                inputMap.get(List.of(key, action)).invoke(window, key, scancode, action, mods);
            }
        });
    }

    private void registerInput() {
        addInput(GLFW_KEY_W, 1, (window, key, code, action, mods) -> { color.incrementRed(0.1f); updateColors(); });
        addInput(GLFW_KEY_S, 1, (window, key, code, action, mods) -> { color.incrementRed(-0.1f); updateColors(); });
        addInput(GLFW_KEY_D, 1, (window, key, code, action, mods) -> { color.incrementBlue(0.1f); updateColors(); });
        addInput(GLFW_KEY_A, 1, (window, key, code, action, mods) -> { color.incrementBlue(-0.1f); updateColors(); });
        addInput(GLFW_KEY_E, 1, (window, key, code, action, mods) -> { color.incrementGreen(0.1f); updateColors(); });
        addInput(GLFW_KEY_Q, 1, (window, key, code, action, mods) -> { color.incrementGreen(-0.1f); updateColors(); });
        addInput(GLFW_KEY_SPACE, 1, (window, key, code, action, mods) -> toggleShow());
    }

    public static void main(String[] args) {

        new Chopped().run();
    }

    private void addInput(int key, int action, GLFWKeyCallbackI callback) {
        inputMap.put(List.of(key, action), callback);
    }

    private void updateColors() {
        glClearColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    private void toggleShow() {
        show = !show;
        max = 0.1f;
        min = -0.04f;
    }

    private float min, max;

    private void colorShowTick(Random rand) {
        float[] a = color.getComponents();

        int components = a.length;

        if((color.sum() / components) <= 0.7f) {

            for(int i = 0; i < components; i++) {
                a[i] += rand.nextFloat(min, max);
            }
        }

        color.updateFromComponents(a);
        updateColors();
    }
}