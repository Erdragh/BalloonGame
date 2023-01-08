package balloons;

import gdi.game.sprite.AbstractSpriteWorld;

public class TrojanBalloon extends Balloon {
    public TrojanBalloon(double xPos, double yPos, AbstractSpriteWorld world) {
        super(xPos, yPos, world);
    }

    @Override
    public boolean onDeath(BalloonGame game) {
        // add two new normal balloons to the game
        game.addBalloons(2, NORMAL_BALLOON);
        // return true, so the original balloon is
        // removed.
        return true;
    }
}
