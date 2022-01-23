public class Cell {
    private char contents;
    private Cell[] adjacentCells;
    private int x;
    private int y;

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

    public char getContents() {
        return this.contents;
    }

    /**
     * Returns the adjacentCells object
     * 
     * @return adjacentCells array
     */
    public Cell[] getAdjacent() {
        return this.adjacentCells;
    }

    public int getNumUnlickedAdj() {
        int count = 0;
        for (int i = 0; i < adjacentCells.length; ++i) {
            if (adjacentCells[i] == null)
                continue;

            if (adjacentCells[i].getContents() == 'U')
                count++;
        }
        return count;
    }

    public int getXCoord() {
        return this.x;
    }

    public int getYCoord() {
        return this.y;
    }

    public int getAdjFlags() {
        int count = 0;
        for (int i = 0; i < adjacentCells.length; ++i) {
            if (adjacentCells[i] == null || adjacentCells[i].getContents() == 'U'
                    || adjacentCells[i].getContents() == 'E')
                continue;

            if (adjacentCells[i].getContents() == 'F')
                count++;
        }
        return count;
    }

    /**
     * Set contents variable.
     * 
     * @param newContents char, new Cell contents
     */
    public void setContents(char newContents) {
        this.contents = newContents;
    }

    public void setCoords(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Sets every adjacent cell. Null = edge cases.
     * 
     * @param x Cell array, cells to be set as adjacents.
     */
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