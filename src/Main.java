import Game.Board;

public class Main {
	static int width = 4;
	static Board game_board = new Board(width); //create blocks
	static GUI window = new GUI(width); 		//create frame
	
	public static void main(String[] args) {
		
		
		//show frame
		window.frame.setVisible(true);
		
		//load blocks numbers
		window.loadNums(game_board.getBoard());
		
		//display time
		while(window.frame.isEnabled()) {
			int[] time = window.calcTime(window.stopWatch.getElapsedTime());
			window.timeField.setText(time[0]+":"+time[1]+":"+time[2]);
		}
	}
	
	//when block is clicked
	public static void moveBlock(int y, int x) {
		//move the block
		game_board.moveRow(y, x);
		window.loadNums(game_board.getBoard());
		
		//check if solved
		if(game_board.isSolved()) {
			System.out.println("Board Solved.");
			System.out.printf("Time:\t%d:%d:%d\n",window.time[0],window.time[1],window.time[2]);
			window.solved();
		}
	}
	
	//new Game button
	public static void newGame(int user_width) {
		//create new frame for different width
		if(width != user_width) {
			width = user_width;
			window.frame.dispose();
			window = new GUI(width);
			window.frame.setVisible(true);
		}
		//create new board
		game_board = new Board(width);
		game_board.randomize();
		window.loadNums(game_board.getBoard());
	}
	
	//reset button
	public static void reset() {
		game_board.solve();
		window.loadNums(game_board.getBoard());
	}
}
