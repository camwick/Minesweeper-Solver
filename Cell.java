public class Cell {
    private char contents;
    private Cell[] adjacentCells;

    /**
     * Default constructor.
     * Constructs Cell object with default content of 'U'.
     */
    public Cell() {
        this('U');
    }

    /**
     * Constructor.
     * Constructs Cell object with specified contents <code>x</code>
     * 
     * @param x char, contents of Cell
     */
    public Cell(char x) {
        this.contents = x;

        this.adjacentCells = new Cell[8];
    }

    public Cell[] getAdjacent() {
        return this.adjacentCells;
    }

    /**
     * Set contents variable.
     * 
     * @param newContents char, new Cell contents
     */
    public void setContents(char newContents) {
        this.contents = newContents;
    }

    public void setAdjacent(Cell[] x) {
        for (int i = 0; i < x.length; ++i) {
            this.adjacentCells[i] = x[i];
        }
    }

    /**
     * Returns the contents of the cell.
     */
    @Override
    public String toString() {
        return Character.toString(this.contents);
    }
}