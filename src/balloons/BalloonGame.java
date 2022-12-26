package balloons;

import gdi.game.Settings;
import gdi.game.sprite.SpriteWorld;

import java.awt.*;
import java.util.Random;

public class BalloonGame extends SpriteWorld {

    public static final Random RANDOM = new Random();

    public BalloonGame(Settings settings) {
        super(settings, 800, 600);
        for (int i = 0; i < 3; i++) {
            double xPos = this.getWidth() * (1 / 3) + RANDOM.nextDouble((this.getWidth() * (2d / 3d)));
            this.addSprite(new Balloon(xPos,
                    RANDOM.nextDouble(this.getHeight()), this));
        }
    }

    public BalloonGame() {
        this(new Settings());
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
