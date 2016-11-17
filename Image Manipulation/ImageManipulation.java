/**

@author: Adam Rosenberg

**/

package homework02;

import java.awt.Color;
import java.awt.Component;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;


public class ImageManipulation {
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
		int [][] grads = new int[img.getWidth()][img.getHeight()];	//Stores the individual gradient value at i,j.
		int threshold = 300;	//If the gradient at a pixel is higher than threshold, we color it white; else, black.
		for (int i=1; i<(img.getWidth()-1); i++) {
			for (int j=1; j<(img.getHeight()-1); j++) {
				//Calculates the red, blue and green gradients and adds them together.
				int gradRed = Math.abs((new Color(img.getRGB(i+1, j)).getRed())-(new Color(img.getRGB(i-1, j)).getRed()))+Math.abs((new Color(img.getRGB(i, j+1)).getRed())-(new Color(img.getRGB(i, j-1)).getRed()));
				int gradBlue = Math.abs((new Color(img.getRGB(i+1, j)).getBlue())-(new Color(img.getRGB(i-1, j)).getBlue()))+Math.abs((new Color(img.getRGB(i, j+1)).getBlue())-(new Color(img.getRGB(i, j-1)).getBlue()));
				int gradGreen = Math.abs((new Color(img.getRGB(i+1, j)).getGreen())-(new Color(img.getRGB(i-1, j)).getGreen()))+Math.abs((new Color(img.getRGB(i, j+1)).getGreen())-(new Color(img.getRGB(i, j-1)).getGreen()));
				int grad = gradRed + gradBlue + gradGreen;
				grads[i][j] = grad;
			}
		}
		//Determines whether or not we color it white or black.
		for (int i=1; i<(img.getWidth()-1); i++) {
			for (int j=1; j<(img.getHeight()-1); j++) {
				if (grads[i][j]>threshold)	{
					img.setRGB(i,j,new Color(255,255,255).getRGB());
				}
				else	{
					img.setRGB(i,j,new Color(0,0,0).getRGB());
				}
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