public class Board {
    private final int width;
    private final int height;
    private int mineCount;
    private Cell[][] board;

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
        this.mineCount = mines;
        this.board = new Cell[width][height];

        // initiate Cell objects
        for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.height; y++) {
                this.board[x][y] = new Cell();
            }
        }
    }

    /**
     * Update board object to match screen.
     * 
     * @param boardKey String representing actual minesweeper board
     */
    public void updateBoard(String boardKey) {
        for (int y = 0; y < this.board.length; ++y) {
            for (int x = 0; x < this.board[y].length; ++x) {
                this.board[y][x].setContents(boardKey.charAt(this.board[y].length * y + x));
            }
        }
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
     * Outputs a string of a board object in a pretty fasion :)
     */
    public String toString() {
        String output = "";

        for (Cell[] cells : this.board) {
            for (Cell cell : cells) {
                output += cell + " ";
            }
            output += "\n";
        }

        return output;
    }
}
