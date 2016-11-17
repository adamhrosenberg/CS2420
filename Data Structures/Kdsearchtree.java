package homework10;

import java.util.ArrayList;
import java.util.ListIterator;

import homework10.Crowd;
import homework10.Crowd.*;
/* 
 * 
 *Author: Adam Rosenberg
 * 
 * December 1, 2014.
 */
public class Kdsearchtree {	//Our search tree
	public Kdsearchtree(ArrayList<Crowd.Boid> boids)	{	//Constructor
		create(boids);
	}
	public static void swap(ArrayList<Crowd.Boid> boids, int a, int b)	{	//Swaps two boids
		double temp = boids.get(a).posx;
		boids.get(a).posx = boids.get(b).posx;
		boids.get(b).posx = temp;
		
		temp = boids.get(a).posy;
		boids.get(a).posy = boids.get(b).posy;
		boids.get(b).posy = temp;
		
		temp = boids.get(a).velx;
		boids.get(a).velx = boids.get(b).velx;
		boids.get(b).velx = temp;
		
		temp = boids.get(a).vely;
		boids.get(a).vely = boids.get(b).vely;
		boids.get(b).vely = temp;
	}
	
	public static int partition(ArrayList<Crowd.Boid> boids, boolean isEven, int start, int end, int pivot)	{//Makes everything less than our pivot on the right of it, and everything greater on the left.
		swap(boids, pivot, end);	//Put our pivot at the end.
		int left = start-1;	//Start 1 to the left of our start due to how we loop through it.
		int right = end;	//Start 1 to the right of our end for similar reasons.
		boolean cont = true;	//Whether or not to continue
		while (cont)	{
			if (!isEven)	{	//If not even, then we compare x values.
				while(left < end && boids.get(++left).posx < boids.get(end).posx);	//Increment left until we find a boid value that's greater than our pivot.
				while(right > start &&boids.get(--right).posx > boids.get(end).posx);	//Decrement right until we find a boid value that's less than our pivot.
			}
			else	{
				while(left < end && boids.get(++left).posy < boids.get(end).posy);
				while(right > start && boids.get(--right).posy > boids.get(end).posy);
			}
			if (left >= right)	{cont = false;}	//If our left is ahead of our right, then we're done.
			else	{swap(boids, left, right);}	//Otherwise, swap the left and right boids.
		}
		right++;	//The value after our right value will have every value after it being larger than our pivot.
		swap(boids, right, end);	//Put our pivot back where it should be.
		return right;	//Return pivot.
	}
	
	public static int boidmedian(ArrayList<Crowd.Boid> boids, boolean isEven)	{	//Finds the median value in a boids array.
		return boidmedian(boids, isEven, 0, boids.size()-1, (boids.size())/2);
	}
	
	public static int boidmedian(ArrayList<Crowd.Boid> boids, boolean isEven, int start, int end, int pivot)	{	//Makes everything to the left of our pivot less than it, everything to the right greater, and goes again if our pivot is not the median.
		if (end - start == 1)	{	//Code to handle the special size=2 case.
			if (!isEven)	{
				if (boids.get(start).posx > boids.get(end).posx)	{	//If our start is larger than our end, swap them.
					swap(boids, start, end);
				}
			}
			else	{
				if (boids.get(start).posy > boids.get(end).posy)	{
					swap(boids, start, end);
				}
			}
			return pivot;
		}
		int pseudoPivot = partition(boids, isEven, start, end, pivot);	//Finds out where our guessed pivot actually is.
		if (pivot == pseudoPivot)	{	//If we've sorted and are at our pivot, we're done.
			return pivot;
		}
		else if (pivot < pseudoPivot)	{	//Otherwise, if our pivot is on the left side, recurse to the left side.
			return boidmedian(boids, isEven, start, pseudoPivot, pivot);
		}
		else	{	//Otherwise, recurse to the right side.
			return boidmedian(boids, isEven, pseudoPivot, end, pivot);
		}
	}
	
	public static class KDTreeNode {	//Tree node objects store the node to its left, its right, its parent and left and right data.
		KDTreeNode left, right, parent;
		boolean even;
		Boid boid;
		public KDTreeNode(Boid j, boolean even, KDTreeNode left, KDTreeNode right, KDTreeNode parent) {
			this.boid = j;
			this.left = left;
			this.right = right;
			this.parent = parent;
			this.even = even;
		}
	}
    KDTreeNode root;	//Root of our tree
	public int size()	{	//Finds the size of our tree.
		return size(this.root);
	}
	public int size(KDTreeNode node) {
		if (node == null)	{
			return 0;
		}
		else	{
			return size(node.left)+size(node.right)+1;
		}
    }
    public int height() {	//Recursively finds the height of the tree.
    	return height(root);
    }
    private int height(KDTreeNode node) {
    	if (node == null)	{
    		return -1;
    	}
    	else	{
    		return Math.max(height(node.left), height(node.right))+1;
    	}
    }
    public static double distance(double ax, double ay, double bx, double by)	{	//Finds the distance between two points.
    	return Math.sqrt((ax - bx)*(ax - bx) + (ay - by)*(ay - by));
    }
    public void create(ArrayList<Crowd.Boid> boids)	{	//Creates our tree.
    	root = create(boids, null);
    }
    
    public KDTreeNode create(ArrayList<Crowd.Boid> boids, KDTreeNode parent)	{	//Creates a sub-tree using the input boids and its parent.
    	if (boids.size() == 0)	{	//If there's nothing to place, return null.
    		return null;
    	}
    	else if (boids.size() == 1)	{	//If there's only one data point, input our node.
    		return new KDTreeNode(boids.get(0), !parent.even, null, null, parent);
    	}
    	else	{
    		int pos;
    		if (parent != null)	{	//If we have a parent, we split the opposite way our parent did.
    			pos = boidmedian(boids, !parent.even);
    		}
    		else {	//If we don't, we split according to x values.
    			pos = boidmedian(boids, false);
    		}
    		ArrayList<Crowd.Boid> temp = new ArrayList<Crowd.Boid>();	//Temp will store all the boids split to the left.
    		ArrayList<Crowd.Boid> temp2 = new ArrayList<Crowd.Boid>(); 	//Temp2 will store all the boids split to the right.
    		for (int i = 0; i < pos; i++)	{
    			temp.add(boids.get(i));
    		}
    		for (int i = pos + 1; i < boids.size(); i++)	{
    			temp2.add(boids.get(i));
    		}
    		KDTreeNode node;	//This will be the node we input.
    		if (parent != null)	{	//If we have a parent, input our node and set it as its parent.
    			node = new KDTreeNode(boids.get(pos), !parent.even, null, null, parent);
    		}
    		else {	//Otherwise, create a new node which will be the root.
    			node = new KDTreeNode(boids.get(pos), false, null, null, null);
    		}
    		node.left = create(temp, node);	//The node to the left will be created using the boids less than this node.
    		node.right = create(temp2, node);	//The node to the right will be created using the boids greater than this node.
    		return node;
    	}
    }
   
	public ArrayList<Crowd.Boid> pointsInCircle(double x, double y, double r)	{	//Returns all points within the circle with center at x,y and radius r.
		return pointsInCircle(x, y, r, this.root);
	}
	
	public ArrayList<Crowd.Boid> pointsInCircle(double x, double y, double r, KDTreeNode node)	{	//Recursively finds all points in the circle with center x,y and radius r.
		if (node == null)	{	//If we're at a null node, return null.
			return null;
		}
		else	{
			ArrayList<Crowd.Boid> inCircle = new ArrayList<Crowd.Boid>();	//inCircle will store the values in our circle.
			boolean pruneLeft, pruneRight;	//pruneLeft and pruneRight say whether or not we should prune left or right.
			if (!node.even)	{
				pruneLeft = (node.boid.posx < x - r);
				pruneRight = (node.boid.posx > x + r);
			}
			else	{
				pruneLeft = (node.boid.posy < y - r);
				pruneRight = (node.boid.posy > y + r);
			}
			if (!pruneLeft)	{	//If we're not pruning the left, get everything in there that is in the circle.
				ArrayList<Crowd.Boid> points = pointsInCircle(x, y, r, node.left);
				if (points != null)	{
					inCircle.addAll(points);
				}
			}
			if (distance(x, y, node.boid.posx, node.boid.posy) < r)	{	//If the node we're at is in the circle, add it.
				inCircle.add(node.boid);
			}
			if (!pruneRight)	{	//If we're not pruning the right, get everything in there that is in the circle.
				ArrayList<Crowd.Boid> points = pointsInCircle(x, y, r, node.right);
				if (points != null)	{
					inCircle.addAll(points);
				}
			}
			return inCircle;	//Return our ArrayList.
		}
	}
	public boolean checkBalance()	{	//Checks whether or not the tree is a KD tree.
		return checkBalance(root);
	}
	public boolean checkBalance(KDTreeNode node)	{	//Checks whether or not the tree is a KD tree recursively.
		if (node.left == null && node.right == null)	{
			return true;
		}
		else if (node.left != null && node.right == null)	{
			if (!node.even)	{
				if (node.left.boid.posx > node.boid.posx)	{
					System.out.println("Case A1");
					return false;
				}
				else return true;
			}
			else	{
				if (node.left.boid.posy > node.boid.posy)	{
					System.out.println("Case A2");
					return false;
				}
				else return true;
			}
		}
		else if (node.right != null && node.left == null)	{
			if (node.even)	{
				if (node.right.boid.posx < node.boid.posx)	{
					System.out.println("Case B1");
					return false;
				}
				else return true;
			}
			else	{
				if (node.right.boid.posy < node.boid.posy){
					System.out.println("Case B2");
					return false;
				}
				else return true;
			}
		}
		else if (!node.even)	{
			if (node.left.boid.posx > node.boid.posx || node.right.boid.posx < node.boid.posx)	{
				System.out.println("Case C1");
				return false;
			}
			else {
				return (checkBalance(node.left) && checkBalance(node.right));
			}
		}
		else {
			if (node.left.boid.posy > node.boid.posy || node.right.boid.posy < node.boid.posy)	{
				System.out.println("Case C2");
				return false;
			}
			else	{
				return (checkBalance(node.left) && checkBalance(node.right));
			}
		}
	}
	public void levelorder()	{	//Performs an iterative zero-space levelorder.
    	System.out.println("Levelorder: ");
    	System.out.println(root.boid.posx + " " + root.boid.posy);	//Print the root.
    	int height = height() + 1;
    	KDTreeNode currentNode = root;
    	for (int i = 1; i < height; i++)	{	//Each i corresponds to the level we're at.
    		int j = 1;
    		for (int k = 0; k<i; k++)	{
    			j*=2;
    		}
    		//j is 2^i.
    		for (int l = 0; l<j; l++)	{	//Each l corresponds to a string, i long, of 0's and 1's.  This will be essentially "instructions" as to how we should travel left and right.
    			for (int m = 0; m<i; m++)	{	//m checks the individual digits of l.
    				if ((l & (1L << (i-1-m))) == 0)	{	//When m is 0, this checks the left-most digit.  When m is 1, this checks the one to the right of that, and so on.
    					if (currentNode.left != null)	{	//If the digit is 0, travel to the left (if possible).  Else, we can not reach a node this way, so do not do anything further.
    						currentNode = currentNode.left;
    					}
    					else 	{	
    						currentNode = null;
    						m = i;
    					}
    				}
    				else if ((l & (1L << (i-1-m))) != 0)	{	//If the digit is 1, travel to the right (if possible).  Else, we can not reach a node this way.
    					if (currentNode.right != null)	{
    						currentNode = currentNode.right;
    					}
    					else	{
    						currentNode = null;
    						m = i;
    					}
    				}
    			}
    			if (currentNode != null)	{	//If we reach a node, print it.
    				System.out.print(currentNode.boid.posx + " " + currentNode.boid.posy + ", ");
    			}
    			currentNode = root;
    		}
    		System.out.println();
    	}
    }
	public static void main (String args []) {
		Crowd crowd = new Crowd();
		Kdsearchtree KDtree = new Kdsearchtree(crowd.boids);
		System.out.println(KDtree.checkBalance());
		KDtree.levelorder();
//		for (int i = 0; i < crowd.boids.size(); i++)	{
//			System.out.println(crowd.boids.get(i).posx + ", " + crowd.boids.get(i).posy);
//		}
	}
}
