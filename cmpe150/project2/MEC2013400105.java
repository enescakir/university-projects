import java.util.Scanner; // We need to import Scanner class, because we want read data from user

public class MEC2013400105 {
	
	/* 
	 * This method is our main method, so it says to compiler what it needs execute.
	 */
	public static void main(String[] args) {
		
		Scanner keyboard = new Scanner(System.in); //keyboard is used for taking input from user.
		String board = "ABGGRTFPPKVIGVJ*"; // board is variable that keeps board configuration. As default, it is keeps default board configuration.
		
		System.out.println("Welcome to this weird game of P*G?");
		System.out.print("Do you want to use the default board configuration? ");
		
		if(keyboard.next().equals("No")){ // Does user wants default configuration or not?
			
			board = ""; // User didn't want default configuration. So I reset board.
			for(int i = 1; i <= 4; i++){ // This for loop manage number of lines in board.
				System.out.printf("Enter row %d of the board: ",i );
				board += keyboard.next(); //It adds up new lines to new board configuration.
			}
			System.out.println();	
		}
		
		play(board, keyboard); // Let's play
	}

	/* This method controls the entire game. It takes 2 parameters.
			board: Current board configuration
			keyboard: Used for taking input from user
	*/
	public static void play(String board, Scanner keyboard) {
		
		int score = 0; // score: user's current total score. At the beginning of the it is 0, because user hasn't played yet.
		
		printBoard(board); // Print board at the beginning. 
		System.out.print("\nHow many moves do you want to make? ");
		int movesCount = keyboard.nextInt(); // movesCount keeps number of moves that user wants, it comes from user input.
		
		int possibleMax = calcMaxScore(board , movesCount); // possibleMax stores possible maximum score from initial board.
		
		System.out.println("Make a move and press enter. After each move, the board configuration and your total points will "
				+ "\nbe printed. Use A for Left, S for Right, W for Up, and Z for Down.\n");
		for(int i = 0; i < movesCount; i++){ // This for loop manage turns of game. It iterate movesCount times.
			
			String move = keyboard.next(); // move holds movement that user wants, so it comes from user input. It should be 'A','W','S', or 'Z'
			
			int index = board.indexOf('*'); // index stores current position of the star('*')
			int targetIndex = calcTargetIndex(move, index); // targetIndex stores index that user want to move
			score = calcScore(board, targetIndex, score); //  We add our new gained score to total score
			board = makeMove(board, targetIndex, index); // We make movement, and change board configuration.
			
			printBoard(board);
			System.out.printf("\nYour total score is %d.\n\n", score);// Print current board configuration
		
		}
		System.out.println("Thank you for playing this game.");
		System.out.printf("Possible maximum point with %d moves is %d." , movesCount, possibleMax);
	
	}
	
	/* This method calculates target index and return it. Target index is index that user want to move .
		It takes 2 parameters.
			move: Movement that user want to do
			index: current position of the star('*')
		It returns target index as an integer.
	 */
	public static int calcTargetIndex(String move, int index){
		
		// This if-else if tree checks movements that user wants
		if(move.equals("A")){ // User wants move left
			if(index % 4 != 0) // If our star is already at first column, we can't move left anymore.
				return index - 1; // If not, decrease index by 1
		}
		else if(move.equals("S")){ // User wants move right
			if(index % 4 != 3)  // If our star is already at last column, we can't move right anymore.
				return index + 1; // If not, increase index by 1
		}
		else if(move.equals("W")){ // User wants move up
			if(index > 3)  // If our star is already at first row, we can't move up anymore.
				return index - 4; // If not, decrease index by 4, so go up
		}
		else if(move.equals("Z")){ // User wants move down
			if(index < 12) // If our star is already at last row, we can't move down anymore.
				return index + 4; // If not, increase index by 4, so go down
		}
		
		return index;  // If user enter unvalid movement, do not anything. Just return same index.
	}

	/* This method swaps our star with the character on target index. Target index is index that user want to move .
		It takes 3 parameters.
			oldBoard: Our current board configuration
			targetIndex: Index that we want to move our star('*') to
			index: current position of the star('*')
		It returns new board configuration as an string.
	 */
	public static String makeMove(String oldBoard, int targetIndex, int index){
		
		String newBoard = ""; // newBoard: It will be store our new board configuration, at the beginning it is empty.
		for(int i = 0; i < 16; i++){ //This for loop comes over every character on our board configuration.
			if(i == targetIndex) // When our i came target index, we add star(*) to new board instead of character that was here on old board 
				newBoard += "*";
			else if(i == index){ // When i came our star's index that on old board
				if(oldBoard.charAt(targetIndex) == 'G' || oldBoard.charAt(targetIndex) == 'P') // If the character on this index is G or P, we add I to new board
					newBoard += "I";
				else // If the character on this index is not G or P, we add the character on target index to new board
					newBoard += oldBoard.charAt(targetIndex);
			}
			else  // If nothing special, we add the character on i to new board
				newBoard += oldBoard.charAt(i);
		}
		return newBoard; // Finally return new board configuration
	}
		
	/* This method calculates new total score and return it.
		It takes 3 parameters.
			board: Our current board configuration
			targetIndex: Index that we want to move our star('*') to
			score: Our current score
		It returns new total score as an integer.
	 */
	public static int calcScore(String board, int targetIndex, int score){
		if(board.charAt(targetIndex) == 'G') // If the character on this target index is G, we add 5 to our score
			score += 5;
		else if(board.charAt(targetIndex) == 'P') // If the character on this target index is P, we add 1 to our score
			score += 1;

		return score; // Return new total score
	}
	
	/* This method prints the board. It doesn't return something.
		It takes 1 parameter.
			board: Our current board configuration
	 */
	public static void printBoard(String board) {
		System.out.println("This is the board configuration now:");
		for(int i = 0; i < 4; i++){ // This for loop manages line number
			System.out.println(board.substring(i * 4, (i + 1) * 4)); // Print next 4 characters
		}
	}
	
	/* This method calculate possible maximum score and return it.
		It takes 2 parameters.
			board: Our current board configuration
			movesCount: number of moves that user wants
		It returns possible maximum score as an integer.
	 */
	public static int calcMaxScore(String board, int movesCount ) {
		if (movesCount == 0) // When number of moves equals to 0, it should be return 0, otherwise it enters endless loop.
			return 0;

		String tempBoard; // I don't want to change my initial board because I need it. So I create a tempBoard and save new boards in it.
		int maxScore = 0; // maxScore keeps possible maximum score.
		int index = board.indexOf('*'); // index stores current position of the star('*')
		
		// Section 1,2,3, and 4 almost same in this method. So I explain just one of them.
		/* Section 1 */
		int targetIndex = calcTargetIndex("W", index); // targetIndex stores index of upper cell
		int upScore = calcScore(board, targetIndex, 0); // upScore holds score that up movement bring to me
		tempBoard = makeMove(board, targetIndex, index); // tempBoard stores my new board configuration
		maxScore = Math.max(maxScore, upScore + calcMaxScore(tempBoard, movesCount - 1 )); // call same method again and calculated rest movements maximum scores, choose higher one, and set it to maxScore 
				
		/* Section 2 */
		targetIndex = calcTargetIndex("A", index);
		int rightScore = calcScore(board, targetIndex, 0);
		tempBoard = makeMove(board,targetIndex, index);
		maxScore = Math.max(maxScore, rightScore + calcMaxScore(tempBoard, movesCount - 1 ));

		/* Section 3 */
		targetIndex = calcTargetIndex("S", index);
		int leftScore = calcScore(board, targetIndex, 0);
		tempBoard = makeMove(board, targetIndex, index);
		maxScore = Math.max(maxScore, leftScore + calcMaxScore(tempBoard, movesCount - 1 ));

		/* Section 4 */
		targetIndex = calcTargetIndex("Z", index);
		int downScore = calcScore(board, targetIndex, 0);
		tempBoard = makeMove(board, targetIndex, index);
		maxScore = Math.max(maxScore, downScore + calcMaxScore(tempBoard, movesCount - 1 ));
		
		return maxScore;
	}
}
