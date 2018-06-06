package br.ufrn.imd.bioinfo.gsop.database;

import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;

public class Neo4jDriverInstance {
	
	private static final Driver driver = GraphDatabase.driver( "bolt://10.7.43.2:7687", AuthTokens.basic( "neo4j", "bif$2017" ) );

	public static Driver getDriver() {
		return driver;
	};	
		
	
}
