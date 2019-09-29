package fr.irit.smac.shield.model;

import java.util.Random;


	
public class Variable {

	private VariableState variableState;
	private double min;
	private double max;
	private FunctionGen fun;
	


	public Variable(String name, double min, double max, double value) {
		this.variableState = new VariableState(name, value);
		this.min = min;
		this.max = max;
	}

	public Variable(String name, double min, double max) {
		this.min = min;
		this.max = max;
		this.variableState = new VariableState(name, this.generateValue());
	}
	
	public Variable(String name, double value) {
		this.variableState = new VariableState(name, value);
	}
	
	public String getName() {
		return variableState.getName();
	}

	public void setName(String name) {
		this.variableState.setName(name);
	}

	public double getMin() {
		return min;
	}

	public void setMin(double min) {
		this.min = min;
	}

	public double getMax() {
		return max;
	}

	public void setMax(double max) {
		this.max = max;
	}

	public double getValue() {
		return variableState.getValue();
	}

	public void setValue(double value) {
		this.variableState.setValue(value);
	}

	public FunctionGen getFun() {
		return fun;
	}

	public void setFun(FunctionGen fun) {
		this.fun = fun;
	}

	public double generateValue () {
		return min + new Random().nextDouble()*(max-min);
	}
	
	/*@Override
	public String toString() {
		return "Variable [name=" + name + ", min=" + min + ", max=" + max + ", value=" + value+"]";
	}*/
	
	@Override
	public String toString() {
		return variableState.toString();
	}
		
}
