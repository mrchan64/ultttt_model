import java.util.PriorityQueue;

public class Test {
	

	public static void main(String[] args) {
		PriorityQueue<Integer> pq = new PriorityQueue<Integer>();
		for(int i = 0; i< 20; i++){
			pq.add((int)(Math.random()*30));
		}
		Integer[] sorted = pq.toArray(new Integer[20]);
		for(int i = 0; i<sorted.length; i++){
			System.out.print(sorted[i]+" ");
		}
		System.out.println();
		for(int i = 0; i<20; i++){
			System.out.print(pq.poll()+" ");
		}
	}

}
