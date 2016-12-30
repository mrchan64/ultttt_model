
public class Game {
	
	public Sub_Game[][] arr;
	public NEAT_Instance neat1;
	public NEAT_Instance neat2;
	public int won=0;
	public boolean updated = false;
	public static int turnDelay = 0;
	public boolean paused = false;
	
	public Game(){
		arr=new Sub_Game[3][3];
		for(int i = 0; i<3; i++){
			for(int j = 0; j<3; j++){
				arr[i][j]=new Sub_Game();
			}
		}
	}
	
	public Game(NEAT_Instance one, NEAT_Instance two){
		this();
		neat1=one;
		neat2=two;
		neat1.load_game(this, 1);
		neat2.load_game(this, 2);
	}
	
	public boolean place(int posx, int posy, int value){
		int big_x=posx/3;
		int big_y=posy/3;
		int little_x=posx%3;
		int little_y=posy%3;
		boolean ret=arr[big_x][big_y].place(little_x, little_y, value);
		updated = ret;
		won = checkArr();
		return ret;
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
		while(won==0){
			while(paused){
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			try{
				Thread.sleep(turnDelay);
				neat1.decision();
				Thread.sleep(turnDelay);
				if(won!=0)break;
				while(paused){
					Thread.sleep(1);
				}
				neat2.decision();
			}catch(Exception e){
				e.printStackTrace();
				System.out.println("Threading Failed");
			}
		}
		if(won==1){
			neat1.fitness+=50;
			neat2.fitness-=50;
		}
		if(won==2){
			neat2.fitness+=50;
			neat1.fitness-=50;
		}
		for(int i = 0; i<3; i++){
			for(int j = 0; j<3; j++){
				if(arr[i][j].finished == neat1.player){
					neat1.fitness+=10;
				}
				if(arr[i][j].finished == neat2.player){
					neat2.fitness+=10;
				}
			}
		}
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
