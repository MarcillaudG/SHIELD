package fr.irit.smac.shield.c2av;

import fr.irit.smac.shield.model.Variable;

public class TypedVariable extends Variable{

	private String type;
	
	public TypedVariable(String name, double min, double max, double value, String type) {
		super(name, min, max, value);
		this.type = type;
	}
	
	public String getType() {
		return this.type;
	}
	
	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return super.toString()+" [type=" + type + "]";
	}
	
	

}
