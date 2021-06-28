import java.util.Scanner;

public class Solver{
	public static void main(String[] args){
		Scanner input = new Scanner(System.in);
		System.out.println("Key:\nu - unmarked cell\ne - empty cell\n# - corrosponding number\nEnter String: ");

		String board = input.nextLine();
		System.out.println(board);
	}
}