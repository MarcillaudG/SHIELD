package fr.irit.smac.shield.c2av;

import java.util.Random;

import fr.irit.smac.shield.model.Variable;

public class CopyTypedVariable extends TypedVariable {
	
	private Variable mother;
	
	private float y;

	public CopyTypedVariable(Variable variable, String name, float morph) {
		super(variable, name);
		this.mother = variable;
		this.morph = morph;
		Random rand = new Random();
		this.y = rand.nextFloat()*100;
		System.out.println(name +"------------------------>>>>"+morph+"----->>>"+this.y);
	}

	
	@Override
	public double getValue() {
		return this.mother.getValue()*this.morph+this.y;
	}


	@Override
	public String toString() {
		return "CopyTypedVariable [mother=" + mother + "]";
	}
	
	
	
}
