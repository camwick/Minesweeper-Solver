import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
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
    System.out.print("\nDebug on? [y/n]: ");
    String ans = input.nextLine().toLowerCase().trim();
    if (ans.equals("y"))
      debug = true;
    System.out.println();

    // create solver object
    Solver game = new Solver(difficulty, debug, width, height, mineCount);

    // start solving
    game.solve();
  }
}