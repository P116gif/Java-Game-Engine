package renderer;

import Components.SpriteRenderer;
import jade.GameObject;

import java.util.ArrayList;
import java.util.List;

public class Renderer {

    private final int maxBatchSize = 1000;
    private List<renderBatch> batches;

    public Renderer(){

        this.batches = new ArrayList<>();
    }

    public void add(GameObject go){

        SpriteRenderer spr = go.getComponent(SpriteRenderer.class);
        if(spr!= null){
            add(spr);
        }
    }

    private void add(SpriteRenderer spr) {
        boolean added = false;

        for(renderBatch batch: batches){
            if(batch.hasSpace()){
                batch.addSprites(spr);
                added = true;
                break;
            }
        }

        if(!added){
            renderBatch newBatch = new renderBatch(maxBatchSize);
            newBatch.start();
            batches.add(newBatch);
            newBatch.addSprites(spr);
        }
    }

    public void render(){
        for(renderBatch batch: batches){
            batch.render();
        }
    }
}
