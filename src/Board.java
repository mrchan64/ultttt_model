import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;

import javax.swing.*;

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
								if(buttons[i*3+k][j*3+l]!=null){
									this.remove(buttons[i*3+k][j*3+l]);
									buttons[i*3+k][j*3+l]=null;
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
	
	public void enableButtons(boolean state){
		for(int i = 0; i< 9; i++){
			for(int j = 0; j<9; j++){
				if(buttons[i][j]==null)continue;
				buttons[i][j].setEnabled(state);
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