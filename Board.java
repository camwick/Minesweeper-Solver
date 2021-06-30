public class Board{
	// private variables
	private int difficulty;
	private int column;
	private int row;
	private Cell[][] board;

	// constructors
	public Board(int difficulty, String gameString){
		this.difficulty = difficulty;

		switch(this.difficulty){
			case 0:
				this.board = new Cell[9][];
				this.column = 9;
				this.row = 9;
				boardSetup(this.board, 9, gameString);
				break;
			case 1:
				board = new Cell[16][];
				this.column = 16;
				this.row = 16;
				boardSetup(this.board, 16, gameString);
				break;
			case 2:
				board = new Cell[16][];
				this.column = 30;
				this.row = 16;
				boardSetup(this.board, 30, gameString);
				break;
			default:
				System.out.print("Board difficulty must be 0, 1, or 2");
				System.exit(0);
		}
	}

	// public methods
	public int getRow(){
		return this.row;
	}

	public int getColumn(){
		return this.column;
	}

	// private methods
	private void boardSetup(Cell[][] board, int column, String gameString){
		int stringIndex = 0;
		for(int i = 0; i < board.length; i++){
			board[i] = new Cell[column];

			for(int j = 0; j < column; j++){
				board[i][j] = new Cell(stringIndex, gameString.charAt(stringIndex));
				stringIndex++;
			}
		}
	}

	// toString
	public String toString(){
		String output = "";

		for(int i = 0; i < column + 12; i++){
			output += "-";
		}
		output += "\n";

		for(int i = 0; i < this.board.length; i++){
			output += "| ";

			for(int j = 0; j < this.column; j++){
				output += this.board[i][j].getCellContents() + " ";
			}

			output += "|\n";
		}

		for(int i = 0; i < column + 12; i++){
			output += "-";
		}
		output += "\n";

		return output;
	}
}