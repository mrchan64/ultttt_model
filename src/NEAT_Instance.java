import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import org.apache.commons.math3.distribution.NormalDistribution;

public class NEAT_Instance {
	
	public Game acting;
	public int fitness;
	public int player;
	public Node[] inputNeurons;
	public Node[] outputNeurons;
	public ArrayList<Link> links;
	public ArrayList<Node> nodes;
	public Node bias;
	public NormalDistribution norm=new NormalDistribution(0,.75);
	
	public NEAT_Instance(){
		//There are 9*9*2=162 input nodes and 7 output nodes (coded in binary)
		inputNeurons = new Node[162];
		for(int i = 0; i<inputNeurons.length; i++){
			inputNeurons[i] = new Node();
		}
		outputNeurons = new Node[81];
		for(int i = 0; i<outputNeurons.length; i++){
			outputNeurons[i] = new Node(true);
		}
		fitness = 0;
		links = new ArrayList<Link>(1);
		nodes = new ArrayList<Node>(1);
		bias = new Node();
	}
	
	public void load_game(Game game, int player){
		acting=game;
		this.player=player;
	}
	
	public void load_genes(String filename){
		//load stuff uhm yeah.
		try{
			BufferedReader rdr = new BufferedReader(new FileReader(filename));
			/**
			 * File format is as follows:
			 * Line 1: #of intermediate nodes
			 * Line 2: #of links
			 * Line 3->n: Each line is a link (in node, out node)
			 *            Bias Neuron: "b"
			 * 			  Output Neurons: "o"
			 * 			  Input Neurons: "i"
			 * 			  Between Neurons: "n"
			 */
			StringTokenizer line = new StringTokenizer(rdr.readLine());
			int numNodes = Integer.parseInt(line.nextToken());
			line = new StringTokenizer(rdr.readLine());
			for(int i = 0; i<numNodes; i++){
				Node newNode = new Node();
				newNode.innovation_ID = line.nextToken();
				Innovations.addExisting(newNode.innovation_ID);
				nodes.add(newNode);
			}
			line = new StringTokenizer(rdr.readLine());
			int numLinks = Integer.parseInt(line.nextToken());
			for(int i = 0; i<numLinks; i++){
				line = new StringTokenizer(rdr.readLine());
				Node in = new Node();
				Node out = new Node();
				String inNode = line.nextToken();
				int num = Integer.parseInt(inNode.substring(1, inNode.length()));
				switch(inNode.substring(0,1)){
				case "b":
					in = bias;
					break;
				case "o":
					in = outputNeurons[num];
					break;
				case "i":
					in = inputNeurons[num];
					break;
				case "n":
					in = nodes.get(num);
					break;
				default:
					System.out.println("Cannot Parse");
				}
				String outNode = line.nextToken();
				num = Integer.parseInt(outNode.substring(1, outNode.length()));
				switch(outNode.substring(0,1)){
				case "o":
					out = outputNeurons[num];
					break;
				case "i":
					out = inputNeurons[num];
					break;
				case "n":
					out = nodes.get(num);
					break;
				default:
					System.out.println("Cannot Parse");
				}
				Link a = new Link(in,out);
				a.weight = Double.parseDouble(line.nextToken());
				String id = line.nextToken();
				a.innovation_ID = id;
				Innovations.addExisting(a.innovation_ID);
				links.add(a);
			}
			rdr.close();
		}catch(Exception e){
			System.out.println("[*] File "+filename+" not found...");
			e.printStackTrace();
		}
	}
	
	public void write_genes(String filename){
		try {
			BufferedWriter wtr = new BufferedWriter(new FileWriter(filename));
			wtr.write(Integer.toString(nodes.size())+"\n");
			String ids = "";
			for(int i = 0; i<nodes.size();i++){
				ids+=nodes.get(i).innovation_ID+" ";
			}
			ids+="\n";
			wtr.write(ids);
			wtr.write(Integer.toString(links.size())+"\n");
			for(int i = 0; i<links.size();i++){
				Link l = links.get(i);
				String w = "";
				int iiind = NEAT_Instance.arrayContains(inputNeurons, l.input);
				int ioind = NEAT_Instance.arrayContains(outputNeurons, l.input);
				if(l.input==bias){
					w+="b"+1;
				}else if(iiind>-1){
					w += "i"+iiind;
				}else if(ioind>-1){
					w += "o"+ioind;
				}else if(nodes.contains(l.input)){
					w += "n"+nodes.indexOf(l.input);
				}
				w += " ";
				int oiind = NEAT_Instance.arrayContains(inputNeurons, l.output);
				int ooind = NEAT_Instance.arrayContains(outputNeurons, l.output);
				if(oiind>-1){
					w += "i"+oiind;
				}else if(ooind>-1){
					w += "o"+ooind;
				}else if(nodes.contains(l.output)){
					w += "n"+nodes.indexOf(l.output);
				}
				w += " ";
				w += Double.toString(l.weight);
				w += " ";
				w += l.innovation_ID;
				wtr.write(w+"\n");
			}
			wtr.close();
		} catch (Exception e) {
			System.out.println("Writing failed...");
		}
	}
	
	public void decision(){
		
		/* RESET ALL NODES */
		for(int i = 0; i<inputNeurons.length;i++){
			inputNeurons[i].activated=0;
		}
		for(int i = 0; i<outputNeurons.length;i++){
			outputNeurons[i].activated=0;
		}
		for(int i = 0; i<nodes.size();i++){
			nodes.get(i).activated=0;
		}
		
		/* SETTING UP INPUTS */
		for(int i = 0; i <9; i++){
			for(int j = 0; j<9;j++){
				if(acting.arr[i/3][j/3].arr[i%3][j%3]==player){
					inputNeurons[(i*9+j)*2].setVal(1);
				}
				if(acting.arr[i/3][j/3].arr[i%3][j%3]==3-player){
					inputNeurons[(i*9+j)*2+1].setVal(1);
				}
			}
		}
		bias.setVal(1);
		
		/* READING OUTPUTS */
		Position max = new Position(-1,-1);
		double highScore = -10000;
		for(int i = 0; i<acting.available.ret.size();i++){
			Position temp = acting.available.ret.get(i);
			double score = outputNeurons[temp.x*9+temp.y].total;
			if(score>highScore){
				max = temp;
				highScore = score;
			}
		}
		
		if(!acting.place(max.x, max.y, player)){
			acting.won=3-player;
		}
	}
	
	public static NEAT_Instance breed(NEAT_Instance neat1, NEAT_Instance neat2){
		//algorithm for blending genes
		NEAT_Instance child = new NEAT_Instance();
		/*for(int i = 0; i<neat1.nodes.size();i++){
			child.nodes.add(new Node());
		}
		for(int i = 0; i<neat1.links.size();i++){
			Link l = neat1.links.get(i);
			int in = arrayContains(neat1.inputNeurons,l.input);
			Node node1;
			if(in==-1){
				node1 = child.nodes.get(neat1.nodes.indexOf(l.input));
			}else{
				node1 = child.inputNeurons[in];
			}
			int out = arrayContains(neat1.outputNeurons,l.output);
			Node node2;
			if(out==-1){
				node2 = child.nodes.get(neat1.nodes.indexOf(l.output));
			}else{
				node2 = child.outputNeurons[out];
			}
			Link a = new Link(node1, node2);
			a.weight = l.weight;
			child.links.add(a);
		}
		for(int i = 0; i<neat2.nodes.size();i++){
			child.nodes.add(new Node());
		}
		for(int i = 0; i<neat2.links.size();i++){
			int j = neat1.nodes.size();
			Link l = neat2.links.get(i);
			int in = arrayContains(neat2.inputNeurons,l.input);
			Node node1;
			if(in==-1){
				node1 = child.nodes.get(neat2.nodes.indexOf(l.input)+j);
			}else{
				node1 = child.inputNeurons[in];
			}
			int out = arrayContains(neat2.outputNeurons,l.output);
			Node node2;
			if(out==-1){
				node2 = child.nodes.get(neat2.nodes.indexOf(l.output)+j);
			}else{
				node2 = child.outputNeurons[out];
			}
			Link a = new Link(node1, node2);
			a.weight = l.weight;
			child.links.add(a);
		}*/
		try{
		ArrayList<String> active_IDs = new ArrayList<String>();
		for(int i = 0; i<neat1.nodes.size();i++){
			Node newNode = new Node();
			newNode.innovation_ID = neat1.nodes.get(i).innovation_ID;
			active_IDs.add(newNode.innovation_ID);
			child.nodes.add(newNode);
		}
		for(int i = 0; i<neat2.nodes.size();i++){
			if(active_IDs.indexOf(neat2.nodes.get(i).innovation_ID)==-1){
				Node newNode = new Node();
				newNode.innovation_ID = neat2.nodes.get(i).innovation_ID;
				active_IDs.add(newNode.innovation_ID);
				child.nodes.add(newNode);
			}
		}
		for(int i = 0; i<neat1.links.size();i++){
			Link l = neat1.links.get(i);
			int in = arrayContains(neat1.inputNeurons,l.input);
			Node node1;
			if(l.input==neat1.bias){
				node1 = child.bias;
			}else if(in==-1){
				int n = active_IDs.indexOf(neat1.links.get(i).input.innovation_ID);
				node1 = child.nodes.get(n);
			}else{
				node1 = child.inputNeurons[in];
			}
			int out = arrayContains(neat1.outputNeurons,l.output);
			Node node2;
			if(out==-1){
				int n = active_IDs.indexOf(neat1.links.get(i).output.innovation_ID);
				node2 = child.nodes.get(n);
			}else{
				node2 = child.outputNeurons[out];
			}
			Link newLink = new Link(node1,node2);
			newLink.innovation_ID = l.innovation_ID;
			active_IDs.add(newLink.innovation_ID);
			newLink.weight = l.weight;
			child.links.add(newLink);
		}
		for(int i = 0; i<neat2.links.size();i++){
			Link l = neat2.links.get(i);
			int ind = active_IDs.indexOf(l.innovation_ID)-child.nodes.size();
			if(ind>-1){
				Link m = child.links.get(ind);
				m.weight = m.weight/2+l.weight/2;
				continue;
			}
			int in = arrayContains(neat2.inputNeurons,l.input);
			Node node1;
			if(l.input==neat2.bias){
				node1 = child.bias;
			}else if(in==-1){
				int n = active_IDs.indexOf(neat2.links.get(i).input.innovation_ID);
				node1 = child.nodes.get(n);
			}else{
				node1 = child.inputNeurons[in];
			}
			int out = arrayContains(neat2.outputNeurons,l.output);
			Node node2;
			if(out==-1){
				int n = active_IDs.indexOf(neat2.links.get(i).output.innovation_ID);
				node2 = child.nodes.get(n);
			}else{
				node2 = child.outputNeurons[out];
			}
			Link newLink = new Link(node1,node2);
			newLink.innovation_ID = l.innovation_ID;
			active_IDs.add(newLink.innovation_ID);
			newLink.weight = l.weight;
			child.links.add(newLink);
		}
		}catch(Exception e){
			e.printStackTrace();
			System.exit(0);
		}
		child.mutate();
		return child;
	}
	
	public void mutate(){
		double rand = Math.random()*20;
		if(rand<3.0){		//add node
			if(links.size()==0)return;
			int r = (int) (Math.random()*links.size());
			Link temp = links.get(r).addNode();
			links.add(temp);
			nodes.add(temp.input);
		}else if(rand<6.0){	//add link
			Node in;
			int r1 = (int) (Math.random()*(nodes.size()+inputNeurons.length+1));
			r1--;
			if(r1 == -1){
				in = bias;
			}else if(r1>=inputNeurons.length){
				r1-=inputNeurons.length;
				in = nodes.get(r1);
			}else{
				in = inputNeurons[r1];
			}
			
			Node out;
			int r2 = (int) (Math.random()*(nodes.size()+outputNeurons.length));
			if(r2>=outputNeurons.length){
				r2-=outputNeurons.length;
				out = nodes.get(r2);
			}else{
				out = outputNeurons[r2];
			}
			
			Link newLink = new Link(in,out);
			newLink.innovation_ID = Innovations.generateID();
			links.add(newLink);
		}else if(rand<9.0){	//remove link
			if(links.size()==0)return;
			int r = (int) (Math.random()*links.size());
			Link temp = links.remove(r);
			Node[] nodeTemp = {temp.input, temp.output};
			temp.remove();
			for(int i = 0; i<2; i++){
				if(!nodeTemp[i].hasValue()){
					nodes.remove(nodeTemp[i]);
				}
			}
		}else if(rand<18.5){				//perturb a weight
			if(links.size()==0)return;
			int r = (int) (Math.random()*links.size());
			links.get(r).weight*=norm.sample();
		}
	}
	
	public static int arrayContains(Object[] source, Object find){
		for(int i = 0; i<source.length; i++){
			if(source[i]==find){
				return i;
			}
		}
		return -1;
	}
	
	public static void main(String[] args){
		for(int i = 0; i<1000; i++){
			NEAT_Instance n = new NEAT_Instance();
			n.mutate();
		}
	}
	
}
