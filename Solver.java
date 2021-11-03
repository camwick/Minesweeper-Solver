public class Solver {
    private Board gameBoard;

    public Solver(int difficulty) {
        switch (difficulty) {
        case 1:
            this.gameBoard = new Board(9, 9, 10);
            break;
        case 2:
            this.gameBoard = new Board(16, 16, 40);
            break;
        case 3:
            this.gameBoard = new Board(30, 16, 99);
            break;
        case 4:
            this.gameBoard = new Board(30, 20, 130);
            break;
        default:
            System.out.println("Invalid difficulty entered: " + difficulty);
            System.exit(1);
            break;
        }
    }
}