package homework04;
/**

@author: Adam Rosenberg
Solves a sudoku puzzle

**/

import java.awt.Component;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

import javax.swing.JFileChooser;

public class SudokuSolver {	//The class SudokuSolver stores a SudokuSolver object that has the starting puzzle, the current puzzle, and the amount of times we have backtracked.
	public int [][] beginningPuzzle = new int [9][9];
	public int [][] currentPuzzle = new int [9][9];
	public int backtrack = 0;
	//Turns out, when you set two arrays equal to each other, you actually make them point to the same place.  Who knew?  I didn't.
	private SudokuSolver(int [][] puzzle)	{	//Actually sets the beginningPuzzle and currentPuzzle arrays equal to the value of the puzzle array.
		for (int i = 0; i<9; i++)	{
			for (int j = 0; j<9; j++)	{
				this.beginningPuzzle[i][j] = puzzle[i][j];
				this.currentPuzzle[i][j] = puzzle[i][j];
			}
		}
	}
	public int GetBlock(int i, int j)	{return (i/3)*3+(j/3)+1;}	//Gets the block an entry at position i,j is located in.  Uses the way division of integers works to calculate it in one line.
	public boolean Check(int i, int j)	{	//Checks whether or not there is a match between a specific entry and any on its row, column, or block.
		for (int checkColumn = 0; checkColumn<9; checkColumn++)	{
			if (this.currentPuzzle[checkColumn][j] == this.currentPuzzle[i][j] && checkColumn !=i)	{return false;}
		}
		for (int checkRow = 0; checkRow<9; checkRow++)	{
			if (this.currentPuzzle[i][checkRow] == this.currentPuzzle[i][j] && checkRow !=j)	{return false;}
		}
		for (int row = 0; row<9; row++)	{
			for (int column = 0; column<9; column++)	{
				if (GetBlock(row, column) == GetBlock(i, j) && this.currentPuzzle[row][column] == this.currentPuzzle[i][j] && (row != i || column != j))	{return false;}
			}
		}
		return true;
	}
	public boolean Solve(int i, int j)	{//Solves the Sudoku puzzle, starting at a specific entry.
		int nexti;
		int nextj;	//nexti and nextj stores at what location we should solve for next.
		if (j == 8)	{nexti = i+1; nextj = 0;}	//If we're on the edge, the next place we should solve is on the next row and on the right side.
		else		{nexti = i; nextj = j+1;}	//Otherwise, we just solve at the next column, same row.
		if (this.currentPuzzle[i][j] !=0 && (i !=8 && j !=8))	{return Solve(nexti, nextj);}	//If there's already a value on the spot we want to solve at, and we aren't at the end, we just start solving from the next entry.
		else for (int k=1; k<10; k++)	{	//Otherwise, we start solving from this entry.
			this.currentPuzzle[i][j] = k;	//Start by setting the value at our entry to be 1, then 2, then 3, and so on.
			if (Check(i, j))	{	//Check to see if our new value matches any it shouldn't.  If not, start solving from the next value.  If so, plug in a new value and check it.
				if (j == 8 && i == 8)	{return true;}	//If we're at the end and nothing matches anything else, we must be done.
				else if (Solve(nexti, nextj))	{return true;}	//If we can solve from the next value, we are done.  If not, redo the loop, plugging in a new value.
			}	
		}
		this.backtrack++;	//We only get here if no values worked.  So, we must backtrack.
		this.currentPuzzle[i][j] = 0;	//We couldn't solve this value yet, so we set it back to 0.
		return false;	//False means we failed to solve it at this point.
	}
	public static void main (String args []) throws FileNotFoundException {
		final JFileChooser fc = new JFileChooser();	
		Component aComponent = null;
		int returnVal = fc.showOpenDialog(aComponent);
		File file = fc.getSelectedFile();
		Scanner scanner = new Scanner(file);	//Above code has the user choose a file then starts reading from it.
		int [][] puzzle = new int[9][9];	//Puzzle is where we store the initial puzzle.
		for (int i = 0; i<9; i++)	{	//Stores the puzzle in the chosen text file into a puzzle array.
			for (int j = 0; j<9; j++)	{
				String current = scanner.next();
				if (current.compareTo(".") == 0) {puzzle[i][j] = 0;}
				else {puzzle[i][j] = Integer.parseInt(current);}
				}
			}
		SudokuSolver Sudoku = new SudokuSolver(puzzle);	//Creates a new SudokuSolver object and stores the puzzle array inside it.
		Sudoku.Solve(0, 0);	//Solves the Sudoku puzzle using the solve method.
		//Outputs the unsolved and solved puzzles as well as the number of backtracking steps needed.
			System.out.println("Your starting puzzle is:");
			System.out.println("");
			for (int i = 0; i<9; i++)	{
				System.out.print("   ");
				for (int j = 0; j<9; j++)	{
					if (Sudoku.beginningPuzzle[i][j] == 0)	{System.out.print(". ");}
					else	{System.out.print(Sudoku.beginningPuzzle[i][j] +  " ");}
					if ((j+1)%3 == 0)	{System.out.print(" ");}
				}
				System.out.println("");
				if ((i+1)%3 == 0)	{System.out.println("");}
			}
			System.out.println("And your solved puzzle is:");
			System.out.println("");
			for (int i = 0; i<9; i++)	{
				System.out.print("   ");
				for (int j = 0; j<9; j++)	{
					System.out.print(Sudoku.currentPuzzle[i][j] + " ");
					if ((j+1)%3 == 0)	{System.out.print(" ");}
				}
				System.out.println("");
					if ((i+1)%3 == 0)	{System.out.println("");}
			}
			System.out.println("Number of backtracking steps: " + Sudoku.backtrack);
		}	
	}
