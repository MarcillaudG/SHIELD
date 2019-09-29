package fr.irit.smac.shield.model;

public class VariableState {
	private String name;
	private double value;
	
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
