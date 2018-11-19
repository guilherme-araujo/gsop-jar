package br.ufrn.imd.bioinfo.gsop;

import java.util.List;

public class SimulationData {

	private int initialPopulation;
	private double birthRate;
	private double deathRate;
	private List<IndType> types;
	private int cycles;
	private int plotDensity;
	private double ephBonus;
	private double ephStartRatio;
	private double ephBirthGenerationChance;
	private boolean neighborhoodInheritance;
	private boolean aOnly;
	private int ephTime;
	
	private List<String> nodeDetail;
	
	public int getInitialPopulation() {
		return initialPopulation;
	}
	public void setInitialPopulation(int initialPopulation) {
		this.initialPopulation = initialPopulation;
	}
	public double getBirthRate() {
		return birthRate;
	}
	public void setBirthRate(double birthRate) {
		this.birthRate = birthRate;
	}
	public double getDeathRate() {
		return deathRate;
	}
	public void setDeathRate(double deathRate) {
		this.deathRate = deathRate;
	}
	public List<IndType> getTypes() {
		return types;
	}
	public void setTypes(List<IndType> types) {
		this.types = types;
	}
	public int getCycles() {
		return cycles;
	}
	public void setCycles(int cycles) {
		this.cycles = cycles;
	}
	public int getPlotDensity() {
		return plotDensity;
	}
	public void setPlotDensity(int plotDensity) {
		this.plotDensity = plotDensity;
	}
	public double getEphBonus() {
		return ephBonus;
	}
	public void setEphBonus(double ephBonus) {
		this.ephBonus = ephBonus;
	}
	public double getEphStartRatio() {
		return ephStartRatio;
	}
	public void setEphStartRatio(double ephStartRatio) {
		this.ephStartRatio = ephStartRatio;
	}
	public double getEphBirthGenerationChance() {
		return ephBirthGenerationChance;
	}
	public void setEphBirthGenerationChance(double ephBirthGenerationChance) {
		this.ephBirthGenerationChance = ephBirthGenerationChance;
	}
	public List<String> getNodeDetail() {
		return nodeDetail;
	}
	public void setNodeDetail(List<String> nodeDetail) {
		this.nodeDetail = nodeDetail;
	}
	public boolean isNeighborhoodInheritance() {
		return neighborhoodInheritance;
	}
	public void setNeighborhoodInheritance(boolean neighborhoodInheritance) {
		this.neighborhoodInheritance = neighborhoodInheritance;
	}
	public boolean isaOnly() {
		return aOnly;
	}
	public void setaOnly(boolean aOnly) {
		this.aOnly = aOnly;
	}
	public int getEphTime() {
		return ephTime;
	}
	public void setEphTime(int ephTime) {
		this.ephTime = ephTime;
	}
	
	
	
}
