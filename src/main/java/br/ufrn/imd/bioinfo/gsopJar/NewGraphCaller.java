package br.ufrn.imd.bioinfo.gsopJar;

import java.util.List;

import br.ufrn.imd.bioinfo.gsop.controller.NewGraphController;
import br.ufrn.imd.bioinfo.gsop.controller.QueriesController;

public class NewGraphCaller {
	
private int numNodes;
	
	private int numEdges;
	
	private String result;
	
	List<String> generatedNodes;
	
	QueriesController queries;
	
	NewGraphController newGraph;			
		
	public NewGraphCaller() {
		numNodes = 100;
		numEdges = 4;
		result = "";
		queries = new QueriesController();
		newGraph = new NewGraphController();
		generatedNodes = queries.listAllAsStringWithNeighbours();
		System.out.println("Classe NewGraph inicializada");
	}
	
	public void generateba() {
				
		newGraph.generateBAGraph(numNodes, numEdges);
		
		generatedNodes = queries.listAllAsStringWithNeighbours();		
		
	}

	public int getNumNodes() {
		return numNodes;
	}

	public void setNumNodes(int numNodes) {
		this.numNodes = numNodes;
	}

	public int getNumEdges() {
		return numEdges;
	}

	public void setNumEdges(int numEdges) {
		this.numEdges = numEdges;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public List<String> getGeneratedNodes() {
		generatedNodes = queries.listAllAsStringWithNeighbours();
		return generatedNodes;
	}

	public void setGeneratedNodes(List<String> generatedNodes) {
		this.generatedNodes = generatedNodes;
	}
	

}
