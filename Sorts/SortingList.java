package homework05;
/**
 * Adam Rosenberg
 * Homework 5 Sorts
 * Insertion, bubble, selection, merge and quick sort.
 * October 16 2014
 */
public class SortingList {
	public static <T extends Comparable<? super T>> void print(T[] list)	{
		for (int i = 0; i<list.length; i++)	{
			System.out.println(list[i]);
		}
	}
	public static <T extends Comparable<? super T>> void swap(T[] list, int i, int j)	{
		T temp = list[i];
		list[i]=list[j];
		list[j]=temp;
	}
	public static <T extends Comparable<? super T>> void match(T[] list, T[] array, int start, int end)	{
		for (int i = start; i<end + 1; i++)	{
			list[i] = array[i-start];
		}
	}
	public static <T extends Comparable<? super T>> void bubbleSort(T[] list)	{
		for (int i = 0; i<list.length; i++)	{
			for (int j = 0; j<list.length-i-1; j++)	{
				if ((list[j].compareTo(list[j+1]) > 0))	{
					swap(list, j, j+1);
				}
			}
		}
	}
	public static <T extends Comparable<? super T>> void selectionSort(T[] list)	{
		for (int i = 0; i<list.length; i++)	{
			int minLoc = i;
			for (int j = i; j<list.length; j++)	{
				if ((list[j].compareTo(list[minLoc]) > 0))	{
					minLoc = j;
				}
			}
			swap(list, i, minLoc);
		}
	}
	public static <T extends Comparable<? super T>> void insertionSort(T[] list)	{
		for (int i = 0; i<list.length-1; i++)	{
			for (int j = i+1; j!=0; j--)	{
				if ((list[j].compareTo(list[j-1]) > 0))	{
					swap(list, j, j-1);
				}
			}
		}
	}
	public static <T extends Comparable<? super T>> void mergeSort(T[] list)	{
		mergeSort(list, 0, list.length-1);
	}
	public static <T extends Comparable<? super T>> void mergeSort(T[] list, int start, int end)	{
		if (start == end)	{
			return;
		}
		int midpoint = (start + end)/2;
		mergeSort(list, start, midpoint);
		mergeSort(list, (midpoint + 1), end);
		T [] temp = (T[]) new Object[end-start+1];
		int i = start;
		int pos = 0;
		int j = midpoint + 1;
		while (i < midpoint + 1 || j < end + 1)	{
			if (i<midpoint+1 && j<end+1)	{
				if ((list[i].compareTo(list[j]) < 0))	{
					temp[pos] = list[i];
					i++;
					pos++;
				}
				else	{
					temp[pos] = list[j];
					j++;
					pos++;
				}
			}
			else if (i == midpoint + 1)	{
				temp[pos] = list[j];
				j++;
				pos++;
			}
			else if (j == end + 1)	{
				temp[pos] = list[i];
				i++;
				pos++;
			}
		}
		match(list, temp, start, end);
		return;
	}

	public static void main (String args [])	{
		//testing
		/**
		 * Run all 5 sorts on sorted, reverse sorted, and random inputs of different sizes 
		 * (10, 100, 1000, 10000, 100000, 1000000). For small inputs, you should run the experiment many times and 
		 * divide the timing results by the number of iterations (i.e. compute the time required to sort 1000 random 
		 * lists of size 10, then divide by 1000). Plot the results. This should result in 21 curves (Bubble, Selection, 
		 * Insertion, Merge, and three Quick) x (sorted, reverse sorted, and random). Draw some conclusions from your 
		 * results.
		 */
		long time=0; 
		int max=3;
		Integer [] newArray = {4, 7, 2, 3, 6, 4, 7, 2, 1, 99};
		int j=0;
		/**
		 * Sorting lists with random inputs [[first for loop is iterating smaller lists]]
		 */
//		for(int k=0; k<1000;k++){ //iterations for smaller inputs 10,100,1000
//		for(int i=0; i<max; i++){
//			newArray[i]=(int) (Math.random()*1000000);  //random
//		}
		
		/**
		 * Sorting lists with sorted inputs [[first for loop is iterating smaller lists]]
		 */
//				//for(int k=0; k<1000;k++){ //iterations for smaller inputs 10,100,1000
//				for(int i=0; i<max; i++){
//				     newArray[i]= i; //sorted
//				}
		
		/**
		 * Sorting lists with reverse sorted inputs [[first for loop is iterating smaller lists]]
		 */
//				//for(int k=0; k<1000;k++){ //iterations for smaller inputs 10,100,1000
//				for(int i=max-1; i>=0; i--){ //reverse sorted
//				     newArray[j]= i; //reverse sorted
//				     j++;//reverse
//		}
//				j=0; //reverse reset to avoid out of bounds exception
//		
		//System.out.println("Beginning sort");
		long startTime = System.nanoTime(); 
		bubbleSort(newArray);
		long estimatedTime = System.nanoTime() - startTime;
		time+=estimatedTime;
		//System.out.println("Done with sort");
		//}//end bracket - iteration for loop for small [[inputs 10,100,1000]]
		System.out.println("time in ns: " + time); // divide by 1000 for smaller inputs
		print(newArray);
				
	}
}