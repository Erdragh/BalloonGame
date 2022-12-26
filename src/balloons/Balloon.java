package balloons;

import gdi.game.sprite.AbstractSpriteWorld;
import gdi.game.sprite.Sprite;
import gdi.util.math.Vec2D;

import java.awt.*;

public class Balloon extends Sprite {

    public static final int TOUGH_BALLOON = 2, TROJAN_BALLOON = 3, NORMAL_BALLOON = 0;

    private final int DIAMETER = 100;
    private final int SPEED = 100;

    private Color color;

    private int movementDirection = 1;

    public Balloon(double xPos, double yPos, AbstractSpriteWorld world) {
        this(xPos, yPos, Color.getHSBColor(BalloonGame.RANDOM.nextFloat(), 1f, 1f), world);
    }
    public Balloon(double xPos, double yPos, Color color, AbstractSpriteWorld world) {
        super(xPos, yPos, world);
        this.color = color;
    }

    protected Color randomizeColor() {
        var c = Color.getHSBColor(BalloonGame.RANDOM.nextFloat(), 1f, 1f);
        this.color = c;
        return c;
    }

    public boolean contains(Vec2D pos) {
        double x = pos.x, y = pos.y, disX = this.getX() - x, disY = this.getY() - y, disSquared = disX * disX + disY * disY;
        return disSquared <= DIAMETER * DIAMETER / 4;
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

    @Override
    public void update(double deltaTime, double time) {
        super.update(deltaTime, time);
        this.move(deltaTime);
    }

    private void move(double deltaTime) {
        double newPos = this.getY() + movementDirection * deltaTime * SPEED;
        if (newPos < 0) {
            movementDirection = 1;
        } else if (newPos > this.getAbstractSpriteWorld().getHeight()) {
            movementDirection = -1;
        }
        newPos = this.getY() + movementDirection * deltaTime * SPEED;
        this.setY(newPos);
    }

    public boolean onDeath(BalloonGame game) {
        // return true, which signifies that this balloon should be removed
        return true;
    }
}
