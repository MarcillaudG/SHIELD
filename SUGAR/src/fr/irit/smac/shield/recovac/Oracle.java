package fr.irit.smac.shield.recovac;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fr.irit.smac.shield.model.Variable;

public class Oracle {
	private List<Variable> currentValueOfAS;
	private List<Variable> currentValueOfEV;
	private List<Variable> intentionalValueOfEV;
	private List<Variable> expectedRealValueOfEV;
	private boolean capacity; //true = capable , false = incapable
	
	public Oracle(Map<String, Variable> variablesActionState, List<Variable> variablesEnvironmental, 
			List<Variable> expectedRealValueEV, List<Variable> intentionalValueEV) {
		this.currentValueOfAS = convertVariables(variablesActionState);
		this.currentValueOfEV = variablesEnvironmental;
		this.expectedRealValueOfEV = expectedRealValueEV;
		this.intentionalValueOfEV = intentionalValueEV;
	}

	public List<Variable> getActionsState() {
		return currentValueOfAS;
	}

	public void setActionsState(List<Variable> actionsState) {
		this.currentValueOfAS = actionsState;
	}

	public List<Variable> getEnvironmentsV() {
		return currentValueOfEV;
	}

	public void setEnvironmentsV(List<Variable> environmentsV) {
		this.currentValueOfEV = environmentsV;
	}

	public List<Variable> getExpectedEV() {
		return expectedRealValueOfEV;
	}

	public void setExpectedEV(List<Variable> expectedEV) {
		this.expectedRealValueOfEV = expectedEV;
	}
	
	public List<Variable> getIntentionalEV() {
		return intentionalValueOfEV;
	}

	public void setIntentionalEV(List<Variable> intentionalEV) {
		this.intentionalValueOfEV = intentionalEV;
	}
	
	public boolean getCapacity() {
		return capacity;
	}
	
	public void setCapacity(boolean capacity) {
		this.capacity = capacity;
	}
	
	public List<Variable> convertVariables(Map<String, Variable> variablesAux){
		List<Variable> aux = new ArrayList<>();
			
			for (String s: variablesAux.keySet()) {
				aux.add(new Variable(variablesAux.get(s).getName(), variablesAux.get(s).getValue()));
			}
		return aux;
	}
	
	@Override
	public String toString() { 
		return "{\n\tAction Variables:\n" + currentValueOfAS + "\n\tEnvironmental Variables:\n" + currentValueOfEV + "\n\tExpected Values of EV:\n" + expectedRealValueOfEV + "\n" + "}";
	}
}
