package homework03;

/**

@author: Adam Rosenberg


**/
import java.awt.Color;
import java.awt.Component;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;


public class SeamRemover {
	//This will calculate the gradient at a given pixel.  The inputs are the coordinate of the pixel, whether or not it is on the left, right, top or bottom edge, and the image itself.
	public static int gradcalc(int i, int j, boolean left, boolean right, boolean top, boolean bottom, BufferedImage img){
		int leftcheck;
		int rightcheck;
		int topcheck;
		int bottomcheck;
		//Normally when calculating gradients, you take the pixels to the left and right and compare them, and the pixels on the top and bottom of it.
		//If the pixel is on one of the edges, this becomes problematic.  So instead, if the program is on an edge, it won't compare the pixel
		//beyond the edge (which doesn't exist), but instead compare itself.
		if (left)	{leftcheck = 0;} else	{leftcheck = 1;}
		if (right)	{rightcheck = 0;} else	{rightcheck = 1;}
		if (top)	{topcheck = 0;} else	{topcheck = 1;}
		if (bottom)	{bottomcheck = 0;} else	{bottomcheck = 1;}
		int gradRed = Math.abs((new Color(img.getRGB(i+rightcheck, j)).getRed())-(new Color(img.getRGB(i-leftcheck, j)).getRed()))+Math.abs((new Color(img.getRGB(i, j+bottomcheck)).getRed())-(new Color(img.getRGB(i, j-topcheck)).getRed()));
		int gradBlue = Math.abs((new Color(img.getRGB(i+rightcheck, j)).getBlue())-(new Color(img.getRGB(i-leftcheck, j)).getBlue()))+Math.abs((new Color(img.getRGB(i, j+bottomcheck)).getBlue())-(new Color(img.getRGB(i, j-topcheck)).getBlue()));
		int gradGreen = Math.abs((new Color(img.getRGB(i+rightcheck, j)).getGreen())-(new Color(img.getRGB(i-leftcheck, j)).getGreen()))+Math.abs((new Color(img.getRGB(i, j+bottomcheck)).getGreen())-(new Color(img.getRGB(i, j-topcheck)).getGreen()));
		int grad = gradRed + gradBlue + gradGreen;
		return grad;
	}
	//This calculates the cost of the lowest-cost path to that pixel from the left.  It passes in a cost array that should have the costs of every pixel to the left of the current pixel.
	public static int costcalc(int i, int j, boolean left, boolean right, boolean top, boolean bottom, BufferedImage img, int[][] costs)	{
		if (left)	{
			return gradcalc(i,j,left,right,top,bottom,img);
		}
		else if (top)	{
			return Math.min(costs[i-1][j],costs[i-1][j+1])+gradcalc(i,j,left,right,top,bottom,img);
		}
		else if (bottom)	{
			return Math.min(costs[i-1][j],costs[i-1][j-1])+gradcalc(i,j,left,right,top,bottom,img);
		}
		else	{
			return Math.min(Math.min(costs[i-1][j],costs[i-1][j-1]),costs[i-1][j+1])+gradcalc(i,j,left,right,top,bottom,img);
		}
	}
	//The program that actually removes the seam, using the above functions to calculate costs.
	public static BufferedImage SeamRemove(BufferedImage img)	{
		int [][] costs = new int[img.getWidth()][img.getHeight()];	//Stores the individual cost value at i,j.
		int [] seamloc = new int[img.getWidth()];	//Stores the height of the seam at a given length.
		boolean [] passedseam = new boolean[img.getWidth()];	//Checks whether or not we passed the seam vertically while creating the modified image.
		BufferedImage newImg = new BufferedImage(img.getWidth(),img.getHeight()-1, BufferedImage.TYPE_INT_RGB);	//Where we will store our modified image
		for (int i=0; i<(img.getWidth()); i++) {
			for (int j=0; j<(img.getHeight()); j++) {
				costs[i][j] = costcalc(i,j,(i==0),(i==img.getWidth()-1),(j==0),(j==img.getHeight()-1), img, costs);
			}
		}
		seamloc[img.getWidth()-1]= img.getHeight()-1;
		for (int j=0; j<(img.getHeight()); j++) {
			if (costs[img.getWidth()-1][j]<seamloc[img.getWidth()-1])	{
				seamloc[img.getWidth()-1] = j;
			}
		}
		for (int i=img.getWidth()-2;i>=0; i--)	{
				if (seamloc[i+1]==0)	{
					if (costs[i][0]<=costs[i][1])
						seamloc[i]=0;
					else {
						seamloc[i]=1;
					}
				}
				else if (seamloc[i+1]==img.getHeight()-1)	{
					if (costs[i][img.getHeight()-1]<=costs[i][img.getHeight()-2])
						seamloc[i]=img.getHeight()-1;
					else {
						seamloc[i]=img.getHeight()-2;
					}
				}
				else {
					if(costs[i][seamloc[i+1]]<costs[i][seamloc[i+1]+1] && (costs[i][seamloc[i+1]]<costs[i][seamloc[i+1]-1]))	{
						seamloc[i]=seamloc[i+1];
					}
					else if (costs[i][seamloc[i+1]+1]<costs[i][seamloc[i+1]-1])	{
						seamloc[i]=seamloc[i+1]+1;
					}
					else	{
						seamloc[i]=seamloc[i+1]-1;
					}
				}
		}
		for (int i=0; i<(img.getWidth()); i++) {
			for (int j=0; j<(img.getHeight()-1); j++) {
				if (j==seamloc[i] || passedseam[i])	{
					passedseam[i]=true;
					newImg.setRGB(i,j, img.getRGB(i,j+1));
				}
				else	{
					newImg.setRGB(i,j,img.getRGB(i,j));
				}
			}
		}
		return newImg;
	}
	public static BufferedImage invertImg(BufferedImage img)	{
		BufferedImage inv = new BufferedImage(img.getHeight(),img.getWidth(), BufferedImage.TYPE_INT_RGB);
		for (int j=0; j<(img.getWidth()); j++) {
			for (int i=0; i<(img.getHeight()); i++) {
				inv.setRGB(i,j, img.getRGB(j,i));
			}
		}
		return inv;
	}

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
		Scanner in = new Scanner (System.in);
		System.out.println("Please enter the desired width");
		String newWidth = in.next();
		boolean invalid=(Integer.parseInt(newWidth)>img.getWidth());
		while(invalid)	{
			System.out.println("Please enter a width less than the width of the picture");
			newWidth = in.next();
			invalid=(Integer.parseInt(newWidth)>img.getWidth());
		}
		System.out.println("Please enter the desired height");
		String newHeight = in.next();
		invalid=(Integer.parseInt(newHeight)>img.getHeight());
		while(invalid)	{
			System.out.println("Please enter a height less than the width of the picture");
			newHeight = in.next();
			invalid=(Integer.parseInt(newHeight)>img.getWidth());
		}
		int startingWidth=img.getWidth();
		int startingHeight=img.getHeight();
		for (int i=Integer.parseInt(newHeight);i<startingHeight;i++)	{
			img = SeamRemove(img);
		}
		img = invertImg(img);
		for (int i=Integer.parseInt(newWidth);i<startingWidth;i++)	{
			img = SeamRemove(img);
		}
		img = invertImg(img);
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