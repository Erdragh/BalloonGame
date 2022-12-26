package balloons;

import gdi.game.Settings;
import gdi.game.sprite.SpriteWorld;

import java.awt.*;

public class BalloonGame extends SpriteWorld {
    public BalloonGame(Settings settings) {
        super(settings, 800, 600);
    }
    public BalloonGame() {
        super(800, 600);
    }

    @Override
    protected void setupWorld() {
        this.setTitle("Balloon Game");
    }

    @Override
    protected void renderBackground(Graphics2D g) {
        g.setColor(Color.decode("#BAFFFF"));
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
    }
}
