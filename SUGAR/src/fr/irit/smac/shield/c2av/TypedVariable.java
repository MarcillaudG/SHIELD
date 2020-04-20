package fr.irit.smac.shield.c2av;

import fr.irit.smac.shield.model.FunctionGen;
import fr.irit.smac.shield.model.Variable;

public class TypedVariable extends Variable{

	private String type;
	
	protected float morph;
	
	public TypedVariable(String name, double min, double max, double value, String type) {
		super(name, min, max, value);
		this.type = type;
		this.morph = 1.0f;
	}
	
	public TypedVariable(Variable variable, String name) {
		super(name, variable.getMin(), variable.getMax(), variable.getValue());
		this.setFun(variable.getFun());
		this.morph = 1.0f;
	}
	
	/*public TypedVariable(Variable variable, String name, float morph) {
		super(name, variable.getMin(), variable.getMax(), variable.getValue());
		this.setFun(variable.getFun(), morph);
		this.morph = morph;
		// TODO REMOVE
		this.morph = 1.0f;
	}*/

	@Override
	public double getValue() {
		return super.getValue()*this.morph;
	}

	public String getType() {
		return this.type;
	}
	
	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return super.toString()+" [type=" + type + "]" + "Morph="+morph;
	}
	
	

}
