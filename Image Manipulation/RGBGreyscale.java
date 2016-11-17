package homework02;

/**

@author: Adam Rosenberg


**/
import java.awt.Color;
import java.awt.Component;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;

import java.util.Scanner;

public class RGBGreyscale {
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
		int [][] grads = new int[img.getWidth()][img.getHeight()];	//Stores the individual gradient values.
		double maxGrad = 0;	//Stores the maximum gradient value.
		double greyscale = 0;
		//R, G and B determine whether or not you want to color the picture red, green or blue.
		boolean R = false;
		boolean G = false;
		boolean B = false;
		boolean cont = true;	//Determines whether or not we continue the while loop; becomes false when a correct input is entered.
		Scanner in = new Scanner (System.in);
		while(cont)	{
			//The person inputs which color they want to use.
			System.out.println("Input r for red edge detector, g for green and b for blue");
			String current = in.next();
			if (current.equals("r"))	{
			R = true;
			cont = false;
			}
			else if (current.equals("b"))	{
			B = true;
			cont = false;
			}
			else if (current.equals("g"))	{
			G = true;
			cont = false;
			}
			else	{
			System.out.println("Please input something correct this time");
			}
			}
		//Finds the gradient for each pixel.
		for (int i=1; i<(img.getWidth()-1); i++) {
			for (int j=1; j<(img.getHeight()-1); j++) {
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
		//Colors the picture either redscale, greenscale or bluescale.
		for (int i=1; i<(img.getWidth()-1); i++) {
			for (int j=1; j<(img.getHeight()-1); j++) {
				greyscale = (double) grads[i][j]/maxGrad;
				greyscale = Math.sqrt(greyscale);
				greyscale = 255*greyscale;
				if (R)	{
					img.setRGB(i,j,new Color(255,(int) greyscale,(int) greyscale).getRGB());
				}
				if (G)	{
					img.setRGB(i,j,new Color((int) greyscale,255,(int) greyscale).getRGB());
				}
				if (B)	{
					img.setRGB(i,j,new Color((int) greyscale,(int) greyscale,255).getRGB());
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
