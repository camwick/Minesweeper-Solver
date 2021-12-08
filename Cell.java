public class Cell {
    private char contents;

    /**
     * Default constructor.
     * Constructs Cell object with default content of 'U'.
     */
    public Cell() {
        this.contents = 'U';
    }

    /**
     * Constructor.
     * Constructs Cell object with specified contents <code>x</code>
     * 
     * @param x char, contents of Cell
     */
    public Cell(char x) {
        this.contents = x;
    }

    /**
     * Set contents variable.
     * 
     * @param newContents char, new Cell contents
     */
    public void setContents(char newContents) {
        this.contents = newContents;
    }

    /**
     * Returns the contents of the cell.
     */
    public String toString() {
        return Character.toString(this.contents);
    }
}