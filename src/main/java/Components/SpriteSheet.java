package Components;

import org.joml.Vector2f;
import renderer.Texture;
import java.util.List;
import java.util.ArrayList;

public class SpriteSheet {

    private Texture texture;
    private List<Sprite> sprites;

                       //texture  width and height of individual sprite     number of sprites spacing between each sprite
    public SpriteSheet(Texture tex, int spriteWidth, int spriteHeight, int numSprites, int spacing){

        this.sprites = new ArrayList<>();
        this.texture = tex;
        int currentX = 0;
        int currentY = texture.getHeight() - spriteHeight; //bottom left corner of top left sprite
        int width = texture.getWidth();
        int height = texture.getHeight();
        for(int i = 0; i < numSprites; i++){

            //we are normalising all the vertice values here
            // so basically, we are calculating what is our y value for the head of the sprite
            // y value at the bottom of the sprite; x value for left and x value for the right of the
            //sprite
            float topDistance = (currentY + spriteHeight)/ (float)height;
            float bottomDistance = currentY / (float)height;
            float rightDistance = (currentX + spriteWidth) / (float)width;
            float leftDistance = currentX / (float)width;

            Vector2f[] texCords = {
                    new Vector2f(rightDistance,topDistance),
                    new Vector2f(rightDistance, bottomDistance),
                    new Vector2f(leftDistance,bottomDistance),
                    new Vector2f(leftDistance,topDistance)
            };

            Sprite sprite = new Sprite();
            sprite.setTexture(this.texture);
            sprite.setTexCords(texCords);
            this.sprites.add(sprite);

            currentX += (spriteWidth + spacing);

            if(currentX > width){
                currentX = 0;
                currentY -= (spriteHeight + spacing);
            }
        }

    }

    public Sprite getSprite(int index){
        return this.sprites.get(index);
    }


}
