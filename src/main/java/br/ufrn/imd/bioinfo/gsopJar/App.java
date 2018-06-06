package br.ufrn.imd.bioinfo.gsopJar;


import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

import br.ufrn.imd.bioinfo.gsop.Simulation;
import br.ufrn.imd.bioinfo.gsop.SimulationResults;
import br.ufrn.imd.bioinfo.gsop.database.Neo4jBootstrapper;

/**
 * Hello world!
 *
 */
public class App 
{
		
    public static void main( String[] args )
    {
    	
    	Neo4jBootstrapper.initialize();
    	
    	Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
            	Neo4jBootstrapper.destroy();
            }
        }, "Shutdown-thread"));
    	
    	String operation = "";
    	Integer samples = 1;
    	Integer cycles = 1000;
    	Integer numNodes = 100;
    	Integer numEdges = 4;
    	Double ephBonus = 0.05;
    	
    	//Parsing arguments
        for(String arg : args) {
        	if(arg.contains("--operation")) {
        		String op[] = arg.split("=");
        		operation = op[1];
        		
        	}else if (arg.contains("--samples")) {
        		String smp[] = arg.split("=");
        		samples = Integer.parseInt(smp[1]);
        	}else if (arg.contains("--cycles")) {
        		String cyc[] = arg.split("=");
        		cycles = Integer.parseInt(cyc[1]);
        	}else if (arg.contains("--numNodes")) {
        		String num[] = arg.split("=");
        		numNodes = Integer.parseInt(num[1]);
        	}else if (arg.contains("--numEdges")) {
        		String num[] = arg.split("=");
        		numEdges = Integer.parseInt(num[1]);
        	}
        	else if (arg.contains("--ephBonus")) {
        		String num[] = arg.split("=");
        		ephBonus = Double.parseDouble(num[1]);
        	}
        	
        }
    	
        if(operation.equals("newgraph")) {
        	NewGraphCaller newGraph = new NewGraphCaller();
            newGraph.setNumNodes(numNodes);            
            newGraph.generateba();
            
            for(String s : newGraph.generatedNodes)
            	System.out.println(s);
        	
        }else if(operation.equals("simulation")) {
        	
        	ArrayList<SimulationResults> resultList = new ArrayList<SimulationResults>();
        	SimulationCaller simulation = new SimulationCaller();
        	simulation.getSimulationData().setCycles(cycles);  
        	simulation.getSimulationData().setEphBonus(ephBonus);        	
        	System.out.println(samples+" samples will be executed");
        	long tStart = System.currentTimeMillis();
        	
        	String csv = "";
        	
        	for(int i = 0; i < samples; i++) {
        		System.out.println("Sample "+i);
        		simulation.runSimV5();
        		SimulationResults results = simulation.getSimulationResults();
        		resultList.add(results);
        		int typeACount = Simulation.typeCount("A", results.finalNodes);
        		int typeBCount = Simulation.typeCount("B", results.finalNodes);
        		String out = "A: "+typeACount+" B: "+typeBCount+" Avg. coeff: "+results.avgCoeff+
        				" Avg. fitness: "+results.avgFitness+" time: "+results.elapsedSeconds;
        		csv += typeACount+";"+typeBCount+"\n";
        		
        		System.out.println(out);
        	}
        	
        	try {
    			FileWriter fileWriter = new FileWriter("out.csv");
    			PrintWriter printWriter = new PrintWriter(fileWriter);
    			printWriter.println(csv);
    			printWriter.close();
    		}catch (Exception e) {
				System.out.println("File error");
			}
        	
        	long tEnd = System.currentTimeMillis();
    		long tDelta = tEnd - tStart;
    		double elapsedSeconds = tDelta / 1000.0;
    		
    		System.out.println(elapsedSeconds+" seconds elapsed");
        	
        }
        
        
        
        
        
    }
    
}
