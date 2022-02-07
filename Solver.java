import java.awt.Robot;
import java.awt.AWTException;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.InputEvent;

public class Solver {
    private Robot bot;
    private Board gameBoard;
    private int ul_x;
    private int ul_y;
    private int cellSideLength;
    private int cellOffset;
    private boolean debug = false;
    private int[] startCoord = new int[2];

    /**
     * Constructor with debugging information.
     * 
     * @param difficulty Integer corresponding to board difficulty
     * @param debug      Boolean - print debug messages and move mouse on
     *                   calibration
     * @throws InterruptedException
     */
    public Solver(int difficulty, boolean debug, int width, int height, int mineCount) throws InterruptedException {
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
            case 5: // custom
                this.gameBoard = new Board(width, height, mineCount);
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
        syncBoard(true);

        // make first move
        this.bot.mouseMove(startCoord[0], startCoord[1]);
        this.bot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        this.bot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        Thread.sleep(500);

        // update board
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
        // csl will help calculate offset

        // find the gray corner of the cell to act as 'true' top left corner
        for (int y = 0; y < csl / 2; ++y) {
            pxColor = this.bot.getPixelColor(this.ul_x + csl / 2, this.ul_y + y);

            if (this.debug)
                this.bot.mouseMove(this.ul_x + csl / 2, this.ul_y + y);

            if (pxColor.getRed() == 198 && pxColor.getGreen() == 198 && pxColor.getBlue() == 198) {
                this.ul_y = this.ul_y + y;
                break;
            }
        }

        for (int x = 0; x < csl / 2; ++x) {
            pxColor = this.bot.getPixelColor(this.ul_x + x, this.ul_y);

            if (this.debug)
                this.bot.mouseMove(this.ul_x + x, this.ul_y);

            if (pxColor.getRed() == 198 && pxColor.getGreen() == 198 && pxColor.getBlue() == 198) {
                this.ul_x = this.ul_x + x;
                break;
            }
        }

        // calculate new side length + offset
        int newCSL = 0;
        for (int x = 0; x < csl; ++x) {
            pxColor = this.bot.getPixelColor(this.ul_x + x, this.ul_y);

            if (this.debug)
                this.bot.mouseMove(this.ul_x + x, this.ul_y);

            newCSL++;
            if (!(pxColor.getRed() == 198 && pxColor.getGreen() == 198 && pxColor.getBlue() == 198))
                break;
        }

        this.cellSideLength = csl;
        this.cellOffset = csl - newCSL;

        if (this.debug) {
            System.out.println("Cell length: " + this.cellSideLength);
            System.out.println("Cell offset: " + this.cellOffset);
        }
    }

    /**
     * Gets the board state from the screen and converts it into a String based on
     * color values.
     * 
     * @param start If true, save the coordinates of the green X
     */
    private void syncBoard(boolean start) {
        boolean breakStart = false;
        int numUnclicked = 0;

        // x and y coordinates of the middle of the first cell
        int centerX = this.ul_x + ((this.cellSideLength - this.cellOffset) / 2) + 1;
        int centerY = this.ul_y + ((this.cellSideLength - this.cellOffset) / 2) + 1;

        // loop through entire board on screen
        // check center of cell's pixel color and create a String based on those colors
        String boardState = "";
        int[] xCoord = new int[this.gameBoard.getSize()];
        int[] yCoord = new int[this.gameBoard.getSize()];
        for (int y = 0; y < this.gameBoard.getWidth(); ++y) {
            for (int x = 0; x < this.gameBoard.getHeight(); ++x) {
                if (this.debug)
                    this.bot.mouseMove(centerX + (x * (this.cellSideLength)),
                            centerY + (y * (this.cellSideLength)));

                // set x and y coordinates of invidiual cells
                xCoord[y * this.gameBoard.getHeight() + x] = centerX + (x * (this.cellSideLength));
                yCoord[y * this.gameBoard.getHeight() + x] = centerY + (y * (this.cellSideLength));

                // color of center of cell
                Color px = this.bot.getPixelColor(centerX + (x * (this.cellSideLength)),
                        centerY + (y * (this.cellSideLength)));

                // distinguish between finding starting green x or not
                if (start) {
                    // start means everything is unclicked except for a single green x cell... we'll
                    // mark this cell

                    if (px.getRed() == 0 && px.getGreen() == 128 && px.getBlue() == 0) {
                        this.startCoord[0] = centerX + (x * this.cellSideLength);
                        this.startCoord[1] = centerY + (y * this.cellSideLength);
                        breakStart = true;
                        break;
                    }
                } else {
                    // start = false

                    // grab gray pixel
                    if (px.getRed() == 198 && px.getGreen() == 198 && px.getBlue() == 198) {
                        // shift over to check for white pixel
                        if (this.debug)
                            this.bot.mouseMove(
                                    ((centerX + (x * this.cellSideLength))
                                            - ((this.cellSideLength) / 2) + 2),
                                    centerY + (y * this.cellSideLength));
                        px = this.bot.getPixelColor(
                                ((centerX + (x * this.cellSideLength))
                                        - ((this.cellSideLength) / 2) + 2),
                                centerY + (y * this.cellSideLength));

                        if (px.equals(Color.WHITE)) {
                            boardState += 'U';
                            numUnclicked++;
                        } else
                            boardState += 'E';
                    }
                    // flag
                    else if (px.equals(Color.BLACK))
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
                    // if something bad happens, print the color that gets detected
                    else {
                        System.out.printf("X: %d\nY: %d\n", x, y);
                        System.out.println("Color: " + px);
                    }
                }
            }
            if (breakStart)
                break;
        }

        if (start)
            return;

        // set x and y cell coordinates
        for (int i = 0; i < this.gameBoard.getSize(); ++i) {
            this.gameBoard.getCellAtIndex(i).setCoords(xCoord[i], yCoord[i]);
        }

        // update the board
        this.gameBoard.updateBoard(boardState);

        // update number of unclicked cells
        this.gameBoard.setNumOfUnclicked(numUnclicked);

        // debug info
        if (this.debug) {
            System.out.println(this.gameBoard);
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

    private void makeMoves() {
        for (int i = 0; i < this.gameBoard.getSize(); ++i) {
            // variables
            Cell cell = this.gameBoard.getCellAtIndex(i);
            char cellContents = cell.getContents();
            Cell[] adjacent = cell.getAdjacent();

            if (cell.getContents() == 'E' || cell.getContents() == 'U' || cell.getContents() == 'F')
                continue;

            // make moves
            if (cell.getAdjFlags() == Character.getNumericValue(cellContents) && !(cell.getNumUnlickedAdj() == 0)) {
                for (int j = 0; j < adjacent.length; ++j) {
                    if (cell.getNumUnlickedAdj() == 0)
                        break;

                    if (adjacent[j] == null || cell.getContents() == 'E')
                        continue;

                    if (adjacent[j].getContents() == 'U') {
                        leftClick(adjacent[j]);
                    }
                }
            }
        }
        syncBoard(false);
    }

    private void rightClick(Cell cell) {
        this.bot.mouseMove(cell.getXCoord(), cell.getYCoord());
        this.bot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
        this.bot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
    }

    private void leftClick(Cell cell) {
        this.bot.mouseMove(cell.getXCoord(), cell.getYCoord());
        this.bot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        this.bot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }

    /**
     * Main solve loop.
     * Loops over the board object repeatedly looking for mine patterns. Marks mines
     * and clicks safe cells.
     */
    public void solve() {
        if (this.debug) {
            System.out.println("Board state before flagging/moves: " + this.gameBoard);
        }

        int counter = 0;

        if (this.debug)
            System.out.println("\nBasic Mine Cases");

        // marks basic flags
        for (int i = 0; i < this.gameBoard.getSize(); ++i) {
            // variables
            Cell cell = this.gameBoard.getCellAtIndex(i);
            char cellContents = cell.getContents();

            // skip empty/unclicked cells
            if (cellContents == 'E' || cellContents == 'U' || cellContents == 'F')
                continue;

            // marks flags
            Cell[] adjacent = cell.getAdjacent();
            if (cell.getNumUnlickedAdj() == Character.getNumericValue(cellContents) - cell.getAdjFlags()
                    && cell.getAdjFlags() != Character.getNumericValue(cellContents)) {
                for (int j = 0; j < adjacent.length; ++j) {
                    if (cell.getAdjFlags() == Character.getNumericValue(cellContents))
                        break;

                    if (adjacent[j] == null || adjacent[j].getContents() == 'F')
                        continue;

                    if (adjacent[j].getContents() == 'U') {
                        adjacent[j].setContents('F');
                        rightClick(adjacent[j]);

                        counter++;
                    }
                }
            }
        }

        if (counter != 0)
            makeMoves();

        if (this.debug)
            System.out.println("Advanced Mine Cases");
        // after checking basic cases - Check for patterns
        if (counter == 0) {
            if (this.debug)
                System.out.println("Checking patterns");

            for (int i = 0; i < this.gameBoard.getSize(); ++i) {
                /*
                 * these variables could all be declared outside of the for loop because they
                 * are used in
                 * multiple loops
                 */
                Cell cell = this.gameBoard.getCellAtIndex(i);
                char cellContents = cell.getContents();

                if (cell.getContents() == 'E' || cell.getContents() == 'U' || cell.getContents() == 'F')
                    continue;

                Patterns pattern = new Patterns(cell);

                // 1 - 2 Pattern
                if (Character.getNumericValue(cellContents) - cell.getAdjFlags() == 2) {
                    if (pattern.onetwoPattern()) {
                        if (this.debug)
                            System.out.println("Found 1-2 Pattern.");
                        // mark flag
                        Cell mine = pattern.getMine();

                        // mark flag
                        mine.setContents('F');
                        rightClick(mine);
                    }
                }
            }
            makeMoves();
        }
        if (this.debug) {
            System.out.println("\nBoard State after flagging/moves: " + this.gameBoard + "\n");
        }

        if (this.gameBoard.getUnclicked() != 0) {
            solve();
        }
    }
}