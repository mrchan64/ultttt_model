

public class PlayerVMinimaxManager {
	
	public Minimax minimax;
	public Board board;
	public Game game;
	
	public PlayerVMinimaxManager(Minimax m, Board b, Game g, int player){
		minimax = m;
		board = b;
		game = g;
		if(player > 2){
			player = (int) (Math.random()*2) +1;
		}
		m.load_game(g, player);
	}
	
	public void runDecision(){
		boolean firstP = true;
		while(true){
			if(minimax.player == board.turn){
				System.out.println("starting ai turn");
				board.enableButtons(false);
				minimax.decision();
				firstP = true;
				board.update();
				board.turn = 3-minimax.player;
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
