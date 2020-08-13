/*Class to turn text input into image for posting to twitter to accommodate max text length for tweets*/



import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

//to avoid overloading with excessively long tweets and enable nicely formatted data,
//converts text tweet status to image for upload

public class TextToJpg {
	
	private static String filePath = new File("").getAbsolutePath();
	
	private static ArrayList<String> convertStringToLineString(String str){
	    ArrayList<String> string = new ArrayList<String>();
	    String s = new String ();
	    char [] ca = str.toCharArray();

	    for(int i=0;i<ca.length;i++){
	        if(ca[i] == '\n'){
	            string.add(s);
	            s = new String();
	        }
	        s += ca[i];
	    }
	    return string;
	}

	static void makeImageFromText(ArrayList<String> arrayList) throws IOException{
				
		//initialize counter for filename, or reset it if already used
		int fileNameCounter = 0;
				
		//iterate through all messages, increment counter to create each image
		//to cover each warning message for an area
		for(String text : arrayList) {
			//clean up format of individual message
			text = text.replace("]", "");
			text = text.replace("[", "");
						
			final String  backgroundImage = filePath+"/src/resources/noahBackground.jpg";
	        File image = new File(backgroundImage);
	        if(! image.exists())
	            throw new FileNotFoundException(String.format("Missing Resource: %s!",backgroundImage));

	        ImageIcon img = new ImageIcon(backgroundImage);

	        BufferedImage bufferedImage = new BufferedImage(
	                img.getIconWidth(),img.getIconHeight(),BufferedImage.TYPE_INT_RGB);

	        Graphics2D g2d = (Graphics2D) bufferedImage.getGraphics();
	        g2d.drawImage(img.getImage(), 0, 0, null);
	        AlphaComposite alc = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.5f);
	        g2d.setColor(Color.black);
	        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	        g2d.setFont(new Font("Calibri",Font.BOLD,15));

	        FontMetrics fontMetrics = g2d.getFontMetrics();

	        Rectangle2D rect = fontMetrics.getStringBounds(text, 0, text.length(), g2d);

	        int x =(img.getIconWidth() - (int) rect.getWidth())/2,
	            y =(img.getIconHeight() - (int) rect.getHeight())/2;

	        ArrayList<String> watermarks = convertStringToLineString(text);
	        for (String w : watermarks){
	        g2d.drawString(w,x,y);
	            y+=20;
	        }
	         //Free graphic resources
            g2d.dispose();
	            
            //hold output file name
	        String outFileName = String.format(filePath + "/bin/out/WarningTest%s.jpg", Integer.toString(fileNameCounter));
	        
	        
	        BufferedOutputStream bo = new BufferedOutputStream(new FileOutputStream(outFileName));
	        ImageIO.write(bufferedImage,"jpg",bo);
	        
	        fileNameCounter += 1;


	    }

			
//			//read in background image
//			BufferedImage image = ImageIO.read(new File(filePath+"/src/resources/noahBackground.jpg"));
//			
//			//set up graphics to write text onto image
//			//TODO: text origin fix
//			Graphics2D g2d = (Graphics2D) image.getGraphics();
//	        Font font = new Font("Calibri", Font.PLAIN, 20);
//	        g2d.setFont(font);
//	        
//	        //dimensions to set for origin of text
//	        int x = image.getWidth()/18;
//	        int y = image.getHeight()/10;
//	        
//	        //draw each line onto the image
//	        for(String line : textArr) {
//		        g2d.drawString(line, x, y);
//		        y += 20;
//	        }
//	        g2d.dispose();
//
//	        //hold output file name
//	        String outFileName = String.format(filePath + "/bin/out/WarningTest%s.jpg", Integer.toString(fileNameCounter));
//	        
//	        ImageIO.write(image, "png", new File(outFileName));
	        
	        //increment counter when done
	}

}
