package br.ufrn.imd.bioinfo.gsop;

import java.util.ArrayList;

import br.ufrn.imd.bioinfo.gsop.model.GsopNode;

public class SimulationResults {

	public ArrayList<Integer> typeAPopHistory;
	public ArrayList<Integer> typeBPopHistory;
	public ArrayList<Integer> ephPopHistory;
	int typeAWithEph;
	int typeBWithEph;
	public ArrayList<Double> partialFitnessAvg;
	public ArrayList<GsopNode> finalNodes;
	public double elapsedSeconds;
	public double avgCoeff;
	public double avgFitness;
	public int fixationCycles;
	
	
}
