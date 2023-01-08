package balloons;

import gdi.game.sprite.AbstractSpriteWorld;

public class ToughBalloon extends Balloon {
    private int health = 2;
    public ToughBalloon(double xPos, double yPos, AbstractSpriteWorld world) {
        super(xPos, yPos, world);
    }

    @Override
    public boolean onDeath(BalloonGame game) {
        // randomize the color of the balloon to signify that
        // it has been hit.
        this.randomizeColor();
        // only return true and therefore remove the balloon if
        // the newly calculated health is below 1.
        return --health < 1;
    }
}
