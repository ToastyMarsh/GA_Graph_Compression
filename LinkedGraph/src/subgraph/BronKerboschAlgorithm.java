package subgraph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import linkedgraph.LinkedGraph;

public final class BronKerboschAlgorithm {

	public static void main(String[] args) {
		// LinkedGraph graphList = LinkedGraph.load("LinkedGraph/yeast.txt");
		// ArrayList<ArrayList<Integer>> graphListArray = graphList.getAdjacencyList();

		// // Build the graph as an adjacency list
	    // Map<String, Set<String>> graph = new HashMap<String, Set<String>>();
		// for ( int i = 0; i < graphListArray.size(); i++ ) {
	    // 	String vertex = String.valueOf(i);
	    // 	Set<String> neighbours = new HashSet<String>();
	    // 	for ( Integer neighbour : graphListArray.get(i) ) {
	    // 		neighbours.add(String.valueOf(neighbour));
	    // 	}
	    // 	graph.put(vertex, neighbours);
	    // }
	    
	    // // Initialize current clique, candidates and processed vertices
	    // Set<String> currentClique = new TreeSet<String>();
	    // Set<String> candidates = new HashSet<String>(graph.keySet());
	    // Set<String> processedVertices = new HashSet<String>();

	    // // Execute the Bron-Kerbosch algorithm to collect the cliques
	    // bronKerbosch(currentClique, candidates, processedVertices, graph);
	    
	    // // Sort the cliques for consistent display
	    // Collections.sort(cliques, listComparator);
	    
	    // // Display the cliques
	    // System.out.println(cliques);

		// getAdjacencyList("LinkedGraph/yeast.txt");
		getAdjacencyList("LinkedGraph/ecoli.txt");
	}
	
	private static void bronKerbosch(Set<String> currentClique, Set<String> candidates,
			                         Set<String> processedVertices, Map<String, Set<String>> graph) {
		
	    if ( candidates.isEmpty() && processedVertices.isEmpty() ) {
	        if ( currentClique.size() > 2 ) {
	            List<String> clique = new ArrayList<String>(currentClique);
	            cliques.add(clique);
	        }
	        return;
	    }
	    
	    // Select a pivot vertex from 'candidates' union 'processedVertices' with the maximum degree
	    Set<String> union = new HashSet<String>(candidates);
	    union.addAll(processedVertices);	 
	    String pivot = 
	    	union.stream().max( (s1, s2) -> Integer.compare(graph.get(s1).size(), graph.get(s2).size()) ).get();

	    // 'possibles' are vertices in 'candidates' that are not neighbours of the 'pivot'
	    Set<String> possibles = new HashSet<String>(candidates);
	    possibles.removeAll(graph.get(pivot));
	    
	    for ( String vertex : possibles) {
	        // Create a new clique including 'vertex'
	        Set<String> newCliques = new TreeSet<String>(currentClique);
	        newCliques.add(vertex);

	        // 'newCandidates' are the members of 'candidates' that are neighbours of 'vertex'
	        Set<String> neighbours = graph.get(vertex);
	        Set<String> newCandidates = new HashSet<String>(candidates);
	        newCandidates.retainAll(neighbours);

	        // 'newProcessedVertices' are members of 'processedVertices' that are neighbours of 'vertex'
	        Set<String> newProcessedVertices = new HashSet<String>(processedVertices);
	        newProcessedVertices.retainAll(neighbours);

	        // Recursive call with the updated sets
	        bronKerbosch(newCliques, newCandidates, newProcessedVertices, graph);

	        // Move 'vertex' from 'candidates' to 'processedVertices'
	        candidates.remove(vertex);
	        processedVertices.add(vertex);
	    }	    
	}
	
	private static Comparator<List<String>> listComparator = (list1, list2) -> {
        for ( int i = 0; i < Math.min(list1.size(), list2.size()); i++ ) {
            final int comparison = list1.get(i).compareTo(list2.get(i));
            if ( comparison != 0 ) {
                return comparison;
            }
        }
        return Integer.compare(list1.size(), list2.size());
	};
	
	private static List<List<String>> cliques = new ArrayList<List<String>>();
	
	private static record Edge(String start, String end) {}

	public static List<List<String>> getAdjacencyList(String filename) {
		LinkedGraph graphList = LinkedGraph.load(filename);
		ArrayList<ArrayList<Integer>> graphListArray = graphList.getAdjacencyList();

		// Build the graph as an adjacency list
		Map<String, Set<String>> graph = new HashMap<String, Set<String>>();
		for ( int i = 0; i < graphListArray.size(); i++ ) {
	    	String vertex = String.valueOf(i);
	    	Set<String> neighbours = new HashSet<String>();
	    	for ( Integer neighbour : graphListArray.get(i) ) {
	    		neighbours.add(String.valueOf(neighbour));
	    	}
	    	graph.put(vertex, neighbours);
	    }
	    
	    // Initialize current clique, candidates and processed vertices
	    Set<String> currentClique = new TreeSet<String>();
	    Set<String> candidates = new HashSet<String>(graph.keySet());
	    Set<String> processedVertices = new HashSet<String>();

	    // Execute the Bron-Kerbosch algorithm to collect the cliques
	    bronKerbosch(currentClique, candidates, processedVertices, graph);
	    
	    // Sort the cliques for consistent display
	    Collections.sort(cliques, listComparator);
	    
	    // Display the cliques
	    System.out.println(cliques);
		cliques = sortCliques(cliques);
		cliqueComposition(cliques);
		return cliques;
	}

	private static List<List<String>> sortCliques(List<List<String>> cliques) {
		cliques.sort(Comparator.comparingInt(List::size)); 
		return cliques;
	}

	private static HashMap<Integer, Integer> cliqueComposition(List<List<String>> cliques) {
		HashMap<Integer, Integer> composition = new HashMap<Integer, Integer>();
		List<Integer> compositionList = new ArrayList<Integer>();
		for(List<String> clique : cliques) {
			if (!composition.containsKey(clique.size())) {
				composition.put(clique.size(), 0);
			}
			else {
				composition.put(clique.size(), composition.get(clique.size()) + 1);
			}
			compositionList.add(clique.size());
		}
		System.out.println(composition);
		// System.out.println(compositionList);
		return composition;
	}

}