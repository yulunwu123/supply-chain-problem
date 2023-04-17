import java.util.ArrayList; 
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class Supply {

    /**
     # This is the method that sets off the computation
     # of the supply chain problem.  It takes as input a list containing lines of input
     # as strings. 
     # @return the total edge-weight sum of a tree that connects Nodes as described
     # in the problem statement
     */
    public int compute(List<String> fileData) {       
        int edgeWeightSum = 0;
        
        String[] s1 = fileData.get(0).split(" ");
        int N = Integer.parseInt(s1[0]);
        
        HashMap<String, String> hmap_name_type = new HashMap<String, String>();
        HashMap<String, Integer> hmap_name_index = new HashMap<String, Integer>();
        HashMap<String, String> hmap_store_dc = new HashMap<String, String>();
        
        //array that associates index with the Node's name
        String[] inverted_index = new String[N];
                
        for(int i = 1; i < 1 + N; i++) {
        	String[] line = fileData.get(i).split(" ");
        	String name = line[0];
        	String type = line[1];
        	hmap_name_type.put(name, type);
        	hmap_name_index.put(name, i-1);
        	
        	inverted_index[i - 1] = name;
        	
        	if (i > 1) {
        		String type_of_previous = fileData.get(i - 1).split(" ")[1];
        		String name_of_previous = fileData.get(i - 1).split(" ")[0];
        		
        		//store immediately after its DC
        		if (type.equals("store") && type_of_previous.equals("dist-center")) {
        			hmap_store_dc.put(name, name_of_previous);
        		}
        		else if (type.equals("store") && type_of_previous.equals("store")){
        			String dc_of_previous_store = hmap_store_dc.get(name_of_previous);
        			hmap_store_dc.put(name, dc_of_previous_store);
        		}
        	}
        }
        
        //create an ArrayList for all edges
        ArrayList<Edge> edges = new ArrayList<Edge>();
        
        //parse all the (viable) edges 
        for (int k = N + 1; k < fileData.size(); k++) {
        	String[] line = fileData.get(k).split(" ");
        	if (line.length == 3) {
        		String src = line[0].trim();
            	String dst = line[1].trim();
            	int weight = Integer.parseInt(line[2].trim());
            	   	
            	boolean addEdge = true;
            	
            	if (hmap_name_type.get(src).equals("port") && hmap_name_type.get(dst).equals("store")) {
            		addEdge = false;
            	}
            	else if (hmap_name_type.get(src).equals("port") && hmap_name_type.get(dst).equals("port")) {
            		addEdge = false;
            	}
            	else if (hmap_name_type.get(dst).equals("port") && hmap_name_type.get(src).equals("store")) {
            		addEdge = false;
            	}
            	else if (hmap_name_type.get(src).equals("rail-hub") && hmap_name_type.get(dst).equals("store")) {
            		addEdge = false;
            	}
            	else if (hmap_name_type.get(src).equals("store") && hmap_name_type.get(dst).equals("rail-hub")) {
            		addEdge = false;
            	}
            	else if (hmap_name_type.get(src).equals("dist-center") && hmap_name_type.get(dst).equals("dist-center")) {
            		addEdge = false;
            	}
            	else if (hmap_name_type.get(src).equals("store") && hmap_name_type.get(dst).equals("dist-center")) {
            		if (hmap_store_dc.get(src).equals(dst)) {
            			addEdge = true;
            		}
            		else {
            			addEdge = false;
            		}
            	}
            	else if (hmap_name_type.get(src).equals("dist-center") && hmap_name_type.get(dst).equals("store")) {
            		if (hmap_store_dc.get(dst).equals(src)) {
            			addEdge = true;
            		}
            		else {
            			addEdge = false;
            		}
            	}
            	else if (hmap_name_type.get(src).equals("store") && hmap_name_type.get(dst).equals("store")) {
            		if (hmap_store_dc.get(src).equals(hmap_store_dc.get(dst))) {
            			addEdge = true;
            		}
            		else {
            			addEdge = false;
            		}	
            	}
            	
            	
            	
            	if (addEdge) {
            		Edge e = new Edge(src, dst, weight);
            		edges.add(e); 
            	}
        	}
	
        }
        
        //Start of Kruskal
        //sort edges by their weight
        Edge[] edgesByWeight = new Edge[edges.size()];
        for (int i = 0; i < edges.size(); i++) {
        	edgesByWeight[i] = edges.get(i);
    	}
        Arrays.sort(edgesByWeight, new CmpByWeight());
        
        //create an array that stores the set information
        int[] disjointsets = new int[N];
        
        //initially, make each node in an independent set, i.e. index = arr[index]
        for (int i = 0; i < N; i++) {
        	disjointsets[i] = i;
        }
        
        int edgesAccepted = 0; //the final edge number should be N - 1;
        int curEdge = 0;
        
        while (edgesAccepted < N - 1) {
        	Edge e = edgesByWeight[curEdge];
        	
        	int src = hmap_name_index.get(e.getSrc());
        	int dst = hmap_name_index.get(e.getDst());
        	//find the source node's set and the destination node's set
        	//if they are not in the same set, add this edge to the MST and union them
        	if (find(disjointsets, src) != find(disjointsets, dst)) {
        		edgesAccepted++;
        		union(disjointsets, src, dst);
        		edgeWeightSum += e.getWeight();
        	}	
        	curEdge++;	
        }
  
        //for testing purpose only:
        
//        System.out.println("index 6 corresponds to: " + inverted_index[6]);
//        System.out.println("neighbor nodes to " + inverted_index[5] + ":");
//        for (Node each:graph.get(5)) {
//        	System.out.println(each);
//        }
//        for (Edge each : edgesByWeight) {
//        	System.out.println(each);
//        }
        
//        for (Edge each : edges) {
//        	System.out.println(each);
//        }
        

        
        return edgeWeightSum;
    }
    
    private class Edge {
    	private String src;
    	private String dst;
    	private int weight;
    	
    	public Edge (String src, String dst, int weight) {
    		this.src = src;
            this.dst = dst;
            this.weight = weight;
    	}

		public String getSrc() {
			return src;
		}

		public String getDst() {
			return dst;
		}

		public int getWeight() {
			return weight;
		}   
		
		@Override
		public String toString() {
			return "From " + this.src + " to " + this.dst + " and the weight is " + this.weight;
		}
    }
    
    private class CmpByWeight implements Comparator<Edge> {
    	@Override
    	public int compare (Edge e1, Edge e2) {
    		if (e1.getWeight() < e2.getWeight()) {
    			return -1;
    		}
    		if (e1.getWeight() > e2.getWeight()) {
    			return 1;
    		}
    		return 0;
    	}
    }
    
    public int find(int[] arr, int node) {
    	if (arr[node] == node) {
    		return node;
    	}
    	else {
    		return find(arr, arr[node]);
    	}
    }
    
    public void union (int[] arr, int node1, int node2) {
    	int root_of_1 = find(arr, node1);
    	int root_of_2 = find(arr, node2);
    	arr[root_of_1] = root_of_2;
    }   
    
}


