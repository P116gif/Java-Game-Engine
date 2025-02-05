package jade;

public class Level_Scene extends Scene {

    public Level_Scene(){
        System.out.println("Inside Level Scene");
        Window.get().r = 1.0f;
        Window.get().b = 1.0f;
        Window.get().g = 1.0f;
    }

    @Override
    public void update(float deltaTime) {

    }
}
