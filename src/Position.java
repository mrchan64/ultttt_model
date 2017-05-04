
public class Position{
	int x1, y1, x2, y2, x, y;
	
	public Position(int x, int y){
		x1 = x/3;
		x2 = x%3;
		y1 = y/3;
		y2 = y%3;
		this.x = x;
		this.y = y;
	}
	
	public boolean checkAvailable(Game g){
		if(g.arr[x1][y1].finished != 0) return false;
		return g.arr[x1][y1].arr[x2][y2] == 0;
	}
}