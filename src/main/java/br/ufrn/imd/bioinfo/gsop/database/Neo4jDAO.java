package br.ufrn.imd.bioinfo.gsop.database;

import java.util.List;

import org.neo4j.graphdb.Result;

public interface Neo4jDAO {

	public void eraseDB();

	public Result getAll();
	
	public List<String> getAllAsStringList();
	
	public List<String> getAllAsUUIDList();
	
	public List<String> getNeighboursUUIDList(String uuid);
	
	public int getNodeCount();
	
	// Erdős–Rényi model
	// Edges: total number of edges
	public void generateERGraph(int nodes, int edges);

	// Barabási–Albert model
	// edges: number of edges per node
	public void generateBAGraph(int nodes, int edges);

	// Watts–Strogatz model
	// meanDegree: degree which a node in the graph has on average (best to choose
	// something < 10)
	/*
	 * * beta: probability an edge will be rewired. Rewiring means that an edge is
	 * removed and replaced by another edge created from a pair chosen at random
	 * from a set of unconnected node pairs. Controls the clustering of the graph.
	 * beta = 1.0: Erdos-Renyi model beta = 0.0: Ring graph 0.0 < beta < 1.0: Fast
	 * convergence towards a random graph, but still sufficiently clustered.
	 *
	 * Recommended value of beta to exploit typical (randomness & clustering)
	 * properties of the W-S model: 0.4 < beta < 0.6
	 */
	public void generateWSGraph(int nodes, int meanDegree, double beta);
	
	public void generateCompleteGraph(int nodes);
	
	// Uses Blitzstein-Diaconis algorithm Ref:
	// A SEQUENTIAL IMPORTANCE SAMPLING ALGORITHM FOR GENERATING RANDOM GRAPHS WITH PRESCRIBED DEGREES
	// By Joseph Blitzstein and Persi Diaconis (Stanford University). (Harvard, June 2006)
	public void generateSimpleGraph(int nodes, int degree);
	
	
}
