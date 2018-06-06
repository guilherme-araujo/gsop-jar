package br.ufrn.imd.bioinfo.gsop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Collections;

import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;

import br.ufrn.imd.bioinfo.gsop.controller.QueriesController;
import br.ufrn.imd.bioinfo.gsop.database.Neo4jDriverInstance;
import br.ufrn.imd.bioinfo.gsop.model.Eph;
import br.ufrn.imd.bioinfo.gsop.model.GsopNode;

/**
 * Hello world!
 *
 */
public class App {

	private static final Driver driver = null;//Neo4jDriverInstance.getDriver();

	private static List<Integer> typeAPopHistory;
	private static List<Integer> typeBPopHistory;

public static SimulationResults runSimV5(SimulationData simulationData) {
		
		SimulationResults simulationResults = new SimulationResults();
	
		QueriesController queriesController = new QueriesController();

		Map<String, GsopNode> nodes = new HashMap<String, GsopNode>();

		// Popular lista de nós (pre-loading de vizinhos?)
		List<String> uuidList = new ArrayList<String>();
		uuidList = queriesController.listAllAsUUIDStringList();
		long seed = System.nanoTime();
		Collections.shuffle(uuidList, new Random(seed));
		for(int i = 0; i < uuidList.size(); i++) {
			String uuid = uuidList.get(i);
			GsopNode node = new GsopNode();
			node.setHash(uuid);
			double abRate = 0.5;
			if(simulationData.isaOnly()) {
				abRate = 1.0;
			}
			if (i < simulationData.getInitialPopulation() * abRate) {
				node.setType(simulationData.getTypes().get(0).getType());
				node.setCoeff(simulationData.getTypes().get(0).getInitialCoeff());
				if(i < simulationData.getInitialPopulation() * abRate * simulationData.getEphStartRatio()) {
					Eph e = new Eph(simulationData.getEphBonus());
					node.setEph(e);
				}
					
			} else {
				node.setType(simulationData.getTypes().get(1).getType());
				node.setCoeff(simulationData.getTypes().get(1).getInitialCoeff());
				node.setEph(null);				
			}
			node.setNeighborsHashList(queriesController.listAllNeighborsAsUUIDStringList(uuid));
			nodes.put(uuid, node);
		}

		int count = 0;

		Simulation.setSimulationData(simulationData);

		// Contagem parcial de fitness
		int steps = simulationData.getPlotDensity();
		int step = simulationData.getCycles() / steps;
		if (step == 0)
			step++;

		typeAPopHistory = new ArrayList<Integer>();
		typeBPopHistory = new ArrayList<Integer>();
		Simulation.setPartialFitnessAvg(new ArrayList<Double>());

		long tStart = System.currentTimeMillis();
		
		for (count = 0; count < simulationData.getCycles(); count++) {
			Simulation.cycleV5(nodes, simulationData.getDeathRate(), simulationData.isNeighborhoodInheritance());
			
			List<GsopNode> nodeslist = new ArrayList<GsopNode>(nodes.values());

			if (count % step == 0) {
				Simulation.addPartialFitnessAvg(Simulation.avgFitness(nodeslist));
				typeAPopHistory.add(Simulation.typeCount("A", nodeslist));
				typeBPopHistory.add(Simulation.typeCount("B", nodeslist));
			}
			// System.out.println((nodes.size()));
		}

		long tEnd = System.currentTimeMillis();
		long tDelta = tEnd - tStart;
		double elapsedSeconds = tDelta / 1000.0;

		List<GsopNode> nodeslist = new ArrayList<GsopNode>(nodes.values());
		
		int typeAWithEph = 0;
		int typeBWithEph = 0;
		
		simulationData.setNodeDetail(new ArrayList<String>());
		
		for(GsopNode n : nodeslist) {
			boolean temEph = n.getEph()!=null;
			String strTemEph = temEph?"S":"N";
			if(temEph) {
				if(n.getType().equals("A")) {
					typeAWithEph++;
				}else {
					typeBWithEph++;
				}
			}
			simulationData.getNodeDetail().add(n.getHash()+" temEph: "+strTemEph+" Type: "+n.getType()+
					" Coeff: "+n.getCoeff()+" Fitness: "+n.getFitness());
		}
		
		simulationResults.finalNodes = (ArrayList<GsopNode>) nodeslist;
		simulationResults.typeAWithEph = typeAWithEph;
		simulationResults.typeBWithEph = typeBWithEph;
		simulationResults.elapsedSeconds = elapsedSeconds;
		simulationResults.partialFitnessAvg = (ArrayList<Double>) Simulation.getPartialFitnessAvg();
		simulationResults.avgCoeff = Simulation.avgCoeff(nodeslist);
		simulationResults.avgFitness = Simulation.avgFitness(nodeslist);
		/*String result = Simulation.printTypeCount(nodeslist) + "\n" + Simulation.printAvgCoeff(nodeslist) + "\n"
				+ Simulation.printAvgFitness(nodeslist);
		result += "\n Cycles: " + count + "\nTime: " + elapsedSeconds 
				+ " A with eph: "+typeAWithEph+" B with eph: "+typeBWithEph;*/

		return simulationResults;
	}
	
	public static String runSimV4(SimulationData simulationData) {
		
		QueriesController queriesController = new QueriesController();

		Map<String, GsopNode> nodes = new HashMap<String, GsopNode>();

		// Popular lista de nós (pre-loading de vizinhos?)
		List<String> uuidList = new ArrayList<String>();
		uuidList = queriesController.listAllAsUUIDStringList();
		long seed = System.nanoTime();
		Collections.shuffle(uuidList, new Random(seed));
		for(int i = 0; i < uuidList.size(); i++) {
			String uuid = uuidList.get(i);
			GsopNode node = new GsopNode();
			node.setHash(uuid);
			if (i < simulationData.getInitialPopulation() * simulationData.getEphStartRatio()) {
				node.setType(simulationData.getTypes().get(0).getType());
				node.setCoeff(simulationData.getTypes().get(0).getInitialCoeff());
				Eph e = new Eph(simulationData.getEphBonus());
				node.setEph(e);				
			} else {
				node.setType(simulationData.getTypes().get(1).getType());
				node.setCoeff(simulationData.getTypes().get(1).getInitialCoeff());
				node.setEph(null);				
			}
			node.setNeighborsHashList(queriesController.listAllNeighborsAsUUIDStringList(uuid));
			nodes.put(uuid, node);
		}

		int count = 0;

		Simulation.setSimulationData(simulationData);

		// Contagem parcial de fitness
		int steps = simulationData.getPlotDensity();
		int step = simulationData.getCycles() / steps;
		if (step == 0)
			step++;

		typeAPopHistory = new ArrayList<Integer>();
		typeBPopHistory = new ArrayList<Integer>();
		Simulation.setPartialFitnessAvg(new ArrayList<Double>());

		long tStart = System.currentTimeMillis();
		
		for (count = 0; count < simulationData.getCycles(); count++) {
			Simulation.cycleV4(nodes, simulationData.getDeathRate());
			
			List<GsopNode> nodeslist = new ArrayList<GsopNode>(nodes.values());

			if (count % step == 0) {
				Simulation.addPartialFitnessAvg(Simulation.avgFitness(nodeslist));
				typeAPopHistory.add(Simulation.typeCount("A", nodeslist));
				typeBPopHistory.add(Simulation.typeCount("B", nodeslist));
			}
			// System.out.println((nodes.size()));
		}

		long tEnd = System.currentTimeMillis();
		long tDelta = tEnd - tStart;
		double elapsedSeconds = tDelta / 1000.0;

		List<GsopNode> nodeslist = new ArrayList<GsopNode>(nodes.values());
		
		int typeAWithEph = 0;
		int typeBWithEph = 0;
		
		simulationData.setNodeDetail(new ArrayList<String>());
		
		for(GsopNode n : nodeslist) {
			boolean temEph = n.getEph()!=null;
			String strTemEph = temEph?"S":"N";
			if(temEph) {
				if(n.getType().equals("A")) {
					typeAWithEph++;
				}else {
					typeBWithEph++;
				}
			}
			simulationData.getNodeDetail().add(n.getHash()+" temEph: "+strTemEph+" Type: "+n.getType()+
					" Coeff: "+n.getCoeff()+" Fitness: "+n.getFitness());
		}
		
		String result = Simulation.printTypeCount(nodeslist) + "\n" + Simulation.printAvgCoeff(nodeslist) + "\n"
				+ Simulation.printAvgFitness(nodeslist);
		result += "\n Cycles: " + count + "\nTime: " + elapsedSeconds 
				+ " A with eph: "+typeAWithEph+" B with eph: "+typeBWithEph;

		
		
		return result;
	}
	
	public static String runSimV3(SimulationData simulationData) {
		
		QueriesController queriesController = new QueriesController();

		Map<String, GsopNode> nodes = new HashMap<String, GsopNode>();

		// Popular lista de nós (pre-loading de vizinhos?)
		List<String> uuidList = new ArrayList<String>();
		uuidList = queriesController.listAllAsUUIDStringList();
		long seed = System.nanoTime();
		Collections.shuffle(uuidList, new Random(seed));
		for(int i = 0; i < uuidList.size(); i++) {
			String uuid = uuidList.get(i);
			GsopNode node = new GsopNode();
			node.setHash(uuid);
			if (i < simulationData.getInitialPopulation() * 0.5) {
				node.setType(simulationData.getTypes().get(0).getType());
				node.setCoeff(simulationData.getTypes().get(0).getInitialCoeff());
			} else {
				node.setType(simulationData.getTypes().get(1).getType());
				node.setCoeff(simulationData.getTypes().get(1).getInitialCoeff());
			}
			node.setNeighborsHashList(queriesController.listAllNeighborsAsUUIDStringList(uuid));
			nodes.put(uuid, node);
		}
		
			

		int count = 0;

		Simulation.setSimulationData(simulationData);

		// Contagem parcial de fitness
		int steps = simulationData.getPlotDensity();
		int step = simulationData.getCycles() / steps;
		if (step == 0)
			step++;

		typeAPopHistory = new ArrayList<Integer>();
		typeBPopHistory = new ArrayList<Integer>();
		Simulation.setPartialFitnessAvg(new ArrayList<Double>());

		long tStart = System.currentTimeMillis();
		
		for (count = 0; count < simulationData.getCycles(); count++) {
			Simulation.cycleV3(nodes, simulationData.getDeathRate());
			
			List<GsopNode> nodeslist = new ArrayList<GsopNode>(nodes.values());

			if (count % step == 0) {
				Simulation.addPartialFitnessAvg(Simulation.avgFitness(nodeslist));
				typeAPopHistory.add(Simulation.typeCount("A", nodeslist));
				typeBPopHistory.add(Simulation.typeCount("B", nodeslist));
			}
			// System.out.println((nodes.size()));
		}

		long tEnd = System.currentTimeMillis();
		long tDelta = tEnd - tStart;
		double elapsedSeconds = tDelta / 1000.0;

		List<GsopNode> nodeslist = new ArrayList<GsopNode>(nodes.values());
		
		String result = Simulation.printTypeCount(nodeslist) + "\n" + Simulation.printAvgCoeff(nodeslist) + "\n"
				+ Simulation.printAvgFitness(nodeslist);
		result += "\n Cycles: " + count + "\nTime: " + elapsedSeconds;

		return result;
	}

	public static String runSim(SimulationData simulationData) {

		List<GsopNode> nodes = new LinkedList<GsopNode>();

		for (int i = 0; i < simulationData.getInitialPopulation(); i++) {
			GsopNode n = new GsopNode();
			n.setFitness(0);
			if (i < simulationData.getInitialPopulation() * 0.5) {
				n.setType(simulationData.getTypes().get(0).getType());
				n.setCoeff(simulationData.getTypes().get(0).getInitialCoeff());
			} else {
				n.setType(simulationData.getTypes().get(1).getType());
				n.setCoeff(simulationData.getTypes().get(1).getInitialCoeff());
			}
			nodes.add(n);
		}

		int count = 0;

		Simulation.setSimulationData(simulationData);

		long tStart = System.currentTimeMillis();

		// Contagem parcial de fitness
		int steps = simulationData.getPlotDensity();
		int step = simulationData.getCycles() / steps;
		if (step == 0)
			step++;

		typeAPopHistory = new ArrayList<Integer>();
		typeBPopHistory = new ArrayList<Integer>();
		Simulation.setPartialFitnessAvg(new ArrayList<Double>());

		for (count = 0; count < simulationData.getCycles(); count++) {
			Simulation.cycleV2(nodes, simulationData.getBirthRate(), simulationData.getDeathRate());

			if (count % step == 0) {
				Simulation.addPartialFitnessAvg(Simulation.avgFitness(nodes));
				typeAPopHistory.add(Simulation.typeCount("A", nodes));
				typeBPopHistory.add(Simulation.typeCount("B", nodes));
			}
			// System.out.println((nodes.size()));
		}

		long tEnd = System.currentTimeMillis();
		long tDelta = tEnd - tStart;
		double elapsedSeconds = tDelta / 1000.0;

		String result = Simulation.printTypeCount(nodes) + "\n" + Simulation.printAvgCoeff(nodes) + "\n"
				+ Simulation.printAvgFitness(nodes);
		result += "\n Cycles: " + count + "\nTime: " + elapsedSeconds;

		return result;
	}

	public static String runSim() {
		List<GsopNode> nodes = new LinkedList<GsopNode>();

		try (Session session = driver.session()) {

			StatementResult result;

			// populate individuals list
			result = session.run("match (n) return n");
			while (result.hasNext()) {
				Record record = result.next();

				GsopNode n = new GsopNode();
				n.setHash(record.get(0).get("uuid").toString());
				n.setType(record.get(0).get("type").toString());
				if (n.getType().compareTo("\"A\"") == 0) {
					n.setVal(5);
				} else {
					n.setVal(6);
				}

				nodes.add(n);

			}

		}

		int count = 0;

		for (count = 0; count < 1000; count++) {
			Simulation.cycle(nodes);
		}

		String result = Simulation.printTypeCount(nodes) + "\n" + Simulation.printAvgCoeff(nodes);

		return result;
	}

	public static void main(String[] args) {
		/*
		 * List<Node> nodes = new LinkedList<Node>();
		 * 
		 * try ( Session session = driver.session() ){
		 * 
		 * StatementResult result;
		 * 
		 * //populate individuals list result = session.run("match (n) return n"); while
		 * (result.hasNext()) { Record record = result.next();
		 * 
		 * Node n = new Node(); n.setHash(record.get(0).get("uuid").toString());
		 * n.setType(record.get(0).get("type").toString());
		 * if(n.getType().compareTo("\"A\"")==0) { n.setVal(5); }else { n.setVal(6); }
		 * 
		 * nodes.add(n);
		 * 
		 * }
		 * 
		 * }
		 * 
		 * int count = 0;
		 * 
		 * for(count = 0; count < 1000; count++) { Simulation.cycle(nodes); }
		 * 
		 * System.out.println(Simulation.printTypeCount(nodes));
		 * 
		 * System.out.println(Simulation.printAvgCoeff(nodes));
		 */
		System.out.println(runSim());

	}

	public static List<Integer> getTypeAPopHistory() {
		return typeAPopHistory;
	}

	public static void setTypeAPopHistory(List<Integer> typeAPopHistory) {
		App.typeAPopHistory = typeAPopHistory;
	}

	public static List<Integer> getTypeBPopHistory() {
		return typeBPopHistory;
	}

	public static void setTypeBPopHistory(List<Integer> typeBPopHistory) {
		App.typeBPopHistory = typeBPopHistory;
	}

}
