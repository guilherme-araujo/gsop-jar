package br.ufrn.imd.bioinfo.gsop;

import br.ufrn.imd.bioinfo.gsop.database.Neo4jBoltDAO;
import br.ufrn.imd.bioinfo.gsop.database.Neo4jDAO;
import br.ufrn.imd.bioinfo.gsop.database.Neo4jEmbeddedDAO;

public class SimulationConfig {

	public static GraphDatabaseImpementation implementation = GraphDatabaseImpementation.EMBEDDED_DATABASE;
	
	public static Neo4jDAO NewGraphDatabaseDAOInstance(){
		if(implementation == GraphDatabaseImpementation.EMBEDDED_DATABASE) {
			return new Neo4jEmbeddedDAO();
		} else {
			return new Neo4jBoltDAO();
		}
	}
}
