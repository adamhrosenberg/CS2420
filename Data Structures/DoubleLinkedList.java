/* Author:  Adam Rosenberg Aug. 8, 2014
 */

package homework09;

public class DoubleLinkedList<AnyType>
{
	// Creates the list node class
	  public class ListNode<AnyType> {
		  // data stores the data, and next and prev store what is next and what is previous
	        AnyType data;
	        ListNode<AnyType> next;
	        ListNode<AnyType> prev;
	        // Constructor for ListNode class
	        public ListNode(ListNode<AnyType> prev, AnyType data, ListNode<AnyType> next) {
	            this.data = data;
	            this.next = next;
	            this.prev = prev;
	        }
	        
	    }
	    // header and footer store the node before the first and after the last node, respectively.
    ListNode<AnyType> header;
    ListNode<AnyType> footer;
    int size=0;	// stores the size of the list
    
    public DoubleLinkedList() {  //constructor for the double linked list
    	// defines header and footer
        header = new ListNode<AnyType>(null, null, footer); 
        footer = new ListNode<AnyType>(header, null, null); 
    }
    
    public void insertEnd(AnyType data) {		// Inserts a node into the end of the list, directly before the footer
    	   footer.prev.next = new ListNode<AnyType>(footer.prev, data, footer);
           footer.prev = footer.prev.next;
           size++;  // increments size
        }
    public void insertBeginning(AnyType data)	{
    	header.next = new ListNode<AnyType>(header, data, header.next);
    	header.next.prev = header.next;
    	size++;
    }
    public void remove(ListNode<AnyType> node) {
        node.next.prev=node.prev;
        node.prev.next= node.next;
        size--;
    }
    public void removeHeader()	{
    	if (header.next.next == null)	{
    		header.next = null;
    	}
    	else	{
    		header.next = header.next.next;
    		header.next.prev = header;
    	}
    	size--;
    }
    public boolean removeSpec(AnyType data) {
        ListNode current = header.next;
        while (current != null && current.next != null && !current.next.data.equals(data)) current = current.next;
        if (current != null && current.next != null) {
            current.next = current.next.next;
            return true;
        }
        return false;
    }
    public DoubleListIterator<AnyType> first() {	// Gives the first node in the list
    	if(header.next!=footer){
        return new DoubleListIterator<AnyType>(header.next);
    	}
    	else
    	{
    		return null;
    	}
    }
    public DoubleListIterator<AnyType> last(){		// Gives the last node in the list
    	if(footer.prev != header){
    		return new DoubleListIterator<AnyType>(footer.prev);
    	}
    	else
    	{
    		return null;
    	}	
    }
      public int size() {	// Gives the size of the list
    	return size;
      }
    boolean contains(AnyType data) {	// Shows whether or not the list contains a certain element.
        DoubleListIterator<AnyType> i = first();
        while (i.valid()) {
            if (i.getData().equals(data)) return true;
            i.next();
        }
        return false;
    }
    public void replace (AnyType data1, AnyType data2) {	// Replaces an element in the list with another of your choice.
        ListNode<AnyType> current = header.next;
        while (current != null && !current.data.equals(data1)) current = current.next;
        if (current != null) current.data = data2;
    }
    
    public static void main(String args[]) {	// Tests the functions.
        DoubleLinkedList<Integer> l = new DoubleLinkedList<Integer>();
        
        l.insertEnd(new Integer(5));
        l.insertEnd(new Integer(55));
        l.insertEnd(new Integer(555));
        l.insertEnd(new Integer(555));
        l.insertBeginning(new Integer(6));
        l.removeHeader();
        
        DoubleLinkedList<Integer>.DoubleListIterator<Integer> iter = l.first();
        while (iter.valid()) {
            System.out.println(iter.getData());
            iter.next();
        }
    }
    public class DoubleListIterator<AnyType>
    {	//This creates the iterator for the double linked list.
    	DoubleLinkedList<AnyType>.ListNode<AnyType> newNode;	// newNode is used to create new nodes.
        DoubleLinkedList<AnyType>.ListNode<AnyType> current;	// current is the node you are currently at.
        
        DoubleListIterator(DoubleLinkedList<AnyType>.ListNode<AnyType> n) {	// Sets the current node
            this.current = n;
        }
        public boolean valid() {	// Returns whether or not the current node is empty.
            return (current.data != null);
        }
        
        public AnyType getData() {	// Gets the data from the current node.
            if (current != null) return current.data;
            else return null;
        }
        
        public void next() {	// Sets the current node to the next node.
            if (current != null) current = current.next;
        }
        
        public void prev() {	// Sets the current node to the previous node.
        	if (current != null) current = current.prev;
        }
        public void remove() {	// Removes the current node.
        	 current.prev.next = current.next;
    	     current.next.prev = current.prev;
    	     current=current.prev;
    	     size--;
        }
        public void insert(AnyType data) {	// Inserts a node after the current node.
        	newNode.data = data;
        	current.next.prev = newNode;
        	newNode.next = current.next;
        	current.next = newNode;
        	newNode.prev = current;
        	size++;
        }

    }
}