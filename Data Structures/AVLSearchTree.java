package homework08;

/**
 * 
 * @author Adam Rosenberg
 * Homework 07
 *
 */

import homework08.SandQ.*;

public class AVLSearchTree<T extends Comparable<? super T>>
{
	public static class TreeNode<J> {	//Tree node objects store the node to its left, its right, its parent and whether or not it has been traversed (used for traversals).
		J data;
		TreeNode<J> left, right, parent;
		boolean traversed;
		int height;
		public TreeNode(J data, TreeNode<J> left, TreeNode<J> right, TreeNode<J> parent) {
			this.left = left;
			this.right = right;
			this.data = data;
			this.parent = parent;
			this.traversed = false;
			this.height = 0;
			if (this.parent != null)	{
				TreeNode<J> currentNode = this;
				while (currentNode.parent != null)	{
					currentNode.parent.height = Math.max(currentNode.parent.height, currentNode.height + 1);
					currentNode = currentNode.parent;
				}
			}
		}
	}
	TreeNode<T> root;	//Root simply stores the root of the tree.
	
    public int size()	{	//Recursively finds the size of the tree.
    	return size(root);
    }
    
    public int size(TreeNode<T> node) {
		if (node == null)	{
			return 0;
		}
		else	{
			return size(node.left)+size(node.right)+1;
		}
    }
    
    public int height(TreeNode<T> node)	{
    	if (node == null)	{
    		return -1;
    	}
    	else return node.height;
    }
	
	public void updateHeightSingle(TreeNode<T> node)	{
		node.height = Math.max(height(node.left), height(node.right)) + 1;
	}
	
	public void updateHeight(TreeNode<T> node)	{
		while (node.parent != null)	{
			node.height = Math.max(height(node.left), height(node.right)) + 1;
			node = node.parent;
		}
		node.height = Math.max(height(node.left), height(node.right)) + 1;
	}
	
    public boolean checkBalance()	{
    	return checkBalance(root);
    }
    
    public boolean checkBalance(TreeNode<T> node)	{
    	if (node == null)	{
    		return true;
    	}
    	else if (height(node.left) > height(node.right) + 1 || height(node.left) + 1 < height(node.right))	{
    		return false;
    	}
    	else return (checkBalance(node.left) && checkBalance(node.right));
    }
    
    public void preorder()	{	//Performs an iterative preorder traversal.
    	//Start at the top, and find the max node of the binary search tree.
    	System.out.print("Preorder: ");
    	TreeNode<T> currentNode = root;
    	TreeNode<T> max = findmax(root);
    	System.out.print(currentNode.data + ", ");	//No need for stacks, simply print the value of each node the first time you traverse it.
    	while (currentNode != max)	{	//Once we get to the max node, all we need to do is travel up the node.
    		while (currentNode.left != null)	{	//First, we travel to the left as much as we can.
    			currentNode = currentNode.left;
    	    	System.out.print(currentNode.data + ", ");
    		}
    		while (currentNode.right == null || currentNode.traversed == true)	{	//Then, we travel up as much as possible, either if there's nothing to the right or if we've already travelled to the right before on that node.
    			currentNode.traversed = false;
    			currentNode = currentNode.parent;
    		}
    		//Then, we perform exactly one travel to the right.
    		currentNode.traversed = true;
    		currentNode = currentNode.right;
    		if (currentNode == max)	{	//The max node is the very last node we print on preorder.
    	    	System.out.println(currentNode.data + ".");
    		}
    		else 	{
    	    	System.out.print(currentNode.data + ", ");
    		}
    	}
    	while (currentNode != root)	{	//Travel up the node, setting all traversed values to false so that subsequent traversals work.
    		currentNode.traversed = false;
    		currentNode = currentNode.parent;
    	}
    	root.traversed = false;
    }
    
    public void inorder() {	//Performs an iterative inorder traversal.
    	//Start at the top, and find the max node of the binary search tree.
    	System.out.print("Inorder: ");
		ListStack<T> stack = new ListStack<T>();
    	TreeNode<T> currentNode = root;
    	TreeNode<T> max = findmax(root);
    	stack.push(currentNode.data);	//The first time you encounter a node, push it on the stack.
    	while (currentNode != max)	{	//Once we get to the max node, all we need to do is travel up the node.
    		while (currentNode.left != null)	{	//First, we travel to the left as much as we can.
    			currentNode = currentNode.left;
    			stack.push(currentNode.data);
    		}
    		while (currentNode.right == null || currentNode.traversed == true)	{	//Then, we travel up as much as possible, either if there's nothing to the right or if we've already travelled to the right before on that node.
    			if (currentNode.traversed == false)	{	//If this is our first time traversing up the node, we pop it off the stack.
    				System.out.print(stack.pop() + ", ");
    			}
    			currentNode.traversed = false;
    			currentNode = currentNode.parent;
    		}
    		//Then, we perform exactly one travel to the right.
    		currentNode.traversed = true;
    		System.out.print(stack.pop() + ", ");	//Inorder pops values when moving to the right.
    		currentNode = currentNode.right;
    		stack.push(currentNode.data);
    	}
    	System.out.println(stack.pop() + ".");
    	while (currentNode != root)	{	//Travel up the node, setting all traversed values to false so that subsequent traversals work.
    		currentNode.traversed = false;
    		currentNode = currentNode.parent;
    	}
    	root.traversed = false;
    }
    
    public void postorder() {	//Performs an iterative postorder traversal.
    	//Start at the top, and find the max node of the binary search tree.
    	System.out.print("Postorder: ");
		ListStack<T> stack = new ListStack<T>();
    	TreeNode<T> currentNode = root;
    	TreeNode<T> max = findmax(root);
    	stack.push(currentNode.data);	//The first time you encounter a node, push it on the stack.
    	while (currentNode != max)	{	//Once we get to the max node, all we need to do is travel up the node.
    		while (currentNode.left != null)	{	//First, we travel to the left as much as we can.
    			currentNode = currentNode.left;
    			stack.push(currentNode.data);
    		}
    		while (currentNode.right == null || currentNode.traversed == true)	{	//Then, we travel up as much as possible, either if there's nothing to the right or if we've already travelled to the right before on that node.
    			System.out.print(stack.pop() + ", ");	//Postorder always pops when going up.
    			currentNode.traversed = false;
    			currentNode = currentNode.parent;
    		}
    		//Then, we perform exactly one travel to the right.
    		currentNode.traversed = true;
    		currentNode = currentNode.right;
    		stack.push(currentNode.data);
    	}
    	while (currentNode != root)	{	//Travel up the node, setting all traversed values to false so that subsequent traversals work.
        	System.out.print(stack.pop() + ", ");	//Postorder always pops when going up.
    		currentNode.traversed = false;
    		currentNode = currentNode.parent;
    	}
    	System.out.println(stack.pop() + ".");
    	root.traversed = false;
    }
    
    public void levelorder()	{	//Performs an iterative zero-space levelorder.
    	System.out.println("Levelorder: ");
    	System.out.println(root.data);	//Print the root.
    	TreeNode<T> currentNode = root;
    	for (int i = 1; i < root.height+1; i++)	{	//Each i corresponds to the level we're at.
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
    				System.out.print(currentNode.data + " ");
    			}
    			currentNode = root;
    		}
    		System.out.println();
    	}
    }

    public void rotate(TreeNode<T> node)	{
    	if (node == node.parent.right)	{
    		if (node.parent == node.parent.parent.right)	{
    			rotateRR(node);
    			return;
    		}
    		else if (node.parent == node.parent.parent.left)	{
    			rotateLR(node);
    			return;
    		}
    	}
    	if (node == node.parent.left)	{
    		if (node.parent == node.parent.parent.right)	{
    			rotateRL(node);
    			return;
    		}
    		else if (node.parent == node.parent.parent.left)	{
    			rotateLL(node);
    			return;
    		}
    	}
    }
    
    public void rotateLL(TreeNode<T> node)	{
    	swap(node.parent, node.parent.parent);
    	TreeNode<T> temp = node.parent;
    	node.parent = node.parent.parent;
    	node.parent.left = node;
    	temp.left = temp.right;
    	temp.right = node.parent.right;
    	if (temp.right != null)	{temp.right.parent = temp;}
    	node.parent.right = temp;
    	updateHeightSingle(node);
    	updateHeightSingle(node.parent.right);
    	updateHeight(node.parent);
    }
    
    public void rotateLR(TreeNode<T> node)	{
    	swap(node, node.parent);
    	TreeNode<T> temp = node.parent.left;
    	node.parent.right = node.right;
    	node.right = node.left;
    	node.left = temp;
    	node.parent.left = node;
    	rotateLL(node);
    }
    
    public void rotateRL(TreeNode<T> node)	{
    	swap(node, node.parent);
    	TreeNode<T> temp = node.parent.right;
    	node.parent.left = node.left;
    	node.left = node.right;
    	node.right = temp;
    	node.parent.right = node;
    	rotateRR(node);
    }
    
    public void rotateRR(TreeNode<T> node)	{
    	swap(node.parent, node.parent.parent);
    	TreeNode<T> temp = node.parent;
    	node.parent.parent.right = node;
    	node.parent = node.parent.parent;
    	temp.right = temp.left;
    	temp.left = node.parent.left;
    	if (temp.left != null)	{temp.left.parent = temp;}
    	temp.parent = node.parent;
    	node.parent.left = temp;
    	updateHeightSingle(node);
    	updateHeightSingle(node.parent.left);
    	updateHeight(node.parent);
    }
    
    public void checkRotate(TreeNode<T> node)	{
    	updateHeight(node);
    	while (node.parent != null && node.parent.parent != null)	{
    		if (!checkBalance(node.parent.parent))	{
    			rotate(node);
    		}
			node = node.parent;
    	}
    }
    
    public void swap(TreeNode<T> a, TreeNode<T> b)	{
    	T temp = a.data;
    	a.data = b.data;
    	b.data = temp;
    }
    
    public <E extends Comparable<? super T>> void insert(T data) {	//Inserts data into the tree.
    	if (root == null)	{	//If we don't have anything yet, put the value at the root.
    		root = new TreeNode<T>(data, null, null, null);
    	}
    	else	{	//Otherwise, put it somewhere else.
    		insert(data, root, null);
    	}
    }
    
    public <E extends Comparable<? super T>> void insert(T data, TreeNode<T> node, TreeNode<T> parent) {	//Inserts  data into the tree.
    	if (node == null)	{	//If the node we're at is null, we've reached where our node will go.
    		node = new TreeNode<T>(data, null, null, parent);
    		if (data.compareTo(parent.data) > 0)	{	//Set the parent to correctly identify its child.
    			parent.right = node;
    		}
    		else	{
    			parent.left = node;
    		}
    		checkRotate(node);
    	}
    	else if (data.compareTo(node.data) > 0)	{	//If our node is bigger than the one we're comparing it to, travel to the right, and compare again.
    		insert(data, node.right, node);
    	}
    	else {	//Otherwise, travel to the left and compare again.
    		insert(data, node.left, node);
    	}
    }
    
    public <E extends Comparable<? super T>> TreeNode<T> findmax(TreeNode<T> node) {	//Finds the maximum value in the tree.
    	if (node.right == null)	{	//If there's nothing to the right, we must be at the max.
    		return node;
    	}
    	else return findmax(node.right);	//Otherwise, travel to the right and try again.
    }
    
    public <E extends Comparable<? super T>> TreeNode<T> findmin(TreeNode<T> node) {	//Finds the minimum value in the tree.
    	if (node.left == null)	{	//If there's nothing to the left, we must be at the min.
    		return node;
    	}
    	else return findmin(node.left);	//Otherwise, travel to the left and try again.
    }
    
    public <E extends Comparable<? super T>> Boolean remove(T data) {	//Removes data.
    	return remove(find(data));
    }
    
    private <E extends Comparable<? super T>> boolean remove(TreeNode<T> node) {
    	if (node.right == null && node.left == null)	{	//If we're at a leaf, simply make the parent not point to this node anymore.
    		if (node.data.compareTo(node.parent.data) > 0)	{
    			node.parent.right = null;
    			checkRotate(node);
    			System.out.println(node.data);
    			return true;
    		}
    		else {
    			node.parent.left = null;
    			checkRotate(node);
    			System.out.println(node.data);
    			return true;
    		}
    	}
    	else if (node.right == null)	{	//If there's only one child, simply replace this node with its child.
    		swap(node, node.left);
    		remove(node.left);
    		return true;
    			}
    	else if (node.left == null)	{	//Same as above, only slightly different code to handle the left child being absent instead of the right.
    		swap(node, node.right);
    		remove(node.right);
    		return true;
    	}
    	else if (node.left != null && node.right != null)	{	//If the node has two childs, replace it with the minimum value on the right, then remove said minimum value.
    		TreeNode<T> temp = findmax(node.left);
    		remove(temp);
    		node.data = temp.data;
    		return true;
    	}
    	return false;
    }
    
    public <E extends Comparable<? super T>> TreeNode<T> find(T data) {	//Finds a node.
    	return find(root, data);
    }
    
    private <E extends Comparable<? super T>> TreeNode<T> find(TreeNode<T> node,T data) {
    	if (node == null)	{	//If we are at a null node, we didn't find it.
    		return null;
    	}
    	if (data.compareTo(node.data) == 0)	{	//If our value is equal to the value of the node, we found it.
    		return node;
    	}
    	else if (data.compareTo(node.data) > 0)	{	//If our value is larger than the value of the node, go to the right.
    		return find(node.right, data);
    	}
    	else {	//If our value is smaller than the value of the node, go to the left.
    		return find(node.left, data);
    	}
    }
    
    public static void main(String args[]) {
        AVLSearchTree<Integer> tree = new AVLSearchTree<Integer>();
        tree.insert(8);
        tree.insert(4);
        tree.insert(12);
        tree.insert(10);
        tree.insert(14);
        tree.insert(15);
        tree.insert(16);
        tree.insert(2);
        tree.insert(1);
        tree.levelorder();
        tree.remove(8);
        tree.levelorder();
        System.out.println(tree.checkBalance());
    }
}
