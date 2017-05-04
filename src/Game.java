import java.util.ArrayList;

public class Game {
	
	public Sub_Game[][] arr;
	public NEAT_Instance neat1;
	public NEAT_Instance neat2;
	public int won=0;
	public boolean updated = false;
	public static int turnDelay = 0;
	public boolean paused = false;
	public AvailableSpaces available;
	public RandomPlayer rp;
	
	public Game(){
		available = new AvailableSpaces();
		arr=new Sub_Game[3][3];
		for(int i = 0; i<3; i++){
			for(int j = 0; j<3; j++){
				arr[i][j]=new Sub_Game();
			}
		}
	}
	
	public Game(Game g){
		available = new AvailableSpaces();
		arr = new Sub_Game[3][3];
		for(int i = 0; i<3; i++){
			for(int j = 0; j<3; j++){
				this.arr[i][j]=new Sub_Game(g.arr[i][j]);
			}
		}
		available.getSpaces(this, -1, -1);
		this.won = g.won;
	}
	
	public Game(NEAT_Instance one, NEAT_Instance two){
		this();
		neat1=one;
		neat2=two;
		neat1.load_game(this, 1);
		neat2.load_game(this, 2);
	}
	
	public Game(NEAT_Instance one, RandomPlayer random){
		this();
		neat1=one;
		rp=random;
		neat1.load_game(this, 1);
		rp.load_game(this, 2);
	}
	
	public Game(RandomPlayer random, NEAT_Instance two){
		this();
		neat2=two;
		rp=random;
		neat2.load_game(this, 2);
		rp.load_game(this, 1);
	}
	
	public boolean place(int posx, int posy, int value){
		int big_x=posx/3;
		int big_y=posy/3;
		int little_x=posx%3;
		int little_y=posy%3;
		boolean ret=available.checkAvailable(this, posx, posy)&&arr[big_x][big_y].place(little_x, little_y, value);
		if(ret)available.getSpaces(this, posx, posy);
		updated = ret;
		won = checkArr();
		return ret;
	}
	
	public int getValue(int posx, int posy){
		return arr[posx/3][posy/3].arr[posx%3][posy%3];
	}
	
	private int checkArr(){
		
		/* COLUMNS */
		for(int i = 0; i<3; i++){
			int first=0;
			for(int j = 0; j<3; j++){
				if(arr[i][j].finished==0){
					first = 0;
					break;
				}
				if(j==0){
					first=arr[i][j].finished;
					continue;
				}
				if(arr[i][j].finished!=first){
					first=0;
					break;
				}
			}
			if(first!=0){
				return first;
			}
		}
		
		/* ROWS */
		for(int j = 0; j<3; j++){
			int first=0;
			for(int i = 0; i<3; i++){
				if(arr[i][j].finished==0){
					first = 0;
					break;
				}
				if(i==0){
					first=arr[i][j].finished;
					continue;
				}
				if(arr[i][j].finished!=first){
					first=0;
					break;
				}
			}
			if(first!=0){
				return first;
			}
		}
		
		/* POSITIVE DIAGONAL */
		int first=0;
		for(int i = 0; i<3; i++){
			if(arr[i][i].finished==0){
				first = 0;
				break;
			}
			if(i==0){
				first=arr[i][i].finished;
				continue;
			}
			if(arr[i][i].finished!=first){
				first=0;
				break;
			}
		}
		if(first!=0){
			return first;
		}
		
		/* NEGATIVE DIAGONAL */
		first=0;
		for(int i = 0; i<3; i++){
			if(arr[i][2-i].finished==0){
				first = 0;
				break;
			}
			if(i==0){
				first=arr[i][2-i].finished;
				continue;
			}
			if(arr[i][2-i].finished!=first){
				first=0;
				break;
			}
		}
		if(first!=0){
			return first;
		}
		return 0;		
	}
	
	public void playEvolution(){
		int turn = 81;
		while(won==0&&available.ret.size()!=0){
			while(paused){
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					System.out.println("Sleep Failed");
				}
			}
			try{
				Thread.sleep(turnDelay);
				if(neat1!=null)neat1.decision();
				else rp.decision();
				Thread.sleep(turnDelay);
				if(won!=0||available.ret.size()==0)break;
				while(paused){
					Thread.sleep(1);
				}
				if(neat2!=null)neat2.decision();
				else rp.decision();
				turn--;
			}catch(Exception e){
				e.printStackTrace();
				System.out.println("Threading Failed");
			}
		}
		if(won==1){
			if(neat1!=null)neat1.fitness+=turn;
			if(neat2!=null)neat2.fitness-=turn;
		}
		if(won==2){
			if(neat2!=null)neat2.fitness+=turn;
			if(neat1!=null)neat1.fitness-=turn;
		}
		/*for(int i = 0; i<3; i++){
			for(int j = 0; j<3; j++){
				if(neat1!=null&&arr[i][j].finished == neat1.player){
					neat1.fitness+=10;
				}
				if(neat2!=null&&arr[i][j].finished == neat2.player){
					neat2.fitness+=10;
				}
			}
		}*/
		while(paused){
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/* TESTING */
	public static void main(String[] args){
		NEAT_Instance n1 = new NEAT_Instance();
		NEAT_Instance n2 = new NEAT_Instance();
		Link l = new Link(n2.inputNeurons[1],n2.outputNeurons[0]);
		n2.links.add(l);
		Game g1 = new Game(n1,n2);
		System.out.println("Starting Fitness: " +n1.fitness+" "+n2.fitness);
		g1.playEvolution();
		System.out.println(g1.won+" won!");
		System.out.println("Ending Fitness: " +n1.fitness+" "+n2.fitness);
		n1.mutate();
	}
	
}
