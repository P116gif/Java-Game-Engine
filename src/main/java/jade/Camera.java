package jade;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.Vector;

public class Camera {

    private Matrix4f projectionMatrix, viewMatrix, inverseProjection, inverseView;
    public Vector2f position;
    private float zoom = 1.0f;
    private Vector2f projectionSize = new Vector2f(32.0f * 40.0f, 32.0f * 21.0f);

    public Camera(Vector2f position){

        this.position = position;
        this.projectionMatrix = new Matrix4f();
        this.viewMatrix = new Matrix4f();
        this.inverseProjection = new Matrix4f();
        this.inverseView = new Matrix4f();
        adjustProjection();
    }

    public void adjustProjection(){

        projectionMatrix.identity(); //gives us an identity matrix
        //give the dimension that the camera should be able to see
        //the near clipping and the far clipping
        projectionMatrix.ortho(0.0f, projectionSize.x * this.zoom, 0.0f
                , projectionSize.y * this.zoom, 0.0f, 100.f);
        //store inverse inside inverseProjection
        projectionMatrix.invert(inverseProjection);
    }

    public Matrix4f getViewMatrix(){

        Vector3f cameraFront = new Vector3f(0.0f,0.0f,-1.0f);
        Vector3f cameraUp = new Vector3f(0.0f,1.0f,0.0f);
        this.viewMatrix.identity();
        viewMatrix.lookAt(new Vector3f(position.x, position.y,20.0f), //looking at
                                            cameraFront.add(position.x,position.y,0.0f), //center of the camera
                                            cameraUp); //
        viewMatrix.invert(inverseView);
        return viewMatrix;
    }

    public Matrix4f getProjectionMatrix(){ return this.projectionMatrix;}

    public Matrix4f getInverseProjection(){ return this.inverseProjection;}

    public Matrix4f getInverseView(){ return this.inverseView;}

    public Vector2f getProjectionSize() {
        return projectionSize;
    }


    public float getZoom() {
        return zoom;
    }

    public void setZoom(float zoom) {
        this.zoom = zoom;
    }

    public void addZoom(float value) {

        this.zoom+=value;
    }
}
