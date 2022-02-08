public class Patterns {
    // because the need for the bot, gameboard and other
    // data is unusable here, these methods will return true
    // if the pattern exists, then the work will be done in Solver.java
    private Cell mine;
    private Cell cell;

    /**
     * Constructs a Pattern object that will recognize various mine patterns based
     * on the cell provided and the called function.
     * 
     * @param cell Cell, mine pattern recognition acts upon Cell
     */
    public Patterns(Cell cell) {
        this.cell = cell;
    }

    /**
     * 1-2 Pattern Recognition function.
     * 
     * if the board is
     * U U U
     * 1 2 1
     * then
     * F U F
     * 1 2 1
     * 
     * also works vertically
     * E 1 U
     * E 2 U
     * E 1 U
     * then
     * E 1 F
     * E 2 U
     * E 1 F
     * 
     * Note
     * check horizontal first, only check for pattern if
     * numeric value is 2. This is done in Solver.java
     * 
     * @return boolean, true if found a match
     */
    public boolean onetwoPattern() {
        if (this.cell.getNumUnlickedAdj() > 3)
            return false;

        Cell[] adjacent = this.cell.getAdjacent();

        // 1 left of 2
        if (Character.getNumericValue(adjacent[3].getContents()) - adjacent[3].getAdjFlags() == 1) {
            // top left U
            if (adjacent[0].getContents() == 'U') {
                int counter = 0;
                for (int i = 1; i <= 2; ++i) {
                    if (adjacent[i].getContents() == 'U')
                        counter++;
                }

                if (counter == 2) {
                    mine = adjacent[2];
                    return true;
                } else
                    return false;

                // bottom left U
            } else if (adjacent[5].getContents() == 'U') {
                int counter = 0;
                for (int i = 6; i <= 7; ++i) {
                    if (adjacent[i].getContents() == 'U')
                        counter++;
                }
                if (counter == 2) {
                    mine = adjacent[7];
                    return true;
                } else
                    return false;
            }
        }

        // 1 right of 2
        if (Character.getNumericValue(adjacent[4].getContents()) - adjacent[4].getAdjFlags() == 1) {
            // top left U
            if (adjacent[0].getContents() == 'U') {
                int counter = 0;
                for (int i = 1; i <= 2; ++i) {
                    if (adjacent[i].getContents() == 'U')
                        counter++;
                }

                if (counter == 2) {
                    mine = adjacent[0];
                    return true;
                } else
                    return false;

                // bottom left U
            } else if (adjacent[5].getContents() == 'U') {
                int counter = 0;
                for (int i = 6; i <= 7; ++i) {
                    if (adjacent[i].getContents() == 'U')
                        counter++;
                }
                if (counter == 2) {
                    mine = adjacent[5];
                    return true;
                } else
                    return false;
            }
        }

        // 1 above the 2
        if (Character.getNumericValue(adjacent[1].getContents()) - adjacent[1].getAdjFlags() == 1) {
            // top right U
            if (adjacent[2].getContents() == 'U') {
                int counter = 0;
                for (int i = 4; i <= 7; i += 3) {
                    if (adjacent[i].getContents() == 'U')
                        counter++;
                }

                if (counter == 2) {
                    mine = adjacent[7];
                    return true;
                } else
                    return false;

                // top left U
            } else if (adjacent[0].getContents() == 'U') {
                int counter = 0;
                for (int i = 3; i <= 5; i += 2) {
                    if (adjacent[i].getContents() == 'U')
                        counter++;
                }
                if (counter == 2) {
                    mine = adjacent[5];
                    return true;
                } else
                    return false;
            }
        }

        // 1 below the 2
        if (Character.getNumericValue(adjacent[6].getContents()) - adjacent[6].getAdjFlags() == 1) {
            // top right U
            if (adjacent[2].getContents() == 'U') {
                int counter = 0;
                for (int i = 4; i <= 7; i += 3) {
                    if (adjacent[i].getContents() == 'U')
                        counter++;
                }

                if (counter == 2) {
                    mine = adjacent[2];
                    return true;
                } else
                    return false;

                // top left U
            } else if (adjacent[0].getContents() == 'U') {
                int counter = 0;
                for (int i = 3; i <= 5; i += 2) {
                    if (adjacent[i].getContents() == 'U')
                        counter++;
                }
                if (counter == 2) {
                    mine = adjacent[0];
                    return true;
                } else
                    return false;
            }
        }

        // if no matches, return false
        return false;
    }

    /**
     * Returns mine position found by pattern recognition functions.
     * 
     * @return Cell, mine to mark
     */
    Cell getMine() {
        return this.mine;
    }
}
