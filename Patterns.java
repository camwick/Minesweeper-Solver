public class Patterns{
    //because the need for the bot, gameboard and other 
    //data is unusable here, these methods will return true
    //if the pattern exists, then the work will be done in Solver.java

    public boolean onetwoPattern(Cell cell)
    {
        //if the board is
        //  U   U   U
        //  1   2   1
        //then
        //  F   U   F
        //  1   2   1

        //also works vertically
        //  E   1   U
        //  E   2   U
        //  E   1   U
        //then
        //  E   1   F
        //  E   2   U
        //  E   1   F

        
        //also make sure to take into account the number of flags
        //adjacent to the cell
        //if the number is 2 but there is a flag already adjacent
        //the 1 2 pattern will not work

        Cell[] adjacent = cell.getAdjacent();
        
        //check horizontal first, only check for pattern if 
        //numeric value is 2. This is done in Solver.java

        //assuming cellContents == 2

        //horizontal possibilities

        //if the 1 is on the left of 2
        //This currently only checks for mines above the pattern - add a below check as well
        if(Character.getNumericValue(adjacent[3].getContents()) - cell.getAdjFlags() == 1 && adjacent[2].getContents() == 'U')
        {     
            if(adjacent[0].getContents() == 'U' && Character.getNumericValue(adjacent[4].getContents()) - cell.getAdjFlags() == 2){
                //flag mine
                return true;
            }
        }  
         
        //if the 1 is on the right of 2
        //also currently only checks for mines above the pattern - add a below check as well
        if(Character.getNumericValue(adjacent[4].getContents()) - cell.getAdjFlags()  == 1 && adjacent[0].getContents() == 'U'){   
            if(adjacent[2].getContents() == 'U' && Character.getNumericValue(adjacent[3].getContents()) - cell.getAdjFlags() == 2){
                //flag mine
                return true;    
            }
        }
            

        /*vertical possibilities
        //if the 1 is above the 2
        //This currently only checks for mines to the right of the pattern - add a left check as well
        */
        if(Character.getNumericValue(adjacent[1].getContents()) - cell.getAdjFlags() == 1 && adjacent[7].getContents() == 'U'){
            if(adjacent[2].getContents() == 'U' && Character.getNumericValue(adjacent[6].getContents()) - cell.getAdjFlags() == 2){
                //flag mine
                return true;
            }
        }

        //if the 1 is below the 2
        //This currently only checks for mines to the right of the pattern - add a left check as well
        if(Character.getNumericValue(adjacent[6].getContents()) - cell.getAdjFlags() == 1 && adjacent[2].getContents() == 'U'){
            if(adjacent[7].getContents() == 'U' && Character.getNumericValue(adjacent[1].getContents()) - cell.getAdjFlags() == 2){
                //flag mine
                return true;
            }
        }
        return false;
    }
    
}
