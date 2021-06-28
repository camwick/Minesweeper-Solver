import java.util.Scanner;

public class Solver{
	public static void main(String[] args){
		Scanner input = new Scanner(System.in);
		System.out.println("Key:\nu - unmarked cell\nf - flag\n0 - empty cell\n# - corrosponding number\nEnter String: ");

		String boardInput = input.nextLine();

		String[] boardArr = new String[9];

		int index = 0;
		for(int i = 0; i < 81; i+=9){
			boardArr[index] = boardInput.substring(i, i+9);
			index++;
		}
	}
}

// Test strings:
// 00001f2110000112f1110111111f102f20001102f200001121100002f21221002f22ff1001112f310