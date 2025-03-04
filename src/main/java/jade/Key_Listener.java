//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package jade;

public class Key_Listener {
    private static Key_Listener instance;
    private boolean[] keyPressed = new boolean[349];

    private Key_Listener() {
    }

    public static Key_Listener get() {
        if (instance == null) {
            instance = new Key_Listener();
        }

        return instance;
    }

    public static void keyCallBack(long window, int key, int scancode, int action, int mods) {
        if (action == 1) {
            get().keyPressed[key] = true;
        } else if (action == 0) {
            get().keyPressed[key] = false;
        }

    }

    public static boolean isKeyPressed(int keyCode) {
        return get().keyPressed[keyCode];
    }
}
