package br.ufrn.imd.bioinfo.gsopJar;

import java.util.ArrayList;
import java.util.List;

import br.ufrn.imd.bioinfo.gsop.App;
import br.ufrn.imd.bioinfo.gsop.IndType;
import br.ufrn.imd.bioinfo.gsop.SimulationData;
import br.ufrn.imd.bioinfo.gsop.SimulationResults;
import br.ufrn.imd.bioinfo.gsop.controller.QueriesController;

public class SimulationCaller {
	
private int builtGraphSize = 0;
	
	private String simResult;
	
	private SimulationData simulationData;
	
	private SimulationResults simulationResults;
	
	private double coeff1;
	private double coeff2;
		
	QueriesController queriesController;
		
	public SimulationCaller() {
		coeff1 = 1.0;
		coeff2 = 1.05;
		simulationData = new SimulationData();
		simulationData.setEphBonus(0.05);
		simulationData.setEphStartRatio(0.5);
		simulationData.setEphBirthGenerationChance(0.5);
		simulationData.setaOnly(false);
		simulationData.setNeighborhoodInheritance(true);
		queriesController = new QueriesController();
		builtGraphSize = queriesController.getNodeCount();
	
		simulationData.setBirthRate(1.04);
		simulationData.setDeathRate(1.04);
		simulationData.setInitialPopulation(200);
		simulationData.setCycles(1000);
		simulationData.setPlotDensity(100);
		simulationData.setNodeDetail(new ArrayList<String>());
		List<IndType> types = new ArrayList<IndType>();
		IndType typeA = new IndType();
		typeA.setInitialCoeff(coeff1);
		typeA.setInitialRatio(0.5);
		typeA.setType("A");
		types.add(typeA);
		
		IndType typeB = new IndType();
		typeB.setInitialCoeff(coeff2);
		typeB.setInitialRatio(0.5);
		typeB.setType("B");
		types.add(typeB);
		
		simulationData.setTypes(types);
	}
	
	public void runSim() {
		simulationData.setDeathRate(simulationData.getBirthRate());
		simulationData.getTypes().get(0).setInitialCoeff(coeff1);
		simulationData.getTypes().get(1).setInitialCoeff(coeff2);
		simResult = App.runSim(simulationData);
		
	}
	
	public void runSimV3() {
		this.builtGraphSize = queriesController.getNodeCount();
		simulationData.setDeathRate(simulationData.getBirthRate());
		simulationData.getTypes().get(0).setInitialCoeff(coeff1);
		simulationData.getTypes().get(1).setInitialCoeff(coeff2);
		simulationData.setInitialPopulation(builtGraphSize);	
		simResult = App.runSimV3(simulationData);
	}
	
	public void runSimV4() {
		this.builtGraphSize = queriesController.getNodeCount();
		simulationData.setDeathRate(simulationData.getBirthRate());
		simulationData.getTypes().get(0).setInitialCoeff(1.0);
		simulationData.getTypes().get(1).setInitialCoeff(1.0);
		simulationData.setInitialPopulation(builtGraphSize);
		simResult = App.runSimV4(simulationData);
	}
	
	public void runSimV5() {
		this.builtGraphSize = queriesController.getNodeCount();
		simulationData.setDeathRate(simulationData.getBirthRate());
		simulationData.getTypes().get(0).setInitialCoeff(1.0);
		simulationData.getTypes().get(1).setInitialCoeff(1.0);
		simulationData.setInitialPopulation(builtGraphSize);
		//simResult = App.runSimV5(simulationData);
		simulationResults = App.runSimV5(simulationData);
	}
		
	public String getSimResult() {
		return simResult;
	}

	public void setSimResult(String simResult) {
		this.simResult = simResult;
	}

	public double getCoeff1() {
		return coeff1;
	}

	public void setCoeff1(double coeff1) {
		this.coeff1 = coeff1;
	}

	public double getCoeff2() {
		return coeff2;
	}

	public void setCoeff2(double coeff2) {
		this.coeff2 = coeff2;
	}

	public SimulationData getSimulationData() {
		return simulationData;
	}

	public void setSimulationData(SimulationData simulationData) {
		this.simulationData = simulationData;
	}

	public int getBuiltGraphSize() {
		return builtGraphSize;
	}

	public void setBuiltGraphSize(int builtGraphSize) {
		this.builtGraphSize = builtGraphSize;
	}

	public SimulationResults getSimulationResults() {
		return simulationResults;
	}

	public void setSimulationResults(SimulationResults simulationResults) {
		this.simulationResults = simulationResults;
	}
	
	

}
