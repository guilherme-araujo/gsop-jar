package br.ufrn.imd.bioinfo.gsop.controller;

import br.ufrn.imd.bioinfo.gsop.SimulationConfig;
import br.ufrn.imd.bioinfo.gsop.database.Neo4jDAO;

public class NewGraphController {
	
	private Neo4jDAO dao;
	
	public NewGraphController() {
		dao = SimulationConfig.NewGraphDatabaseDAOInstance();		
	}	
		
	public void generateERGraph(int nodes, int edges) {
		dao.eraseDB();
		dao.generateERGraph(nodes, edges);
	}
	
	public void generateBAGraph(int nodes, int edges) {
		dao.eraseDB();
		dao.generateBAGraph(nodes, edges);		
	}
	
	public void generateWSGraph(int nodes, int meanDegree, double beta) {
		dao.eraseDB();
		dao.generateWSGraph(nodes, meanDegree, beta);
	}
	
	public void generateCompleteGraph(int nodes) {
		dao.eraseDB();
		dao.generateCompleteGraph(nodes);
	}
	
	public void generateSimpleGraph(int nodes, int degree) {
		dao.eraseDB();
		dao.generateSimpleGraph(nodes, degree);
	}
	

}
