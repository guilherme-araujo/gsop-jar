package br.ufrn.imd.bioinfo.gsop.database;

import static java.util.Arrays.asList;

import java.io.File;
import java.util.List;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.internal.kernel.api.exceptions.KernelException;
import org.neo4j.kernel.impl.proc.Procedures;
import org.neo4j.kernel.internal.GraphDatabaseAPI;

//@WebListener
public class Neo4jBootstrapper {//implements ServletContextListener {
	
	private static GraphDatabaseService graphDb;

	public static void initialize() {
		System.out.println("Inicializando");
		File f = new File("/home2/gfaraujo/neo4j-embedded");
		GraphDatabaseFactory dbFactory = new GraphDatabaseFactory();
		graphDb = dbFactory.newEmbeddedDatabase(f);

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