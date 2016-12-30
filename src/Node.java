import java.util.*;

public class Node {
	
	private ArrayList<Link> inLinks;
	private ArrayList<Link> outLinks;
	public int activated=0;
	public String innovation_ID;
	public double biasInput = 0;
	
	public Node(){
		inLinks=new ArrayList<Link>(1);
		outLinks=new ArrayList<Link>(1);
	}
	
	public void addOutLink(Link en){
		outLinks.add(en);
	}
	
	public void addInLink(Link en){
		inLinks.add(en);
	}
	
	public void remOutLink(Link en){
		outLinks.remove(en);
	}
	
	public void remInLink(Link en){
		inLinks.remove(en);
	}
	
	public boolean hasValue(){
		return inLinks.size()+outLinks.size()!=0;
	}
	
	public void evaluate(){
		if(activated == 1)return;
		double total = biasInput;
		for(int i = 0; i<inLinks.size();i++){
			Link temp = inLinks.get(i);
			total+=((double)temp.input.activated)*temp.weight;
		}
		if(total>0){
			activated=1;
			for(int i = 0; i<outLinks.size();i++){
				outLinks.get(i).output.evaluate();
			}
		}
		
	}
	
	public void setVal(int input){
		if(input==0){
			return;
		}
		activated = 1;
		for(int i = 0; i<outLinks.size();i++){
			outLinks.get(i).output.evaluate();
		}
	}
	
}
