public class Board {
    private final int width;
    private final int height;
    private int mineCount;
    private Cell[][] board;

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
}