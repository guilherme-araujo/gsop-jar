package br.ufrn.imd.bioinfo.gsop.database;

import org.neo4j.graphdb.GraphDatabaseService;

public class Backend {

    private static Backend INSTANCE;
    
    static void init(GraphDatabaseService graphDb) {
        INSTANCE = new Backend(graphDb);
    }
    
    public static Backend getInstance() {      
        return INSTANCE;
    }
    
    private final GraphDatabaseService graphDb;
    
    private Backend(GraphDatabaseService graphDb) {
        this.graphDb = graphDb;
    }
    
    public GraphDatabaseService getGraphDb() {
        return graphDb;
    }
}
