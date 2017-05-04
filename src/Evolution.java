import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

import org.apache.commons.math3.distribution.GammaDistribution;


public class Evolution{
	
	public static int popSize = 100;  //Make this a multiple of 4
	public static final String geneDir = "genes/";
	public int generation = -1;
	public int set = -1;
	public int gameNum = -1;
	public int gameMax = 150;
	public int ai1 = -1;
	public int ai2 = -1;
	public NEAT_Instance[] neats;
	public Thread t;
	public boolean paused = true;
	public Game activeGame;
	public GammaDistribution norm=new GammaDistribution(1,1.5);
	public double randomWinPerc = 0.0;
	
	public static void main(String[] args){
		//need stuff for reading a saved file
		/*neats = new NEAT_Instance[popSize];
		for(int i = 0; i<popSize; i++){
			neats[i] = new NEAT_Instance();
		}*/
		Evolution e = new Evolution();
		//e.recordGenes();
	}
	
	public Evolution(){
		neats = new NEAT_Instance[popSize];
		for(int i = 0; i<popSize; i++){
			neats[i] = new NEAT_Instance();
		}
	}
	
	public void recordGenes(){
		try{
			for(int i = 0; i<popSize; i++){
				File gene = new File(geneDir, "net"+i+".g");
				gene.delete();
				gene.createNewFile();
				neats[i].write_genes(geneDir+"net"+i+".g");
			}
			File info = new File(geneDir, "info.info");
			info.delete();
			info.createNewFile();
			BufferedWriter wtr = new BufferedWriter(new FileWriter(geneDir+"info.info"));
			wtr.write(Integer.toString(generation));
			wtr.close();
			System.out.println("[*] Total of "+Innovations.length()+" id's written");
		}catch(Exception e){
			System.out.println("[*] File writing failed...");
		}
	}
	
	public void readGenes(){
		try{
			Innovations.reset();
			for(int i = 0; i<popSize; i++){
				neats[i]=new NEAT_Instance();
				neats[i].load_genes(geneDir+"net"+i+".g");
			}
			BufferedReader rdr = new BufferedReader(new FileReader(geneDir+"info.info"));
			StringTokenizer tkn = new StringTokenizer(rdr.readLine());
			generation = Integer.parseInt(tkn.nextToken());
			rdr.close();
			System.out.println("[*] Read a total of "+Innovations.length()+" unique id's");
		}catch(Exception e){
			System.out.println("[*] File reading failed...");
		}
	}
	
	public void playGeneration(){
		while(paused){
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		generation++;
		NEAT_Instance[] set1 = new NEAT_Instance[popSize/4];
		NEAT_Instance[] set2 = new NEAT_Instance[popSize/4];
		NEAT_Instance[] set3 = new NEAT_Instance[popSize/4];
		NEAT_Instance[] set4 = new NEAT_Instance[popSize/4];
		gameMax = (int)(popSize*1.5);
		for(int i = 0; i<popSize; i++){
			if(i<popSize*3/4){
				if(i<popSize/2){
					if(i<popSize/4){
						set1[i%(popSize/4)]=neats[i];
					}else{
						set2[i%(popSize/4)]=neats[i];
					}
				}else{
					set3[i%(popSize/4)]=neats[i];
				}
			}else{
				set4[i%(popSize/4)]=neats[i];
			}
		}
		
		set = 1;
		gameNum = 1;
		for(int i = 0; i<set1.length; i++){
			Game g1 = new Game(set1[i],set2[i]);
			activeGame = g1;
			ai1 = i;
			ai2 = popSize/4+i;
			g1.playEvolution();
			Game g2 = new Game(set2[i],set1[i]);
			activeGame = g2;
			ai2 = i;
			ai1 = popSize/4+i;
			g2.playEvolution();
			gameNum++;
		}
		set++;
		for(int i = 0; i<set1.length; i++){
			Game g1 = new Game(set3[i],set4[i]);
			activeGame = g1;
			ai1 = popSize/2+i;
			ai2 = popSize/4*3+i;
			g1.playEvolution();
			Game g2 = new Game(set4[i],set3[i]);
			activeGame = g2;
			ai2 = popSize/2+i;
			ai1 = popSize/4*3+i;
			g2.playEvolution();
			gameNum++;
		}
		set++;
		for(int i = 0; i<set1.length; i++){
			Game g1 = new Game(set2[i],set3[i]);
			activeGame = g1;
			ai1 = popSize/4+i;
			ai2 = popSize/2+i;
			g1.playEvolution();
			Game g2 = new Game(set3[i],set2[i]);
			activeGame = g2;
			ai2 = popSize/4+i;
			ai1 = popSize/2+i;
			g2.playEvolution();
			gameNum++;
		}
		set++;
		for(int i = 0; i<set1.length; i++){
			Game g1 = new Game(set1[i],set4[i]);
			activeGame = g1;
			ai1 = i;
			ai2 = popSize/4*3+i;
			g1.playEvolution();
			Game g2 = new Game(set4[i],set1[i]);
			activeGame = g2;
			ai2 = i;
			ai1 = popSize/4*3+i;
			g2.playEvolution();
			gameNum++;
		}
		set++;
		for(int i = 0; i<set1.length; i++){
			Game g1 = new Game(set1[i],set3[i]);
			activeGame = g1;
			ai1 = i;
			ai2 = popSize/2+i;
			g1.playEvolution();
			Game g2 = new Game(set3[i],set1[i]);
			activeGame = g2;
			ai2 = i;
			ai1 = popSize/2+i;
			g2.playEvolution();
			gameNum++;
		}
		set++;
		for(int i = 0; i<set1.length; i++){
			Game g1 = new Game(set2[i],set4[i]);
			activeGame = g1;
			ai1 = popSize/4+i;
			ai2 = popSize/4*3+i;
			g1.playEvolution();
			Game g2 = new Game(set4[i],set2[i]);
			activeGame = g2;
			ai2 = popSize/4+i;
			ai1 = popSize/4*3+i;
			g2.playEvolution();
			gameNum++;
		}
		ai1 = -1;
		ai2 = -1;
		nextGen();
	}
	
	public void playGenerationRandom(){
		while(paused){
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		generation++;
		gameMax = popSize*4;
		gameNum = 1;
		set = 1;
		int gameswon = 0;
		RandomPlayer rp = new RandomPlayer();
		for(int j = 0; j<4; j++){
			for(int i = 0; i<neats.length; i++){
				Game g1 = new Game(neats[i],rp);
				activeGame = g1;
				ai1 = i;
				ai2 = -1;
				g1.playEvolution();
				if(g1.won==neats[1].player)gameswon++;
				Game g2 = new Game(rp,neats[i]);
				activeGame = g2;
				ai2 = i;
				ai1 = -1;
				g2.playEvolution();
				gameNum++;
				if(g2.won==neats[1].player)gameswon++;
			}
			set++;
		}
		randomWinPerc = ((double)gameswon)/((double)gameNum)*100;
		ai1 = -1;
		ai2 = -1;
		nextGen();
	}
	
	public void nextGen(){
		PriorityQueue<NEAT_Instance> sorting = new PriorityQueue<NEAT_Instance>(popSize,new BestGenes());
		for(int i = 0; i<neats.length;i++){
			sorting.add(neats[i]);
		}
		NEAT_Instance[] sorted = new NEAT_Instance[popSize];
		for(int i = 0; i<popSize; i++){
			sorted[i] = sorting.poll();
		}
		String perc="";
		if(randomWinPerc>0.1)perc=" [Won: "+randomWinPerc+"%]";
		System.out.println("[*] Generation "+generation+" data:"+perc);
		System.out.print("Fitness: \t");
		for(int i = 0; i<popSize/4; i++){
			System.out.print(sorted[i].fitness+" \t");
		}
		System.out.println();
		System.out.print("Num Links: \t");
		for(int i = 0; i<popSize/4; i++){
			System.out.print(sorted[i].links.size()+" \t");
		}
		System.out.println();
		for(int i = 0; i<popSize; i++){
			int first = linearRandom(popSize/4);
			int second = linearRandom(popSize/4);
			neats[i]=NEAT_Instance.breed(sorted[first], sorted[second]);
		}
	}
	
	public void pause(boolean b){
		paused = b;
		if(activeGame!=null)activeGame.paused = b;
	}
	
	public class BestGenes implements Comparator<NEAT_Instance>{
		@Override
		public int compare(NEAT_Instance arg0, NEAT_Instance arg1) {
			if(arg1.fitness>arg0.fitness){
				return 1;
			}else if(arg1.fitness==arg0.fitness){
				if(arg0.links.size()<arg1.links.size()){
					return -1;
				}else{
					return 1;
				}
			}
			return -1;
		}
	}
	
	public static int linearRandom(int popSize){
		int max = (popSize+1)*popSize/2;
		int rng =(int)(Math.random()*max);
		for(int i = popSize; i>0; i--){
			rng-=i;
			if(rng<0){
				return popSize-i;
			}
		}
		return -1;
	}
}
