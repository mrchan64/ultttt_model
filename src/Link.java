
public class Link {
	
	public Node input;
	public Node output;
	public double weight;
	public String innovation_ID;
	
	public Link(Node in, Node out){
		in.addOutLink(this);
		out.addInLink(this);
		input=in;
		output=out;
		weight=1;
	}
	
	public Link addNode(){
		Node newNode = new Node();
		output.remInLink(this);
		Link newLink = new Link(newNode, output);
		output=newNode;
		newNode.addInLink(this);
		double newWeight=Math.sqrt(weight);
		weight=newWeight;
		newLink.weight=newWeight;
		newNode.innovation_ID = Innovations.generateID();
		newLink.innovation_ID = Innovations.generateID();
		this.innovation_ID = Innovations.generateID();
		return newLink;
	}
	
	public void remove(){
		input.remOutLink(this);
		output.remInLink(this);
	}
	
}
