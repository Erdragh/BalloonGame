package balloons;

import gdi.game.sprite.AbstractSpriteWorld;
import gdi.game.sprite.Sprite;

import java.awt.*;
import java.util.Random;

public class Balloon extends Sprite {
    private final int DIAMETER = 100;

    private final Color color;

    private static Random random = new Random();

    public Balloon(double xPos, double yPos, AbstractSpriteWorld world) {
        super(xPos, yPos, world);
        this.color = Color.getHSBColor(random.nextFloat(), 1f, 1f);
    }
}
