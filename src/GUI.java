import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

import Elfor.StopWatch;
import Game.Block;


public class GUI {
	StopWatch stopWatch = new StopWatch();

	int width;
	JFrame frame;
	JPanel panel = new JPanel();
	JButton[] btns;
	JLabel finishedLabel = new JLabel("");
	JLabel highScoreLabel = new JLabel("");
	boolean isSolved;
	int[] time = new int[3];
	public JTextField timeField = new JTextField("");

	/**
	 * Create the application.
	 */
	public GUI(int width) {
		System.out.println("Creating New Window...");
		this.width = width;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 480, 550);
		frame.setTitle("Sliding Puzzle");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel msgLabel = new JLabel("");
		msgLabel.setForeground(Color.RED);
		msgLabel.setBounds(57, 429, 360, 14);
		frame.getContentPane().add(msgLabel);

		// width = 4;  //Eclipse designer error (uncomment theline for the designer to work)
		panel.setBounds(57, 65, 360, 360);
		frame.getContentPane().add(panel);
		panel.setLayout(new GridLayout(width, width, 0, 0));

		JSpinner widthSelector = new JSpinner();
		widthSelector.setBackground(Color.GRAY);
		widthSelector.setModel(new SpinnerNumberModel(width, 3, 6, 1));
		widthSelector.setBounds(168, 467, 29, 23);
		frame.getContentPane().add(widthSelector);

		JButton newGameBtn = new JButton("New Game");
		newGameBtn.setForeground(Color.WHITE);
		newGameBtn.setBackground(Color.GRAY);
		newGameBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stopWatch.stop();
				timeField.setText("0:0:0");
				finishedLabel.setText("");
				highScoreLabel.setText("");
				msgLabel.setText("");
				isSolved = false;
				System.out.println("\nNew game of width "+(int)widthSelector.getValue());
				Main.newGame((int)widthSelector.getValue());
			}
		});
		newGameBtn.setBounds(57, 466, 110, 23);
		frame.getContentPane().add(newGameBtn);

		JButton resetBtn = new JButton("Reset");
		resetBtn.setForeground(Color.WHITE);
		resetBtn.setBackground(Color.GRAY);
		resetBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stopWatch.stop();
				timeField.setText("0:0:0");
				finishedLabel.setText("");
				highScoreLabel.setText("");
				msgLabel.setText("");
				isSolved = false;
				Main.reset();
			}
		});

		resetBtn.setBounds(307, 466, 110, 23);
		frame.getContentPane().add(resetBtn);

		finishedLabel.setForeground(Color.BLUE);
		finishedLabel.setFont(new Font("Myriad Pro", Font.BOLD, 20));
		finishedLabel.setBounds(57, 38, 360, 23);
		frame.getContentPane().add(finishedLabel);

		timeField.setHorizontalAlignment(SwingConstants.CENTER);
		timeField.setEditable(false);
		timeField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		timeField.setBounds(57, 11, 78, 20);
		frame.getContentPane().add(timeField);
		timeField.setColumns(10);
		
		
		highScoreLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
		highScoreLabel.setForeground(Color.RED);
		highScoreLabel.setBounds(258, 13, 123, 14);
		frame.getContentPane().add(highScoreLabel);

		btns = new JButton[width*width];
		for (int i = 0; i < btns.length; i++) {
			int index = i; //plz don't ask because idk.
			btns[i] = new JButton("");
			btns[i].setBackground(Color.white);
			btns[i].setFocusPainted(false);
			btns[i].setFont(new Font("Tahoma", Font.PLAIN, 21));
			btns[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(!isSolved) {
						if(!stopWatch.isRunning()) {
							stopWatch.start();
						}
						Main.moveBlock((index)/width, (index)%width);
					}else {
						msgLabel.setText("Press New Game to start a new one");
						System.out.println("already solved");
					}
				}});
			panel.add(btns[i]);
		}
	}

	/**
	 * reads the nums from the board and sets the buttons text to them
	 */
	public void loadNums(Block[][] board) {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				btns[(j+1)+(i*board.length)-1].setText(""+(board[i][j].isEmpty?"":board[i][j].num));
				if(board[i][j].isEmpty) {
					btns[(j+1)+(i*board.length)-1].setEnabled(false);
					btns[(j+1)+(i*board.length)-1].setBackground(Color.LIGHT_GRAY);;
				}else {
					btns[(j+1)+(i*board.length)-1].setEnabled(true);
					btns[(j+1)+(i*board.length)-1].setBackground(Color.white);
				}
			}
		}
		panel.revalidate();
	}

	/**
	 * called when the board is solved
	 * shows a message and completes the last tile
	 */
	public void solved() {
		stopWatch.stop();
		isSolved = true;

		time = calcTime(stopWatch.getElapsedTime());
		if(setHighScore(time))
			highScoreLabel.setText("NEW HIGH SCORE");
		finishedLabel.setText("Well Done. took you "+time[0]+":"+time[1]+":"+time[2]);
		btns[width*width-1].setText(""+(width*width));
		btns[width*width-1].setEnabled(true);
		btns[width*width-1].setBackground(Color.white);
		panel.revalidate();
	}

	/**
	 * converts seconds into a H:M:S time format
	 * Array indexes:
	 * 	0-hours
	 * 	1-minutes
	 * 	2-seconds
	 */
	public int[] calcTime(long seconds) {
		int[] time = new int[3];
		time[2] = (int)(seconds % 60);
		seconds -= seconds%60;
		time[1] = (int)(seconds%3600)/60;
		seconds -= seconds%3600;
		time[0] = (int)seconds/3600;

		return time;
	}

	private boolean setHighScore(int[] end_time){
		//read scores file
		File score = new File("Score.txt");

		//create file if not already found
		try {
			if(score.createNewFile()) {
				FileWriter writer = new FileWriter("Score.txt");
				writer.write("each line number represent the width\n" + "\n\n\n\n\n\n");
				writer.close();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
			return false;
		}
		
		String[] hScores = new String[6]; //string array that stores all lines form Scores.txt
		int[] highScoreTime = new int[3]; //int array to store the highscore of current width
		boolean preSet = true; //true if there is a previous high score

		//read hScores and highScoreTime
		try {
			Scanner sc = new Scanner(score);

			for (int i = 0; i < hScores.length; i++) 
				hScores[i] = sc.nextLine();
			sc.close();
			
			for (int i = 0; i < hScores[width-1].split(":").length; i++) 
				try {
					highScoreTime[i] = Integer.parseInt(hScores[width-1].split(":")[i]);
				}catch(Exception e) {
					preSet = false;
				}
			

		} catch (FileNotFoundException e) {
			System.out.println("ERROR: high score file not found");
			return false;
		}


		/*
		 * if there is a previous score and the time is more than high score return false
		 */
		if(preSet) {	
			if(time[0] > highScoreTime[0]) {
				return false;
			}else if(time[1] > highScoreTime[1]) {
				return false;
			}else if(time[2] >= highScoreTime[2]) {
				return false;
			}
		}

		///////////// NEW HIGH SCORE /////////////
		
		System.out.println("NEW HIGH SCORE");
		hScores[width-1] = time[0]+":"+time[1]+":"+time[2]; // change the line in hScores array 

		// write hScores array in Scores.txt
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("Score.txt"));

			for (int i = 0; i < hScores.length; i++) {
				writer.write(hScores[i]+"\n");
			}
			writer.close();

		} catch (IOException e) {
			System.out.println("ERROR: couldn't store new highscore");
			return false;
		}

		return true;
	}
}
