import java.awt.*;

public class ScreenInput{
	// variables
	private Robot bot;
	private int row;
	private int column;
	private Color one = new Color(0, 0, 255);
	private Color two = new Color(0, 128, 0);
	private Color three = new Color(255, 0, 0);
	private Color four = new Color(0, 0, 128);
	private Color empty = new Color(198, 198, 198);
	private Color unmarked = new Color(128, 128, 128);
	private Color flag = new Color(0, 0, 0);

	// constructors
	public ScreenInput(int row, int column){
		try{
			this.bot = new Robot();
			this.row = row;
			this.column = column;
		}
		catch(AWTException e){
			System.out.println(e);
			System.exit(0);
		}
	}

	// public methods
	public String getBoardString(){
		String output = "";
		Color cellColor;

		for(int i = 0; i < this.column; i++){
			for(int j = 0; j < this.row; j++){
				cellColor = this.bot.getPixelColor(1086 + 40 * j, 396 + 40 * i);

				if(this.bot.getPixelColor(1086 + 40 * j, 396 + 40 * i + 6).equals(this.flag))
					output += "f";
				else if(cellColor.equals(this.empty)){
					if(this.bot.getPixelColor(1086 + 40 * j + 17, 396 + 40 * i).equals(this.unmarked))
						output += "u";
					else 
						output += " ";
				}
				else if(cellColor.equals(this.one))
					output += "1";
				else if(cellColor.equals(this.two))
					output += "2";
				else if(cellColor.equals(this.three))
					output += "3";
				else if(cellColor.equals(this.four))
					output += "4";
				else{
					System.out.println("Found color not saved: " + cellColor + "\nRow: " + i + "\nColumn: " + j);
					System.exit(0);
				}
			}
		}

		return output;
	}
}