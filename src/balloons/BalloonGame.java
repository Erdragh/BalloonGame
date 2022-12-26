package balloons;

import gdi.game.Settings;
import gdi.game.sprite.SpriteWorld;
import gdi.util.math.Vec2D;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

public class BalloonGame extends SpriteWorld {

    public static final Random RANDOM = new Random();

    private Dart activeDart;
    private ArrayList<Dart> movingDarts = new ArrayList();
    private ArrayList<Balloon> balloons = new ArrayList();

    public BalloonGame(Settings settings) {
        super(settings, 800, 600);
        resetBalloons();
        regenerateDart();
    }

    public BalloonGame() {
        this(new Settings());
    }

    public void resetBalloons() {
        for (int i = 0; i < 3; i++) {
            double xPos = this.getWidth() * (1d / 3d) + RANDOM.nextDouble((this.getWidth() * (2d / 3d)));
            var balloon = new Balloon(xPos,
                    RANDOM.nextDouble(this.getHeight()), this);
            this.balloons.add(balloon);
            this.addSprite(balloon);
        }
    }

    public void regenerateDart() {
        if (this.activeDart != null) {
            this.movingDarts.add(this.activeDart);
        }
        this.activeDart = this.activeDart == null ? new Dart(this, 0) : this.activeDart.copy();
        this.addSprite(this.activeDart);
    }

    public void removeDart(Dart dart) {
        this.movingDarts.remove(dart);
        this.removeSprite(dart);
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
                break;
            case KeyEvent.VK_R:
                resetBalloons();
                break;
        }
    }

    @Override
    protected void update(double deltaTime, double time) {
        super.update(deltaTime, time);
        ArrayList<Dart> dartsToRemove = new ArrayList();
        ArrayList<Balloon> balloonsToRemove = new ArrayList();
        for (var dart : this.movingDarts) {
            for (var balloon : this.balloons) {
                Vec2D endPos = dart.getEndPos();
                if (balloon.contains(endPos)) {
                    dartsToRemove.add(dart);
                    balloonsToRemove.add(balloon);
                }
            }
        }
        for (var dart : dartsToRemove) {
            removeDart(dart);
        }
        for (var balloon : balloonsToRemove) {
            this.balloons.remove(balloon);
            this.removeSprite(balloon);
        }
    }

    @Override
    protected void renderBackground(Graphics2D g) {
        g.setColor(Color.decode("#BAFFFF"));
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
    }

    public static void print(Object... objects) {
        for (var obj : objects) {
            System.out.println(obj + " ");
        }
    }
}
