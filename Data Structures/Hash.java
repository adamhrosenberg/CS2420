package homework09;

/* Authors:  Adam Rosenberg November 17, 2014
 */

import java.util.Scanner;
import java.util.ArrayList;
import java.awt.Component;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.JFileChooser;

import homework09.DoubleLinkedList.ListNode;
import homework09.Heap;

public class Hash {	//The class used to do everything hash-related.
	int capacity;	//How many spots are available
	int size;	//How many spots are used up
	double loadFactor = .5;	//If capacity / size exceeds this value, we expand our arrays.
	int probe;	//The type of probing done.  0 for linear probing, 1 for quadratic probing and 2 for separate chaining.
	int type;	//The type of hash function being used.  0 for the bad function, 1 for the okay function and 2 for the good function.
	int collisions;	//Remembers the amount of colliisons done.
	String [] buckets;	//Stores the hashed values.
	int [] counters;	//Stores how many times a particular value has been hashed.
	ArrayList<DoubleLinkedList<String>> bucketsList;	//Stores the hashed values in the case of separate chaining.
	ArrayList<DoubleLinkedList<Integer>> countersList;	//Stores how many times a particular value has been hashed in the case of separate chaining.
	public Hash(int probe, int type)	{
		capacity = 37;	//Start with a capacity of 37.
		size = 0;
		this.probe = probe;	//Remembers the probe type.
		this.type = type;	//Remembers the hash function type.
		if (probe == 2)	{	//Sets things up in the case of separate chaining.
			bucketsList = new ArrayList<DoubleLinkedList<String>>();	//bucketsList is an ArrayList of linked lists.
			for (int i = 0; i < capacity; i++)	{
				bucketsList.add(new DoubleLinkedList<String>());
			}
			countersList = new ArrayList<DoubleLinkedList<Integer>>();	//counterList is an ArrayList of linked lists.
			for (int i = 0; i < capacity; i++)	{
				countersList.add(new DoubleLinkedList<Integer>());
			}
		}
		else	{	//Sets things up for linear or quadratic probing.
			buckets = new String[capacity];	//buckets is a String array.
			counters = new int[capacity];	//counters is an int array.
			for (int i = 0; i < capacity; i++)	{	//Initialize the values of the capacity array.
				counters[i] = 0;
			}
		}
	}
	
	public static <AnyType> ArrayList<DoubleLinkedList<AnyType>> cloneList(ArrayList<DoubleLinkedList<AnyType>> list)	{	//Function used to clone an ArrayList of linked lists.
		ArrayList<DoubleLinkedList<AnyType>> clone = new ArrayList<DoubleLinkedList<AnyType>>();
		for (int i = 0; i < list.size(); i++)	{	//For each entry in the array, we clone the linked list in the array.
			ListNode currentNode = list.get(i).header.next;	//Start at the first value...
			DoubleLinkedList<AnyType> temp = new DoubleLinkedList<AnyType>();
			while (currentNode != null && currentNode.next != null)	{	//And clone!
				temp.insertEnd((AnyType) currentNode.data);
				currentNode = currentNode.next;
			}
			clone.add(temp);	//Add our linked list to the ArrayList.
		}
		return clone;	//Returns our cloned list.
	}
	
	public static boolean ifPrime(int data)	{	//Quick and dirty way to check if a number is prime.
		for (int i = 2; i < Math.sqrt(data); i++)	{	//Checks up to the square root of the data, because we don't need to go beyond there.
			if (data%i == 0)	{	//If we find a divisor, clearly not prime.
				return false;
			}
		}
		return true;	//If we found no divisors, clearly prime.
	}
	public void extend()	{	//Extends our hash when it gets too full.
		collisions = 0;
		int oldCapacity = capacity;
		size = 0;
		capacity = 2*capacity + 1;
		while(!ifPrime(capacity))	{	//Find a prime.
			capacity = capacity + 2;
		}
		if (probe != 2)	{	//Separate cases for probing and separate chaining (you'll be seeing this a lot, sorry).
			String [] temp = buckets.clone();	//Temp has the old values of buckets.
			buckets = new String[capacity];	//buckets is grown.
			int [] temp2 = new int[capacity];
			for (int i = 0; i < oldCapacity; i++)	{
				temp2[i] = counters[i];
			}
			counters = new int[capacity];
			for (int i = 0; i < capacity; i++)	{
				counters[i] = 0;
			}
			for (int i = 0; i < temp.length; i++)	{
				if (temp[i] != null)	{
					rehash(temp[i], temp2[i]);	//Everything is rehashed.
				}
			}
		}
		else	{	//Special code for the separate chaining.  Similar to above.
			ArrayList<DoubleLinkedList<String>> temp = cloneList(bucketsList);
			bucketsList = new ArrayList<DoubleLinkedList<String>>();
			for (int i = 0; i < capacity; i++)	{
				bucketsList.add(new DoubleLinkedList<String>());
			}
			ArrayList<DoubleLinkedList<Integer>> temp2 = cloneList(countersList);
			countersList = new ArrayList<DoubleLinkedList<Integer>>();
			for (int i = 0; i < capacity; i++)	{
				countersList.add(new DoubleLinkedList<Integer>());
			}
			for (int i = 0; i < temp.size(); i++)	{
				if (temp.get(i) != null)	{
					rehash(temp.get(i), temp2.get(i));
				}
			}
		}
	}	
	public void print()	{	//Prints things.
		if (probe != 2)	{	//Prints in the case of linear probing.
			for (int i = 0; i < capacity; i++)	{
				System.out.println(buckets[i]);
			}
		}
		else	{	//Prints in the case of separate chaining, showing all contents of each bucket.
			for (int i = 0; i < capacity; i++)	{
				ListNode currentNode = bucketsList.get(i).header.next;
				if (currentNode == null)	{
					System.out.println("null.");
				}
				while(currentNode != null && currentNode.data != null)	{
					if (currentNode.next.data != null)	{
						System.out.print(currentNode.data + ", ");
					}
					else {
						System.out.println(currentNode.data + ".");
					}
					currentNode = currentNode.next;
				}
			}
		}
	}
	
	public void insert(String data, int place, int counter)	{	//Inserts data into our hash.  data is the data to be entered, place is the place we desire to put it, and counter is used to store how many times this has been hashed if we are rehashing.
		place = place % capacity;	//Makes the code work.  Shouldn't be necessary but is.
		if (probe != 2)	{	//Probing.
			if (buckets[place] != null && buckets[place].equals(data))	{	//If our data matches the data already in the bucket, increment the counter.
				if (counter == 0)	{	//If rehashing, counter != 0, in which case we remember how many times it's previously been counted.
					counters[place]++;
				}
				else	{
					counters[place] = counter;
				}
				return;
			}
			else if (buckets[place] == null)	{	//If we're at an empty spot, put our data here.
				buckets[place] = data;
				size++;
				if (counter == 0)	{
					counters[place]++;
				}
				else	{
					counters[place] = counter;
				}
			}
			else if (probe == 0)	{	//If we're not at an empty spot, go to the next spot for linear probing, and try again.
				collisions++;
				insert(data, place + 1 % capacity, counter);
			}
			else if (probe == 1)	{	//If we're not at an empty spot and are quadratically probing, start to quadratically probe.
				collisions++;
				quadInsert(data, place, 1, counter);
			}
			if ((double) size / capacity >= loadFactor)	{	//If our size is too high compared to capacity, expand the hash table.
				extend();
			}
		}
		else	{	//Code for separate chaining.
			boolean insert = true;	//Gets set to false if we find our data.
			if (bucketsList.get(place).header.next == null)	{
				size++;
				collisions--;	//Cheeky way to keep collisions the same if we will add to a null list.
			}
			else	{
				ListNode currentNode = bucketsList.get(place).header.next;	//currentNode traverses our bucketsList...
				ListNode currentNode2 = countersList.get(place).header.next;	//while currentNode2 traverses our countersList/
				while(currentNode != null && currentNode.data != null)	{	//While we still can travel
					if (currentNode.data.equals(data))	{	//If we've found our data, increment the appropriate counterList and set insert to false.
						currentNode2.data = (int) currentNode2.data + 1;
						insert = false;
					}
					currentNode = currentNode.next;
					currentNode2 = currentNode2.next;
				}
			}
			if (insert)	{	//If we should insert, do so.
				collisions++;
				bucketsList.get(place).insertEnd(data);
				countersList.get(place).insertEnd(1);
			}
			if ((double) size / (double) capacity >= loadFactor)	{	//If we should extend, do so.
				extend();
			}	
		}
	}
	
	public void quadInsert(String data, int place, int n, int counter)	{	//Special code for when we need to do quadratic probing.
		if (buckets[place].equals(data))	{	//If we've found our data, increment counters and then stop.  Hammertime.
			if (counter == 0)	{
				counters[place]++;
			}
			else	{
				counters[place] = counter;
			}
			return;
		}
		int s = n*n;
		place = (place + s) % capacity;
		if (buckets[place] == null)	{	//If we can insert a value, do so.
			buckets[place] = data;
			size++;
		}
		else	{	//Oterwise, try again.
			n++;
			collisions++;
			quadInsert(data, place, n, counter);
		}
	}
	
	public int find(String data)	{	//Our finding code.  Starts looking where the data would be hashed, then continues on according to the probing type.
		if (type == 0)	{
			return find(data, 0, 0, true);
		}
		else if (type == 1)	{
			int place = (int) (data.charAt(0) + data.charAt(data.length()-1) - 128) % capacity;
			if (place < 0)	{
				place = place + capacity;
			}
			return find(data, place, place, true);
			}
		else	{
			int place = 0;
			for (int i = 0; i < data.length(); i++)	{
				place = place + (int) (data.charAt(i) - 64) % capacity;
			}
			place = place*place % capacity;
			if (place < 0)	{
				place = place + capacity;
			}
			return find(data, place, place, true);
		}
	}
	
	public int find(String data, int place, int start, boolean starting)	{	//Looks for data.  data is the data we look for, place is the place we're looking for it, start is where we started looking for it, and starting lets us know that we've just started looking, so it's ok if start and place are the same.
		if (probe == 2)	{	//Separate chaining code first because I'm a rebel.
			ListNode currentNode = bucketsList.get(place).header.next;	//currentNode traverses bucketsList.
			while(currentNode != null && currentNode.data != null)	{	//While we can traverse, do so.
				if (currentNode.data.equals(data))	{
					return place;	//If we find our data, return which bucket it was placed in.
				}
				currentNode = currentNode.next;
			}
			return -1;	//Return -1 means we did not find anything.
		}
		if (place == start && starting == false)	{	//If we're at where we started and didn't just start looking, it must not be there.
			return -1;
		}
		if (data.equals(buckets[place]))	{	//If we found our data, return the place it's in.
			return place;
		}
		else if (probe == 0)	{	//If not, keep looking.
			return find(data, (place+1)%capacity, 0, false);
		}
		else if (probe == 1)	{	//If quadratc probing, start using the quadfind method.
			return quadFind(data, place, 1, 0);
		}
		else	{	//Shouldn't be accessable unless you initialize hash wrong.
			return -1;
		}
	}
	
	public int quadFind(String data, int place, int n, int start)	{	//data is what we're looking for, place is where we're looking, n is how many times we've looked, and start is where we started looking.
		int s = n*n % capacity;	//Quadratic probing!
		if (n == 0)	{	//If n becomes 0 we haven't been able to find it.
			return -1;
		}
		else {	//Otherwise, keep looking.
			place = (place + s) % capacity;	//Look at the new place.
			if (data.equals(buckets[place]))	{	//If you found it, hooray!
				return place;
			}
			else	{	//Otherwise, increment n and try again.
				n = (n + 1) % capacity;
				return quadFind(data, place, n, start);
			}
		}
	}
	
	public void delete(String data)	{	//Finds data, then deletes it.
		if (probe != 2)	{
			buckets[find(data)] = null;
			size--;
		}
		else	{
			ListNode currentNode = bucketsList.get(find(data)).header.next;
			while (currentNode != null && currentNode.data != null)	{
				if (currentNode.data.equals(data))	{
					bucketsList.get(find(data)).remove(currentNode);
				}
				currentNode = currentNode.next;
			}
		}
	}
	
	public void hash(String data)	{	//Depending on what type of hash you initialized, you go to the appropriate hash functoin.
		if (type == 0)	{
			badHash(data, 0);
		}
		if (type == 1)	{
			okayHash(data, 0);
		}
		if (type == 2)	{
			goodHash(data, 0);
		}
	}
	
	public void rehash(String data, int counter)	{	//Rehash needs its own code because counter needs to now be stored again.
		if (type == 0)	{
			badHash(data, counter);
		}
		if (type == 1)	{
			okayHash(data, counter);
		}
		if (type == 2)	{
			goodHash(data, counter);
		}
	}
	public void rehash(DoubleLinkedList<String> data, DoubleLinkedList<Integer> counter)	{	//Hashing for double linked lists.
		if (type == 0)	{
			badHash(data, counter);
		}
		if (type == 1)	{
			okayHash(data, counter);
		}
		if (type == 2)	{
			goodHash(data, counter);
		}
	}
	
	public void badHash(String data, int counter)	{	//Tries to store everything at the beginning.  Terrible.  Can't handle all the data due to recursion problems.
		insert(data, 0, counter);
	}
	
	public void okayHash(String data, int counter)	{	//Takes the first and last letters of our word and adds them mod capacity.  Works fairly well.
		int place = (int) (data.charAt(0) + data.charAt(data.length()-1) - 128) % capacity;
		if (place < 0)	{
			place = place + capacity;
		}
		insert(data, place, counter);
	}
	public void goodHash(String data, int counter)	{	//Adds all the letters of our word and then squares it mod capacity.  Works quite well.
		int place = 0;
		for (int i = 0; i < data.length(); i++)	{
			place = place + (int) (data.charAt(i) - 64) % capacity;
		}
		place = place*place % capacity;
		if (place < 0)	{
			place = place + capacity;
		}
		insert(data, place, counter);
	}
	public void badHash(DoubleLinkedList<String> data, DoubleLinkedList<Integer> counter)	{	//Simply runs badHash for each value in the nodes.
		ListNode currentNode = data.header.next;
		ListNode counterNode = counter.header.next;
		while (currentNode != null && currentNode.next != null)	{
			badHash((String) currentNode.data, (int) counterNode.data);
			currentNode = currentNode.next;
		}
	}
	public void okayHash(DoubleLinkedList<String> data, DoubleLinkedList<Integer> counter)	{	//Simply runs okayHash for each value in the nodes.
		ListNode currentNode = data.header.next;
		ListNode counterNode = counter.header.next;
		while (currentNode != null && currentNode.next != null)	{
			okayHash((String) currentNode.data, (int) counterNode.data);
			currentNode = currentNode.next;
		}
	}
	public void goodHash(DoubleLinkedList<String> data, DoubleLinkedList<Integer> counter)	{	//Simply runs goodHash for each value in the nodes.
		ListNode currentNode = data.header.next;
		ListNode counterNode = counter.header.next;
		while (currentNode != null && currentNode.next != null)	{
			goodHash((String) currentNode.data, (int) counterNode.data);
			currentNode = currentNode.next;
		}
	}
	
	public static void main(String args[]) throws FileNotFoundException {
		Hash hash00 = new Hash(0, 0);
		Hash hash01 = new Hash(0, 1);
		Hash hash02 = new Hash(0, 2);
		Hash hash10 = new Hash(1, 0);
		Hash hash11 = new Hash(1, 1);
		Hash hash12 = new Hash(1, 2);
		Hash hash20 = new Hash(2, 0);
		Hash hash21 = new Hash(2, 1);
		Hash hash22 = new Hash(2, 2);
		final JFileChooser fc = new JFileChooser();	
		Component aComponent = null;
		int returnVal = fc.showOpenDialog(aComponent);
		File file = fc.getSelectedFile();
		Scanner scanner = new Scanner(file);
		for (String s = scanner.next(); scanner.hasNext(); s = scanner.next()) {
				s = s.replaceAll("\\W", "");  // get rid of non-"word" characters
				s = s.toLowerCase(); // make the string lower case
				if (s.length() == 0 || s.matches(".*\\d.*")) continue;  // skip strings of zero length or that contain numbers
//				hash00.hash(s);
//				hash01.hash(s);
//				hash02.hash(s);
//				hash10.hash(s);
				hash11.hash(s);
				hash12.hash(s);
//				hash20.hash(s);
//				hash21.hash(s);
//				hash22.hash(s);
		}
		Heap heap12 = new Heap();
		for (int i = 0; i < hash12.capacity; i++)	{
			if (hash12.buckets[i] != null)	{
				heap12.insertMatch(hash12.counters[i], hash12.buckets[i]);
			}
		}
		heap12.printMatch();
		System.out.println(hash12.collisions);
	}
}
