package jade;

import org.joml.Vector2f;
import org.joml.Vector4f;

public class Transform {

    public Vector2f position;
    public Vector2f scale;

    public Transform(){

        init(new Vector2f(), new Vector2f());
    }

    public Transform(Vector2f position){

        init(position, new Vector2f());
    }

    public Transform(Vector2f position, Vector2f scale){

        init(position, scale);
    }

    public void init(Vector2f pos, Vector2f scal){

        this.position = pos;
        this.scale = scal;
    }

    public Transform copy(){

        return new Transform(new Vector2f(this.position), new Vector2f(this.scale));
    }

    public void copy(Transform to){
        to.position.set(this.position);
        to.scale.set(this.scale);
    }

    //to compare two transforms
    @Override
    public boolean equals(Object o){

        if(o==null) return false;
        if(!(o instanceof Transform)) return false;

        Transform t = (Transform) o;

        return (this.position.equals(t.position)) && (this.scale.equals(t.scale));
    }

}
