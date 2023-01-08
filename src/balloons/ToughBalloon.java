package balloons;

import gdi.game.sprite.AbstractSpriteWorld;

public class ToughBalloon extends Balloon {
    private int health = 2;
    public ToughBalloon(double xPos, double yPos, AbstractSpriteWorld world) {
        super(xPos, yPos, world);
    }

    @Override
    public boolean onDeath(BalloonGame game) {
        this.randomizeColor();
        return --health < 1;
    }
}
