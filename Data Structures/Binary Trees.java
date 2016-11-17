/**
 * @author rosenberg
 * Homework 7
 * 3/7/13
 * Binary Trees.java
 */

public class BinaryTree<T extends Comparable<? super T>>
{

	public static class Node<J> {
		//Initializes the nodes
		Node<J> next, prev;
		J data;
		//Sets up the node to be all fine and dandy.
		public Node(Node<J> previous,J t, Node<J> target) {
			this.prev = previous;
			this.next = target;
			this.data = t;
		}
	}

	public class ListStack {
		private Node<TreeNode<T>> header = new Node<TreeNode<T>>(null,null,null);
		private Node<TreeNode<T>> current = header;
		/**
		 * Adds the data to the list. No inflation necessary.
		 * @param data : int the data to be added at the end of the stack.
		 */
		public <E extends Comparable<? super T>> void push(TreeNode<T> data) {
			current.next = new Node<TreeNode<T>>(current,data,null);
			current = current.next;
		}
		/**
		 * Removes and returns the latest data entered. No shrinkage necessary.
		 * @return int : the last item added to the list.
		 */
		public <E extends Comparable<? super T>> TreeNode<T> pop() {
			TreeNode<T> tmp = current.data;
			current = current.prev;
			return tmp;
		}
		/**
		 * Returns a "peek" of an object (its data)
		 */
		public <E extends Comparable<? super T>> TreeNode<T> peek() {
			return current.data;
		}
		/**
		 * Returns if empty or not
		 */
		public <E extends Comparable<?super T>> boolean isempty() {
			if(current == header) return true;
			return false;
		}
	}
	
	public class ListQueue {
		private Node<TreeNode<T>> header = new Node<TreeNode<T>>(null,null,null);
		private Node<TreeNode<T>> footer = new Node<TreeNode<T>>(header,null,null);
		/**
		 * Because you can't set header.next = footer out in the middle of nowhere with the rest of them.
		 */
		public ListQueue() {
			header.next = footer;
		}
		/**
		 * Adds the data to the queue at the end. No inflation needed.
		 * @param data : int the data to be added.
		 */
		public void push(TreeNode<T> data) {
			footer.prev.next = new Node<TreeNode<T>>(footer.prev, data, footer);
			footer.prev = footer.prev.next;
		}
		/**
		 * Removes and returns the first item from the queues. No shrinkage necessary.
		 * @return int the oldest object in the linked list.
		 */
		public TreeNode<T> pop() {
			TreeNode<T> tmp = header.next.data;
			header.next.next.prev = header;
			header.next = header.next.next;
			return tmp;
		}
		/**
		 * returns if its empty or not
		 * @return boolean 
		 */
		public boolean isempty() {
			if(header.next == footer) return true;
			return false;
		}
		/**
		 * @return TreeNode<T> the data or "peek" of the object
		 */
		public TreeNode<T> peek() {
			return header.next.data;
		}
	}
	/**
	 * Class TreeNode.
	 */
    public class TreeNode<E>{
        T data;
        TreeNode<E> left, right;
        /**
         * Constructor of the TreeNode object. Sets the member variables also.
         */
        public TreeNode(T data, TreeNode<E> left, TreeNode<E> right) {
            this.left = left;
            this.right = right;
            this.data = data;
        }
    }
    TreeNode<T> root;
    /**
     * @return int the size of the tree rooted with the target
     */
	public <E extends Comparable<? super T>> int size(TreeNode<T> target) {
        return size2(target);
    }
	/**
	 * @return int 
	 * recursively traverse the tree and compute the size by calling the left and the right. swag. 
	 */
    private <E extends Comparable<? super T>> int size2(TreeNode<T> n) {
        if (n == null) return 0;
        return 1 + size(n.left) + size(n.right);
    }
    public int height() {
        return height(root);
    }
    /**
     * helper method
     * @return int height. 
     */
    private int height(TreeNode<T> n) {
        if (n == null) return 0;
        return 1 + Math.max(height(n.left), height(n.right));
    }
    /**
     * Visit the root.
	 * Traverse the left subtree.
	 * Traverse the right subtree.
     * Made for stack data structure 
     */
    public void preorderstack() {
    	if(root == null) return;
    	ListStack stack = new ListStack();
    	stack.push(root);
    	while(!stack.isempty()) {
    		TreeNode<T> current = stack.pop();
    		if(current.right != null) stack.push(current.right);
    		if(current.left != null) stack.push(current.left);
    		System.out.print(current.data + " ");
    	}
    }
    public void inorder() {
        inorder(root);
    }
    /**
     *  Traverse the left subtree.
     *  Visit the root.
	 *  Traverse the right subtree.
     */
    private void inorder(TreeNode<T> n) {
        if (n == null) return; //base case
        inorder(n.left);
        inorder(n.right);
    }
    /**
     * Adaption of inorder traversal for a stack
     * LEFT, ROOT, RIGHT
     */
    public void inorderstack() {
    	TreeNode<T> node = root;
    	ListStack stack = new ListStack();
    	while(!stack.isempty() || node != null) {
    		if(node != null) {
    			stack.push ( node);
    			node = node.left;
    		} else {
    			node = stack.pop();
    			node = node.right;
    		}
    	}
    }
    /**
     *  Traverse the left subtree.
     *  Traverse the right subtree.
     *  Visit the root. For a Stack
     */
    public void postorderstack() {
    	if(root == null) return;
    	ListStack stack = new ListStack();
    	TreeNode<T> current = root;
    	while(true) {
    		if(current != null) {
    			if(current.right != null) stack.push(current.right);
    			stack.push(current);
    			current = current.left;
    			continue;
    		}
    		if(stack.isempty()) return;
    		current = stack.pop();
    		if(current.right != null && !stack.isempty() && current.right == stack.peek()) {
    			stack.pop();
    			stack.push(current);
    			current = current.right;
    		} else {
    			System.out.print(current.data + ", ");
    			current = null;
    		}
    	}
    }
    /**
     * Do a level order traversal with a queue
     * Level order visits the nodes horizontally across the tree.
     */
    public void levelorderqueue() {
    	ListQueue queque = new ListQueue();
    	TreeNode<T> current = root;
    	queque.push(current);
    	while(!queque.isempty()) {
    		current = queque.pop();
    		System.out.print(current.data);
    		if(current.left != null) queque.push(current.left);
    		if(current.right != null) queque.push(current.right);
    	}
    }
    /**
     * @return void
     * Inserts a node into the tree. It then puts the node in the correct spot
     */
    public <E extends Comparable<? super T>> void insert(T thisdata) {
    	
    	if(root == null) {
    		root = new TreeNode<T>(thisdata,null,null);
    		return;
    	}
    	TreeNode<T> current = (TreeNode<T>) root;
    	while(current.left != null || current.right != null) {
    		if(current.data.compareTo(thisdata)<0 && current.right != null) {
    			current = current.right;
    			continue;
    		}
    		else if(current.data.compareTo(thisdata)<0 && current.right == null) {
    			current.right = new TreeNode<T>(thisdata,null,null);
    			return;
    		}
    		
    		if(current.data.compareTo(thisdata)>0 && current.left != null) {
    			current = current.left;
    			continue;
    		}
    		else if(current.data.compareTo(thisdata)>0 && current.left == null) {
    			current.left = new TreeNode<T>(thisdata,null,null);
    			return;
    		}
    	}
    	
    	if(current.data.compareTo(thisdata)>0) {
			current.left = new TreeNode<T>(thisdata,null,null);
			return;
		}
		else {
			current.right = new TreeNode<T>(thisdata,null,null);
			return;
		}
    }
    /**
     * @return TreeNode<T>
     * goes all the way down the ride half of the tree to find the max
     */
    public <E extends Comparable<? super T>> TreeNode<T> findmax(TreeNode<T> target) {
    	if(target == null) return null;
    	while(target.right!=null) {
    		target = target.right;
    	}
    	return target;
    }
    /**
     * @return TreeNode<T> 
     * Traverses down the left side as far as possible to find and return the min node.
     */
    public <E extends Comparable<? super T>> TreeNode<T> findmin(TreeNode<T> target) {
    	if(target == null) return null;
    	while(target.left!=null) {
    		target = target.left;
    	}
    	return target;
    }
    /**
     * @return Boolean
     * Starter function for remove. We have to code the remove to deal with a node with no children, one child, or two children.
     */
    public <E extends Comparable<? super T>> Boolean remove(T thatdata) {
    	if(root == null) return false;
    	if(root.data == thatdata) {
    		root.data = findmax(root.left).data;
    		remove(findmax(root.left),findparent(findmax(root.left).data));
    		return true;
    	}
    	return (remove(find(thatdata),findparent(thatdata)));
    }
    /**
     * Removes the node and re-sorts the subtree it was in. 
     */
    private <E extends Comparable<? super T>> boolean remove(TreeNode<T> target, TreeNode<T> parent) {
    	if(target.left == null && target.right == null) {
    		if(parent.left == target) parent.left = null;
    		if(parent.right == target) parent.right = null;
    		return true;
    	}
    	if(target.left != null && target.right == null) {
    		parent.left = target.left;
    		return true;
    	}
    	if(target.right != null && target.left == null) {
    		parent.right = target.right;
    		return true;
    	}
    	if(target.right != null && target.left != null) {
			T tmp = findmin(target.right).data;
			remove(tmp);
			target.data = tmp;
			return true;
    	}
    	return false;
    }
    /**
     * @return TreeNode<T> find method. 
     * 
     */
    public <E extends Comparable<? super T>> TreeNode<T> find(T data) {
    	return find(root,data);
    }
    /**
     * @return TreeNode<T>
     * recursively traverse the tree and check every node untill the node's data == target's data.  
     */
    private <E extends Comparable<? super T>> TreeNode<T> find(TreeNode<T> target,T data) {
    	if(target == null) return null;
    	if(target.data.compareTo(data) == 0) return target;
    	if(target.data.compareTo(data) < 0) return find(target.right,data);
    	if(target.data.compareTo(data) > 0) return find(target.left,data);
    	return null;
    }
    /**
     * @return TreeNode<T>
     * checks base case if the node in question is the root node
     */
    public <E extends Comparable<? super T>> TreeNode<T> findparent(T data) {
    	if (root.data == data) {
    		System.out.println("No parent node, root.");
    		return null;
    	}
    	return findparent(root,data);
    }
    private <E extends Comparable<? super T>> TreeNode<T> findparent(TreeNode<T> target,T data) {
    	if(target == null) return null;
    	if(target.data.compareTo(data) < 0) {
	    	if(target.right != null) {
	    		if(target.right.data.compareTo(data) == 0){
	    			return target;
	    		}
	    		else {
	    			return find(target.right,data);
	    		}
	    	}
    	}
    	if(target.data.compareTo(data) > 0) {
    		if(target.left != null) {
	    		if(target.left.data.compareTo(data) == 0){
	    			return target;
	    		}
	    		else {
	    			return find(target.left,data);
	    		}
	    	}
    	}
    	return null;
    }
    /**
     * Main function for the Binary Trees.java class
     * Declare a tree, and check the run times for all the sorts etc.
     */
    public static void main(String args[]) {
        BinaryTree<Double> tree = new BinaryTree<Double>();
        long time = System.nanoTime();
        double tmp = 0;
        int checker = 100000;
        double[] memos = new double[checker];
        for(int i =0;i<checker;i++) {
        	tmp = Math.random()*1000000;
        	boolean exists = false;
        	for(int k = 0;k<checker;k++) {
        		if(memos[k] == tmp)  {
        			exists = true;
        			i--;
        			break;
        		}
        		exists = false;
        	}
        	if(!exists) memos[i] = tmp;
        }
        time = System.nanoTime();
        for(int l =0;l<checker;l++) {
        	tree.insert(memos[l]);
        }
        /**
         * Below is the testing code to test all the methods.
         */
//        System.out.println("size: " + tree.size(tree.root));
//        System.out.println("height: " + tree.height());
//        System.out.println("In order:");
//        tree.inorder();
//        System.out.println(" ");
//        System.out.println("Time: " + (System.nanoTime() - time) + "ns Amount: " + checker);
//        System.out.println("Pre order");
//        tree.preorderstack();
//        System.out.println("Post order");
//        tree.postorderstack();
//        System.out.println("Level order:");
//        tree.levelorderqueue();
    }
}