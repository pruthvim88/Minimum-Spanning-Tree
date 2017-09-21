import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;

class Heap {
	//Heap Priority Queue
	public int size=0;
	public int key;
	public int [] A;
	public int [] positionofV;
	public int [] keyValue;
	
	public Heap(int size) {
		A = new int[size];
		positionofV = new int[size];
		keyValue = new int[size];
	}
	
	public void insert(int v, int key) {
		size++;
		A[size] = v;
		positionofV[v] = size;
		keyValue[v] = key;
		heapify_up(size);
	}
		
	public void heapify_up(int i) {
		int j, tmp;
		while(i>1) {
			j = i/2;
			if (keyValue[A[i]]<keyValue[A[j]]) {
				tmp = A[i];
				A[i] = A[j];
				A[j] = tmp;
				positionofV[A[i]] = i;
				positionofV[A[j]] = j;
				i=j;				
			} else break;
		}	
	}

	public int extract_min() {
		int ret = A[1];
		A[1] = A[size];
		positionofV[A[1]] = 1;
		size--;
		if (size >=1) {
			heapify_down(1);
			}
		return ret;
	}

	public void heapify_down(int i) {
		int j,temp;
		while(2*i<=size) {
			if(2*i==size || keyValue[A[2*i]]<=keyValue[A[2*i+1]]) {
				j = 2*i;
			}
			else j = 2*i+1;
			if(keyValue[A[j]]<keyValue[A[i]]) {
				temp = A[i];
				A[i] = A[j];
				A[j] = temp;
				positionofV[A[i]] = i;
				positionofV[A[j]] = j;
				i=j;
			}
			else break;
		}
	}
	
	public void decrease_key(int v, int key) {
		keyValue[v] = key;
		heapify_up(positionofV[v]);
	}
}

public class MinSpanTree{

	public static void main(String [] args) throws IOException{
		
		File fileReader = new File("input.txt");
		BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(fileReader)));
		Writer pw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("output.txt"), "UTF-8"));
		HashMap<Integer, HashMap<Integer,Integer>> graph = new HashMap<>();
		HashSet<Integer> V = new HashSet<Integer>();
		HashSet<Integer> S = new HashSet<Integer>();
		Heap heap;
		String output = null;
		String [] read = null;
		read = input.readLine().split(" ");
		while((output=input.readLine()) != null){
			read = output.split(" ");	
			int u = Integer.parseInt(read[0]);
			int v =Integer.parseInt(read[1]);
			int w = Integer.parseInt(read[2]);
			graph = AddEdge(u,v,w,graph);
			graph = AddEdge(v,u,w,graph);
			V.add(u);
			V.add(v);
		}
		
		//Prims Algorithm
		int u;
		int MST = 0;
		heap = new Heap(V.size()+1);
		int[] pi = new int[V.size()+1];
		for (int i = 1; i < V.size()+1;i++) {
			if(i == 1){
				heap.insert(i, 0);
			}
			else
				heap.insert(i, Integer.MAX_VALUE);
		}
		while(!S.equals(V)){
			u = heap.extract_min();
			S.add(u);
			MST = MST+heap.keyValue[u];
			for(int v:graph.get(u).keySet()){
				if (!S.contains(v)){
					if(graph.get(u).get(v) < heap.keyValue[v]){
						heap.decrease_key(v, graph.get(u).get(v));
						pi[v] = u;
					}
				}
			}
		}
		pw.write(MST+"\n");
		for (int i = 2;i < V.size()+1;i++) {
			pw.write(i+" "+pi[i]+"\n");
		}
		input.close();
		pw.close();
	}

	public static HashMap<Integer, HashMap<Integer, Integer>> AddEdge(int u, int v, int w, HashMap<Integer,HashMap<Integer,Integer>> graph) {
		if(!graph.containsKey(u))
		{   HashMap<Integer,Integer> map = new HashMap<Integer,Integer>();
			graph.put(u, map);
		    graph.get(u).put(v, w);
		}
		else{
			if(!graph.get(u).containsKey(v)){
				graph.get(u).put(v, w);
			}
			else {
				if (graph.get(u).get(v) > w){
					graph.get(u).put(v, w);
				}			
			}
		}

		return graph;
	}
}		
