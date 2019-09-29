package fr.irit.smac.shield.recovac;

import java.util.ArrayList;
import java.util.List;

import fr.irit.smac.shield.recovac.VariableTuple;

public class SituationTuple {
	private int idSituation;
	private List<VariableTuple> variables = new ArrayList<VariableTuple>();
	private boolean capacity; //true = capable , false = incapable
	private float noiseAdded;
	
	public SituationTuple(int idSituation, List<VariableTuple> variablesTuple, boolean capacity) {
		this.idSituation = idSituation;
		this.variables = variablesTuple;
		this.capacity = capacity;
	}
	
	public SituationTuple(int idSituation, List<VariableTuple> variablesTuple, float noise) {
		this.idSituation = idSituation;
		this.variables = variablesTuple;
		this.noiseAdded = noise;
	}

	public int getIdSituation() {
		return idSituation;
	}
	
	public void setIdSituation(int idSituation) {
		this.idSituation = idSituation;
	}
	
	public List<VariableTuple> getVariables() {
		return variables;
	}
	
	public void setVariables(List<VariableTuple> variables) {
		this.variables = variables;
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
		return "Situation " + idSituation + ": \n" + variables + "\n noiseAdded=" + noiseAdded + "\n capacity=" + capacity + "\n";
	}
	
	public String print(List<VariableTuple> actions) {
		return "Situation " + idSituation + ": \n" + variables + "\n" + actions + "\n noiseAdded=" + noiseAdded + "\n capacity=" + capacity + "\n";
	}
}
