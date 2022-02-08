public class Board {
    private final int width;
    private final int height;
    private int mineCount;
    private Cell[] board;
    private int unclicked;

    /**
     * Default constuctor.
     * Constructs 2-D board object
     * 
     * @param width  int, board width
     * @param height int, board height
     * @param mines  int, amount of mines in board
     */
    public Board(int width, int height, int mines) {
        this.width = width;
        this.height = height;
        this.unclicked = width * height;
        this.mineCount = mines;
        this.board = new Cell[width * height];

        // initiate Cell objects
        for (int i = 0; i < this.width * this.height; i++)
            this.board[i] = new Cell();

        // set adjacent cells
        setBoardAdjacents();
    }

    /**
     * Get board size.
     * 
     * @return int, size of Cell array
     */
    public int getSize() {
        return this.board.length;
    }

    /**
     * Get a Cell at specified index.
     * 
     * @param index int, index of Cell in Cell array
     * @return Cell
     */
    public Cell getCellAtIndex(int index) {
        return this.board[index];
    }

    /**
     * Get number of unclicked cells
     * 
     * @return int, unclicked cells
     */
    public int getUnclicked() {
        return this.unclicked;
    }

    /**
     * Get width of board object
     * 
     * @return int, width of board
     */
    public int getWidth() {
        return this.width;
    }

    /**
     * Get height of board object
     * 
     * @return int, height of board
     */
    public int getHeight() {
        return this.height;
    }

    /**
     * Set number of unclicked cells
     * 
     * @param unclicked int, number of unclicked cells
     */
    public void setNumOfUnclicked(int unclicked) {
        this.unclicked = unclicked;
    }

    /**
     * Set the contents of a specified Cell
     * 
     * @param index   int, index of Cell in Cell array
     * @param content char, contents of the Cell to be changed to
     */
    public void setCellContent(int index, char content) {
        this.board[index].setContents(content);
    }

    /**
     * Set board object's adjacent cells.
     * 
     * Loops through entire board object and sets every cell's adjacent adjacentCell
     * array to the correct cells.
     */
    public void setBoardAdjacents() {
        Cell[] adjacent = new Cell[8];

        // ul corner
        int index = 0;
        for (int i = 0; i < 4; ++i) {
            if (i >= 0 || i <= 3)
                adjacent[i] = null;
        }
        adjacent[4] = this.board[index + 1];
        adjacent[5] = null;
        adjacent[6] = this.board[index + this.width];
        adjacent[7] = this.board[index + this.width + 1];
        this.board[index].setAdjacent(adjacent);

        // ur corner
        index = this.width - 1;
        for (int i = 0; i < 3; ++i) {
            if (i >= 0 || i <= 2)
                adjacent[i] = null;
        }
        adjacent[3] = this.board[index - 1];
        adjacent[4] = null;
        adjacent[5] = this.board[index + this.width - 1];
        adjacent[6] = this.board[index + this.width];
        adjacent[7] = null;
        this.board[index].setAdjacent(adjacent);

        // ll
        index = (this.width * (this.height - 1));
        adjacent[0] = null;
        adjacent[1] = this.board[index - this.width];
        adjacent[2] = this.board[index - this.width + 1];
        adjacent[3] = null;
        adjacent[4] = this.board[index + 1];
        for (int i = 5; i < 8; ++i) {
            if (i >= 5 || i <= 7)
                adjacent[i] = null;
        }
        this.board[index].setAdjacent(adjacent);

        // lr
        index = this.width * this.height - 1;
        adjacent[0] = this.board[index - this.width - 1];
        adjacent[1] = this.board[index - this.width];
        adjacent[2] = null;
        adjacent[3] = this.board[index - 1];
        for (int i = 4; i < 8; ++i) {
            if (i >= 4 || i <= 7)
                adjacent[i] = null;
        }
        this.board[index].setAdjacent(adjacent);

        // Find the top of the board and set the adjacents
        // The top of the board will be the indexes between 0 and (width-2)
        for (int i = 1; i < this.width - 1; ++i) {
            // each index at the top of the board will have adjacent cells in surrounding
            // locations 3-7
            for (int j = 0; j < 8; ++j) {
                if (j < 3) {
                    adjacent[j] = null;
                } else if (j == 3) {
                    adjacent[j] = this.board[i - 1];
                } else if (j == 4) {
                    adjacent[j] = this.board[i + 1];
                } else {
                    adjacent[j] = this.board[i + this.width - 1 + (j - 5)];
                }
            }
            this.board[i].setAdjacent(adjacent);
        }

        // Find the bottom of the board and set the adjacents
        // The bottom of the board will be the indexes between (height-1) to (this.width
        // * this.height) - 2
        for (int i = (this.height - 1) * this.width + 1; i <= (this.width * this.height) - 2; ++i) {
            for (int j = 0; j < 8; ++j) {
                if (j < 3) {
                    adjacent[j] = this.board[i - this.width - 1 + j];
                } else if (j == 3) {
                    adjacent[j] = this.board[i - 1];
                } else if (j == 4) {
                    adjacent[j] = this.board[i + 1];
                } else {
                    adjacent[j] = null;
                }
            }
            this.board[i].setAdjacent(adjacent);
        }

        // Find the lefthand side of the board and set the adjacents
        for (int i = this.width; i <= this.width * (this.height - 2); i = i + this.width) {
            for (int j = 0; j < 8; ++j) {
                if (j == 0 || j == 3 || j == 5) {
                    adjacent[j] = null;
                } else if (j < 3) {
                    adjacent[j] = this.board[i - this.width + j - 1];
                } else if (j == 4) {
                    adjacent[j] = this.board[i + 1];
                } else if (j == 6) {
                    adjacent[j] = this.board[i + this.width];
                } else if (j == 7) {
                    adjacent[j] = this.board[i + this.width + 1];
                }
            }
            this.board[i].setAdjacent(adjacent);
        }

        // Find the righthand side of the board and set the adjacents
        for (int i = (this.width - 1) + this.width; i <= (this.width * this.height - 1) - this.width; i = i
                + this.width) {
            for (int j = 0; j < 8; j++) {
                if (j == 2 || j == 4 || j == 7) {
                    adjacent[j] = null;
                } else if (j < 2) {
                    adjacent[j] = this.board[i - this.width - 1 + j];
                } else if (j == 3) {
                    adjacent[j] = this.board[i - 1];
                } else if (j == 5) {
                    adjacent[j] = this.board[i + this.width - 1];
                } else if (j == 6) {
                    adjacent[j] = this.board[i + this.width];
                }
            }
            this.board[i].setAdjacent(adjacent);
        }

        // Set the adjacents for every square in the inside of the board
        int row = 2;
        for (int i = this.width + 1; i <= (this.width * this.height - 2) - this.width; ++i) { // 10 70
            for (int j = 0; j < 8; ++j) {
                if (j <= 2) {
                    adjacent[j] = this.board[i - this.width - 1 + j]; // 0 1 2
                } else if (j == 3) {
                    adjacent[j] = this.board[i - 1];
                } else if (j == 4) {
                    adjacent[j] = this.board[i + 1];
                } else {
                    // j == 5 || 6 || 7
                    adjacent[j] = this.board[i + this.width - 1 + j - 5];
                }
            }

            // set adjacents
            this.board[i].setAdjacent(adjacent);

            // Move the index to the next row
            if (i % (row * this.width - 2) == 0) {
                ++row;
                i = i + 2;
                continue;
            }
        }
    }

    /**
     * Update board object to match screen.
     * 
     * @param boardKey String representing actual minesweeper board
     */
    public void updateBoard(String boardKey) {
        for (int i = 0; i < this.board.length; ++i) {
            this.board[i].setContents(boardKey.charAt(i));
        }
    }

    public void resetVisitedCells() {
        for (int i = 0; i < this.board.length; ++i) {
            this.board[i].resetVisited();
        }
    }

    /**
     * Outputs a string of a board object in a pretty fasion :)
     */
    @Override
    public String toString() {
        String output = "";

        for (int i = 0; i < this.board.length; ++i) {
            if (i % this.width == 0)
                output += "\n";

            output += this.board[i] + " ";
        }

        return output;
    }
}