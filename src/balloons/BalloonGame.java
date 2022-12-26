package balloons;

import gdi.game.Settings;
import gdi.game.sprite.SpriteWorld;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;

public class BalloonGame extends SpriteWorld {

    public static final Random RANDOM = new Random();

    private boolean rotateUp, rotateDown, shoot;

    public BalloonGame(Settings settings) {
        super(settings, 800, 600);
        for (int i = 0; i < 3; i++) {
            double xPos = this.getWidth() * (1d / 3d) + RANDOM.nextDouble((this.getWidth() * (2d / 3d)));
            this.addSprite(new Balloon(xPos,
                    RANDOM.nextDouble(this.getHeight()), this));
        }
        this.addSprite(new Dart(this));
    }

    public BalloonGame() {
        this(new Settings());
    }

    public boolean shouldRotateUp() {
        return rotateUp;
    }

    public boolean shouldRotateDown() {
        return rotateDown;
    }

    public boolean shouldShoot() {
        if (shoot) {
            shoot = !shoot;
            return true;
        }
        return shoot;
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
                rotateUp = true;
                break;
            case KeyEvent.VK_DOWN:
                rotateDown = true;
                break;
        }
    }

    @Override
    protected void keyUp(gdi.game.events.KeyEvent ke) {
        super.keyUp(ke);
        switch (ke.getKeyCode()) {
            case KeyEvent.VK_UP:
                rotateUp = false;
                break;
            case KeyEvent.VK_DOWN:
                rotateDown = false;
                break;
        }
    }

    @Override
    protected void renderBackground(Graphics2D g) {
        g.setColor(Color.decode("#BAFFFF"));
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
    }
}
