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

        // this.cellSideLength = csl;
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

        this.cellSideLength = newCSL;
        this.cellOffset = csl - newCSL;

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
        int[] xCoord = new int[this.gameBoard.getSize()];
        int[] yCoord = new int[this.gameBoard.getSize()];
        for (int y = 0; y < this.gameBoard.getWidth(); ++y) {
            for (int x = 0; x < this.gameBoard.getHeight(); ++x) {
                if (this.debug)
                    this.bot.mouseMove(centerX + (x * this.cellSideLength), centerY + (y * this.cellSideLength));

                // set x and y coordinates of invidiual cells
                xCoord[y * this.gameBoard.getHeight() + x] = centerX + (x * this.cellSideLength);
                yCoord[y * this.gameBoard.getHeight() + x] = centerY + (y * this.cellSideLength);

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
                        if (px.equals(Color.WHITE)) {
                            for (int i = 0; i < this.cellSideLength / 2; ++i) {
                                px = this.bot.getPixelColor(centerX + (x * this.cellSideLength) + i,
                                        centerY + (y * this.cellSideLength));

                                if (px.equals(Color.BLACK)) {
                                    boardState += 'F';
                                    break;
                                } else if (px.getRed() == 128 && px.getGreen() == 128 && px.getBlue() == 128) {
                                    boardState += 'U';
                                    break;
                                }
                            }
                        } else // empty cell with no white pixel
                            boardState += 'E';
                    }
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

        // set x and y cell coordinates
        for (int i = 0; i < this.gameBoard.getSize(); ++i) {
            this.gameBoard.getCellAtIndex(i).setCoords(xCoord[i], yCoord[i]);
        }

        // update the board
        this.gameBoard.updateBoard(boardState);

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

    public void solve() {

        int numOfFlags = 0;
        int numOfMoves = 0;

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
            if (cell.getNumUnlickedAdj() == Character.getNumericValue(cellContents)) {

                for (int j = 0; j < adjacent.length; ++j) {
                    // TODO WRITE BREAK STATEMENT WHEN FOUND ALL MINES

                    if (adjacent[j] == null)
                        continue;

                    if (adjacent[j].getContents() == 'U') {
                        adjacent[j].setContents('F');
                        numOfFlags++;
                        this.bot.mouseMove(adjacent[j].getXCoord(), adjacent[j].getYCoord());
                        this.bot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
                        this.bot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
                        this.gameBoard.setNumOfUnclicked(this.gameBoard.getUnclicked() - 1);
                    }
                }
            }
        }

        if (this.debug) {
            System.out.println("Current Board: " + this.gameBoard);
            System.out.println("number of flags: " + numOfFlags + "\n");
        }

        for (int i = 0; i < this.gameBoard.getSize(); ++i) {
            // variables
            Cell cell = this.gameBoard.getCellAtIndex(i);
            char cellContents = cell.getContents();
            Cell[] adjacent = cell.getAdjacent();

            if (cell.getContents() == 'E' || cell.getContents() == 'U')
                continue;

            // make moves
            if (cell.getAdjFlags() == Character.getNumericValue(cellContents)) {
                for (int j = 0; j < adjacent.length; ++j) {
                    if (adjacent[j] == null)
                        continue;

                    if (adjacent[j].getContents() == 'U') {
                        // adjacent[j].setContents('C');
                        numOfMoves++;
                        this.bot.mouseMove(adjacent[j].getXCoord(), adjacent[j].getYCoord());
                        this.bot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                        this.bot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                        // this.gameBoard.setNumOfUnclicked(this.gameBoard.getUnclicked() - 1);
                    }
                }
            }
        }

        // update number of unclicked cells
        this.gameBoard.updateUnclicked();

        if (this.debug) {
            System.out.println("Current Board: " + this.gameBoard);
            System.out.println("number of moves: " + numOfMoves + "\n");
        }

        if (this.gameBoard.getUnclicked() != 0) {
            syncBoard(false);
            solve();
        }
    }
}