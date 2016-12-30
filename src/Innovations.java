import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;


public class Innovations {

	public static void reduce(NEAT_Instance n1, NEAT_Instance n2, NEAT_Instance child){
		PriorityQueue<Node> pq1 = new PriorityQueue<Node>(n1.nodes.size());
	}
	
	public static ArrayList<String> created_IDs = new ArrayList<String>(1);
	
	public static String generateID(){
		String value = "";
		do{
			for(int i = 0; i<20; i++){
				value += (int)(Math.random()*10);
			}
		}while(created_IDs.indexOf(value)>-1);
		created_IDs.add(value);
		return value;
	}
	
	public class NodeSort implements Comparator<Node>{
		@Override
		public int compare(Node arg0, Node arg1) {
			
			return 0;
		}
	}
	
	
	public static void main(String[] args){
		ArrayList<String> test = new ArrayList<String>(1);
		test.add("nihaoma");
		System.out.println(test.indexOf("niaoma"));
	}
	
}
