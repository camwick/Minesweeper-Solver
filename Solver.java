import java.util.Scanner;

public class Solver{
	public static void main(String[] args){
		// variables
		Scanner input = new Scanner(System.in);
		String difficulty, boardInput;

		// user input
		System.out.print("Board Difficulty Key:\n0 - Beginner - 9x9\n1 - Intermediate - 16x16\n2 - Expert - 16x30\nResponse: ");
		difficulty = input.nextLine();

		System.out.print("\n\nGame Input String Key:\nu - unmarked cell\nf - flag\n0 - empty cell\n# - corrosponding number\nEnter String: ");
		boardInput = input.nextLine();

		// board creation
		Board game = new Board(0, boardInput);
	}
}

// Test strings:
// 00001f2110000112f1110111111f102f20001102f200001121100002f21221002f22ff1001112f310