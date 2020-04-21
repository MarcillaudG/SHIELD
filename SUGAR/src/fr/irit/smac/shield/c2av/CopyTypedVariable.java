package fr.irit.smac.shield.c2av;

import fr.irit.smac.shield.model.Variable;

public class CopyTypedVariable extends TypedVariable {
	
	private Variable mother;

	public CopyTypedVariable(Variable variable, String name, float morph) {
		super(variable, name);
		this.mother = variable;
		this.morph = morph;
		System.out.println(name +"------------------------>>>>"+morph);
	}

	
	@Override
	public double getValue() {
		return this.mother.getValue()*this.morph;
	}


	@Override
	public String toString() {
		return "CopyTypedVariable [mother=" + mother + "]";
	}
	
	
	
}
