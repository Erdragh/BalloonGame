package balloons;

import gdi.game.sprite.Sprite;
import gdi.util.math.Vec2D;

import java.awt.*;

public class Dart extends Sprite {

    // the constant values for the dart.
    private final double MOVEMENT_SPEED = 600, ROTATION_SPEED = 40, LENGTH = 60;

    // the current movement speed.
    private double speedX, speedY;

    // the booleans controlling the movement/actions of the dart.
    private boolean moving, rotateUp, rotateDown, shoot;

    public Dart(BalloonGame game, double angle) {
        super(0, game.getHeight() / 2, angle, game);
    }

    /**
     * Copies the current dart and returns the copy.
     * This is used when shooting a dart, so the angle
     * and whether the dart should be rotating are preserved.
     * @return the copy of the current dart.
     */
    public Dart copy() {
        // create a new dart with our game and our angle.
        var dart = new Dart((BalloonGame) this.getAbstractSpriteWorld(), this.getAngle());
        // copy over the rotation values.
        dart.setRotateDown(this.rotateDown);
        dart.setRotateUp(this.rotateUp);
        return dart;
    }

    // the setters for the booleans.
    public void setRotateUp(boolean rotateUp) {
        this.rotateUp = rotateUp;
    }

    public void setRotateDown(boolean rotateDown) {
        this.rotateDown = rotateDown;
    }

    public void setShoot(boolean shoot) {
        this.shoot = shoot;
    }

    /**
     * Calculates the position of the end of the dart in
     * world space coordinates. This is used in collision detection.
     * @return the position of the end of the dart in world
     * space coordinates.
     */
    public Vec2D getEndPos() {
        // convert the angle to radians, as the java Math methods use
        // radians.
        double angleInRadians = Math.toRadians(this.getAngle());
        return new Vec2D(Math.cos(angleInRadians) * LENGTH + this.getX(), Math.sin(angleInRadians) * LENGTH + this.getY());
    }

    @Override
    protected void renderLocal(Graphics2D g) {
        // since I use the engine's builtin rotation feature,
        // I don't need to worry about it in my render method,
        // so this is rather simple, it just draws a line with the
        // length of the dart.
        g.setColor(Color.BLACK);
        g.drawLine(0, 0, (int)LENGTH, 0);
    }

    @Override
    public void update(double deltaTime, double time) {
        super.update(deltaTime, time);

        if (shoot && !moving) {
            // if the dart is not yet moving, but shoot is true, it should be shot.
            this.shoot(deltaTime);
        } else if (!moving) {
            // if we're neither moving nor shooting, we are still in the phase,
            // where the player controls the dart and rotates it.

            // the rotation direction starts at 0 and gets
            // increased/decreased based on whether it should
            // rotate up or down. If both are set, they will
            // cancel each other out and the direction will
            // again be 0.
            byte rotationDirection = 0;
            if (rotateUp) {
                rotationDirection -= 1;
            }
            if (rotateDown) {
                rotationDirection += 1;
            }
            // calculate the new angle using the direction and the speed.
            var newAngle = this.getAngle() + deltaTime * ROTATION_SPEED * rotationDirection;

            // clamp the angle between 90 and -90, because the player shouldn't be able to shoot
            // to the left.
            if (newAngle > 90) {
                newAngle = 90;
            } else if (newAngle < -90) {
                newAngle = -90;
            }
            this.setAngle(newAngle);
        } else if (moving) {
            // if we're here it means that the dart is already moving,
            // so we move it further.
            this.move(deltaTime);
        }
    }

    /**
     * Shoots the dart.
     * @param deltaTime
     */
    private void shoot(double deltaTime) {
        // sets shoot to false, because the dart has now been shot.
        setShoot(false);
        // set the dart to be moving.
        moving = true;
        // tell the game to regenerate a dart, as this one is now
        // moving.
        var game = (BalloonGame) this.getWorld();
        game.regenerateDart();
        // calculate the speed of the dart based on its angle.
        var angleInRadians = this.getAngle() * Math.PI / 180;
        this.speedY = Math.sin(angleInRadians) * MOVEMENT_SPEED;
        this.speedX = Math.cos(angleInRadians) * MOVEMENT_SPEED;
    }

    /**
     * Moves the dart.
     * @param deltaTime
     */
    private void move(double deltaTime) {
        // if the dart is out of bounds, it should be removed.
        if (this.getX() < 0 || this.getX() > this.getAbstractSpriteWorld().getWidth() ||
        this.getY() < 0 || this.getY() > this.getAbstractSpriteWorld().getHeight()) {
            ((BalloonGame) this.getAbstractSpriteWorld()).removeDart(this);
            return;
        }

        // use the given value for gravity to accelerate the dart.
        speedY += 490.5 * deltaTime;

        // ----------------------------------------------------------------------
        // In case the movement speed of 600 is supposed to be constant
        // this would adjust speedX so that the dart still has a speed
        // of 600:
        // speedX = Math.sqrt(MOVEMENT_SPEED * MOVEMENT_SPEED - speedY * speedY);
        // ----------------------------------------------------------------------

        // calculate the new angle based on the 2d-speed of the dart.
        this.setAngle(Math.toDegrees(Math.atan(speedY / speedX)));
        // actually move the dart to its new location.
        var newX = this.getX() + deltaTime * speedX;
        var newY = this.getY() + deltaTime * speedY;
        this.setLocation(newX, newY);
    }
}
