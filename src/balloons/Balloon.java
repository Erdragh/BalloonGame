package balloons;

import gdi.game.sprite.AbstractSpriteWorld;
import gdi.game.sprite.Sprite;

import java.awt.*;
import java.util.Random;

public class Balloon extends Sprite {
    private final int DIAMETER = 100;

    private final Color color;

    public Balloon(double xPos, double yPos, AbstractSpriteWorld world) {
        this(xPos, yPos, Color.getHSBColor(BalloonGame.RANDOM.nextFloat(), 1f, 1f), world);
    }
    public Balloon(double xPos, double yPos, Color color, AbstractSpriteWorld world) {
        super(xPos, yPos, world);
        this.color = color;
    }

    @Override
    protected void renderLocal(Graphics2D g) {
        g.setColor(this.color);
        g.fillOval((-this.DIAMETER / 2),
                (-this.DIAMETER / 2),
                this.DIAMETER, this.DIAMETER);
        var triangle = new Polygon();
        int offset = DIAMETER / 2;
        triangle.xpoints = new int[] {-10, 0, 10};
        triangle.ypoints = new int[] {offset + 10, offset + 0, offset + 10};
        triangle.npoints = 3;
        g.fillPolygon(triangle);
    }
}
