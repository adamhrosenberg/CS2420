package homework07;
/**
 * 
 * @author  Adam Rosenberg
 * Homework 06
 *
 */
public class SandQ {

	//4 classes will be created, one for each combination of array-list and stack-queue.
	public static class ArrayStack<T>{	//Array stores our values, end tracks the end of our stack, capacity stores how many values we can store.
		T[] array;
		int end;
		int capacity;
		public ArrayStack()	{
			end = 0;
			capacity = 2;
			array = (T[]) new Object[capacity];
		}
		public void push(T data)	{	//Puts the input value at the end of the stack.
			//If we are about to add to many values, create a new array twice the size then copy values over to that array.
			if (end >= capacity-1)	{
				capacity*=2;
				T[] temp = (T[]) new Object[capacity];
				for (int i = 0; i < end+1; i++)	{
					temp[i] = this.array[i];
				}
				this.array = temp;
			}
			//Put our data at the end of the stack, then increment the end.
			this.array[end] = data;
			end++;
		}
		public T pop()	{	//"removes" the end value of the stack by decrementing end.
			//If our stack becomes 3/4ths empty, create a new array half the size, then copy values to that array to form a new stack.
			if (end <= capacity/4)	{
				capacity/=2;
				T[] temp = (T[]) new Object[capacity];
				for (int i = 0; i<end; i++)	{
					temp[i] = this.array[i];
				}
				this.array = temp;
			}
			//Decrement end, then return what value we "removed".
			end--;
			return this.array[end];
		}
		public T peek()	{	//Gives the value next to be popped without popping it.
			return this.array[end];
		}
		public void print()	{	//Prints out the array from first to be popped to last.
			for (int i = end-1; i!=0; i++)	{
				System.out.print("|" + this.array[i] + "|");
			}
			System.out.println();
		}
	}
	public static class ArrayQueue<T>{	//Array stores our values, start stores where in the array our queue starts, end stores where the next value goes, size stores the size of the queue, and capacity stores the capacity of the array.
		T[] array;
		int start;
		int end;
		int size;
		int capacity;
		public ArrayQueue()	{
			start = 0;
			end = 0;
			size = 0;
			capacity = 2;
			array = (T[]) new Object[capacity];
		}
		public void enqueue(T data)	{	//Adds a value to the end of the queue.
			if (size == capacity)	{	//We need to increase the size of our array if size = capacity
				int oldcapacity = capacity;
				if (capacity == 0)	{	//If we have 0 capacity, set it back to 1.
					capacity = 1;
				}
				else	{	//Otherwise, double it.
					capacity*=2;
				}
				//Create a new array of size capacity, then go around the queue, copying from start to end.
				T[] temp = (T[]) new Object[capacity];
				for (int i = start; i< start + oldcapacity; i++)	{
					temp[i-start] = array[i%oldcapacity];
				}
				//Might as well have our queue start at 0 and end at size.
				end = size;
				start = 0;
				array = temp;
			}
			//Put the value at the end of our queue, then incrememnt end (going to the beginning if you increment past capacity).
			array[end] = data;
			end = (end + 1)%capacity;
			size++;
		}
		public T dequeue()	{	//Removes the element at the beginning of the queue, then returns it.
			if (size == 0)	{	//Nothing to remove.
				return null;
			}
			else if (size <= capacity/4)	{	//If our stack is 3/4ths empty, shrink down the array by half.
				this.capacity/=2;
				T[] temp = (T[]) new Object[capacity];
				for (int i = start; i < size + start; i++)	{
					temp[i-start] = array[i%capacity];
				}
				end = size;
				start = 0;
				array = temp;
			}
			//Get the value at the start of our queue, then increment our queue and return what was at the start.
			T temp = array[start];
			start = (start + 1)%capacity;
			size--;
			return temp;
		}
		public void print()	{	//Print out our queue, from first to last to be removed.
			for (int i = start; i<start + size; i++)	{
				System.out.print("|" + this.array[i%capacity] + "|");
			}
			System.out.println();
		}
		public T peek()	{	//Returns the value at the front of the queue.
			if (end == start)	{
				return null;
			}
			else	{
				return this.array[end];
			}
		}
	}
	public static class ListStack<T>	{
		//Create a list to store our data.
		DoubleLinkedList<T> stack = new DoubleLinkedList<T>();
		public ListStack()	{
		}
		public void push(T data)	{
			//Store the data at the top of the stack.
			stack.insertBeginning(data);
		}
		public T pop()	{
			//Get the value at the top of the stack, then remove it.
			T temp = stack.header.next.data;
			stack.removeHeader();
			return temp;
		}
		public T peek()	{
			//Get the value at the top of the stack.
			if (stack.header.next.data == null)	{
				return null;
			}
			else	{
				return stack.header.next.data;
			}
		}
		public void print()	{
			DoubleLinkedList<T>.DoubleListIterator<T> iter = this.stack.first();
	        for(int i = 0; i<this.stack.size; i++) {
	            System.out.print("|" + iter.getData() + "|");
	            iter.next();
	        }
	        System.out.println();
		}
	}
	public static class ListQueue<T>	{
		//queue is a list that stores our queue.
		DoubleLinkedList<T> queue = new DoubleLinkedList<T>();
		public ListQueue()	{
		}
		//Stores our value at the back of the queue.
		public void enqueue(T data)	{
			queue.insertEnd(data);
		}
		public T dequeue()	{
			//Gets the value at the front of the queue, then removes it.
			T temp = queue.header.next.data;
			queue.removeHeader();
			return temp;
		}
		public T peek()	{
			//Gets the value at the front of the queue.
			if (queue.header.next.data == null)	{
				return null;
			}
			else	{
				return queue.header.next.data;
			}
		}
		public void print()	{	//Prints our queue, starting from the first to be removed.
			DoubleLinkedList<T>.DoubleListIterator<T> iter = this.queue.first();
	        for(int i = 0; i<this.queue.size; i++) {
	            System.out.print("|" + iter.getData() + "|");
	            iter.next();
	        }
	        System.out.println();
		}
	}
	public static void main(String args[]) {
		ArrayStack<Integer> arrayStack = new ArrayStack<Integer>();
		ArrayQueue<Integer> arrayQueue = new ArrayQueue<Integer>();
		ListStack<Integer> listStack = new ListStack<Integer>();
		ListQueue<Integer> listQueue = new ListQueue<Integer>();
	}
}
