import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;


public class Innovations {
	
	private static final int idLength = 15;

	public static void reduce(NEAT_Instance n1, NEAT_Instance n2, NEAT_Instance child){
		PriorityQueue<Node> pq1 = new PriorityQueue<Node>(n1.nodes.size());
	}
	
	private static ArrayList<String> created_IDs = new ArrayList<String>(1);
	
	public static String generateID(){
		String value = "";
		do{
			for(int i = 0; i<idLength; i++){
				value += (int)(Math.random()*10);
			}
		}while(created_IDs.indexOf(value)>-1);
		created_IDs.add(value);
		return value;
	}
	
	public static void reset(){
		created_IDs = new ArrayList<String>(1);
	}
	
	public static void addExisting(String id){
		if(!checkValid(id))System.out.println("invalid id");
		if(created_IDs.indexOf(id)==-1){
			created_IDs.add(id);
		}
	}
	
	public static int length(){
		return created_IDs.size();
	}
	
	public static boolean checkValid(String id){
		if(id.length()!=idLength)return false;
		try{
			Long.parseLong(id);
		}catch(Exception e){
			return false;
		}
		return true;
	}
	
	public static boolean contains(String id){
		return created_IDs.indexOf(id)>=0;
	}
	
	public class NodeSort implements Comparator<Node>{
		@Override
		public int compare(Node arg0, Node arg1) {
			
			return 0;
		}
	}
	
	
	public static void main(String[] args){
		ArrayList<String> test = new ArrayList<String>(1);
		test.add(new String("nihaoma"));
		System.out.println(test.indexOf(new String("nihaoma")));
	}
	
	
}
