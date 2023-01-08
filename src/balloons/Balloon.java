package balloons;

import gdi.game.sprite.AbstractSpriteWorld;
import gdi.game.sprite.Sprite;
import gdi.util.math.Vec2D;

import java.awt.*;

public class Balloon extends Sprite {

    // the balloon types.
    public static final int TOUGH_BALLOON = 2, TROJAN_BALLOON = 3, NORMAL_BALLOON = 0;

    public static final int DEFAULT_DIAMETER = 100;


    /**
     * The speed at which balloons move up and down the screen.
     */
    private final int SPEED = 100;

    /**
     * The color of the balloon. This will be random.
     */
    private Color color;

    /**
     * The movement direction of the balloon. This will
     * be used as a multiplier later in the update
     * method.
     */
    private int movementDirection = 1;
    /**
     * The diameter of balloons.
     * This is used both for rendering and collision detection.
     */
    private int diameter;

    public Balloon(double xPos, double yPos, int diameter, AbstractSpriteWorld world) {
        // this constructor uses getHSBColor to create a random color with full saturation and brightness.
        this(xPos, yPos, Color.getHSBColor(BalloonGame.RANDOM.nextFloat(), 1f, 1f), diameter, world);
    }

    public Balloon(double xPos, double yPos, Color color, int diameter, AbstractSpriteWorld world) {
        super(xPos, yPos, world);
        this.diameter = diameter;
        this.color = color;
    }

    public Balloon(double xPos, double yPos, AbstractSpriteWorld world) {
        this(xPos, yPos, 100, world);
    }

    /**
     * This method generates a random color, returns it to the
     * caller and sets it as the balloons color.
     *
     * @return the randomly generated color.
     */
    protected Color randomizeColor() {
        var c = Color.getHSBColor(BalloonGame.RANDOM.nextFloat(), 1f, 1f);
        this.color = c;
        return c;
    }

    /**
     * This method checks whether the given position is inside
     * the balloon, which is used in collision detection.
     *
     * @param pos the position which may be inside the balloon.
     * @return whether the specified position is inside the balloon.
     */
    public boolean contains(Vec2D pos) {
        // I use squared distances, so I don't have to do expensive
        // root calculations.
        double x = pos.x, y = pos.y, disX = this.getX() - x, disY = this.getY() - y, disSquared = disX * disX + disY * disY;
        return disSquared <= diameter * diameter / 4;
    }

    @Override
    protected void renderLocal(Graphics2D g) {
        // set the color to this balloon's color.
        g.setColor(this.color);

        // fill the circle that is this balloon.
        g.fillOval((-this.diameter / 2),
                (-this.diameter / 2),
                this.diameter, this.diameter);

        // draw a triangle on the bottom, so the
        // balloon looks more like an actual balloon.
        var triangle = new Polygon();
        int offset = this.diameter / 2;
        triangle.xpoints = new int[]{-10, 0, 10};
        triangle.ypoints = new int[]{offset + 10, offset + 0, offset + 10};
        triangle.npoints = 3;
        g.fillPolygon(triangle);
    }

    @Override
    public void update(double deltaTime, double time) {
        super.update(deltaTime, time);
        // move the balloon.
        this.move(deltaTime);
    }

    /**
     * Moves the balloon according to its current direction.
     * The direction might change if the balloon goes outside the
     * window (or rather, the bounds of the current <code>AbstractSpriteWorld</code>).
     *
     * @param deltaTime
     */
    private void move(double deltaTime) {
        // calculate what would be the new position
        double newPos = this.getY() + movementDirection * deltaTime * SPEED;
        // if the position is outside the bounds of the current world,
        // change the direction.
        if (newPos < 0) {
            movementDirection = 1;
        } else if (newPos > this.getAbstractSpriteWorld().getHeight()) {
            movementDirection = -1;
        }
        // calculate the actual new position based on the movementDirection,
        // which may have changed.
        newPos = this.getY() + movementDirection * deltaTime * SPEED;
        // set this balloon's y position to the newly calculated one.
        this.setY(newPos);
    }

    public boolean onDeath(BalloonGame game) {
        // return true, which signifies that this balloon should be removed
        return true;
    }
}
