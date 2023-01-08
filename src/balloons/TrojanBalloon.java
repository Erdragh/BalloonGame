package balloons;

import gdi.game.sprite.AbstractSpriteWorld;

public class TrojanBalloon extends Balloon {
    public TrojanBalloon(double xPos, double yPos, int diameter, AbstractSpriteWorld world) {
        super(xPos, yPos, diameter, world);
    }

    @Override
    public boolean onDeath(BalloonGame game) {
        // add two new normal balloons to the game
        game.addBalloons(2, NORMAL_BALLOON, 50);
        // return true, so the original balloon is
        // removed.
        return true;
    }
}
