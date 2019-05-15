package fr.irit.smac.shield.recovac;

import java.util.ArrayList;
import java.util.List;

import fr.irit.smac.shield.recovac.VariableTuple;

public class SituationTuple {
	private int idSituation;
	private List<VariableTuple> variables = new ArrayList<VariableTuple>();
	private boolean capacity; //true = capable , false = incapable
	
	public SituationTuple(int _idSituation, List<VariableTuple> _variablesTuple, boolean _capacity) {
		this.idSituation = _idSituation;
		this.variables = _variablesTuple;
		this.capacity = _capacity;
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

	public boolean getCapacity() {
		return capacity;
	}
	
	public void setCapacity(boolean _capacity) {
		this.capacity = _capacity;
	}
	
	@Override
	public String toString() {
		return "Situation " + idSituation + ": \n" + variables + "\n capacity=" + capacity + "\n";
	}
}
