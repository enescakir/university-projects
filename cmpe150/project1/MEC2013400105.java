
public class MEC2013400105 {

	public static final int N = 4; // It keeps given n variable. 
	public static final int M = 23415; // It keeps given m variable
	
	/* 
	 * This method is our main method, so it says to compiler what it needs execute.
	 */
	public static void main(String[] args) {
		
		problem1();
		System.out.println();
		problem2();
		
	}
	
	/* 
	 * This method solves first problem. It creates kind of multiplication table for the kids. It is goes up to n*n.
	 */
	public static void problem1(){
		
		// First Loop: It used for managing number of lines, so i is line number.
		for(int i = 1; i <= N; i++){
			// Second Loop: It used for managing number of columns and printing result , so j is column number.
			for(int j = 1; j <= i; j++){
				System.out.print(i * j + " ");
			}
			System.out.println();
		}
		
	}
	
	/* 
	 * This method solves second problem. It calculate product of the number of digits in the previous number and n,
	 * and do it for 4 times.
	 */
	public static void problem2(){
		int pNumber = M; //  pNumber is the previous number.
		
		// Third Loop: It used for making 4 iterations and print result for each iteration.
		for(int i = 1; i <= 4; i++){
			int digits = 0; // digits is the number of digits in the previous number.
			
			// Forth Loop: It used for calculating the number of digits in the previous number.
			for(int j = pNumber; j > 0; j = j / 10){
				digits++;
			}
			pNumber = digits * N;
			System.out.println(pNumber);
		}
	}
}
