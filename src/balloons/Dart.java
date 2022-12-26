package balloons;

import gdi.game.sprite.AbstractSpriteWorld;
import gdi.game.sprite.Sprite;

import java.awt.*;

public class Dart extends Sprite {

    public Dart(AbstractSpriteWorld w) {
        super(0, w.getHeight() / 2, 0, w);
    }

    @Override
    protected void renderLocal(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.drawLine(0, 0, 60, 0);
    }
}
