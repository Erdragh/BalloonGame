package balloons;

import gdi.game.Settings;
import gdi.game.sprite.SpriteWorld;
import gdi.util.math.Vec2D;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

public class BalloonGame extends SpriteWorld {

    /**
     * A single final instance of a <code>Random</code>
     * object, which is used everywhere, so there aren't
     * multiple instances of <code>Random</code> everywhere.
     */
    public static final Random RANDOM = new Random();

    /**
     * The currently active dart. This is the dart that
     * is still being controlled by the player, before
     * it's shot.
     */
    private Dart activeDart;

    /**
     * A list storing all currently moving darts.
     */
    private ArrayList<Dart> movingDarts = new ArrayList();
    /**
     * A list storing all currently active balloons.
     */
    private ArrayList<Balloon> balloons = new ArrayList();

    /**
     * The font size used for text in the game.
     * Currently, the only text ever displayed
     * is the win-screen.
     */
    private final int FONT_SIZE = 30;
    /**
     * The font used for text in the game.
     * Currently, the only text ever displayed
     * is the win-screen.
     */
    private final Font FONT = new Font("Comic Sans MS", Font.BOLD, FONT_SIZE);

    public BalloonGame(Settings settings) {
        super(settings, 800, 600);
        // add three balloons, because the game is supposed to start with three.
        addBalloons(3);
        // generate the first dart.
        regenerateDart();
    }

    public BalloonGame() {
        this(new Settings());
    }

    /**
     * This method adds the specified amount of balloons of
     * a single given type.
     * @param count the amount of balloons added.
     * @param balloonType the type of the balloons added.
     */
    public void addBalloons(int count, int balloonType) {
        for (int i = 0; i < count; i++) {
            // get a random position in the valid x-coordinates.
            double xPos = this.getWidth() * (1d / 3d) + RANDOM.nextDouble((this.getWidth() * (2d / 3d)));
            // create a local variable that will store the created balloon instance
            Balloon balloon;
            // which type of balloon is generated
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
            // add the balloon to the list of all balloons
            this.balloons.add(balloon);
            // add the balloon to the sprite list of the game,
            // so its render and update method are called.
            this.addSprite(balloon);
        }
    }

    /**
     * This method adds an amount of random balloons to the game.
     * @param count the amount of balloons added.
     */
    public void addBalloons(int count) {
        for (int i = 0; i < count; i++) {
            // currently, deciding which balloon spawns is rather easy
            // because the only currently in use probabilities are 25%.
            // This means that we can just generate a random integer
            // within bound 4, and have the balloon types be integers
            // within that bound.
            int balloonType = RANDOM.nextInt(4);
            this.addBalloons(1, balloonType);
        }
    }

    /**
     * Adds a new dart to the game and moves the currently active dart
     * to the moving darts.
     */
    public void regenerateDart() {
        // if there is a currently active dart
        // this means that we need to move it to
        // the currently moving darts before generating
        // a new active dart.
        if (this.activeDart != null) {
            this.movingDarts.add(this.activeDart);
        }
        // if there is already an active dart, we will copy it, before it's shot,
        // so we retain the current angle of it and whether it is currently moving.
        this.activeDart = this.activeDart == null ? new Dart(this, 0) : this.activeDart.copy();
        // add the new sprite to the game, so it gets rendered and updated.
        this.addSprite(this.activeDart);
    }

    /**
     * removes the specified dart from the game.
     * @param dart the dart that will be removed.
     */
    public void removeDart(Dart dart) {
        this.movingDarts.remove(dart);
        this.removeSprite(dart);
    }

    /**
     * removes the specified balloon from the game,
     * if the balloon's onDeath function returns true,
     * otherwise the balloon will stay alive.
     * This is used for the tough balloon to stay
     * alive and the trojan balloon to spawn new balloons.
     * @param balloon the balloon that may be removed.
     */
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

    // I didn't import the KeyEvent class from
    // the balloon game library, because the native
    // java KeyEvent has the KeyCodes stored in variables.
    @Override
    protected void keyDown(gdi.game.events.KeyEvent ke) {
        super.keyDown(ke);
        switch (ke.getKeyCode()) {
            case KeyEvent.VK_UP:
                // tell the dart that it should be moving upwards
                this.activeDart.setRotateUp(true);
                break;
            case KeyEvent.VK_DOWN:
                // tell the dart that it should be moving downwards
                this.activeDart.setRotateDown(true);
                break;
        }
    }

    @Override
    protected void keyUp(gdi.game.events.KeyEvent ke) {
        super.keyUp(ke);
        switch (ke.getKeyCode()) {
            case KeyEvent.VK_UP:
                // tell the dart that it should stop moving
                // upwards
                this.activeDart.setRotateUp(false);
                break;
            case KeyEvent.VK_DOWN:
                // tell the dart that it should stop moving
                // downwards.
                this.activeDart.setRotateDown(false);
                break;
            case KeyEvent.VK_SPACE:
                // tell the dart that it should shoot.
                this.activeDart.setShoot(true);
                break;
        }
    }

    @Override
    protected void update(double deltaTime, double time) {
        super.update(deltaTime, time);

        // if there are no more balloons the game should stop.
        if (balloons.size() < 1) {
            this.stop();
            return;
        }

        // two lists that store all balloons and
        // darts that should be removed in this update
        // frame.
        ArrayList<Dart> dartsToRemove = new ArrayList();
        ArrayList<Balloon> balloonsToRemove = new ArrayList();

        // check each dart and balloon combination to see
        // if there is a hit anywhere. If there is, add
        // them to the lists of things to be removed.
        for (var dart : this.movingDarts) {
            for (var balloon : this.balloons) {
                Vec2D endPos = dart.getEndPos();
                if (balloon.contains(endPos)) {
                    dartsToRemove.add(dart);
                    balloonsToRemove.add(balloon);
                }
            }
        }

        // remove all the darts and balloons that
        // we want to remove.
        for (var dart : dartsToRemove) {
            removeDart(dart);
        }
        for (var balloon : balloonsToRemove) {
            removeBalloon(balloon);
        }
    }

    @Override
    protected void renderBackground(Graphics2D g) {
        // to be as accurate as possible I used a color picker
        // on the provided image.
        g.setColor(Color.decode("#BAFFFF"));
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
    }

    @Override
    protected void renderForeground(Graphics2D g) {
        // if there are no more balloons in the game
        // we should render the win message.
        if (this.balloons.size() < 1) {
            this.renderWinMessage(g);
        }
    }

    private void renderWinMessage(Graphics2D g) {

        // set the color to black. This is for the border
        // around the plaque.
        g.setColor(Color.black);

        // set the font, so we can use FontMetrics to calculate
        // the width of the text.
        g.setFont(FONT);
        // store the text in a String, so it can easily be changed.
        String text = "You Won! Good game!";
        // the width and height of the given text, based on the FontMetrics.
        int width = g.getFontMetrics().stringWidth(text);
        int height = g.getFontMetrics().getHeight();
        // the base coordinates (top left corner). However, text renders upwards
        // from the baseY coordinates, which will be adjusted later.
        int baseX = getWidth() / 2 - width / 2;
        int baseY = getHeight() / 2 - height / 2;

        // fill the outer rectangle in black for the border.
        g.fillRoundRect(baseX - 20, baseY - 20, width + 40, height + 40, 40, 40);
        // fill the inner rectangle in red for the plaque itself.
        g.setColor(Color.RED);
        g.fillRoundRect(baseX - 15, baseY - 15, width + 30, height + 30, 30, 30);

        // draw the text on the plaque in orange (which should resemble gold at least a little).
        g.setColor(Color.ORANGE);
        g.drawString(text, baseX, baseY + (height / 2) + (FONT_SIZE / 2));
    }
}
