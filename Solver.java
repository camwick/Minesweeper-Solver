import java.awt.Robot;
import java.awt.AWTException;

public class Solver {
    private Robot bot;
    private Board gameBoard;

    public Solver(int difficulty) {
        // create bot object
        try{
            this.bot = new Robot();
        }
        catch(AWTException e){
            System.out.println(e);
            System.exit(1);
        }
        
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
    }
}