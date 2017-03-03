import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Line2D;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

public class GameDisplay {
	
	private JFrame window;	
	
	Game current;
	
	public GameDisplay(){
		window = new JFrame("Ultimate Tic Tac Toe Evolution");
		window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		window.getContentPane().setLayout(null);
		window.setResizable(false);
		window.getContentPane().setPreferredSize(new Dimension(2500,1500));
		window.pack();
		window.setVisible(true);
		entryMenu();
	}
	
	public void entryMenu(){
		clearInterface();
		Container menu = new Container();
		
		JButton enter = new JButton("Start PvP Game");
		enter.setBounds(750,700,1000,100);
		enter.setFont(new Font("Century Gothic",Font.PLAIN, 40));
		enter.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				newPvPGame(new Game());
			}
		});

		JButton evolution = new JButton("Simulate Evolution");
		evolution.setBounds(750,810,1000,100);
		evolution.setFont(new Font("Century Gothic",Font.PLAIN, 40));
		evolution.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				evolutionGUI();
			}
		});
		
		JButton minimax = new JButton("Play Minimax");
		minimax.setBounds(750,920,1000,100);
		minimax.setFont(new Font("Century Gothic",Font.PLAIN, 40));
		minimax.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				minimaxGUI();
			}
		});
		
		Container manage = window.getContentPane();
		manage.add(enter);
		manage.add(evolution);
		manage.add(minimax);
		
		window.pack();
		window.repaint();
	}
	
	public void newPvPGame(Game g){
		clearInterface();
		Container manage = window.getContentPane();
		Board board = new Board(g, window);
		JButton reset = new JButton("<html><center>Reset</center><center>Game</center></html>");
		reset.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				newPvPGame(new Game());
			}
		});
		reset.setBounds(1900,650,200,200);
		reset.setFont(new Font("Century Gothic",Font.PLAIN, 40));
		manage.add(reset);
		board.addButtons();
		manage.add(board);
		KeyboardFocusManager kfm = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		kfm.addKeyEventDispatcher(new KeyEventDispatcher(){
			@Override
			public boolean dispatchKeyEvent(KeyEvent arg0) {
				if(arg0.getKeyCode()==KeyEvent.VK_ESCAPE){
					kfm.removeKeyEventDispatcher(this);
					entryMenu();
				}
				return false;
			}
		});
		window.pack();
		window.repaint();
	}
	
	public void evolutionGUI(){
		clearInterface();
		
		Evolution evo = new Evolution();
		
		current = new Game();
		Board board = new Board(current, window);
		
		/* DECLARATION */
		GenNum genNum = new GenNum();
		GameNum gameNum = new GameNum();
		GenPerc genPerc = new GenPerc();
		AI ai1 = new AI();
		AI ai2 = new AI();
		NetDisp net1 = new NetDisp();
		NetDisp net2 = new NetDisp();
		JButton start = new JButton("Start");
		JButton write = new JButton("<html><center>Write</center><center>Genes</center></html>");
		JButton read = new JButton("<html><center>Read</center><center>Genes</center></html>");
		
		/* BOUNDS */
		genNum.setBounds(1600, 100, 800, 150);
		gameNum.setBounds(1600, 260, 565, 150);
		genPerc.setBounds(2175, 260, 225, 150);
		ai1.setBounds(1600, 420, 395, 150);
		ai2.setBounds(2005, 420, 395, 150);
		net1.setBounds(1600, 580, 395, 610);
		net2.setBounds(2005, 580, 395, 610);
		start.setBounds(1600, 1200, 260, 200);
		write.setBounds(1870, 1200, 260, 200);
		read.setBounds(2140, 1200, 260, 200);

		/* FONT */
		genNum.setFont(new Font("Consolas",Font.PLAIN, 60));
		gameNum.setFont(new Font("Consolas",Font.PLAIN, 60));
		genPerc.setFont(new Font("Consolas",Font.PLAIN, 60));
		ai1.setFont(new Font("Consolas",Font.PLAIN, 60));
		ai2.setFont(new Font("Consolas",Font.PLAIN, 60));
		start.setFont(new Font("Century Gothic",Font.PLAIN, 70));
		write.setFont(new Font("Century Gothic",Font.PLAIN, 50));
		read.setFont(new Font("Century Gothic",Font.PLAIN, 50));

		/* OPACITY */
		genNum.setOpaque(true);
		gameNum.setOpaque(true);
		genPerc.setOpaque(true);
		ai1.setOpaque(true);
		ai2.setOpaque(true);
		net1.setOpaque(true);
		net2.setOpaque(true);
		
		/* BACKGROUND COLOR */
		Color bg = new Color(150,225,255);
		genNum.setBackground(bg);
		gameNum.setBackground(bg);
		genPerc.setBackground(bg);
		ai1.setBackground(Color.WHITE);
		ai2.setBackground(Color.WHITE);
		net1.setBackground(Color.WHITE);
		net2.setBackground(Color.WHITE);
		
		/* BORDER */
		Border bd = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
		genNum.setBorder(bd);
		gameNum.setBorder(bd);
		genPerc.setBorder(bd);
		ai1.setBorder(bd);
		ai2.setBorder(bd);
		net1.setBorder(bd);
		net2.setBorder(bd);
		
		/* SWINGWORKERS */
		SwingWorker bgThread = new SwingWorker<Void, Void>(){
			@Override
			public Void doInBackground() throws Exception {
				while(true){
					evo.playGeneration();
					Thread.sleep(10);
				}
			}
		};

		/* BUTTON FUNCTIONS */
		start.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(start.getText().equals("Start")){
					start.setText("Pause");
					write.setEnabled(false);
					read.setEnabled(false);
					evo.pause(false);
				}else{
					start.setText("Start");
					write.setEnabled(true);
					read.setEnabled(true);
					evo.pause(true);
				}
			}
		});
		write.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				write.setEnabled(false);
				read.setEnabled(false);
				evo.recordGenes();
				write.setEnabled(true);
				read.setEnabled(true);
			}
		});
		read.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				write.setEnabled(false);
				read.setEnabled(false);
				evo.readGenes();
				write.setEnabled(true);
				read.setEnabled(true);
			}
		});
		
		/* ADD TO CONTAINER */
		Container manage = window.getContentPane();
		manage.add(genNum);
		manage.add(gameNum);
		manage.add(genPerc);
		manage.add(ai1);
		manage.add(ai2);
		manage.add(net1);
		manage.add(net2);
		manage.add(start);
		manage.add(write);
		manage.add(read);
		manage.add(board);

		KeyboardFocusManager kfm = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		kfm.addKeyEventDispatcher(new KeyEventDispatcher(){
			@Override
			public boolean dispatchKeyEvent(KeyEvent arg0) {
				if(arg0.getKeyCode()==KeyEvent.VK_ESCAPE && start.getText().equals("Start")){
					kfm.removeKeyEventDispatcher(this);
					entryMenu();
				}
				return false;
			}
		});
		
		window.pack();
		window.repaint();
		
		Timer timer = new Timer(100, new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ai2.upd(evo.ai2);
				ai1.upd(evo.ai1);
				genNum.upd(evo.generation);
				gameNum.upd(evo.set);
				genPerc.upd(evo.gameNum);
				if(evo.activeGame!=null){
					if(evo.activeGame!=current){
						current = evo.activeGame;
						board.reset(current);
					}
					board.update();
				}
			}
		});
		timer.start();
		bgThread.execute();
	}
	
	public void minimaxGUI(){
		clearInterface();
		Container manage = window.getContentPane();
		Game game = new Game();
		Board board = new Board(game, window);
		PlayerVMinimaxManager pvmm = new PlayerVMinimaxManager(new Minimax(), board, game, 2);
		SwingWorker bgThread = new SwingWorker<Void, Void>(){
			@Override
			public Void doInBackground() throws Exception {
				pvmm.runDecision();
				return null;
			}
		};
		JButton reset = new JButton("<html><center>Reset</center><center>Game</center></html>");
		reset.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				bgThread.cancel(true);
				minimaxGUI();
			}
		});
		reset.setBounds(1900,650,200,200);
		reset.setFont(new Font("Century Gothic",Font.PLAIN, 40));
		MinimaxProgress mmp = new MinimaxProgress(pvmm.minimax);
		manage.add(reset);
		manage.add(mmp);
		board.addButtons();
		manage.add(board);
		KeyboardFocusManager kfm = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		kfm.addKeyEventDispatcher(new KeyEventDispatcher(){
			@Override
			public boolean dispatchKeyEvent(KeyEvent arg0) {
				if(arg0.getKeyCode()==KeyEvent.VK_ESCAPE){
					kfm.removeKeyEventDispatcher(this);
					entryMenu();
				}
				return false;
			}
		});
		Timer timer = new Timer(100, new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				mmp.upd();
			}
		});
		timer.start();
		window.pack();
		window.repaint();
		bgThread.execute();
	}
	
	public void clearInterface(){
		window.getContentPane().removeAll();
	}
	
	public static void main(String[] args){
		GameDisplay gd = new GameDisplay();
	}
	
	public class GenNum extends JLabel{
		public int gen = -1;
		public GenNum(){
			super("Generation: -", SwingConstants.CENTER);
		}
		public void upd(){
			if(gen==-1){
				this.setText("Generation: -");
			}
			this.setText("Generation: "+gen);
		}
		public void upd(int n){
			gen = n;
			upd();
		}
	}
	
	public class GameNum extends JLabel{
		public int num = -1;
		public GameNum(){
			super("Set Number: -", SwingConstants.CENTER);
		}
		public void upd(){
			if(num==-1){
				this.setText("Set Number: -");
				return;
			}
			this.setText("Set Number: "+num);
		}
		public void upd(int n){
			num = n;
			upd();
		}
	}
	
	public class GenPerc extends JLabel{
		int percentage = -1;
		public GenPerc(){
			super("--%", SwingConstants.CENTER);
		}
		public void upd(){
			if(percentage == -1){
				this.setText("--%");
				return;
			}
			String perc = "";
			if(percentage<10){
				perc = "0";
			}
			perc+=Integer.toString(percentage)+"%";
			this.setText(perc);
		}
		public void upd(int p){
			percentage = (int)(((double)p)/((double)(Evolution.popSize*1.5))*100);
			upd();
		}
	}
	
	public class AI extends JLabel{
		public int num=-1;
		public AI(){
			super("--", SwingConstants.CENTER);
		}
		public void upd(){
			if(num==-1){
				this.setText("--");
				this.setBackground(Color.WHITE);
				this.setForeground(Color.DARK_GRAY);
				return;
			}
			this.setText(Integer.toString(num));
			switch(num/(Evolution.popSize/4)){
			case 0:
				this.setBackground(Color.RED);
				this.setForeground(Color.WHITE);
				break;
			case 1:
				this.setBackground(Color.BLUE);
				this.setForeground(Color.WHITE);
				break;
			case 2:
				this.setBackground(Color.GREEN);
				this.setForeground(Color.WHITE);
				break;
			case 3:
				this.setBackground(Color.ORANGE);
				this.setForeground(Color.WHITE);
				break;
			}
		}
		public void upd(int v){
			num = v;
			upd();
		}
	}
	
	public class NetDisp extends JLabel{
		
	}
	
	public class MinimaxProgress extends JComponent{
		
		public Minimax minimax;
		public JProgressBar[] bars;
		public final int MIN_OFFSET = 10;
		public final int MAX_HEIGHT = 50;
		
		public MinimaxProgress(Minimax m){
			this.setBounds(1550, 100, 900, 500);
			minimax = m;
			bars = new JProgressBar[minimax.MAX_DEPTH];
			int offset = 0;
			int height = 0;
			if(500 > (MAX_HEIGHT + MIN_OFFSET) * minimax.MAX_DEPTH){
				height = MAX_HEIGHT;
				offset = 500/minimax.MAX_DEPTH - height;
			}else{
				offset = MIN_OFFSET;
				height = 500/minimax.MAX_DEPTH - offset;
			}
			for(int i = 0; i<minimax.MAX_DEPTH; i++){
				bars[i] = new JProgressBar(SwingConstants.HORIZONTAL, 0, 100);
				bars[i].setBounds(0,500/minimax.MAX_DEPTH*i+offset/2,900,height);
				this.add(bars[i]);
			}
		}
		
		public void upd(){
			if(minimax.completion == null)return;
			for(int i = 0; i<minimax.completion.length; i++){
				bars[i].setValue(minimax.completion[i]*100/80);
			}
		}
		
	}
	
}
