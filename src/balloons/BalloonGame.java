package balloons;

import gdi.game.Settings;
import gdi.game.sprite.SpriteWorld;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class BalloonGame extends SpriteWorld {
    private ArrayList balloons = new ArrayList<Balloon>();

    public static final Random RANDOM = new Random();

    public BalloonGame(Settings settings) {
        super(settings, 800, 600);
        for (int i = 0; i < 3; i++) {
            balloons.add(new Balloon(this.getWidth() * 1 / 3 + RANDOM.nextInt(this.getWidth() * (2 / 3)),
                    RANDOM.nextInt(this.getHeight()), this));
        }
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
