package jade;

import java.util.ArrayList;
import java.util.List;

import Components.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class GameObject {

    private static final Logger logger = LoggerFactory.getLogger(GameObject.class);
    private String name;

    private List<Component> componentList;
    public Transform transform;
    private int zIndex;

    private static int ID_COUNTER = 0;
    private int uid = -1;

    /*Constructor*/

    public GameObject(String name, Transform trans, int z){
        this.name = name;
        this.zIndex = z;
        this.componentList = new ArrayList<>();
        this.transform = trans;
        this.uid = ID_COUNTER++;
    }



    public <T extends Component> T getComponent(Class<T> componentClass){

        for(Component c: componentList){
            //isAssignableFrom checks whether componentClass is either the same as or the super class /
            // super interface of the class inside the brackets,which in this case is 'c'
            if(componentClass.isAssignableFrom(c.getClass())){
                try {
                    return componentClass.cast(c);
                }catch (ClassCastException e){

                    logger.error("Error: Casting of component in GameObject class went wrong", e);
                    assert false : "";
                }
            }
        }

        return null;

    }

    public <T extends Component> void removeComponent(Class<T> componentClass){

        for(int i = 0; i < componentList.size();i++){

            Component c = componentList.get(i);
            if(componentClass.isAssignableFrom(c.getClass())){
                componentList.remove(i);
                return;
            }
        }

    }

    public void addComponent(Component c){

        c.generateID();
        this.componentList.add(c);
        c.gameObject = this;
    }

    public void update(float dt){
        for (Component component : componentList) {
            component.update(dt);
        }
    }

    public void start(){

        for(Component c: componentList){
            c.start();
        }
    }

    public void imgui(){
        for(Component c: componentList){
            c.imgui();
        }
    }

    public int getzIndex() {
        return zIndex;
    }

    public static void init(int maxID){

        ID_COUNTER = maxID;
    }

    public int getUid(){

        return this.uid;
    }

    public List<Component> getComponentList() {
        return componentList;
    }
}
