package homework09;
/* 
 * Author:  Adam Rosenberg November 17, 2014
 */

public class Heap {	//Heaps!
	int capacity = 25;
	int [] heap = new int [capacity];
	String [] heap2 = new String [capacity];
	int size;
	public Heap()	{	//size stores how much is in it, heap and heap2 store and int heap (which will be modified normally) and a string "heap" (which will be modified in exactly the same way the int heap will be.
		size = 0;
		for (int i = 0; i < capacity; i++)	{
			heap[i] = 0;
			heap2[i] = null;
		}
	}
	public void swap(int [] array, int a, int b)	{	//Swaps.
		int temp = array[a];
		array[a] = array[b];
		array[b] = temp;
	}
	public void swap(String [] array, int a, int b)	{	//Guess.
		String temp = array[a];
		array[a] = array[b];
		array[b] = temp;
	}
	
	public int parent(int child)	{	//Finds the parent.
		return (child-1)/2;
	}
	public int child1(int parent)	{	//Finds the first child.
		return 2*parent + 1;
	}
	public int child2(int parent)	{	//Finds the second child.
		return 2*parent + 2;
	}
	
	public int min(int a, int b, int c)	{	//Finds the minimum of 3 values.
		if (Math.min(Math.min(a, b), c) == a)	{
			return a;
		}
		else if (Math.min(Math.min(a, b), c) == b)	{
			return b;
		}
		else	{
			return c;
		}
	}
	
	public void delete()	{	//Deletes.
		heap[0] = heap[size - 1];
		size--;
		int i = 0;
		while (i < 12)	{
			int min = min(heap[i], heap[child1(i)], heap[child2(i)]);
			if (min == heap[i])	{
				i = 13;
			}
			else if (min == heap[child1(i)])	{
				swap(heap, i, child1(i));
				i = child1(i);
			}
			else 	{
				swap(heap, i, child2(i));
				i = child2(i);
			}
		}
	}
	public void deleteMatch()	{	//Deletes, and matches the modifications in our string "heap".
		heap[0] = heap[size - 1];
		heap2[0] = heap2[size - 1];
		size--;
		int i = 0;
		while (i < 12)	{
			int min = min(heap[i], heap[child1(i)], heap[child2(i)]);
			if (min == heap[i])	{
				i = 13;
			}
			else if (min == heap[child1(i)])	{
				swap(heap, i, child1(i));
				swap(heap2, i, child1(i));
				i = child1(i);
			}
			else 	{
				swap(heap, i, child2(i));
				swap(heap2, i, child2(i));
				i = child2(i);
			}
		}
	}
	
	public void insert(int data)	{	//Inserts data by percolating up.
		if (size < capacity)	{
			heap[size] = data;
			size++;
			int current = size - 1;
			while (current >= 1)	{
				if (heap[parent(current)] > heap[current])	{
					swap(heap, parent(current), current);
				}
				current = parent(current);
			}
		}
		else if (heap[0] < data)	{
			delete();
			insert(data);
		}
	}
	public void insertMatch(int data, String string)	{	//Same code as above, but also modifies heap2 in tandem.
		if (size < capacity)	{
			heap[size] = data;
			heap2[size] = string;
			size++;
			int current = size - 1;
			while (current > 1)	{
				if (heap[parent(current)] > heap[current])	{
					swap(heap, parent(current), current);
					swap(heap2, parent(current), current);
				}
				current = parent(current);
			}
		}
		else if (heap[0] < data)	{
			deleteMatch();
			insertMatch(data, string);
		}
	}
	
	public void selectionSort(int [] list, String [] companion)	{	//Basically the same sort in homework05 used to sort the heap after we store values in it.
		for (int i = 0; i<list.length; i++)	{
			int minLoc = i;
			for (int j = i; j<list.length; j++)	{
				if (list[j] < list[minLoc])	{
					minLoc = j;
				}
			}
			swap(list, i, minLoc);
			swap(companion, i, minLoc);
		}
	}
	
	public void print()	{	//Prints the heap.
		for (int i = size - 1; i >= 0; i--)	{
			System.out.println(heap[i]);
		}
	}
	public void printMatch()	{	//Prints the heap and its string counterpart in a way specifically tailored to this assignment.
		selectionSort(heap, heap2);
		for (int i = size - 1; i >= 0; i--)	{
			System.out.println("The #" + (size - i) + " word is \"" + heap2[i] + "\" with " + heap[i] + " entries.");
		}
	}
}
