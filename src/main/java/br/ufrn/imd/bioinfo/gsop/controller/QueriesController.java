package br.ufrn.imd.bioinfo.gsop.controller;

import java.util.ArrayList;
import java.util.List;

import br.ufrn.imd.bioinfo.gsop.SimulationConfig;
import br.ufrn.imd.bioinfo.gsop.database.Neo4jDAO;

public class QueriesController {

	private Neo4jDAO dao;

	public QueriesController() {
		dao = SimulationConfig.NewGraphDatabaseDAOInstance();
	}

	public List<String> listAllAsString() {
		List<String> result = new ArrayList<String>();

		result = dao.getAllAsStringList();

		return result;
	}

	public List<String> listAllAsStringWithNeighbours() {
		
		List<String> result = new ArrayList<String>();
		
		result = dao.getAllAsUUIDList();
		
		List<String> nodesAndNeighbours = new ArrayList<String>();

		for (String s : result) {
			String list = "";

			List<String> neighbours = dao.getNeighboursUUIDList(s);
			for (String n : neighbours) {
				list += n + " | ";
			}

			nodesAndNeighbours.add(s + ": " + list);

		}
		
		return nodesAndNeighbours;
	}
	
	public List<String> listAllAsUUIDStringList(){
		return dao.getAllAsUUIDList();
	}
	
	public List<String> listAllNeighborsAsUUIDStringList(String uuid){
		return dao.getNeighboursUUIDList(uuid);
	}

	public int getNodeCount() {
		return dao.getNodeCount();
	}
}
