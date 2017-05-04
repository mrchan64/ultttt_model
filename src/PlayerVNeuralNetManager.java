
public class PlayerVNeuralNetManager {
	
	public NEAT_Instance neat;
	public Board board;
	public Game game;
	
	public PlayerVNeuralNetManager(String gene, Board b, Game g, int player){
		neat = new NEAT_Instance();
		board = b;
		game = g;
		if(player > 2){
			player = (int) (Math.random()*2) +1;
		}
		neat.load_game(g, player);
		neat.load_genes(gene);
	}
	
	public void runDecision(){
		boolean firstP = true;
		while(true){
			if(neat.player == board.turn){
				System.out.println("starting ai turn");
				board.enableButtons(false);
				neat.decision();
				firstP = true;
				board.update();
				board.turn = 3-neat.player;
			}else{
				if(firstP){
					System.out.println("starting player turn");
					firstP = false;
					board.enableButtons(true);
				}
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
