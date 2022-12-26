package balloons;

import gdi.game.sprite.Sprite;

import java.awt.*;

public class Dart extends Sprite {

    private final double ROTATION_SPEED = 40;

    public Dart(BalloonGame game) {
        super(0, game.getHeight() / 2, 0, game);
    }

    @Override
    protected void renderLocal(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.drawLine(0, 0, 60, 0);
    }

    @Override
    public void update(double deltaTime, double time) {
        super.update(deltaTime, time);
        var game = (BalloonGame) this.getAbstractSpriteWorld();
        int rotationDirection = 0;
        if (game.shouldRotateUp()) {
            rotationDirection -= 1;
        }
        if (game.shouldRotateDown()) {
            rotationDirection += 1;
        }
        var newAngle = this.getAngle() + deltaTime * ROTATION_SPEED * rotationDirection;
        if (newAngle > 90) {
            newAngle = 90;
        } else if (newAngle < -90) {
            newAngle = -90;
        }
        this.setAngle(newAngle);
    }
}
