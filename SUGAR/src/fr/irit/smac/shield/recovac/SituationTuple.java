package fr.irit.smac.shield.recovac;

import java.util.ArrayList;
import java.util.List;

import fr.irit.smac.shield.recovac.VariableEVTuple;

public class SituationTuple {
	private int idSituation;
	private List<VariableEVTuple> variablesEV = new ArrayList<VariableEVTuple>();
	private boolean capacity; //true = capable , false = incapable
	private float noiseAdded;
	
	public SituationTuple(int idSituation, List<VariableEVTuple> variablesTuple, boolean capacity) {
		this.idSituation = idSituation;
		this.variablesEV = variablesTuple;
		this.capacity = capacity;
	}
	
	public SituationTuple(int idSituation, List<VariableEVTuple> variablesTuple, float noise) {
		this.idSituation = idSituation;
		this.variablesEV = variablesTuple;
		this.noiseAdded = noise;
	}

	public int getIdSituation() {
		return idSituation;
	}
	
	public void setIdSituation(int idSituation) {
		this.idSituation = idSituation;
	}
	
	public List<VariableEVTuple> getVariables() {
		return variablesEV;
	}
	
	public void setVariables(List<VariableEVTuple> variables) {
		this.variablesEV = variables;
	}

	public boolean getCapacity() {
		return capacity;
	}
	
	public void setCapacity(boolean capacity) {
		this.capacity = capacity;
	}

	public double getNoiseAdded() {
		return this.noiseAdded;
	}
	
	public void setNoiseAdded(float noiseAdded) {
		this.noiseAdded = noiseAdded;
	}
	
	@Override
	public String toString() {
		return "Situation " + idSituation + ": \n" + variablesEV + "\n noiseAdded=" + noiseAdded + "\n capacity=" + capacity + "\n";
	}
	
	public String print(List<VariableEVTuple> actions) {
		return "Situation " + idSituation + ": \n" + variablesEV + "\n" + actions + "\n noiseAdded=" + noiseAdded + "\n capacity=" + capacity + "\n";
	}
}
