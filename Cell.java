public class Cell{
    private char contents;

    public Cell(){
        this.contents = 'U';
    }

    public Cell(char x){
        this.contents = x;
    }

    // set methods
    public void setContents(char newContents){
        this.contents = newContents;
    }

    // toString
    public String toString(){
        return Character.toString(this.contents);
    }
}