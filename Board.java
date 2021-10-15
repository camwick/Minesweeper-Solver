public class Board{
    private int width = -1;
    private int height = -1;
    private Cell[][] board;
    
    public Board(int difficulty){
        switch(difficulty){
            case 1:
                this.width = 9;
                this.height = 9;
                this.board = new Cell[this.width][this.height];
                break;
            case 2:
                this.width = 16;
                this.height = 16;
                this.board = new Cell[this.width][this.height];
                break;
            case 3:
                this.width = 30;
                this.height = 16;
                this.board = new Cell[this.width][this.height];
                break;
            case 4:
                this.width = 30;
                this.height = 20;
                this.board = new Cell[this.width][this.height];
                break;
            default:
                System.out.println("Invalid difficulty entered: " + difficulty);
                System.exit(1);
                break;
        }
        
        // initiate Cell objects
        for(int x = 0; x < this.height; x++){
            for(int y = 0; y < this.width; y++){
                board[x][y] = new Cell();
            }
        }
    
    }
}