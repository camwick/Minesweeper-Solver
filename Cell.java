public class Cell{
	// private variables
	private int id;
	private char cellContents;

	// constructors
	public Cell(int id, char x){
		this.id = id;
		this.cellContents = x;
	}

	// toString
	public String toString(){
		return "(" + id + ", " + cellContents + ")";
	}
}