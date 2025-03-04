//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package jade;

public class Mouse_Listener {

    private static Mouse_Listener instance;
    private double scrollX = 0.0F;
    private double scrollY = 0.0F;
    private double xPos = 0.0F;
    private double yPos = 0.0F;
    private double lastY = 0.0F;
    private double lastX = 0.0F;
    private boolean[] mouseButtonPressed = new boolean[8];
    private boolean isDragging;

    private Mouse_Listener() {
    }

    public static Mouse_Listener get() {
        if (instance == null) {
            instance = new Mouse_Listener();
        }

        return instance;
    }

    public static void mousePosCallBack(long window, double xPos, double yPos) {
        get().lastX = get().xPos;
        get().lastY = get().yPos;
        get().xPos = xPos;
        get().yPos = yPos;
        get().isDragging = get().mouseButtonPressed[0] || get().mouseButtonPressed[1] || get().mouseButtonPressed[2];
    }

    public static void mouseButtonCallBack(long window, int button, int action, int mods) {
        if (action == 1) {
            get().mouseButtonPressed[button] = true;
        } else if (action == 0) {
            get().mouseButtonPressed[button] = false;
            get().isDragging = false;
        }

    }

    public static void mouseScrollCallBack(long window, double xOffset, double yOffset) {
        get().scrollY = yOffset;
        get().scrollX = xOffset;
    }

    public static void endFrame() {
        get().scrollY = 0.0F;
        get().scrollX = 0.0F;
        get().lastX = get().xPos;
        get().lastY = get().yPos;
    }

    public static float getX() {
        return (float)get().xPos;
    }

    public static float getY() {
        return (float)get().yPos;
    }

    public static float dX() {
        return (float)(get().lastX - get().xPos);
    }

    public static float dY() {
        return (float)(get().lastY - get().yPos);
    }

    public static float scrollX() {
        return (float)get().scrollX;
    }

    public static float scrollY() {
        return (float)get().scrollY;
    }

    public static boolean isDragging() {
        return get().isDragging;
    }

    public static boolean mouseButtonDown(int button) {
        return get().mouseButtonPressed[button];
    }
}
