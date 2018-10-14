package br.ufrn.imd.bioinfo.gsop.database;

import static java.util.Arrays.asList;

import java.io.File;
//import java.util.HashMap;
import java.util.List;
//import java.util.Map;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
//import org.neo4j.graphdb.factory.GraphDatabaseSettings;
import org.neo4j.internal.kernel.api.exceptions.KernelException;
import org.neo4j.kernel.impl.proc.Procedures;
import org.neo4j.kernel.internal.GraphDatabaseAPI;

//import apoc.ApocConfiguration;

//@WebListener
public class Neo4jBootstrapper {//implements ServletContextListener {
	
	private static GraphDatabaseService graphDb;

	public static void initialize() {
		System.out.println("Inicializando");
		String neo4jPath = "/home2/gfaraujo/neo4j-embedded";
		File f = new File(neo4jPath);
		GraphDatabaseFactory dbFactory = new GraphDatabaseFactory();
		//graphDb = dbFactory.newEmbeddedDatabase(f);
		//graphDb = dbFactory.newEmbeddedDatabaseBuilder(f).setConfig(GraphDatabaseSettings., value)
		graphDb = dbFactory.newEmbeddedDatabaseBuilder(f).loadPropertiesFromFile(neo4jPath+"/neo4j.conf").newGraphDatabase();
		
		/*Map<String, Object> map =  new HashMap<String, Object>();
		map.put("apoc.export.file.enabled", true);
		ApocConfiguration.addToConfig(map);*/
		

		Procedures procedures = ((GraphDatabaseAPI) graphDb).getDependencyResolver().resolveDependency(Procedures.class);

		List<Class<?>> apocProcedures = asList(apoc.generate.Generate.class);

				
		apocProcedures.forEach((proc) -> {
			try {
				procedures.registerProcedure(proc);
			} catch (KernelException e) {
				throw new RuntimeException("Error registering " + proc, e);
			}
		}

				);
		
		apocProcedures = asList(apoc.export.cypher.ExportCypher.class);
		
		apocProcedures.forEach((proc) -> {
			try {
				procedures.registerProcedure(proc);
			} catch (KernelException e) {
				throw new RuntimeException("Error registering " + proc, e);
			}
		}

				);
		
		

		Backend.init(graphDb);
		System.out.println("Inicialização completa");
	}

	
	public static void destroy() {
		if (graphDb != null) {
			graphDb.shutdown();
		}
		System.out.println("Shutdown completo");
	}
}