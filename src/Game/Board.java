package Game;

import java.util.Random;

import Elfor.MyArrays;

public class Board {
	private int width;
	private Block[][] board;
	public int[] emptyBlock = new int[2]; //empty block coordinates 0-y 1-x
	boolean isSolved;

	//creates a square board and sets the empty block to the last one
	public Board(int width) {
		System.out.println("Creating New Board...");
		
		this.width = width;
		board = new Block[width][width];
		for(int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				board[i][j] = new Block();
			}
		}
		emptyBlock[0] = width - 1;
		emptyBlock[1] = width - 1;
		board[width-1][width-1].setEmpty();
	}
	
	public Block[][] getBoard(){
		return board;
	}

	//simple swap with the empty block
	public void moveBlock(int y,int x) {
		if(!canBlockMove(x,y)) return;

		Block tmpBlock = board[emptyBlock[0]][emptyBlock[1]];
		board[emptyBlock[0]][emptyBlock[1]] = board[y][x];
		board[y][x] = tmpBlock;
		emptyBlock[0] = y;
		emptyBlock[1] = x;
	}

	/*
	 * calculates the difference by summing the difference between the x and y cords with the empty block
	 * and return true if the difference equals 1 (1 block away)
	 */
	public boolean canBlockMove(int x, int y) {
		int d_x = Math.abs((x - emptyBlock[1]));
		int d_y = Math.abs((y - emptyBlock[0]));
		if((d_x == 0 && d_y == 1) || (d_x == 1 && d_y == 0))
			return true;
		return false;
	}

	/*
	 * Shuffle method that keep shuffling until a solvable board is generated
	 */
	public void randomize() {
		System.out.println("Shuffling...");
		Random random = new Random();
		int[] used = new int[width*width-1];
		int c = 0;
		int rand;
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if(board[i][j].isEmpty) continue;
				do{
					rand = random.nextInt(width*width-1)+1;
				}while(MyArrays.searchArray(used, rand) != -1);
				board[i][j].num = rand;
				used[c++] = rand;
			}
		}
		if(!isSolvable()) {
			System.out.println("Board unsolvable, creating a new one...");
			randomize();
		}else {
			System.out.println("Board created.\n");
		}

	}

	/*
	 * Resets the board to the right order (solves it)
	 * - used in reset
	 */
	public void solve() {
		System.out.println("Resetting...");
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if((j+1) + (i*width) == width*width) {
					board[i][j].setEmpty();
				}else {
					board[i][j].num = (j+1) + (i*width);
					board[i][j].isEmpty = false;
				}
				emptyBlock[0] = width - 1;
				emptyBlock[1] = width - 1;
			}
		}
	}


	/*
	 * Checks if the board is solvable. 
	 * 
	 * The algorithm: (N = width)
	 * If N is odd, then puzzle instance is solvable if number of inversions is even in the input state.
	 * If N is even, puzzle instance is solvable if
	 *	 the blank is on an even row counting from the bottom (second-last, fourth-last, etc.) and number of inversions is odd.
	 *	 the blank is on an odd row counting from the bottom (last, third-last, fifth-last, etc.) and number of inversions is even.
	 * For all other cases, the puzzle instance is not solvable.
	 */
	private boolean isSolvable() {
		int invs = countInversions();
		if(width%2 != 0) {
			if(invs%2 == 0)
				return true;
		}else{
			if(invs%2 == 0)
				return true;
		}
		return false;
	}

	/*
	 * Counts the inversions in the board
	 * inversion is when a before b and a > b
	 */
	private int countInversions() {
		int count = 0;
		for (int i = 0; i < board.length*board.length; i++) {
			if(board[i/width][i%width].isEmpty)
				continue;
			for (int j = 0; j < i; j++)
				if(board[i/width][i%width].num < board[j/width][j%width].num) {
					count++;
				}
		}
		return count;
	}
	
	/*
	 * Checks the number order to determines if it's solved or not
	 * (x_cord + 1) + (y_cord * width) = the number that should be in these cord if solved
	 * example: (1,2), width:4 --> (2+1)+(1*4) = 7
	 */
	public boolean isSolved() {
		for(int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if(!board[i][j].isEmpty && ((j+1) + (i*width) != board[i][j].num) ){
					return false;
				}
			}
		}
		return true;
	}
	
	////////////////////////Row pushing.////////////////////////
	
	public void moveRow(int y, int x) {
		int direction = canRowMove(y,x);
		
		if(direction == -1) {
			return;
		}else if(direction == 0) {
			while(emptyBlock[0] != y) {
				moveBlock(emptyBlock[0]+1,emptyBlock[1]);
			}
		}else if(direction == 2) {
			while(emptyBlock[0] != y) {
				moveBlock(emptyBlock[0]-1,emptyBlock[1]);
			}
		}else if(direction == 1) {
			while(emptyBlock[1] != x) {
				moveBlock(emptyBlock[0],emptyBlock[1]-1);
			}
		}else if(direction == 3) {
			while(emptyBlock[1] != x) {
				moveBlock(emptyBlock[0],emptyBlock[1]+1);
			}
		}else {
			System.out.println("ERROR 010: idk how did u get here but something is wrong\n"
					+ "Pushing is set to a fifth direction (not up, down, right, or left) ¯\\(°_o)/¯");
			return;
		}
	}
	
	/*
	 *-1: can't be moved
	 * 0: move up
	 * 1: move right
	 * 2: move down
	 * 3: move left
	 */
	public int canRowMove(int y, int x) {
		int dy = emptyBlock[0] - y;
		int dx = emptyBlock[1] - x;
		if(dy == 0 ^ dx == 0) {
			if(dy == 0) {
				if(dx > 0)
					return 1;
				else
					return 3;
			}else {
				if(dy > 0)
					return 2;
				else
					return 0;
			}
		}
		return -1;
	}

	////////////////////////Methods used in console version or for debugging.////////////////////////

	/*
	 * returns the coordinates of the block with the given number
	 */
	private int[] searchBoard(int x) {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if(board[i][j].num == x)
					return new int[] {i,j};
			}
		}
		return null;
	}

	//simple console print method
	public void printBoard() {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++)
				System.out.printf("%d\t",board[i][j].num);
			System.out.println();
		}
		System.out.println("-----------------------------------");
	}
	
	/*
	 * moves the block with the given number instead of cords
	 */
	public void moveBlockByNum(int n) {
		int[] found = searchBoard(n);
		if(found != null) {
			moveBlock(found[0], found[1]);
		}
	}
}
