package br.ufrn.imd.bioinfo.gsop.model;

import java.util.List;

public class GsopNode {
	
	private String hash;
	private String type;
	private String id;
	private int val;
	private double coeff;
	private int fitness;
	private Eph eph;
	
	private List<String> neighborsHashList;
	
	public String getHash() {
		return hash;
	}
	public void setHash(String hash) {
		this.hash = hash;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getVal() {
		return val;
	}
	public void setVal(int val) {
		this.val = val;
	}
	public double getCoeff() {
		if(this.getEph()!=null) {
			return coeff+this.getEph().getBonus();
		}
		return coeff;
	}
	public double getRawCoeff() {
		return coeff;
	}
	public void setCoeff(double coeff) {
		this.coeff = coeff;
	}
	public int getFitness() {
		return fitness;
	}
	public void setFitness(int fitness) {
		this.fitness = fitness;
	}
	public List<String> getNeighborsHashList() {
		return neighborsHashList;
	}
	public void setNeighborsHashList(List<String> neighborsHashList) {
		this.neighborsHashList = neighborsHashList;
	}
	public Eph getEph() {
		return eph;
	}
	public void setEph(Eph eph) {
		this.eph = eph;
	}
	
}
