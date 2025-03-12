package Gson;

import com.google.gson.*;
import Components.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;

public class forComponents implements JsonSerializer<Component>, JsonDeserializer<Component> {

    private static final Logger logger = LoggerFactory.getLogger(forComponents.class);

    @Override
    public JsonElement serialize(Component src, Type typeOfSrc, JsonSerializationContext context){

        JsonObject result = new JsonObject();
        result.add("type", new JsonPrimitive(src.getClass().getCanonicalName()));
        result.add("properties", context.serialize(src, src.getClass()));

        return result;
    }


    @Override
    public Component deserialize(JsonElement jsonElement, Type typeofT, JsonDeserializationContext context)
            throws JsonParseException {

        JsonObject result = jsonElement.getAsJsonObject();

        //getting the name of component
        String type = result.get("type").getAsString();

        //getting the properties
        JsonElement properties = result.get("properties");


        try{

            return context.deserialize(properties, Class.forName(type));
        }
        catch(ClassNotFoundException e){

            logger.error("Class not found while deserialising + {}", type, e);
            return null;
        }
    }
}
