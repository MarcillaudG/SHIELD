package fr.irit.smac.shield.model;

import java.io.Serializable;
import java.util.List;

public class VariableState implements Serializable {
	protected String name;
	protected double value;
	
	public VariableState(String name, double value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "[name=" + name + ", value=" + value+"]";
	}
	
	
	public static int numberVariablesActive(List<VariableState> variableStateList) {
		int counter = 0;
		
		for(VariableState var: variableStateList) {
			if(var.getValue() != 0)
				counter++;
		}
		return counter;
	}
	
	public static VariableState findVariableStateIn(List<VariableState> variableStateList,  String idVariable) {
		for (VariableState variableState : variableStateList) {
			if(variableState.getName().equals(idVariable)) {
				return variableState;
			}
		}
		return null;
	}			
				
	
}
