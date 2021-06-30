import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class BoardCapture{
	// private variables
	Robot bot;
	String savePath = "./Images/board.png";

	// constructors
	public BoardCapture(){
		try{ 
			this.bot = new Robot();
		}
		catch(AWTException e){
			System.out.println(e);
		}
	}

	public void screenshot(){
		Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
		BufferedImage img =  this.bot.createScreenCapture(screenRect);

		try{	
			ImageIO.write(img, "png", new File(this.savePath));
		}
		catch(IOException e){
			System.out.println(e);
		}
	}
}