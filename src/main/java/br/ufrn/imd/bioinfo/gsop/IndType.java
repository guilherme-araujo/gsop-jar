package br.ufrn.imd.bioinfo.gsop;

public class IndType {
	
	private String type;
	private double initialCoeff;
	private double initialRatio;
	
	public IndType() {
		initialCoeff = 1.0;
		initialRatio = 0.5;
		
	}
	
	public IndType(String t) {
		type = t;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public double getInitialCoeff() {
		return initialCoeff;
	}

	public void setInitialCoeff(double initialCoeff) {
		this.initialCoeff = initialCoeff;
	}

	public double getInitialRatio() {
		return initialRatio;
	}

	public void setInitialRatio(double initialRatio) {
		this.initialRatio = initialRatio;
	}		
	
	
	

}
