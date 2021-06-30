public class Board{
	// private variables
	private int difficulty;
	private Cell[][] board;

	// constructors
	public Board(int difficulty, String gameString){
		this.difficulty = difficulty;

		switch(this.difficulty){
			case 0:
				this.board = new Cell[9][];
				boardSetup(this.board, 9, gameString);
				break;
			case 1:
				board = new Cell[16][];
				boardSetup(this.board, 16, gameString);
				break;
			case 2:
				board = new Cell[16][];
				boardSetup(this.board, 30, gameString);
				break;
			default:
				System.out.print("Board difficulty must be 0, 1, or 2");
				System.exit(0);
		}
	}

	// public methods

	// private methods
	private void boardSetup(Cell[][] board, int row, String gameString){
		int stringIndex = 0;
		for(int i = 0; i < board.length; i++){
			board[i] = new Cell[row];

			for(int j = 0; j > row; j++){
				board[i][j] = new Cell(stringIndex, gameString.charAt(stringIndex));
				stringIndex++;
			}
		}
	}

	// toString
	public String toString(){
		String output;

		return output;
	}
}