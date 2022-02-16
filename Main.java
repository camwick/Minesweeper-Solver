import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Main {
  public static void main(String[] args) throws InterruptedException {
    System.out.println("Before running the program, ensure browser zoom is set to 100%.");
    System.out.println("The smaller the in-game board zoom the better.");
    System.out.println("We've found that a zoom of 40 or lower seems to work best.\n");

    // variables
    Scanner input = new Scanner(System.in);

    // grab difficulty
    System.out.println("Board difficulties:\n1 - Easy\n2 - Medium\n3 - Hard/Expert\n4 - Evil\n5 - Custom");
    System.out.print("Enter difficulty (int): ");
    int difficulty = input.nextInt();
    input.nextLine();

    int width = 0;
    int height = 0;
    int mineCount = 0;
    if (difficulty == 5) {
      System.out.print("\nWidth: ");
      width = input.nextInt();
      input.nextLine();

      System.out.print("Height: ");
      height = input.nextInt();
      input.nextLine();

      System.out.print("Mine count: ");
      mineCount = input.nextInt();
      input.nextLine();
    }

    // grab debug state
    boolean debug = false;
    System.out.println("\nWith debug on, the mouse cursor will move whenever the program is checking pixel positions.");
    System.out.println("Messages will also be printed to the console window (board state, error messages, etc...).");
    System.out.print("Debug on? [y/n]: ");
    String ans = input.nextLine().toLowerCase().trim();
    if (ans.equals("y"))
      debug = true;
    System.out.println();

    input.close();

    // create solver object
    long startTime = System.nanoTime();
    Solver game = new Solver(difficulty, debug, width, height, mineCount);

    // start solving
    long solveStart = System.nanoTime();
    game.solve();
    long endTime = System.nanoTime();

    System.out.println("\nElapsed Execution Time: "
        + (endTime - startTime) / 1000000000.0 + "s");
    System.out.println("Solve time: " + (solveStart - startTime) / 1000000000.0 + "s");
  }
}