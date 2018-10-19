package br.ufrn.imd.bioinfo.gsop.controller;

import br.ufrn.imd.bioinfo.gsop.SimulationConfig;
import br.ufrn.imd.bioinfo.gsop.database.Neo4jDAO;

public class SaveGraphController {
	
	private Neo4jDAO dao;
	
	public SaveGraphController() {
		dao = SimulationConfig.NewGraphDatabaseDAOInstance();
	}

	public void saveGraph(String saveFile) {
		dao.saveGraph(saveFile);
		
	}
	
	public void restoreGraph(String saveFile) {
		dao.restoreGraph(saveFile);
	}
}
