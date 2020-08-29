package fr.irit.smac.shield.model;

import java.io.Serializable;

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

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "[name=" + name + ", value=" + value+"]";
	}
}
