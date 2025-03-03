//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import jade.Window;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    public Main() {
    }

    private static final Logger logger = LoggerFactory.getLogger(Main.class);


    public static void main(String[] args) {
        Window window = Window.get();
        //ImGUI (Immediate Graphical User Interface) Layer
        //Application.launch(new ImGuiLayer());
        window.run();
    }
}
