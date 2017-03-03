
public class Sub_Game {
	
	public int[][] arr;
	public int finished;
	
	public Sub_Game(){
		arr = new int[3][3];
		finished = 0;
	}
	
	public Sub_Game(Sub_Game sg){
		arr = new int[3][3];
		for(int i = 0; i<3; i++){
			for(int j = 0; j<3; j++){
				this.arr[i][j]=sg.arr[i][j];
			}
		}
		this.finished = sg.finished;
	}
	
	public boolean place(int posx, int posy, int value){
		if(finished!=0){
			return false;
		}
		if(arr[posx][posy]!=0){
			return false;
		}
		arr[posx][posy] = value;
		checkArr();
		return true;
	}
	
	private boolean checkArr(){
		
		/* COLUMNS */
		for(int i = 0; i<3; i++){
			int first=0;
			for(int j = 0; j<3; j++){
				if(arr[i][j]==0){
					first = 0;
					break;
				}
				if(j==0){
					first=arr[i][j];
					continue;
				}
				if(arr[i][j]!=first){
					first=0;
					break;
				}
			}
			if(first!=0){
				finished = first;
				return true;
			}
		}
		
		/* ROWS */
		for(int j = 0; j<3; j++){
			int first=0;
			for(int i = 0; i<3; i++){
				if(arr[i][j]==0){
					first = 0;
					break;
				}
				if(i==0){
					first=arr[i][j];
					continue;
				}
				if(arr[i][j]!=first){
					first=0;
					break;
				}
			}
			if(first!=0){
				finished = first;
				return true;
			}
		}
		
		/* POSITIVE DIAGONAL */
		int first=0;
		for(int i = 0; i<3; i++){
			if(arr[i][i]==0){
				first = 0;
				break;
			}
			if(i==0){
				first=arr[i][i];
				continue;
			}
			if(arr[i][i]!=first){
				first=0;
				break;
			}
		}
		if(first!=0){
			finished = first;
			return true;
		}
		
		/* NEGATIVE DIAGONAL */
		first=0;
		for(int i = 0; i<3; i++){
			if(arr[i][2-i]==0){
				first = 0;
				break;
			}
			if(i==0){
				first=arr[i][2-i];
				continue;
			}
			if(arr[i][2-i]!=first){
				first=0;
				break;
			}
		}
		if(first!=0){
			finished = first;
			return true;
		}
		return false;		
	}
	
}
