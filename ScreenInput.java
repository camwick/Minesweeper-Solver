import java.awt.*;

public class ScreenInput{
	// variables
	Robot bot;

	// constructors
	public ScreenInput(){
		try{
			this.bot = new Robot();
		}
		catch(AWTException e){
			System.out.println(e);
		}
	}

	// public methods
	public String getBoardString(Board board){

	}
}

/*
First Cell position: 
	- 1086x396

Green - rgb(0, 128, 0)
red   - rgb(255, 0, 0)
blue  - rgb(0, 0, 255)
gray  - rgb(198, 198, 198)
*/