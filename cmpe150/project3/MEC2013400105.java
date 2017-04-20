import java.io.*;
import java.util.*;

public class MEC2013400105 {
	
	/* 
	 * This method is our main method, so it says to compiler what it needs execute.
	 */
	public static void main(String[] args) throws FileNotFoundException {
		char[][] board = new char[18][18]; // board is variable that keeps board configuration.
		// Although our board is 16x16, our array is 18x18. Because it is contains 2 rows, 2 columns as outside.
		char[][] oldBoard = new char[18][18]; // oldBoard is variable that keeps previous board configuration.
		
		readFile(board, "input"); // Read the file
		print(board, "INITIAL: "); // Print the initial board and initial counts
		
		// Check to conditions until last two boards are equals
		while(!isEqualArray(board, oldBoard)){
			oldBoard = duplicate(board); // Clone board to oldBoard, and start to change current board.
			
			// This 2 nested for loops comes over every character on our board configuration.
			// i and j starts with 1 and ends with length-1 because edges of our board array filled with O. It represents outside.
			// We don't need to check and change them
			for(int i = 1; i < board.length - 1; i++){
				for(int j = 1; j < board[i].length - 1; j++){
					
					// This if-else if tree checks conditions one by one
					if(isDC(board[i][j])){ // If a DC
						if(countConnectedDC(board, i, j) == 4) //connected to 4 other DC
							board[i][j] = 'B';
						else if(countConnectedDC(board, i, j) == 1 //connected to 3 LC
								|| (countConnectedDC(board, i, j) == 2 && countConnectedChar('P', board, i, j) >= 1 ) // connected to 2 LC and at least 1 P 
								|| (countConnectedDC(board, i, j) == 3 && countConnectedChar('P', board, i, j) >= 2 )) // connected to 1 LC and at least 2 P 
							board[i][j] = 'P';
						else if(board[i][j] != 'B' && board[i][j] != 'P') // If a DC is not B and not P
							board[i][j] = 'C';
					}
					else if(board[i][j] == 'W' && countConnectedChar('O', board, i, j) >= 1 ) // If a W is connected to at least one O
						board[i][j] = 'O';
					else if(board[i][j] == 'O' && ( //If an O
								   (countConnectedChar('Y', board, i, j) >= 2 && countConnectedChar('O', board, i, j) <= 1 ) // connected to at least 2 Y and at most one O 
								|| (countConnectedChar('Y', board, i, j) == 1 && countConnectedDC(board, i, j) >= 2 ) // connected to 1 Y and at least 2 DC
								|| (countConnectedChar('O', board, i, j) >= 1 && countConnectedDC(board, i, j) >= 2 ))) // connected to at least one O and at least 2 DC
						board[i][j] = 'Y';		
				}
			}
		}
		
		// When board is finalized, this nested for loops changed remained W's with L's
		for(int i = 1; i < board.length-  1; i++){
			for(int j = 1; j < board[i].length - 1; j++){
				if(board[i][j] == 'W')
					board[i][j] = 'L';
			}
		}
		print(board, "EXPLORED: "); // Print explored board
	}
	
	/* This method read the text file that with given name, and put characters to board array.
	It takes 2 parameters.
		board: Our board configuration array
		fileName: Name of the file that we want read
	It returns nothing.
	 */
	public static void readFile(char[][] board, String fileName) throws FileNotFoundException{
		File file = new File(fileName + ".txt"); // import file whose name is given
		Scanner scanner = new Scanner(file); // scanner is used for taking input from text file.
		
		// This 2 nested for loops comes over every character on our board, and fill it with O's. 
		for(int i = 0; i < board.length; i++){
			for(int j = 0; j < board[i].length; j++){
				board[i][j] = 'O';
			}
		}
		
		// This 2 nested for loops comes over every character on our board, and filled it with input character
		// i starts with 1 and ends with length - 1 because top/bottom edges of our board array filled with O. It represents outside.
		// We set to our array with board[i][j + 1], not with board[i][j]; because we want to keep right/left edges as O. It represents outside.
		for(int i = 1 ; i < board.length - 1; i++){
			String line = scanner.nextLine(); // It keeps current line of input text
			for(int j = 0; j < line.length(); j++ )
				board[i][j + 1] = line.charAt(j);
		}
	}
	
	/* This method print current board configuration; count and print number of each character
	It takes 2 parameters.
		board: Our current board configuration
		label: The text that written at the beginning of the numbers line
	It returns nothing.
	 */
	public static void print(char[][] board, String label) {
		int[] counter = new int[8]; // It keeps number of each character, It's length is 8 because we have 8 characters
		char[] chars = {'P', 'C', 'B', 'G', 'O', 'Y', 'L', 'W'}; // It keeps all character
		
		// This 2 nested for loops comes over every character on our board configuration.
		// i and j starts with 1 and ends with length-1 because edges of our board array filled with O. It represents outside.
		// We don't need to print and count them
		for(int i = 1; i < board.length - 1; i++){
			for(int j = 1; j < board[i].length - 1; j++){
				System.out.print(board[i][j]); // Print the character character at this position
				// This for loop goes through every index on counter and chars array
				for(int h = 0; h < chars.length; h++){
					if(board[i][j] == chars[h]) // If chars are matched increase corresponding index on counter array by one
						counter[h]++;
				}
			}
			System.out.println(); // Go to the next line at the end of the each row
		}
		System.out.print(label); // Print desired text to the beginning of the counts line.
		
		// This for loop goes through every index on counter and chars array
		for(int i = 0; i < counter.length; i++){
			if(counter[i] != 0) // If number of character is not equal to zero, print it
				System.out.print(chars[i] + "=" + counter[i] + " ");
		}
		System.out.print("ALL=256\n\n"); // Print the total number
	}
	
	
	/* This method checks given character is DC or not.
	It takes a character as parameter.
	It returns situation of character as an boolean.
	 */
	public static boolean isDC(char ch) {
		return ( ch == 'G' || ch == 'B' || ch == 'P' || ch == 'C');
	}
	
	/* This method calculate number of DC connected to given position and return it.
	It takes 3 parameters.
		board: Our current board configuration
		i: row number
		j: column number
	It returns number of connected DC as an integer.
	 */
	public static int countConnectedDC(char[][] board, int i, int j) {
		int count = 0; // It keeps number of connected DC
		// Check around given position. If one of them is DC increase count by one.
		if( isDC( board[i - 1][j] )) count++;
		if( isDC( board[i + 1][j] )) count++;
		if( isDC( board[i][j - 1] )) count++;
		if( isDC( board[i][j + 1] )) count++;
		return count; // Return number of connected DC 
	}
	
	/* This method calculate number of the given character connected to given position and return it.
	It takes 4 parameters.
		ch: Character that we want to count
		board: Our current board configuration
		i: row number
		j: column number
	It returns number of given character as an integer.
	 */
	public static int countConnectedChar(char ch, char[][] board, int i, int j) {
		int count = 0; // It keeps number of given character around given position
		if( board[i - 1][j] == ch ) count++;
		if( board[i + 1][j] == ch ) count++;
		if( board[i][j - 1] == ch ) count++;
		if( board[i][j + 1] == ch ) count++;
		return count; // Return number of given character
	}
	
	
	/* This method duplicates our board to new variable and return new variable
	It takes 1 parameters.
		oldBoard: Our current board configuration
	It returns board as a new 2d char array variable.
	 */
	public static char[][] duplicate(char[][] oldBoard) {
		char[][] newBoard = new char[18][18]; // It keeps board in new variables
		
		//This nested for loops  comes over every character on our board configuration and copy them to new board variable.
		for(int i = 0; i < oldBoard.length; i++){
			for(int j = 0; j < oldBoard[i].length; j++)
				newBoard[i][j] = oldBoard[i][j];
		}
		return newBoard; // return new board variable
	}
	
	/* This method checks 2 board are equal or not, and return result
	It takes 2 boards as parameter, and check their equality
	It returns result of the equality as boolean .
	 */
	public static boolean isEqualArray(char[][] board1, char[][] board2) {
		
		//This nested for loops  comes over every character on our board configuration
		for(int i = 0; i < 18; i++){
			for(int j = 0; j < 18; j++)
				if(board1[i][j] != board2[i][j])
					return false; //If characters on that position aren't equal, return false 
		}
		return true; // If for loops didn't return false, boards are equal and return true
	}
}