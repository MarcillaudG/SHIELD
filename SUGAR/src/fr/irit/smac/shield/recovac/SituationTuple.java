package fr.irit.smac.shield.recovac;

import java.util.ArrayList;
import java.util.List;

import fr.irit.smac.shield.recovac.VariableTuple;

public class SituationTuple {
	private int idSituation;
	private List<VariableTuple> variables = new ArrayList<VariableTuple>();
	private boolean capacity1; //true = capable , false = incapable
	private boolean capacity2; //true = capable , false = incapable
	private float noiseAdded1;
	private float noiseAdded2;
	
	public SituationTuple(int _idSituation, List<VariableTuple> _variablesTuple, boolean _capacity) {
		this.idSituation = _idSituation;
		this.variables = _variablesTuple;
		this.capacity1 = _capacity;
		this.capacity2 = _capacity;
	}
	
	public SituationTuple(int _idSituation, List<VariableTuple> _variablesTuple, float _noise1, float _noise2) {
		this.idSituation = _idSituation;
		this.variables = _variablesTuple;
		this.noiseAdded1 = _noise1;
		this.noiseAdded2 = _noise2;
	}

	public int getIdSituation() {
		return idSituation;
	}
	
	public void setIdSituation(int _idSituation) {
		this.idSituation = _idSituation;
	}
	
	public List<VariableTuple> getVariables() {
		return variables;
	}
	
	public void setVariables(List<VariableTuple> _variables) {
		this.variables = _variables;
	}

	public boolean getCapacity1() {
		return capacity1;
	}
	
	public void setCapacity1(boolean _capacity) {
		this.capacity1 = _capacity;
	}
	public boolean getCapacity2() {
		return capacity2;
	}
	
	public void setCapacity2(boolean _capacity) {
		this.capacity2 = _capacity;
	}
	public double getNoiseAdded1() {
		return this.noiseAdded1;
	}
	
	public void setNoiseAdded1(float _noiseAdded) {
		this.noiseAdded1 = _noiseAdded;
	}
	
	public float getNoiseAdded2() {
		return this.noiseAdded2;
	}
	
	public void setNoiseAdded2(float _noiseAdded) {
		this.noiseAdded2 = _noiseAdded;
	}
	
	@Override
	public String toString() {
		return "Situation " + idSituation + ": \n" + variables + "\n noiseAdded1=" + noiseAdded1 + "\n noiseAdded2=" + noiseAdded2 + "\n capacity1=" + capacity1 + "\n capacity2=" + capacity2 + "\n";
	}
}
