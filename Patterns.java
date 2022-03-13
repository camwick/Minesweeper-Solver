public class Patterns {
    // because the need for the bot, gameboard and other
    // data is unusable here, these methods will return true
    // if the pattern exists, then the work will be done in Solver.java
    private Cell mine;
    private Cell cell;
    private Cell[] safeCells;
    private char holeDirection;

    /**
     * Constructs a Pattern object that will recognize various mine patterns based
     * on the cell provided and the called function.
     * 
     * @param cell Cell, mine pattern recognition acts upon Cell
     */
    public Patterns(Cell cell) {
        this.cell = cell;
        this.safeCells = new Cell[3];
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
        if (adjacent[3] != null
                && Character.getNumericValue(adjacent[3].getContents()) - adjacent[3].getNumAdjFlags() == 1) {
            // top left U
            if (adjacent[0] != null && adjacent[0].getContents() == 'U') {
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
            } else if (adjacent[5] != null && adjacent[5].getContents() == 'U') {
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
        if (adjacent[4] != null
                && Character.getNumericValue(adjacent[4].getContents()) - adjacent[4].getNumAdjFlags() == 1) {
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
        if (adjacent[1] != null
                && Character.getNumericValue(adjacent[1].getContents()) - adjacent[1].getNumAdjFlags() == 1) {
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
        if (adjacent[6] != null
                && Character.getNumericValue(adjacent[6].getContents()) - adjacent[6].getNumAdjFlags() == 1) {
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

    public boolean oneOnePattern() {
        Cell[] adjacents = this.cell.getAdjacent();

        // 1 to right
        if (adjacents[4] != null
                && Character.getNumericValue(adjacents[4].getContents()) - adjacents[4].getNumAdjFlags() == 1) {
            if ((adjacents[1] != null && adjacents[1].getContents() == 'U' && adjacents[2].getContents() == 'U')
                    || (adjacents[6] != null && adjacents[6].getContents() == 'U'
                            && adjacents[7].getContents() == 'U')) {
                int index = 0;
                for (int i = 2; i < adjacents.length; ++i) {
                    if (adjacents[4].getAdjacent()[i] == null)
                        continue;

                    if (i == 2 || i == 4 || i == 7) {
                        if (adjacents[4].getAdjacent()[i].getContents() == 'U') {
                            safeCells[index] = adjacents[4].getAdjacent()[i];
                            index++;
                        }
                    }
                }
                return true;
            }
        }

        // 1 to left
        if (adjacents[3] != null
                && Character.getNumericValue(adjacents[3].getContents()) - adjacents[3].getNumAdjFlags() == 1) {
            if ((adjacents[0] != null && adjacents[0].getContents() == 'U' && adjacents[1].getContents() == 'U')
                    || (adjacents[5] != null && adjacents[5].getContents() == 'U'
                            && adjacents[6].getContents() == 'U')) {
                int index = 0;
                for (int i = 0; i < adjacents.length - 2; ++i) {
                    if (adjacents[3].getAdjacent()[i] == null)
                        continue;

                    if (i == 0 || i == 3 || i == 5) {
                        if (adjacents[3].getAdjacent()[i].getContents() == 'U') {
                            safeCells[index] = adjacents[3].getAdjacent()[i];
                            index++;
                        }
                    }
                }
                return true;
            }
        }

        // 1 above
        if (adjacents[1] != null
                && Character.getNumericValue(adjacents[1].getContents()) - adjacents[1].getNumAdjFlags() == 1) {
            if ((adjacents[0] != null && adjacents[0].getContents() == 'U' && adjacents[3].getContents() == 'U')
                    || (adjacents[2] != null && adjacents[2].getContents() == 'U'
                            && adjacents[4].getContents() == 'U')) {
                int index = 0;
                for (int i = 0; i < 3; ++i) {
                    if (adjacents[1].getAdjacent()[i] == null)
                        continue;

                    if (adjacents[1].getAdjacent()[i].getContents() == 'U') {
                        safeCells[index] = adjacents[1].getAdjacent()[i];
                        index++;
                    }
                }
                return true;
            }
        }

        // 1 below
        if (adjacents[6] != null
                && Character.getNumericValue(adjacents[6].getContents()) - adjacents[6].getNumAdjFlags() == 1) {
            if ((adjacents[3] != null && adjacents[3].getContents() == 'U' && adjacents[5].getContents() == 'U')
                    || (adjacents[4] != null && adjacents[4].getContents() == 'U'
                            && adjacents[7].getContents() == 'U')) {
                int index = 0;
                for (int i = 5; i < adjacents.length; ++i) {
                    if (adjacents[6].getAdjacent()[i] == null)
                        continue;

                    if (adjacents[6].getAdjacent()[i].getContents() == 'U') {
                        safeCells[index] = adjacents[6].getAdjacent()[i];
                        index++;
                    }
                }
                return true;
            }
        }

        return false;
    }

    boolean holePattern() {
        Cell[] adjacents = this.cell.getAdjacent();

        // safe to left
        if (adjacents[2] != null && adjacents[7] != null && adjacents[2].getContents() == 'U'
                && adjacents[7].getContents() == 'U'
                && Character.getNumericValue(adjacents[4].getContents()) - adjacents[4].getNumAdjFlags() == 1) {
            if (adjacents[4].getAdjacent()[4] != null && (adjacents[4].getAdjacent()[2].getContents() == 'U'
                    || adjacents[4].getAdjacent()[4].getContents() == 'U'
                    || adjacents[4].getAdjacent()[7].getContents() == 'U')) {
                this.safeCells[0] = adjacents[4].getAdjacent()[2];
                this.safeCells[1] = adjacents[4].getAdjacent()[4];
                this.safeCells[2] = adjacents[4].getAdjacent()[7];

                this.holeDirection = 'r';

                return true;
            }
        }

        // safe to right - hole to the left
        if (adjacents[0] != null && adjacents[5] != null && adjacents[0].getContents() == 'U'
                && adjacents[5].getContents() == 'U'
                && Character.getNumericValue(adjacents[3].getContents()) - adjacents[3].getNumAdjFlags() == 1) {
            if (adjacents[3].getAdjacent()[3] != null && (adjacents[3].getAdjacent()[0].getContents() == 'U'
                    || adjacents[3].getAdjacent()[3].getContents() == 'U'
                    || adjacents[3].getAdjacent()[5].getContents() == 'U')) {
                this.safeCells[0] = adjacents[3].getAdjacent()[0];
                this.safeCells[1] = adjacents[3].getAdjacent()[3];
                this.safeCells[2] = adjacents[3].getAdjacent()[5];

                this.holeDirection = 'l';

                return true;
            }

        }

        // safe above
        if (adjacents[5] != null && adjacents[7] != null && adjacents[5].getContents() == 'U'
                && adjacents[7].getContents() == 'U'
                && Character.getNumericValue(adjacents[6].getContents()) - adjacents[6].getNumAdjFlags() == 1) {
            if (adjacents[6].getAdjacent()[6] != null && (adjacents[6].getAdjacent()[5].getContents() == 'U'
                    || adjacents[6].getAdjacent()[6].getContents() == 'U'
                    || adjacents[6].getAdjacent()[7].getContents() == 'U')) {
                int index = 0;
                for (int i = 5; i <= 7; ++i) {
                    this.safeCells[index] = adjacents[6].getAdjacent()[i];
                    index++;
                }

                this.holeDirection = 'd';

                return true;
            }

        }

        // safe below
        if (adjacents[0] != null && adjacents[2] != null && adjacents[0].getContents() == 'U'
                && adjacents[2].getContents() == 'U'
                && Character.getNumericValue(adjacents[1].getContents()) - adjacents[1].getNumAdjFlags() == 1) {
            if (adjacents[1].getAdjacent()[1] != null && (adjacents[1].getAdjacent()[0].getContents() == 'U'
                    || adjacents[1].getAdjacent()[1].getContents() == 'U'
                    || adjacents[1].getAdjacent()[2].getContents() == 'U')) {
                for (int i = 0; i <= 2; ++i) {
                    this.safeCells[i] = adjacents[1].getAdjacent()[i];
                }

                this.holeDirection = 'u';

                return true;
            }
        }

        return false;
    }

    public boolean advancedHole() {
        Cell cell = this.safeCells[1];

        switch (this.holeDirection) {
            case 'r':
                if (cell.getAdjacent()[3] != null && (cell.getAdjacent()[0].getContents() == 'U'
                        || cell.getAdjacent()[3].getContents() == 'U'
                        || cell.getAdjacent()[5].getContents() == 'U')) {
                    this.safeCells[0] = cell.getAdjacent()[2];
                    this.safeCells[1] = cell.getAdjacent()[4];
                    this.safeCells[2] = cell.getAdjacent()[7];
                }

                return true;
            case 'l':
                if (cell.getAdjacent()[3] != null && (cell.getAdjacent()[0].getContents() == 'U'
                        || cell.getAdjacent()[3].getContents() == 'U'
                        || cell.getAdjacent()[5].getContents() == 'U')) {
                    this.safeCells[0] = cell.getAdjacent()[0];
                    this.safeCells[1] = cell.getAdjacent()[3];
                    this.safeCells[2] = cell.getAdjacent()[5];
                }

                return true;
            case 'd':
                if (cell.getAdjacent()[3] != null && (cell.getAdjacent()[0].getContents() == 'U'
                        || cell.getAdjacent()[3].getContents() == 'U'
                        || cell.getAdjacent()[5].getContents() == 'U')) {
                    this.safeCells[0] = cell.getAdjacent()[5];
                    this.safeCells[1] = cell.getAdjacent()[6];
                    this.safeCells[2] = cell.getAdjacent()[7];
                }

                return true;
            case 'u':
                if (cell.getAdjacent()[3] != null && (cell.getAdjacent()[0].getContents() == 'U'
                        || cell.getAdjacent()[3].getContents() == 'U'
                        || cell.getAdjacent()[5].getContents() == 'U')) {
                    this.safeCells[0] = cell.getAdjacent()[0];
                    this.safeCells[1] = cell.getAdjacent()[1];
                    this.safeCells[2] = cell.getAdjacent()[2];
                }

                return true;
            default:
                return false;
        }
    }

    /**
     * Returns mine position found by pattern recognition functions.
     * 
     * @return Cell, mine to mark
     */
    Cell getMine() {
        return this.mine;
    }

    Cell[] getSafeCells() {
        return this.safeCells;
    }
}
