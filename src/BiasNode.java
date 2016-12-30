import java.util.ArrayList;


public class BiasNode{
	
	public ArrayList<Node> connected;
	public double bias;
	
	public BiasNode(double b){
		bias = b;
		connected = new ArrayList<Node>(1);
	}
	
	public void addNode(Node n){
		if(connected.indexOf(n)>-1){
			return;
		}
		connected.add(n);
		connected = new ArrayList<Node>(1);
	}
	
	public void removeNode(){
		if(connected.size()==0){
			return;
		}
		int rem = (int)(Math.random()*connected.size());
		Node n = connected.remove(rem);
		n.biasInput = 0;
	}
	
	public void evaluate(){
		for(int i = 0; i<connected.size(); i++){
			Node n = connected.get(i);
			n.biasInput = bias;
			n.evaluate();
			n.biasInput = 0;
		}
	}
	
	public int[] indices(ArrayList<Node> source){
		int count = 0;
		for(int i = 0; i<connected.size(); i++){
			if(source.indexOf(connected.get(i))>-1){
				count++;
			}
		}
		int[] ind = new int[count];
		count = 0;
		for(int i = 0; i<connected.size(); i++){
			if(source.indexOf(connected.get(i))>-1){
				ind[count] = source.indexOf(connected.get(i));
				count++;
			}
		}
		return ind;
	}
	
	public String[] innos(){
		String[] ind = new String[connected.size()];
		for(int i = 0; i<ind.length; i++){
			ind[i] = connected.get(i).innovation_ID;
		}
		return ind;
	}
}
