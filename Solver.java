import java.awt.Robot;
import java.awt.AWTException;
import java.awt.Color;
import java.awt.Toolkit;

public class Solver {
    private Robot bot;
    private Board gameBoard;
    private int ul_x;
    private int ul_y;
    private int cellSideLength;
    private boolean debug = false;

    /**
     * Default constructor. Calls the debugging constructor with debugging
     * information set to false.
     * 
     * @param difficulty Integer corresponding to board difficulty
     */
    public Solver(int difficulty) {
        this(difficulty, false);
    }

    /**
     * Constructor with debugging information.
     * 
     * @param difficulty Integer corresponding to board difficulty
     * @param debug      Boolean - print debug messages and move mouse on
     *                   calibration
     */
    public Solver(int difficulty, boolean debug) {
        // set debug boolean
        this.debug = debug;

        // create bot object
        try {
            this.bot = new Robot();
        } catch (AWTException e) {
            System.out.println(e);
            System.exit(1);
        }

        if (this.debug)
            System.out.println("Difficulty given: " + difficulty);

        // create board object based on difficulty
        switch (difficulty) {
        case 1: // easy
            this.gameBoard = new Board(9, 9, 10);
            break;
        case 2: // medium
            this.gameBoard = new Board(16, 16, 40);
            break;
        case 3: // hard
            this.gameBoard = new Board(30, 16, 99);
            break;
        case 4: // evil
            this.gameBoard = new Board(30, 20, 130);
            break;
        default: // invalid difficulty
            System.out.println("Invalid difficulty entered: " + difficulty);
            System.exit(1);
            break;
        }

        // get upper left-hand corner of game board
        System.out.println("Starting board calibration...\nPlease wait the few seconds this will take.");
        calibrateUpperCorner();
        System.out.println("Board calibrated.");
    }

    /**
     * Finds the upper left-hand of the game board. Position found is the first
     * pixel of the Cell array.
     */
    private void calibrateUpperCorner() {
        // declaring variables
        Color pxColor;
        int width = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        int height = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();

        // finding ul_x
        // loop right until first gray bar
        for (int x = 0; x < width / 2; ++x) {
            if (this.debug)
                this.bot.mouseMove(x, height / 2);

            pxColor = this.bot.getPixelColor(x, height / 2);
            if (pxColor.getRed() == 198 && pxColor.getGreen() == 198 && pxColor.getBlue() == 198) {
                this.ul_x = x;
                break;
            }
        }

        // from gray bar, loop right until white pixel
        for (int x = this.ul_x; x < width / 2; ++x) {
            if (this.debug)
                this.bot.mouseMove(x, height / 2);

            pxColor = this.bot.getPixelColor(x, height / 2);
            if (pxColor.getRed() == 255 && pxColor.getGreen() == 255 && pxColor.getBlue() == 255) {
                this.ul_x = x;
                break;
            }
        }

        // finding game boarder: y
        // loops upwards until it reaches the top gray bar
        for (int y = height / 2; y > 0; --y) {
            if (this.debug)
                this.bot.mouseMove(this.ul_x, y);

            pxColor = this.bot.getPixelColor(this.ul_x, y);
            if (pxColor.getRed() == 128 && pxColor.getGreen() == 128 && pxColor.getBlue() == 128) {
                this.ul_y = y + 1;
                break;
            }
        }

        if (this.debug)
            System.out.println("upper left corner coordinates: " + this.ul_x + ", " + this.ul_y);

        // get cell side length
        // loops starting from the upper left-hand -> until gray pixel is found
        // counts every pixel including the gray pixel
        int csl = 0;
        for (int x = this.ul_x; x < width / 2; ++x) {
            if (this.debug)
                this.bot.mouseMove(x, this.ul_y);

            pxColor = this.bot.getPixelColor(x, this.ul_y);
            csl++;
            if (pxColor.getRed() == 184 && pxColor.getGreen() == 184 && pxColor.getBlue() == 184) {
                break;
            }
        }

        this.cellSideLength = csl;

        if (this.debug)
            System.out.println("Cell Side Length: " + this.cellSideLength);
    }
}
