public class Cell{
	// private variables
	private int id;
	private char cellContents;

	// constructors
	public Cell(int id, char x){
		this.id = id;
		this.cellContents = x;
	}

	// Get methods
	private int getId(){
		return this.id;
	}

	public char getCellContents(){
		return this.cellContents;
	}

	// public methods
	public void markMine(){
		this.cellContents = 'f';
	}

	// toString
	public String toString(){
		return "(" + id + ", " + cellContents + ")";
	}
}