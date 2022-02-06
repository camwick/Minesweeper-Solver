public class Patterns {
    // because the need for the bot, gameboard and other
    // data is unusable here, these methods will return true
    // if the pattern exists, then the work will be done in Solver.java
    private Cell mine;
    private Cell cell;

    public Patterns(Cell cell) {
        this.cell = cell;
    }

    public boolean onetwoPattern() {
        // if the board is
        // U U U
        // 1 2 1
        // then
        // F U F
        // 1 2 1

        // also works vertically
        // E 1 U
        // E 2 U
        // E 1 U
        // then
        // E 1 F
        // E 2 U
        // E 1 F

        // also make sure to take into account the number of flags
        // adjacent to the cell
        // if the number is 2 but there is a flag already adjacent
        // the 1 2 pattern will not work

        Cell[] adjacent = this.cell.getAdjacent();

        // check horizontal first, only check for pattern if
        // numeric value is 2. This is done in Solver.java

        // assuming cellContents == 2

        // horizontal possibilities

        // if the 1 is on the left of 2
        // This currently only checks for mines above the pattern - add a below check as
        // well

        if (this.cell.getNumUnlickedAdj() > 3)
            return false;

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
                    // mark the mine
                    mine = adjacent[2];

                    // flag mine
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
                    // mark the mine
                    mine = adjacent[7];

                    // flag mine
                    return true;
                } else
                    return false;
            }
        }

        // if the 1 is on the right of 2
        if (Character.getNumericValue(adjacent[4].getContents()) - adjacent[4].getAdjFlags() == 1) {
            // top left U
            if (adjacent[0].getContents() == 'U') {
                int counter = 0;
                for (int i = 1; i <= 2; ++i) {
                    if (adjacent[i].getContents() == 'U')
                        counter++;
                }

                if (counter == 2) {
                    // mark the mine
                    mine = adjacent[0];

                    // flag mine
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
                    // mark the mine
                    mine = adjacent[5];

                    // flag mine
                    return true;
                } else
                    return false;
            }
        }

        // if the 1 is above the 2
        if (Character.getNumericValue(adjacent[1].getContents()) - adjacent[1].getAdjFlags() == 1) {
            // top left U
            if (adjacent[2].getContents() == 'U') {
                int counter = 0;
                for (int i = 4; i <= 7; i += 3) {
                    if (adjacent[i].getContents() == 'U')
                        counter++;
                }

                if (counter == 2) {
                    // mark the mine
                    mine = adjacent[7];

                    // flag mine
                    return true;
                } else
                    return false;
                // bottom left U
            } else if (adjacent[0].getContents() == 'U') {
                int counter = 0;
                for (int i = 3; i <= 5; i += 2) {
                    if (adjacent[i].getContents() == 'U')
                        counter++;
                }
                if (counter == 2) {
                    // mark the mine
                    mine = adjacent[5];

                    // flag mine
                    return true;
                } else
                    return false;
            }
        }

        // if the 1 is below the 2
        if (Character.getNumericValue(adjacent[6].getContents()) - adjacent[6].getAdjFlags() == 1) {
            // top left U
            if (adjacent[2].getContents() == 'U') {
                int counter = 0;
                for (int i = 4; i <= 7; i += 3) {
                    if (adjacent[i].getContents() == 'U')
                        counter++;
                }

                if (counter == 2) {
                    // mark the mine
                    mine = adjacent[2];

                    // flag mine
                    return true;
                } else
                    return false;
                // bottom left U
            } else if (adjacent[0].getContents() == 'U') {
                int counter = 0;
                for (int i = 3; i <= 5; i += 2) {
                    if (adjacent[i].getContents() == 'U')
                        counter++;
                }
                if (counter == 2) {
                    // mark the mine
                    mine = adjacent[0];

                    // flag mine
                    return true;
                } else
                    return false;
            }
        }

        // if no matches, returnf alse
        return false;
    }

    Cell getMine() {
        return this.mine;
    }
}
