package fr.irit.smac.shield.model;

import java.util.Random;

public class Variable {

	
	private String name;
	
	private double min;
	
	private double max;
	
	private double value;
	
	private FunctionGen fun;

	public Variable(String name, double min, double max, double value) {
		this.name = name;
		this.min = min;
		this.max = max;
		this.value = value;
	}

	public Variable(String name, double min, double max) {
		this.name = name;
		this.min = min;
		this.max = max;
		this.value = min + new Random().nextDouble()*(max-min);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public FunctionGen getFun() {
		return fun;
	}

	public void setFun(FunctionGen fun) {
		this.fun = fun;
	}

	@Override
	public String toString() {
		return "Variable [name=" + name + ", min=" + min + ", max=" + max + ", value=" + value+"]";
	}
	
	
	
	
	
}
