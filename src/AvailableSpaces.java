import java.util.ArrayList;

public class AvailableSpaces {
	public ArrayList<Position> pos = new ArrayList<Position>(1);
	public ArrayList<Position> ret = new ArrayList<Position>(1);
	
	public AvailableSpaces(){
		for(int i = 0; i<3; i++){
			for(int j = 0; j<3; j++){
				for(int k = 0; k<3; k++){
					for(int l = 0; l<3; l++){
						pos.add(new Position(i*3+k, j*3+l));
					}
				}
			}
		}
		ret = pos;
	}
	
	public AvailableSpaces(AvailableSpaces as){
		for(int i = 0; i<as.pos.size();i++){
			pos.add(as.pos.get(i));
		}
	}
	
	public ArrayList<Position> getSpaces(Game g, int lastx, int lasty){
		lastx %= 3;
		lasty %= 3;
		for(int i = 0; i<pos.size(); i++){
			if(!pos.get(i).checkAvailable(g)){
				pos.remove(i);
				i--;
			}
		}
		
		ret = new ArrayList<Position>(1);
		for(int i = 0; i<pos.size(); i++){
			Position ana = pos.get(i);
			if(ana.x1 == lastx && ana.y1 == lasty){
				ret.add(ana);
			}else if(ana.x1 > lastx && ana.y1 > lasty) break;
		}
		
		if(ret.size() == 0){
			ret = pos;
		}
		return ret;
	}
	
	public boolean checkAvailable(Game g, int lastx, int lasty){
		for(int i = 0; i<ret.size(); i++){
			Position p = ret.get(i);
			if(p.x == lastx && p.y == lasty && p.checkAvailable(g)){
				return true;
			}else if(p.x == lastx && p.y == lasty){
				return false;
			}
		}
		return false;
	}
}
