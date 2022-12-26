package balloons;

import gdi.game.Settings;
import gdi.game.sprite.SpriteWorld;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;

public class BalloonGame extends SpriteWorld {

    public static final Random RANDOM = new Random();

    private Dart activeDart;

    public BalloonGame(Settings settings) {
        super(settings, 800, 600);
        for (int i = 0; i < 3; i++) {
            double xPos = this.getWidth() * (1d / 3d) + RANDOM.nextDouble((this.getWidth() * (2d / 3d)));
            this.addSprite(new Balloon(xPos,
                    RANDOM.nextDouble(this.getHeight()), this));
        }
        regenerateDart();
    }

    public BalloonGame() {
        this(new Settings());
    }

    public void regenerateDart() {
        this.activeDart = this.activeDart == null ? new Dart(this, 0) : this.activeDart.copy();
        this.addSprite(this.activeDart);
    }

    @Override
    protected void setupWorld() {
        this.setTitle("Balloon Game");
    }

    @Override
    protected void keyDown(gdi.game.events.KeyEvent ke) {
        super.keyDown(ke);
        switch (ke.getKeyCode()) {
            case KeyEvent.VK_UP:
                this.activeDart.setRotateUp(true);
                break;
            case KeyEvent.VK_DOWN:
                this.activeDart.setRotateDown(true);
                break;
        }
    }

    @Override
    protected void keyUp(gdi.game.events.KeyEvent ke) {
        super.keyUp(ke);
        switch (ke.getKeyCode()) {
            case KeyEvent.VK_UP:
                this.activeDart.setRotateUp(false);
                break;
            case KeyEvent.VK_DOWN:
                this.activeDart.setRotateDown(false);
                break;
            case KeyEvent.VK_SPACE:
                this.activeDart.setShoot(true);
        }
    }

    @Override
    protected void renderBackground(Graphics2D g) {
        g.setColor(Color.decode("#BAFFFF"));
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
    }
}
