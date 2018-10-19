package br.ufrn.imd.bioinfo.gsop.database;

import java.util.List;

import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Session;
import org.neo4j.graphdb.Result;

public class Neo4jBoltDAO implements Neo4jDAO{

	private static final Driver driver = Neo4jDriverInstance.getDriver();

	@Override
	public void eraseDB() {
		try ( Session session = driver.session() ){
			session.run("MATCH (n) DETACH DELETE n");
		}
	}
	
	@Override
	public Result getAll() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<String> getAllAsStringList() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<String> getAllAsUUIDList() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<String> getNeighboursUUIDList(String uuid) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int getNodeCount() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public void generateERGraph(int nodes, int edges) {
		try (Session session = driver.session()){
			session.run("CALL apoc.generate.er("+nodes+","+edges+" ,'TMP_LABEL','TMP_REL')");
		}
	}

	@Override
	public void generateBAGraph(int nodes, int edges) {
		try (Session session = driver.session()){
			session.run("CALL apoc.generate.ba("+nodes+","+edges+" ,'TMP_LABEL','TMP_REL')");
		}
	}

	@Override
	public void generateWSGraph(int nodes, int meanDegree, double beta) {
		try (Session session = driver.session()){
			session.run("CALL apoc.generate.ws("+nodes+","+meanDegree+" ,"+beta+", 'TMP_LABEL','TMP_REL')");
		}
	}

	@Override
	public void generateCompleteGraph(int nodes) {
		try (Session session = driver.session()){
			session.run("CALL apoc.generate.complete("+nodes+",'TMP_LABEL','TMP_REL')");
		}
	}

	@Override
	public void generateSimpleGraph(int nodes, int degree) {
		String degreeList = "["; 
		for(int i = 0; i < nodes-1; i++) {
			degreeList+=degree+",";
		}
		degreeList+=degree+"]";
		try (Session session = driver.session()){
			session.run("CALL apoc.generate.simple("+degreeList+",'TMP_LABEL','TMP_REL')");
		}
	}
	
	@Override
	public void saveGraph(String saveFile) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void restoreGraph(String saveFile) {
		// TODO Auto-generated method stub
		
	}

}
