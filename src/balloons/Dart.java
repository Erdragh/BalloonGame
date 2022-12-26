package balloons;

import gdi.game.sprite.Sprite;

import java.awt.*;

public class Dart extends Sprite {

    private final double MOVEMENT_SPEED = 600, ROTATION_SPEED = 40;

    private double speedX, speedY;

    private boolean moving, rotateUp, rotateDown, shoot;

    public Dart(BalloonGame game, double angle) {
        super(0, game.getHeight() / 2, angle, game);
    }

    public Dart copy() {
        var dart = new Dart((BalloonGame) this.getAbstractSpriteWorld(), this.getAngle());
        dart.setRotateDown(this.rotateDown);
        dart.setRotateUp(this.rotateUp);
        return dart;
    }

    public void setRotateUp(boolean rotateUp) {
        this.rotateUp = rotateUp;
    }

    public void setRotateDown(boolean rotateDown) {
        this.rotateDown = rotateDown;
    }

    public void setShoot(boolean shoot) {
        this.shoot = shoot;
    }

    @Override
    protected void renderLocal(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.drawLine(0, 0, 60, 0);
    }

    @Override
    public void update(double deltaTime, double time) {
        super.update(deltaTime, time);

        if (shoot && !moving) {
            this.shoot(deltaTime);
        } else if (!moving) {
            byte rotationDirection = 0;
            if (rotateUp) {
                rotationDirection -= 1;
            }
            if (rotateDown) {
                rotationDirection += 1;
            }
            var newAngle = this.getAngle() + deltaTime * ROTATION_SPEED * rotationDirection;
            if (newAngle > 90) {
                newAngle = 90;
            } else if (newAngle < -90) {
                newAngle = -90;
            }
            this.setAngle(newAngle);
        } else if (moving) {
            this.move(deltaTime);
        }
    }

    private void shoot(double deltaTime) {
        setShoot(false);
        moving = true;
        var game = (BalloonGame) this.getWorld();
        game.regenerateDart();
        var angleInRadians = this.getAngle() * Math.PI / 180;
        this.speedY = Math.sin(angleInRadians) * MOVEMENT_SPEED;
        this.speedX = Math.cos(angleInRadians) * MOVEMENT_SPEED;
    }

    private void move(double deltaTime) {
        if (this.getX() < 0 || this.getX() > this.getAbstractSpriteWorld().getWidth() ||
        this.getY() < 0 || this.getY() > this.getAbstractSpriteWorld().getHeight()) {
            this.getAbstractSpriteWorld().removeSprite(this);
            return;
        }

        speedY += 490.5 * deltaTime;
        // In case the movement speed of 600 is said to be constant
        // this will adjust speedX so that the dart still has a speed
        // of 600
        // speedX = Math.sqrt(MOVEMENT_SPEED * MOVEMENT_SPEED - speedY * speedY);
        this.setAngle(Math.toDegrees(Math.atan(speedY / speedX)));
        var newX = this.getX() + deltaTime * speedX;
        var newY = this.getY() + deltaTime * speedY;
        this.setLocation(newX, newY);
    }
}
