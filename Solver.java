import java.util.Scanner;

public class Solver{
	public static void main(String[] args){
		// variables
		Scanner input = new Scanner(System.in);
		ScreenInput screenInput;
		String difficulty, boardInput;
		int row = -1, column = -1;

		// get user difficulty input
		System.out.print("\nBoard Difficulty Key:\n0: Beginner     - 9x9\n1: Intermediate - 16x16\n2: Expert       - 16x30\nResponse: ");
		difficulty = input.nextLine();
		System.out.println("");

		// getting row and column from difficulty
		switch(difficulty){
			case "0":
				row = 9;
				column = 9;
				break;
			case "1":
				row = 16;
				column = 16; 
				break;
			case "2":
				row = 16;
				column = 30;
				break;
			default:
				System.out.println("Invalid difficulty entered.");
				System.exit(0);
		}
		screenInput = new ScreenInput(row, column);

		// getting board string
		boardInput = screenInput.getBoardString();

		// board creation
		Board game = new Board(0, boardInput);

		System.out.println(game);
	}
}