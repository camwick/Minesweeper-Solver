import javax.swing.event.CellEditorListener;

public class Board {
    private final int width;
    private final int height;
    private int mineCount;
    private Cell[] board;

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
        this.board = new Cell[width * height];

        // initiate Cell objects
        for (int i = 0; i < this.width * this.height; i++) {
            this.board[i] = new Cell();
        }
    }

    // set board object's adjacent cells
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