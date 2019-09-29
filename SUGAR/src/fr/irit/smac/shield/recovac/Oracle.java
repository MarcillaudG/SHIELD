package fr.irit.smac.shield.recovac;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fr.irit.smac.shield.model.Variable;

public class Oracle {
	private List<Variable> actionsState;
	private List<Variable> environmentsV;
	private List<Variable> expectedEV;
	
	public Oracle(Map<String, Variable> variablesActionState, List<Variable> variables, List<Variable> observedVariables) {
		this.actionsState = convertVariables(variablesActionState);
		this.environmentsV = variables;
		this.expectedEV = observedVariables;
	}

	public List<Variable> getActionsState() {
		return actionsState;
	}

	public void setActionsState(List<Variable> actionsState) {
		this.actionsState = actionsState;
	}

	public List<Variable> getEnvironmentsV() {
		return environmentsV;
	}

	public void setEnvironmentsV(List<Variable> environmentsV) {
		this.environmentsV = environmentsV;
	}

	public List<Variable> getExpectedEV() {
		return expectedEV;
	}

	public void setExpectedEV(List<Variable> expectedEV) {
		this.expectedEV = expectedEV;
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
		return "{\n\tAction Variables:\n" + actionsState + "\n\tEnvironmental Variables:\n" + environmentsV + "\n\tExpected Values of EV:\n" + expectedEV + "\n" + "}";
	}
}
