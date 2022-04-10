import java.awt.Robot;
import java.awt.AWTException;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.util.Random;

public class Solver {
    private Robot bot;
    private Board gameBoard;
    private int ul_x;
    private int ul_y;
    private int cellSideLength;
    private int cellOffset;
    private boolean debug = false;
    private Cell startCell;
    private boolean infiniteLoop = true;
    private boolean guessIfDumb = true;

    /**
     * Default constructor.
     * Initiates Solver object that will ultimately solve the minesweeper board.
     * 
     * @param difficulty int, difficulty of minesweeper board.
     * @param guessing   boolean, guessing turned on when true.
     * @param debug      boolean, debug mode turned on when true.
     */
    public Solver(int difficulty, boolean guessing, boolean debug) {
        this(difficulty, guessing, debug, 0, 0, 0);
    }

    /**
     * Overloaded constructor.
     * Initiates Solver object that will ultimately solve the minesweeper board
     * 
     * @param difficulty int, difficulty of minesweeper board.
     * @param guessing   boolean, guessing turned on when true.
     * @param debug      boolean, guessing turned on when true.
     * @param width      int, width of minesweeper board.
     * @param height     int, height of minesweeper board.
     * @param mineCount  int, number of mines on minesweeper board.
     */
    public Solver(int difficulty, boolean guessing, boolean debug, int width, int height, int mineCount) {
        // set debug boolean
        this.debug = debug;

        // set guessing boolean
        this.guessIfDumb = guessing;

        // create bot object
        try {
            this.bot = new Robot();
            this.bot.setAutoWaitForIdle(true);
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

        // get initial board state
        syncBoard();

        System.out.println("Board calibrated.");

        // make first move
        leftClick(startCell);

        // update Cells
        try {
            Thread.sleep(100);
            updateCells(this.startCell);
            this.gameBoard.resetUnclickedVisitedCells();
        } catch (InterruptedException e) {
            System.out.println(e);
        }
    }

    /**
     * Finds the upper left hand corner of the minesweeper board, cell length, and
     * starting green x.
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
     * Sets all the cell value's x and y positions.
     * Also finds and stores the starting green x position to start the game.
     */
    private void syncBoard() {
        // x and y coordinates of the middle of the first cell
        int centerX = this.ul_x + ((this.cellSideLength - this.cellOffset) / 2) + 1;
        int centerY = this.ul_y + ((this.cellSideLength - this.cellOffset) / 2) + 1;

        // loop through entire board on screen
        // setting x/y coordinates of all cells and locating start position
        int xCoord;
        int yCoord;
        for (int y = 0; y < this.gameBoard.getHeight(); ++y) {
            for (int x = 0; x < this.gameBoard.getWidth(); ++x) {
                if (this.debug)
                    this.bot.mouseMove(centerX + (x * (this.cellSideLength)),
                            centerY + (y * (this.cellSideLength)));

                // set x and y coordinates of invidiual cells
                xCoord = centerX + (x * (this.cellSideLength));
                yCoord = centerY + (y * (this.cellSideLength));
                this.gameBoard.getCellAtIndex(y * this.gameBoard.getWidth() + x).setCoords(xCoord, yCoord);

                // set cell index
                this.gameBoard.getCellAtIndex(y * this.gameBoard.getWidth() + x)
                        .setIndex(y * this.gameBoard.getWidth() + x);

                // color of center of cell
                Color px = this.bot.getPixelColor(centerX + (x * (this.cellSideLength)),
                        centerY + (y * (this.cellSideLength)));

                // save position of first click
                if (px.getRed() == 0 && px.getGreen() == 128 && px.getBlue() == 0) {
                    this.startCell = this.gameBoard.getCellAtIndex(y * this.gameBoard.getWidth() + x);
                }
            }
        }
    }

    /**
     * Recursively traverses the adjacent Cells to update the uncovered cells'
     * contents. Uses BFS tree/graph search algorithm to traverse the cell objects.
     * 
     * @param cell starting node of the graph DFS
     */
    private void updateCells(Cell cell) {
        // skip if already visited
        if (cell.isVisited())
            return;

        if (this.debug)
            this.bot.mouseMove(cell.getXCoord(), cell.getYCoord());

        // get pixel of given cell
        Color px = this.bot.getPixelColor(cell.getXCoord(), cell.getYCoord());
        char contents = '!';

        // gray pixel
        if (px.getRed() == 198 && px.getGreen() == 198 && px.getBlue() == 198) {
            // need to differentiate between empty and unclicked
            if (this.debug)
                this.bot.mouseMove(cell.getXCoord() - ((this.cellSideLength) / 2) + 2, cell.getYCoord());

            // white pixel location
            px = this.bot.getPixelColor(cell.getXCoord() - ((this.cellSideLength) / 2) + 2, cell.getYCoord());

            if (px.equals(Color.WHITE))
                contents = 'U';
            else
                contents = 'E';
        }
        // flag
        else if (px.equals(Color.BLACK))
            contents = 'F';
        // blue pixel == 1
        else if (px.getBlue() == 255 && px.getRed() == 0 && px.getGreen() == 0)
            contents = '1';
        // green pixel == 2
        else if (px.getGreen() == 128 && px.getRed() == 0 && px.getBlue() == 0)
            contents = '2';
        // red pixel == 3
        else if (px.getRed() == 255 && px.getGreen() == 0 && px.getBlue() == 0)
            contents = '3';
        // dark blue pixel == 4
        else if (px.getBlue() == 128 && px.getRed() == 0 && px.getGreen() == 0)
            contents = '4';
        // maroon pixel == 5
        else if (px.getRed() == 128 && px.getGreen() == 0 && px.getBlue() == 0)
            contents = '5';
        // turqoise pixel == 6
        else if (px.getGreen() == 128 && px.getRed() == 0 && px.getBlue() == 128)
            contents = '6';
        // black pixel == 7
        else if (px.getRed() == 0 && px.getGreen() == 0 && px.getBlue() == 0)
            contents = '7';
        // light gray pixel == 8
        else if (px.getRed() == 128 && px.getGreen() == 128 && px.getBlue() == 128)
            contents = '8';
        // if something bad happens, print the color that gets detected
        else {
            System.out.println("Cell index: " + cell.getIndex());
            System.out.println("Color: " + px);
        }

        if (contents == 'U') {
            cell.visit();
            return;
        }

        // change cell's contents if it changed
        if (cell.getContents() != contents) {
            cell.setContents(contents);
            this.gameBoard.setNumOfUnclicked(this.gameBoard.getUnclicked() - 1);
            cell.visit();
        } else
            return;

        // if cell changed contents then check it's neighbors for changes
        Cell[] adjacent = cell.getAdjacent();
        for (int i = 0; i < adjacent.length; ++i) {
            if (adjacent[i] != null)
                updateCells(adjacent[i]);
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

    /**
     * Left clicks cells that are safe.
     */
    private void makeMoves() {
        // loop through entire board
        for (int i = 0; i < this.gameBoard.getSize(); ++i) {
            // variables
            Cell cell = this.gameBoard.getCellAtIndex(i);
            char cellContents = cell.getContents();
            Cell[] adjacent = cell.getAdjacent();

            if (cellContents == 'E' || cellContents == 'U' || cellContents == 'F')
                continue;

            // make moves
            if (cell.getNumAdjFlags() == Character.getNumericValue(cellContents) && cell.getNumUnlickedAdj() != 0) {
                for (int j = 0; j < adjacent.length; ++j) {
                    if (adjacent[j] == null)
                        continue;

                    if (adjacent[j].getContents() == 'U') {
                        leftClick(adjacent[j]);
                        updateCells(adjacent[j]);
                        this.gameBoard.resetUnclickedVisitedCells();
                        this.infiniteLoop = false;
                    }
                }
            }
        }
        // this.bot.setAutoDelay(0);
    }

    /**
     * Right clicks the given cell.
     * 
     * @param cell
     */
    private void rightClick(Cell cell) {
        this.bot.mouseMove(cell.getXCoord(), cell.getYCoord());
        this.bot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
        this.bot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
    }

    /**
     * Left clicks the given cell.
     * 
     * @param cell
     */
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
            System.out.println("\nBoard state before flagging/moves: " + this.gameBoard);
        }

        boolean basicFound = false;

        if (this.debug)
            System.out.print("\nBasic Mine Cases.");

        // basic flags
        for (int i = 0; i < this.gameBoard.getSize(); ++i) {
            // variables
            Cell cell = this.gameBoard.getCellAtIndex(i);
            char cellContents = cell.getContents();

            if (cellContents == 'E' || cellContents == 'U' || cellContents == 'F')
                continue;

            // marks flags
            Cell[] adjacent = cell.getAdjacent();
            if (cell.getNumUnlickedAdj() == Character.getNumericValue(cellContents) - cell.getNumAdjFlags()
                    && cell.getNumAdjFlags() != Character.getNumericValue(cellContents)) {
                for (int j = 0; j < adjacent.length; ++j) {
                    if (cell.getNumAdjFlags() == Character.getNumericValue(cellContents))
                        break;

                    if (adjacent[j] == null || adjacent[j].getContents() == 'F')
                        continue;

                    // flag unclicked cell
                    if (adjacent[j].getContents() == 'U') {
                        adjacent[j].setContents('F');
                        rightClick(adjacent[j]);
                        this.gameBoard.setNumOfUnclicked(this.gameBoard.getUnclicked() - 1);
                        adjacent[j].visit();
                        this.gameBoard.decreaseMinecount();
                        basicFound = true;
                    }
                }
            }
        }

        if (this.debug)
            System.out.println(this.gameBoard);

        // if basic mines were found -> make moves
        if (basicFound) {
            makeMoves();

            if (this.debug)
                System.out.println("\nAfter making moves" + this.gameBoard);
        }
        // if no basic mines found -> check advanced patterns
        else {
            if (this.debug) {
                System.out.print("\nNo basic cases.\n\n");
                System.out.println("\nChecking patterns");
            }

            // assume infinite loop
            this.infiniteLoop = true;

            // advanced patterns
            for (int i = 0; i < this.gameBoard.getSize(); ++i) {
                /*
                 * these variables could all be declared outside of the for loop because they
                 * are used in multiple loops
                 */
                Cell cell = this.gameBoard.getCellAtIndex(i);
                char cellContents = cell.getContents();

                if (cell.getContents() == 'E' || cell.getContents() == 'U' || cell.getContents() == 'F')
                    continue;

                Patterns pattern = new Patterns(cell);

                // 1 - 2 Pattern
                // check for a two
                if (Character.getNumericValue(cellContents) - cell.getNumAdjFlags() == 2) {
                    if (pattern.onetwoPattern()) {
                        if (this.debug)
                            System.out.println("Found 1-2 Pattern.");
                        // mark flag
                        Cell mine = pattern.getMine();

                        // mark flag
                        mine.setContents('F');
                        rightClick(mine);
                        this.gameBoard.setNumOfUnclicked(this.gameBoard.getUnclicked() - 1);
                        this.gameBoard.decreaseMinecount();
                        mine.visit();
                        this.infiniteLoop = false;
                    }
                }

                // 1-1 Pattern
                // check for a one
                if (cell.getNumUnlickedAdj() == 2
                        && Character.getNumericValue(cellContents) - cell.getNumAdjFlags() == 1) {
                    if (pattern.oneOnePattern()) {
                        if (this.debug)
                            System.out.println("Found 1-1 Pattern.");

                        Cell[] safeCells = pattern.getSafeCells();
                        for (int j = 0; j < safeCells.length; ++j) {
                            if (safeCells[j] != null) {
                                leftClick(safeCells[j]);
                                updateCells(safeCells[j]);
                                this.gameBoard.resetUnclickedVisitedCells();
                                this.infiniteLoop = false;
                            }
                        }

                    }
                }

                // hole Pattern
                if (cell.getNumUnlickedAdj() - 1 == Character.getNumericValue(cellContents) - cell.getNumAdjFlags()) {
                    if (this.debug)
                        System.out.println("HOLE CANDIDATE");

                    if (pattern.holePattern()) {
                        if (this.debug) {
                            System.out.println("Found hole Pattern.");
                        }

                        Cell[] safeCells = pattern.getSafeCells();
                        for (int j = 0; j < safeCells.length; ++j) {
                            if (safeCells[j] != null) {
                                leftClick(safeCells[j]);
                                updateCells(safeCells[j]);
                                this.gameBoard.resetUnclickedVisitedCells();
                                this.infiniteLoop = false;
                            }
                        }

                        // advanced hole case
                        if (Character.getNumericValue(safeCells[1].getContents())
                                - safeCells[1].getNumAdjFlags() == 1) {
                            if (pattern.advancedHole()) {
                                if (this.debug)
                                    System.out.println("Found advanced hole");
                                safeCells = pattern.getSafeCells();

                                for (int j = 0; j < safeCells.length; ++j) {
                                    if (safeCells[j] != null) {
                                        leftClick(safeCells[j]);
                                        updateCells(safeCells[j]);
                                        this.gameBoard.resetUnclickedVisitedCells();
                                    }
                                }
                            }
                        }
                    }
                }
            }

            /*
             * not including the two cells adjacent to the hole:
             * - trunk of the hole needs one less unclickedAdj not including around the hole
             */

            if (this.debug)
                System.out.println("Advanced cases" + this.gameBoard);

            makeMoves();
        }

        // end solver if all Unclicked cells are clicked
        if (this.gameBoard.getUnclicked() == 0) {
            System.out.println("\nBoard solved!");
            return;
        }

        // click all unclicked cells if minecount == 0 & end the solver
        if (this.gameBoard.getMineCount() == 0) {
            for (int i = 0; i < this.gameBoard.getSize(); ++i) {
                if (this.gameBoard.getCellAtIndex(i).getContents() == 'U')
                    leftClick(this.gameBoard.getCellAtIndex(i));
            }
            System.out.println("\nBoard solved!");
            return;
        }

        // if an infinite loop were to occur: click a random unclicked cell
        if (this.infiniteLoop && this.gameBoard.getUnclicked() != 0 && this.guessIfDumb) {

            if (this.debug) {
                System.out.println("\nNo Solution found, I am now guessing!\n");
            }

            int random = new Random().nextInt(this.gameBoard.getUnclicked());
            Cell guessing = this.gameBoard.getCellAtIndex(0);

            int counter = 0;
            for (int i = 0; i < this.gameBoard.getSize(); ++i) {
                Cell cell = this.gameBoard.getCellAtIndex(i);
                char cellContents = cell.getContents();
                if (cellContents == 'U') {
                    if (counter == random) {
                        guessing = cell;
                        break;
                    }
                    counter++;
                }
            }

            leftClick(guessing);

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Color px = this.bot.getPixelColor(guessing.getXCoord(), guessing.getYCoord());

            if ((px.getRed() == 103 || px.getRed() == 102) && px.getGreen() == 108 && px.getBlue() == 101) {
                System.out.println("Oh no! I am so dumb and clumsy... I hit a mine!");
                return;
            }

            updateCells(guessing);
            this.gameBoard.resetUnclickedVisitedCells();
            this.infiniteLoop = false;
            makeMoves();
        }

        // exit if infinite loop will occurr
        if (this.infiniteLoop) {
            System.out.println("No solution found - avoiding infinite loop.\nEnding Program.");
            System.out.println("Number of unclicked cells: " + this.gameBoard.getUnclicked());
            System.out.println("State of board" + this.gameBoard);
            return;
        }

        if (this.gameBoard.getUnclicked() != 0) {
            solve();
        }
    }
}