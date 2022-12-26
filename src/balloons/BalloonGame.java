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

    private final int FONT_SIZE = 30;
    private final Font FONT = new Font("Comic Sans MS", Font.BOLD, FONT_SIZE);

    public BalloonGame(Settings settings) {
        super(settings, 800, 600);
        addBalloons(3);
        regenerateDart();
    }

    public BalloonGame() {
        this(new Settings());
    }

    public void addBalloons(int count, int balloonType) {
        for (int i = 0; i < count; i++) {
            double xPos = this.getWidth() * (1d / 3d) + RANDOM.nextDouble((this.getWidth() * (2d / 3d)));
            Balloon balloon;
            switch (balloonType) {
                case Balloon.TOUGH_BALLOON:
                    balloon = new ToughBalloon(xPos, RANDOM.nextDouble(this.getHeight()), this);
                    break;
                case Balloon.TROJAN_BALLOON:
                    balloon = new TrojanBalloon(xPos, RANDOM.nextDouble(this.getHeight()), this);
                    break;
                default:
                    balloon = new Balloon(xPos, RANDOM.nextDouble(this.getHeight()), this);
                    break;
            }
            this.balloons.add(balloon);
            this.addSprite(balloon);
        }
    }

    public void addBalloons(int count) {
        for (int i = 0; i < count; i++) {
            int balloonType = RANDOM.nextInt(4);
            this.addBalloons(1, balloonType);
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

    public void removeBalloon(Balloon balloon) {
        if (balloon.onDeath(this)) {
            this.balloons.remove(balloon);
            this.removeSprite(balloon);
        }
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
        }
    }

    @Override
    protected void update(double deltaTime, double time) {
        super.update(deltaTime, time);

        if (balloons.size() < 1) {
            this.stop();
            return;
        }

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
            removeBalloon(balloon);
        }
    }

    @Override
    protected void renderBackground(Graphics2D g) {
        g.setColor(Color.decode("#BAFFFF"));
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
    }

    @Override
    protected void renderForeground(Graphics2D g) {
        if (this.balloons.size() < 1) {
            this.renderWinMessage(g);
        }
    }

    private void renderWinMessage(Graphics2D g) {
        g.setColor(Color.black);
        g.setFont(FONT);

        String text = "You Won! Good game!";
        int width = g.getFontMetrics().stringWidth(text);
        int height = g.getFontMetrics().getHeight();
        int baseX = getWidth() / 2 - width / 2;
        int baseY = getHeight() / 2 - height / 2;

        g.fillRoundRect(baseX - 20, baseY - 20, width + 40, height + 40, 40, 40);
        g.setColor(Color.RED);
        g.fillRoundRect(baseX - 15, baseY - 15, width + 30, height + 30, 30, 30);

        g.setColor(Color.ORANGE);
        g.drawString(text, baseX, baseY + (height / 2) + (FONT_SIZE / 2));
    }

    public static void print(Object... objects) {
        for (var obj : objects) {
            System.out.println(obj + " ");
        }
    }
}
