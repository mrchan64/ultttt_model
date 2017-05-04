
public class RandomPlayer {
	
	Game acting;
	int player;
	
	public void load_game(Game game, int player){
		acting=game;
		this.player=player;
	}
	
	public void decision(){
		int size = acting.available.ret.size();
		int ind = (int)(Math.random()*size);
		Position place = acting.available.ret.get(ind);
		acting.place(place.x, place.y, player);
	}
}
