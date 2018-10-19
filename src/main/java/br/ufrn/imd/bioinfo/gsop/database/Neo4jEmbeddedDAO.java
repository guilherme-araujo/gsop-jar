package br.ufrn.imd.bioinfo.gsop.database;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;

public class Neo4jEmbeddedDAO implements Neo4jDAO {

	private static GraphDatabaseService db = Backend.getInstance().getGraphDb();

	public Neo4jEmbeddedDAO() {

	}

	@Override
	public void eraseDB() {
		try (Transaction tx = db.beginTx()) {
			db.execute("MATCH (n) DETACH DELETE n");
			tx.success();
		}

	}

	@Override
	public Result getAll() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<String> getAllAsStringList() {

		ArrayList<String> result = new ArrayList<String>();

		Result r;

		try (Transaction tx = db.beginTx()) {
			r = db.execute("match (n) return n");

			while (r.hasNext()) {
				Map<String, Object> row = r.next();

				Node n = (Node) row.get("n");

				System.out.println(n.getAllProperties());
				// Map<String, Object> nodeDetails = n.getAllProperties();
				// for ( String key : nodeDetails.keySet() ) {
				// result.add(key + " : " + nodeDetails.get(key));
				// }
				result.add(n.getAllProperties().toString());

			}

			tx.success();
		}

		return result;
	}
	
	@Override
	public List<String> getAllAsUUIDList() {
		ArrayList<String> result = new ArrayList<String>();

		Result r;

		try (Transaction tx = db.beginTx()) {
			r = db.execute("match (n) return n.uuid");

			while (r.hasNext()) {
				Map<String, Object> row = r.next();

				result.add(row.get("n.uuid").toString());

			}

			tx.success();
		}

		return result;
	}
	
	@Override
	public List<String> getNeighboursUUIDList(String uuid) {
		List<String> result = new ArrayList<String>();
		
		try (Transaction tx = db.beginTx()) {
			Result r = db.execute("MATCH (n { uuid: '"+uuid+"' }) -- (m) RETURN m.uuid");
			
			while(r.hasNext()) {
				Map<String, Object> row = r.next();
				result.add(row.get("m.uuid").toString());
				
			}
		}
		
		return result;
	}
	
	@Override
	public int getNodeCount() {
		int total = 0;
		try (Transaction tx = db.beginTx()) {
			Result r = db.execute("MATCH (n) return count(*) as total"); 
			Map<String, Object> row = r.next();
			total = Integer.parseInt(row.get("total").toString());
		}
		return total;
	}

	@Override
	public void generateERGraph(int nodes, int edges) {
		try (Transaction tx = db.beginTx()) {
			db.execute("CALL apoc.generate.er(" + nodes + "," + edges + " ,'TMP_LABEL','TMP_REL')");
			tx.success();
		}

	}

	@Override
	public void generateBAGraph(int nodes, int edges) {
		try (Transaction tx = db.beginTx()) {
			db.execute("CALL apoc.generate.ba(" + nodes + "," + edges + " ,'TMP_LABEL','TMP_REL')");
			tx.success();
		}

	}

	@Override
	public void generateWSGraph(int nodes, int meanDegree, double beta) {
		try (Transaction tx = db.beginTx()) {
			db.execute("CALL apoc.generate.ws(" + nodes + "," + meanDegree + " ," + beta + ", 'TMP_LABEL','TMP_REL')");
			tx.success();
		}

	}

	@Override
	public void generateCompleteGraph(int nodes) {
		try (Transaction tx = db.beginTx()) {
			db.execute("CALL apoc.generate.complete(" + nodes + ",'TMP_LABEL','TMP_REL')");
			tx.success();
		}

	}

	@Override
	public void generateSimpleGraph(int nodes, int degree) {
		String degreeList = "[";
		for (int i = 0; i < nodes - 1; i++) {
			degreeList += degree + ",";
		}
		degreeList += degree + "]";

		try (Transaction tx = db.beginTx()) {
			db.execute("CALL apoc.generate.simple(" + degreeList + ",'TMP_LABEL','TMP_REL')");
			tx.success();
		}

	}
	
	@Override
	public void saveGraph(String saveFile) {
		try (Transaction tx = db.beginTx()){
			db.execute("CALL apoc.export.cypher.all(\""+saveFile+"\",{})");
			tx.success();
		}
		
	}
	
	@Override
	public void restoreGraph(String saveFile) {
		/*try (Transaction tx = db.beginTx()){
			db.execute("MATCH (n) DETACH DELETE n");
			tx.success();
		}*/
		try (Transaction tx = db.beginTx()){
			String exec = "CALL apoc.cypher.runFile(\'file:"+saveFile+"\')";
			System.out.println(exec);			
			db.execute(exec);
			tx.success();
		}
	}

}
