package renderer;

import Components.SpriteRenderer;
import jade.GameObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Renderer {
    private final int MAX_BATCH_SIZE = 1000;
    private List<RenderBatch> batches;

    public Renderer() {
        this.batches = new ArrayList<>();
    }

    public void add(GameObject go) {
        SpriteRenderer spr = go.getComponent(SpriteRenderer.class);
        if (spr != null) {
            add(spr);
        }
    }

    private void add(SpriteRenderer sprite) {
        boolean added = false;
        for (RenderBatch batch : batches) {
            //is batch has space and is one the same index as the sprite
            if (batch.hasSpace() && batch.getzIndex() == sprite.gameObject.getzIndex()) {
                Texture tex = sprite.getTexture();
                if(tex == null || batch.hasTexture(tex) || batch.hasTextureRoom()) {
                    batch.addSprites(sprite);
                    added = true;
                    break;
                }
            }
        }

        if (!added) {
            RenderBatch newBatch = new RenderBatch(MAX_BATCH_SIZE, sprite.gameObject.getzIndex());
            newBatch.start();
            batches.add(newBatch);
            newBatch.addSprites(sprite);
            //sort the batches based on their z index (comparator made in renderbatch)
            Collections.sort(batches);
        }
    }

    public void render() {
        for (RenderBatch batch : batches) {
            //calls the renderbatch render function
            batch.render();
        }
    }
}