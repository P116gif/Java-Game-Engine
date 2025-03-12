package Gson;

import com.google.gson.*;
import Components.Component;
import jade.GameObject;
import jade.Transform;

import java.lang.reflect.Type;

/*
THE REASON WE NEED A GAME OBJECT DESERIALIZER ONLY IS BECAUSE WHEN WE SERIALISE A GAME OBJECT,
WE ACTUALLY SERIALISE ONLY THE COMPONENTS OF THE GAME OBJECT. THIS IS BECAUSE OF A CIRCULAR
DEPENDENCY BETWEEN GAME OBJECT AND COMPONENTS THAT CAUSES A STACK OVERFLOW ERROR IF GSON SERIALIZES IT
ITSELF.
WHAT WE DO IS SIMPLE SERIALISE ONLY THE COMPONENTS AND VARIABLES OF A GAME OBJECT,
AND WHEN DESERIALIZING IT, WE CALL THIS CLASS
 */
public class forGameObject implements JsonDeserializer<GameObject> {

    @Override
    public GameObject deserialize(JsonElement jsonElement, Type typeofT, JsonDeserializationContext context) throws JsonParseException {

        JsonObject jsonObject = jsonElement.getAsJsonObject();

        //since each gameobject has a name which will always be included in the json file
        // we can get that from the json file
        //we also get all the other variables
        String name = jsonObject.get("name").getAsString();
        JsonArray components = jsonObject.get("componentList").getAsJsonArray();
        Transform transform = context.deserialize(jsonObject.get("transform"), Transform.class);
        int zIndex = context.deserialize(jsonObject.get("zIndex"), int.class);

        //construct the game object
        GameObject go = new GameObject(name, transform, zIndex);

        for(JsonElement e: components){

            Component c = context.deserialize(e, Component.class);
            go.addComponent(c);
        }

        return go;
    }
}
