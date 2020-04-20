package fr.irit.smac.shield.c2av;

import fr.irit.smac.shield.model.Variable;

public class CopyTypedVariable extends TypedVariable {
	
	private Variable mother;

	public CopyTypedVariable(Variable variable, String name, float morph) {
		super(variable, name);
		// TODO Auto-generated constructor stub
		this.mother = variable;
		System.out.println(name +"------------------------>>>>"+morph);
	}

	
	@Override
	public double getValue() {
		return this.mother.getValue()*this.morph;
	}
	
}
