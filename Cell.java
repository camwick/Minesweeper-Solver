public class Cell{
	// private variables
	private int id;
	private char cellContents;
	private int flagCount;

	// constructors
	public Cell(int id, char x){
		this.id = id;
		this.cellContents = x;
		this.flagCount = 0;
	}

	// Get methods
	private int getId(){
		return this.id;
	}

	public char getCellContents(){
		return this.cellContents;
	}

	// toString
	public String toString(){
		return "(" + id + ", " + cellContents + ")";
	}
}