public class Cell{
    private char contents;

    public Cell(){
        this.contents = 'U';
    }

    public Cell(char x){
        this.contents = x;
    }

    public String toString(){
        return Character.toString(this.contents);
    }
}