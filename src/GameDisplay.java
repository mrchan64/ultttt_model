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
		
		Container manage = window.getContentPane();
		manage.add(enter);
		manage.add(evolution);
		
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
	
	public class Board extends JPanel{
		
		int unit = 150;
		Game game;
		JFrame frame;
		int[][] nine9;
		int[][] three3;
		JButton[][] buttons;
		int turn = 1;
		boolean playergame = false;
		
		public Board(Game g, JFrame f){
			this.setBounds(75,75,9*unit,9*unit);
			this.setLayout(null);
			game = g;
			frame=f;
			nine9=new int[9][9];
			three3=new int[3][3];
			if(g.neat1==null){
				playergame = true;
			}
		}
		
		public void reset(Game g){
			this.removeAll();
			game = g;
			nine9=new int[9][9];
			three3=new int[3][3];
			if(g.neat1==null){
				playergame = true;
			}else{
				playergame = false;
			}
		}
		
		@Override
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			for(int i = 1; i<9;i++){
				if(i%3==0){
					g2.setStroke(new BasicStroke(4));
				}else{
					g2.setStroke(new BasicStroke(1));
				}
				g2.draw(new Line2D.Float(i*unit, 0 , i*unit, 9*unit));
				g2.draw(new Line2D.Float(0, i*unit, 9*unit, i*unit));
				
			}
		}
		
		public void update(){
			if(game.updated){
				game.updated = false;
				for(int  i = 0; i < 3; i++){
					for(int j = 0; j < 3; j++){
						for(int k = 0; k < 3; k++){
							for(int l = 0; l < 3; l++){
								if(game.arr[i][j].arr[k][l]!=nine9[i*3+k][j*3+l]){
									switch(game.arr[i][j].arr[k][l]){
									case 1:
										this.add(new Square((i*3+k)*unit,(j*3+l)*unit,'x'));
										break;
									case 2:
										this.add(new Square((i*3+k)*unit,(j*3+l)*unit,'o'));
										break;
									}
									
								}
							}
						}
						if(game.arr[i][j].finished!=three3[i][j]){
							switch(game.arr[i][j].finished){
							case 1:
								this.add(new Square(i*3*unit,j*3*unit,'x',true));
								break;
							case 2:
								this.add(new Square(i*3*unit,j*3*unit,'o',true));
								break;
							}
							if(playergame)removeButtons33(i,j);
						}
					}
				}
			}
			if(game.won!=0 && playergame){
				for(int i = 0; i<3; i++){
					for(int j = 0; j<3; j++){
						removeButtons33(i,j);
					}
				}
			}
			frame.pack();
			this.repaint();
		}
		
		public void addButtons(){
			int margin = 10;
			Board bt = this;
			buttons = new JButton[9][9];
			for(int i = 0; i < 9; i++){
				for(int j = 0; j < 9; j++){
					final int x = i;
					final int y = j;
					JButton b = new JButton();
					b.setBounds(i*unit+margin,j*unit+margin,unit-2*margin,unit-2*margin);
					b.addActionListener(new ActionListener(){
						@Override
						public void actionPerformed(ActionEvent arg0) {
							// TODO Auto-generated method stub
							bt.remove(b);
							buttons[x][y]=null;
							game.place(x, y, turn);
							turn = 3-turn;
							bt.update();
						}
					});
					this.add(b);
					buttons[i][j]=b;
				}
			}
		}
		
		public void removeButtons33(int x, int y){
			for(int i = 0; i<3; i++){
				for(int j = 0; j<3; j++){
					if(buttons[x*3+i][y*3+j]!=null){
						this.remove(buttons[x*3+i][y*3+j]);
						buttons[x*3+i][y*3+j]=null;
					}
				}
			}
		}
		
		public class Square extends JPanel{
			
			int unit = 150;
			int margin = 30;
			char type = 'n';
			
			public Square(int x, int y, char t){
				this.setBounds(x,y,unit,unit);
				this.setOpaque(false);
				type = t;
			}
			
			public Square(int x, int y, char t, boolean big){
				unit*=3;
				margin*=2;
				this.setBounds(x,y,unit,unit);
				this.setOpaque(false);
				type = t;
			}
			
			@Override
			public void paint(Graphics g){
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				int colorvar = 0;
				if(unit==150){
					g2.setStroke(new BasicStroke(10));
				}else{
					g2.setStroke(new BasicStroke(30));
					colorvar = 100;
				}
				if(type == 'x'){
					g2.setColor(new Color(colorvar,colorvar,255));
					g2.draw(new Line2D.Float(margin, margin , unit-margin, unit-margin));
					g2.draw(new Line2D.Float(margin, unit-margin , unit-margin, margin));
				}else{
					g2.setColor(new Color(255,colorvar,colorvar));
					g2.drawOval(margin, margin, unit-2*margin, unit-2*margin);
				}
			}
		}
		
	}
	
}
