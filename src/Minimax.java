
public class Minimax {
	
	public Game acting;
	public int player;
	public int time=81;
	
	public final int MAX_DEPTH = 6;
	
	public final int WIN_WEIGHT = 50;
	public final double SUB_WIN_WEIGHT = 35;
	public final double TWO_ROW_WEIGHT = 15;
	public final double OCCUPY_MIDDLE_WEIGHT = 10;
	
	public int[] completion;
	public int[] maxcompletion;
	
	private int xpos, ypos;
	
	public void load_game(Game game, int player){
		acting=game;
		this.player=player;
	}
	
	public void decision(){
		time--;
		completion = new int[MAX_DEPTH];
		maxcompletion = new int[MAX_DEPTH];
		for(int i = 0; i<maxcompletion.length; i++){
			maxcompletion[i]=1;
		}
		long start = System.currentTimeMillis();
		examine2(MAX_DEPTH, player, acting, Integer.MAX_VALUE/2, Integer.MIN_VALUE/2);
		boolean valid = acting.place(xpos, ypos, player);
		double time = (System.currentTimeMillis()-start)/1000.0;
		System.out.println(xpos+" "+ypos+" "+valid+" "+time+"s");
	}
	
	public double examine(int depth, int player, Game game){
		
		if(depth==0){
			return 0;
		}
		if(game.won!=0){
			return -1*WIN_WEIGHT;
		}
		double max = Integer.MIN_VALUE;
		for(int i = 0; i<9; i++){
			for(int j = 0; j<9; j++){
				completion[depth-1]=i*9+j;
				Game g = new Game(game);
				int bigX = i/3;
				int smallX = i%3;
				int bigY = j/3;
				int smallY = j%3;
				if(g.arr[bigX][bigY].finished!=0)continue;
				if(g.arr[bigX][bigY].arr[smallX][smallY]!=0)continue;
				g.place(i, j, player);
				double score = moveScore(game, i, j) - examine(depth-1, 3-player, g);
				if(score > max){
					max = score;
					if(depth == MAX_DEPTH){
						xpos = i;
						ypos = j;
					}
				}
			}
		}
		return max;
	}
	
	public double moveScore(Game game, int x, int y){
		int bigX = x/3;
		int smallX = x%3;
		int bigY = y/3;
		int smallY = y%3;
		int score = 0;
		int p = game.arr[bigX][bigY].arr[smallX][smallY];
		if(game.arr[bigX][bigY].finished != 0){		//The ai doesnt place in finished subs so this would mean that the move won the box
			return SUB_WIN_WEIGHT;
		}
		if(smallX == 1 && smallY == 1){
			score+=OCCUPY_MIDDLE_WEIGHT;
		}
		for(int i = -1; i<2; i++){
			for(int j = -1; j<2; j++){
				if(i==0 && j==0)continue;
				int testX = smallX+i;
				int testY = smallY+j;
				if(testX<0 || testX>2 || testY<0 || testY>2)continue;
				if(game.arr[bigX][bigY].arr[testX][testY]==game.arr[bigX][bigY].arr[smallX][smallY]){
					score+=TWO_ROW_WEIGHT;
				}
			}
		}
		return score;
	}
	
	public int examine2(int depth, int player, Game game, int alpha, int beta){
		for(int i = 0; i<MAX_DEPTH-depth; i++){
			System.out.print("  ");
		}
		System.out.println(alpha+ " "+beta);
		if(game.won!=0)return -time*WIN_WEIGHT;
		if(depth==0)return -stateScore(game);
		
		int max = beta;
		Position bestMove = null;
		maxcompletion[depth-1]=game.available.ret.size();
		for(int i = 0; i<maxcompletion[depth-1]; i++){
			Position move = game.available.ret.get(i);
			Game g = new Game(game);
			if(!g.place(move.x, move.y, player))continue;
			int score = -examine2(depth-1, 3-player, g, -max, -alpha);
			if(score >= alpha) return score;
			if(score > max){
				max = score;
				bestMove = move;
			}
			completion[depth-1]=i+1;
		}
		if(depth == MAX_DEPTH){
			xpos = bestMove.x;
			ypos = bestMove.y;
		}
		return max;
	}
	
	public int stateScore(Game g){
		int score = 0;
		for(int i = 0; i<9; i++){
			if(g.arr[i/3][i%3].finished==player){
				score+=time*WIN_WEIGHT/4;
			}else if(g.arr[i/3][i%3].finished==3-player){
				score+=time*WIN_WEIGHT/6;
			}
		}
		return score;
	}
}
