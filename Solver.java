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
    private int[] startCoord = new int[2];

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
        calibrateBoard();
        System.out.println("Board calibrated.");

        // get initial board state
        syncBoard(false);
    }

    /**
     * Finds the upper left-hand of the game board. Position found is the first
     * pixel of the Cell array.
     */
    private void calibrateBoard() {
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

    /**
     * Gets the board state from the screen and converts it into a String based on
     * color values.
     * 
     * @param start If true, save the coordinates of the green X
     */
    private void syncBoard(boolean start) {
        // x and y coordinates of the middle of the first cell
        int centerX = this.ul_x + (this.cellSideLength / 2);
        int centerY = this.ul_y + (this.cellSideLength / 2);

        // loop through entire board on screen
        // check center of cell's pixel color and create a String based on those colors
        String boardState = "";
        for (int y = 0; y < this.gameBoard.getHeight(); ++y) {
            for (int x = 0; x < this.gameBoard.getWidth(); ++x) {
                if (this.debug)
                    this.bot.mouseMove(centerX + (x * this.cellSideLength), centerY + (y * this.cellSideLength));

                // color of center of cell
                Color px = this.bot.getPixelColor(centerX + (x * this.cellSideLength),
                        centerY + (y * this.cellSideLength));

                // distinguish between finding starting green x or not
                if (start) {
                    // start means everything is unclicked except for a single green x cell... we'll
                    // mark this cell

                    if (px.getRed() == 0 && px.getGreen() == 128 && px.getBlue() == 0) {
                        this.startCoord[0] = centerX + (x * this.cellSideLength);
                        this.startCoord[1] = centerY + (y * this.cellSideLength);
                        boardState += 'S';
                    } else
                        boardState += 'U';
                } else {
                    // start = false
                    // board will consist of more than just unclicked cells and won't have a green x

                    // grabs gray pixels
                    if (px.getRed() == 198 && px.getGreen() == 198 && px.getBlue() == 198) {
                        px = this.bot.getPixelColor(centerX + (x * this.cellSideLength) - (this.cellSideLength / 2),
                                centerY + (y * this.cellSideLength));

                        // if the cell has a white pixel ... unclicked cell
                        if (px.equals(Color.WHITE))
                            boardState += 'U';
                        else // empty cell with no white pixel
                            boardState += 'E';
                    }
                    // different shade of gray == flag
                    else if (px.getRed() == 168 && px.getGreen() == 168 && px.getBlue() == 168)
                        boardState += 'F';
                    // blue pixel == 1
                    else if (px.getBlue() == 255 && px.getRed() == 0 && px.getGreen() == 0)
                        boardState += '1';
                    // green pixel == 2
                    else if (px.getGreen() == 128 && px.getRed() == 0 && px.getBlue() == 0)
                        boardState += '2';
                    // red pixel == 3
                    else if (px.getRed() == 255 && px.getGreen() == 0 && px.getBlue() == 0)
                        boardState += '3';
                    // dark blue pixel == 4
                    else if (px.getBlue() == 128 && px.getRed() == 0 && px.getGreen() == 0)
                        boardState += '4';
                    // maroon pixel == 5
                    else if (px.getRed() == 128 && px.getGreen() == 0 && px.getBlue() == 0)
                        boardState += '5';
                    // turqoise pixel == 6
                    else if (px.getGreen() == 128 && px.getRed() == 0 && px.getBlue() == 128)
                        boardState += '6';
                    // black pixel == 7
                    else if (px.getRed() == 0 && px.getGreen() == 0 && px.getBlue() == 0)
                        boardState += '7';
                    // light gray pixel == 8
                    else if (px.getRed() == 128 && px.getGreen() == 128 && px.getBlue() == 128)
                        boardState += '8';
                }
            }
        }

        // update the board
        this.gameBoard.updateBoard(boardState);

        // debug info
        if (this.debug) {
            int counter = 0;
            for (int i = 0; i < boardState.length(); ++i) {
                if (counter <= 30) {
                    System.out.print(boardState.charAt(i));
                    counter++;
                }

                if (counter == 30) {
                    System.out.println("");
                    counter = 0;
                }
            }
            System.out.println("");
        }
    }

    /**
     * Get board object
     * 
     * @return board
     */
    public Board getBoard() {
        return this.gameBoard;
    }
}