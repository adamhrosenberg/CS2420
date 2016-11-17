package homework02;

/**

@author: Adam Rosenberg
Turns an image greyscale

**/
import java.awt.Color;
import java.awt.Component;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;


public class Greyscale {
	public static void main (String args []) {
		BufferedImage img = null;	
		//Create a file chooser
		final JFileChooser fc = new JFileChooser();
		
		Component aComponent = null;
		//In response to a button click:
		int returnVal = fc.showOpenDialog(aComponent);
		File file = fc.getSelectedFile();
		try {	
			img = ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		int [][] grads = new int[img.getWidth()][img.getHeight()];	//Stores the gradient values at pos. i,j.
		double maxGrad = 0;	//Stores the maximum gradient value
		double greyscale = 0;
		for (int i=1; i<(img.getWidth()-1); i++) {
			for (int j=1; j<(img.getHeight()-1); j++) {
				//Calculates the red, blue and green gradients, then adds them together and stores them.
				int gradRed = Math.abs((new Color(img.getRGB(i+1, j)).getRed())-(new Color(img.getRGB(i-1, j)).getRed()))+Math.abs((new Color(img.getRGB(i, j+1)).getRed())-(new Color(img.getRGB(i, j-1)).getRed()));
				int gradBlue = Math.abs((new Color(img.getRGB(i+1, j)).getBlue())-(new Color(img.getRGB(i-1, j)).getBlue()))+Math.abs((new Color(img.getRGB(i, j+1)).getBlue())-(new Color(img.getRGB(i, j-1)).getBlue()));
				int gradGreen = Math.abs((new Color(img.getRGB(i+1, j)).getGreen())-(new Color(img.getRGB(i-1, j)).getGreen()))+Math.abs((new Color(img.getRGB(i, j+1)).getGreen())-(new Color(img.getRGB(i, j-1)).getGreen()));
				int grad = gradRed + gradBlue + gradGreen;
				grads[i][j] = grad;
				if (grad > maxGrad)	{
					maxGrad = grad;
				}
			}
		}
		
		for (int i=1; i<(img.getWidth()-1); i++) {
			for (int j=1; j<(img.getHeight()-1); j++) {
				//Uses the stored gradient values and the max gradient value to calculate the shading for the grayscale.
				greyscale = (double) grads[i][j]/maxGrad;
				greyscale = Math.sqrt(greyscale);
				greyscale = 255*greyscale;
					img.setRGB(i,j,new Color((int) greyscale,(int) greyscale,(int) greyscale).getRGB());
			}
		}
		
		final JFileChooser fc2 = new JFileChooser();
		
		aComponent = null;
		//In response to a button click:
		returnVal = fc2.showOpenDialog(aComponent);
		file = fc2.getSelectedFile();
		try {
			ImageIO.write(img, "jpeg", file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}